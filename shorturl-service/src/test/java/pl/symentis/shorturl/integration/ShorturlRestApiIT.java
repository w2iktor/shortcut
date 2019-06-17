package pl.symentis.shorturl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.mongodb.MongoClient;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

    @Test
    public void generated_shorturl_is_accessible_and_redirect_to_given_url() throws Exception {

        String accountId = generateRandomString();
    }

    @Test
    public void create_shorturl_with_known_shortcut_returns_location_that_redirect_to_given_url() throws Exception {

        String accountId = generateRandomString();
        String shortcut = generateRandomString();

    }

    @Test
    public void create_duplicated_shortcut_returns_conflicted_status() throws Exception {
        // given
        String accountId = generateRandomString();
        String shortcut = generateRandomString();
    }

    @Test
    public void create_new_account_returns_location_of_properly_mapped_account() {
    }

    private static String generateRandomString() {
        return RandomStringUtils.randomAlphanumeric(5);
    }

}
