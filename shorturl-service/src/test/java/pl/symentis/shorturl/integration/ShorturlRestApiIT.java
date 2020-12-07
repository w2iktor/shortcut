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
    void newly_created_account_has_no_shortcuts() {
        // use POST on "/api/accounts" with CreateAccountRequest object as a entity in order to create account
        // verify if created account has no registered shourcuts
    }

    @Test
    void create_shorturl_add_shortcut_to_account() throws Exception {
        // use TestAccountProvider to save account in DB
        // use PUT on "/api/accounts/{accountId}/shortcuts/{shortcut}" in order to register shortcut
        // read account from DB to verify if it contains expected shortcut - to do that you could use
        // ExtendedAccountAssert#hasOnlyShortcuts
    }

    @Test
    void dont_allow_to_create_two_shortcuts_with_same_same_id() throws Exception {
        // use test provider to save account with one shortcut registered
        // use PUT on "/api/accounts/{accountId}/shortcuts/{shortcut}" to register duplicated shortcut
        // verify if response contains status code: Conflicted
    }

    @Test
    void generated_shorturl_is_accessible_and_redirect_to_given_url() {
        // use TestAccountProvider to save account in DB
        // remember to use CreateShortcutRequest for shurtcut creating action
        // verify that create shortcut request returns status CREATED and "Location" header started from "http://localhost:"
        // verify GET request on header value returns status 301 and "Location" header pointed to original url

        // following snippet instructs RestAssured to do not follow redirections
//        given()
//            .redirects()
//                .follow(false)
    }

    private static String generateRandomString() {
        return RandomStringUtils.randomAlphanumeric(5);
    }

}
