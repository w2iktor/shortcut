package pl.symentis.shorturl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import pl.symentis.shorturl.api.CreateAccountRequest;
import pl.symentis.shorturl.api.CreateShortcutRequest;
import pl.symentis.shorturl.api.GetAccountResponse;
import pl.symentis.shorturl.api.RedirectsExpiryPolicyData;

import java.net.URI;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static pl.symentis.shorturl.api.CreateAccountRequestBuilder.createAccountRequestBuilder;
import static pl.symentis.shorturl.api.CreateShortcutRequestBuilder.createShortcutRequestBuilder;
import static pl.symentis.shorturl.integration.assertions.ExtendedAccountResponseAssert.assertThat;

@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ShorturlRestApiIT {

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper objectMapper;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @TestConfiguration
    public static class MongoOverrides {
        @Bean
        public MongoClient mongoClient() {
            Integer mongoPort = mongoDBContainer.getMappedPort(27017);
            return new MongoClient("localhost", mongoPort);
        }
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void generated_shorturl_is_accessible_and_redirect_to_given_url() throws Exception {

        String accountId = generateRandomString();
        CreateAccountRequest createAccountRequest = createAccountRequestBuilder()
            .withEmail("account@account.com")
            .withMaxShortcuts(1)
            .withName(accountId)
            .withTaxnumber("taxnumber")
            .build();

        given()
            .contentType("application/json")
            .body(createAccountRequest)
        .when()
            .post("/api/accounts/")
        .then()
            .statusCode(201);

        CreateShortcutRequest createShortcutRequest = createShortcutRequestBuilder()
            .withUrl(new URL("http://onet.pl"))
            .withExpiry(new RedirectsExpiryPolicyData(1))
            .build();

        String location = given()
            .contentType("application/json")
            .body(createShortcutRequest)
            .pathParam("accountId", accountId)
        .when()
            .post("/api/accounts/{accountId}/shortcuts")
        .then()
            .statusCode(201)
            .header("Location", startsWith("http://localhost:" + port))
        .extract()
            .header("Location");

        given()
            .redirects()
                .follow(false)
        .when()
            .get(new URL(location))
        .then()
            .statusCode(301)
            .header("Location", equalTo("http://onet.pl"));
    }

    @Test
    public void create_shorturl() throws Exception {

        String accountId = generateRandomString();
        String shortcut = generateRandomString();

        CreateAccountRequest createAccountRequest = createAccountRequestBuilder()
            .withName(accountId)
            .withTaxnumber("taxnumber")
            .withMaxShortcuts(1)
            .withEmail("account@account.com")
            .build();

        given()
            .contentType("application/json")
            .body(createAccountRequest)
        .when()
            .post("/api/accounts/")
        .then()
            .statusCode(201);

        CreateShortcutRequest createShortcutRequest = createShortcutRequestBuilder()
            .withExpiry(new RedirectsExpiryPolicyData(1))
            .withUrl(new URL("http://onet.pl"))
            .build();

        String location = given()
            .contentType("application/json")
            .body(createShortcutRequest)
            .pathParam("accountId", accountId)
            .pathParam("shortcut", shortcut)
        .when()
            .put("/api/accounts/{accountId}/shortcuts/{shortcut}")
        .then()
            .statusCode(201)
            .header("Location", startsWith("http://localhost:" + port))
        .extract()
            .header("Location");

        given()
            .redirects()
            .follow(false)
        .when()
            .get(new URL(location))
        .then()
            .statusCode(301)
            .header("Location", equalTo("http://onet.pl"));
    }

    @Test
    public void dont_allow_to_create_two_shortcuts() throws Exception {

        String accountId = generateRandomString();
        String shortcut = generateRandomString();

        CreateAccountRequest createAccountRequest = createAccountRequestBuilder()
            .withName(accountId)
            .withTaxnumber("taxnumber")
            .withMaxShortcuts(10)
            .withEmail("account@account.com")
            .build();

        given()
            .contentType("application/json")
            .body(createAccountRequest)
        .when()
            .post("/api/accounts/")
        .then()
            .statusCode(201);

        CreateShortcutRequest createOnetShortcutRequest = createShortcutRequestBuilder()
                .withExpiry(new RedirectsExpiryPolicyData(1))
                .withUrl(new URL("http://onet.pl"))
                .build();

        given()
            .contentType("application/json")
            .body(createOnetShortcutRequest)
            .pathParam("accountId", accountId)
            .pathParam("shortcut", shortcut)
        .when()
            .put("/api/accounts/{accountId}/shortcuts/{shortcut}")
        .then()
            .statusCode(201);

        CreateShortcutRequest createWpShortcutRequest = createShortcutRequestBuilder()
                .withExpiry(new RedirectsExpiryPolicyData(1))
                .withUrl(new URL("http://wp.pl"))
                .build();

        given()
            .contentType("application/json")
            .body(createWpShortcutRequest)
            .pathParam("accountId", accountId)
            .pathParam("shortcut", shortcut)
        .when()
            .put("/api/accounts/{accountId}/shortcuts/{shortcut}")
        .then()
            .statusCode(409);
    }

    @Test
    public void create_new_account() {
        String email = "account@account.com";
        String name = "acc123";

        CreateAccountRequest createAccountRequest = createAccountRequestBuilder()
                .withName(name)
                .withTaxnumber("taxnumber")
                .withMaxShortcuts(1)
                .withEmail(email)
                .build();
        String location = given()
            .contentType("application/json")
            .body(createAccountRequest)
        .when()
            .post("/api/accounts")
        .then()
            .statusCode(201)
            .header("Location", equalTo("http://localhost:" + port +
                    "/api/accounts/acc123"))
        .extract()
            .header("Location");

        GetAccountResponse accountResponse =
                RestAssured.get(URI.create(location))
            .as(GetAccountResponse.class);

        assertThat(accountResponse)
            .hasEmail(email)
            .hasName(name)
            .hasNoRegisteredShortcuts();
    }

    private static String generateRandomString() {
        return RandomStringUtils.randomAlphanumeric(5);
    }

}
