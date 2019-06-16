package pl.symentis.shorturl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.mongodb.MongoClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.symentis.shorturl.api.CreateAccountRequest;
import pl.symentis.shorturl.api.CreateShortcutRequest;
import pl.symentis.shorturl.api.GetAccountResponse;
import pl.symentis.shorturl.api.RedirectsExpiryPolicyData;

import java.net.URI;
import java.net.URL;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;
import static pl.symentis.shorturl.api.CreateAccountRequestBuilder.createAccountRequestBuilder;
import static pl.symentis.shorturl.api.CreateShortcutRequestBuilder.createShortcutRequestBuilder;
import static pl.symentis.shorturl.integration.assertions.ExtendedAccountResponseAssert.assertThat;

@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ShorturlRestApiIT {

    private RestTemplate restTemplate = new RestTemplate();

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper objectMapper;

    @Container
    public static GenericContainer<?> mongo = new GenericContainer<>("mongo:3.4-xenial")
            .withExposedPorts(27017)
            .waitingFor(Wait.forListeningPort());

    @TestConfiguration
    public static class MongoOverrides {

        @Bean
        public MongoClient mongoClient() {
            Integer mongoPort = mongo.getMappedPort(27017);
            return new MongoClient("localhost", mongoPort);
        }

    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Disabled
    @Test
    public void swagger_is_accessible() {
        String uriString = fromHttpUrl("http://localhost")
            .port(port)
            .path("/v2/api-docs")
            .toUriString();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(uriString, String.class);
        assertThat(forEntity.getStatusCode())
            .as("swagger.json not found")
            .isEqualTo(HttpStatus.OK);
    }

    @Test
    public void swagger_ui_is_accessible() {
        // given
        String swaggerUiUrl = fromHttpUrl("http://localhost")
            .port(port)
            .path("/swagger-ui.html")
            .toUriString();

        // when
        ResponseEntity<String> forEntity = restTemplate.getForEntity(swaggerUiUrl, String.class);

        // then
        assertThat(forEntity.getStatusCode())
            .as("swagger-ui not found")
            .isEqualTo(HttpStatus.OK);
    }

    @Test
    public void generated_shorturl_is_accessible_and_redirect_to_() throws Exception {

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
    public void create_new_account() throws Exception {
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
            .header("Location", equalTo("http://localhost:" + port + "/api/accounts/acc123"))
        .extract()
            .header("Location");

        GetAccountResponse accountResponse = when()
            .get(URI.create(location))
        .then()
            .statusCode(HttpStatus.OK.value())
        .extract()
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
