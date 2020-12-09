package pl.symentis.shorturl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
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
import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.domain.RedirectsExpiryPolicy;
import pl.symentis.shorturl.domain.Shortcut;
import pl.symentis.shorturl.integration.assertions.ExtendedAccountAssert;

import java.net.URI;
import java.net.URL;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static pl.symentis.shorturl.api.CreateAccountRequestBuilder.createAccountRequestBuilder;
import static pl.symentis.shorturl.api.CreateShortcutRequestBuilder.createShortcutRequestBuilder;
import static pl.symentis.shorturl.domain.FakeAccountBuilder.fakeAccountBuilder;
import static pl.symentis.shorturl.domain.FakeShortcutBuilder.fakeShortcutBuilder;
import static pl.symentis.shorturl.domain.ShortcutBuilder.shortcutBuilder;
import static pl.symentis.shorturl.integration.assertions.ExtendedAccountResponseAssert.assertThat;

@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ShorturlRestApiIT {

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

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
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void generated_shorturl_is_accessible_and_redirect_to_given_url() throws Exception {
        // given
        Account account = TestAccountProvider.testAccountProvider(accountRepository)
            .withAccount(fakeAccountBuilder()
                .withMaxShortcuts(10)
                .build())
            .save();

        CreateShortcutRequest createShortcutRequest = createShortcutRequestBuilder()
            .withUrl(new URL("http://onet.pl"))
            .withExpiry(new RedirectsExpiryPolicyData(1))
            .build();

        String location = given()
            .contentType("application/json")
            .body(createShortcutRequest)
            .pathParam("accountId", account.getName())
        .when()
            .post("/api/accounts/{accountId}/shortcuts")
        .then()
            .statusCode(201)
            .header("Location", startsWith("http://localhost:" + port))
        .extract()
            .header("Location");

        // when
        String targetUrl = given()
            .redirects()
                .follow(false)
        .when()
            .get(new URL(location))
        .then()
            .statusCode(301)
            .extract()
            .header("Location");

        // then
        Assertions.assertThat(targetUrl)
            .isEqualTo("http://onet.pl");
    }

    @Test
    void create_shorturl_add_shortcut_to_account() throws Exception {
        // given
        String shortcut = generateRandomString();
        Account account = TestAccountProvider.testAccountProvider(accountRepository)
            .withAccount(fakeAccountBuilder()
                .withMaxShortcuts(10)
                .build())
            .save();
        CreateShortcutRequest createShortcutRequest = createShortcutRequestBuilder()
            .withExpiry(new RedirectsExpiryPolicyData(1))
            .withUrl(new URL("http://onet.pl"))
            .build();

        // when
         given()
            .contentType("application/json")
            .body(createShortcutRequest)
            .pathParam("accountId", account.getName())
            .pathParam("shortcut", shortcut)
        .when()
            .put("/api/accounts/{accountId}/shortcuts/{shortcut}")
        .then()
            .statusCode(201)
            .header("Location", startsWith("http://localhost:" + port));

        // then
        Optional<Account> accountFromDb = accountRepository.findById(account.getName());
        Assertions.assertThat(accountFromDb)
            .isNotEmpty();

        Shortcut expectedShorcut = shortcutBuilder()
            .withExpiryPolicy(new RedirectsExpiryPolicy(1))
            .withUrl(new URL("http://onet.pl"))
            .withShortcut(shortcut)
            .build();
        ExtendedAccountAssert.assertThat(accountFromDb.get())
            .hasOnlyShortcuts(expectedShorcut);

    }

    @Test
    void dont_allow_to_create_two_shortcuts_with_same_same_id() throws Exception {

        // given
        String shortcutId = "my-shortcut";
        Account account = TestAccountProvider.testAccountProvider(accountRepository)
            .withAccount(fakeAccountBuilder()
                .withMaxShortcuts(10)
                .build())
            .withShortcuts(
                fakeShortcutBuilder()
                    .withValidExpiryPolicy()
                    .withShortcut(shortcutId)
                    .withUrl("http://onet.pl")
                    .build()
            )
            .save();

        CreateShortcutRequest createOnetShortcutRequest = createShortcutRequestBuilder()
                .withExpiry(new RedirectsExpiryPolicyData(1))
                .withUrl(new URL("http://onet.pl"))
                .build();

        given()
            .contentType("application/json")
            .body(createOnetShortcutRequest)
            .pathParam("accountId", account.getName())
            .pathParam("shortcut", shortcutId)
        .when()
            .put("/api/accounts/{accountId}/shortcuts/{shortcut}")
        .then()
            .statusCode(409);
    }

    @Test
    void newly_created_account_has_no_shortcuts() {
        String name = "acc123";

        CreateAccountRequest createAccountRequest = createAccountRequestBuilder()
                .withName(name)
                .withMaxShortcuts(1)
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
            .hasNoRegisteredShortcuts();
    }

    private static String generateRandomString() {
        return RandomStringUtils.randomAlphanumeric(5);
    }

}
