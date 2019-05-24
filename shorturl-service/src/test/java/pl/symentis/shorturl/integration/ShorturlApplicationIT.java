package pl.symentis.shorturl.integration;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import java.net.URI;
import java.net.URL;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.mongodb.MongoClient;

import pl.symentis.shorturl.api.GetAccountResponse;
import pl.symentis.shorturl.api.CreateAccountRequest;
import pl.symentis.shorturl.api.CreateShortcutRequest;
import pl.symentis.shorturl.api.RedirectsExpiryPolicyData;
import pl.symentis.shorturl.integration.assertions.ExtendedAccountResponseAssert;

@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ShorturlApplicationIT {

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

  @Test
  public void swagger_is_accessible() {
    UriComponentsBuilder builder = fromHttpUrl("http://localhost").port(port).path("/v2/api-docs");
    ResponseEntity<String> forEntity = restTemplate.getForEntity(builder.toUriString(), String.class);
    assertThat(forEntity.getStatusCode()).as("swagger.json not found").isEqualTo(HttpStatus.OK);
  }

  @Test
  public void swagger_ui_is_accessible() {
    UriComponentsBuilder builder = fromHttpUrl("http://localhost").port(port).path("/swagger-ui.html");
    ResponseEntity<String> forEntity = restTemplate.getForEntity(builder.toUriString(), String.class);
    assertThat(forEntity.getStatusCode()).as("swagger-ui not found").isEqualTo(HttpStatus.OK);

  }

  @Test
  public void generate_shorturl() throws Exception {

    String accountId = generateRandomString();

    given()
      .contentType("application/json")
      .body(new CreateAccountRequest(accountId, "account@account.com", "taxnumber", 1))
      .when()
      .post("/api/accounts/")
      .then()
      .statusCode(201);

    String location = given()
        .contentType("application/json")
        .body(new CreateShortcutRequest(new URL("http://onet.pl"), new RedirectsExpiryPolicyData(1)))
        .pathParam("accountId", accountId)
        .when()
        .post("/api/accounts/{accountId}/shortcuts")
        .then()
        .statusCode(201)
        .header("Location", startsWith("http://localhost:" + port))
        .extract()
        .header("Location");

    given().
      redirects()
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

    given()
      .contentType("application/json")
      .body(new CreateAccountRequest(accountId, "account@account.com", "taxnumber", 1))
      .when()
      .post("/api/accounts/")
      .then()
      .statusCode(201);

    String location = given()
        .contentType("application/json")
        .body(new CreateShortcutRequest(new URL("http://onet.pl"), new RedirectsExpiryPolicyData(1)))
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

    given()
      .contentType("application/json")
      .body(new CreateAccountRequest(accountId, "account@account.com", "taxnumber", 1))
      .when()
      .post("/api/accounts/")
      .then()
      .statusCode(201);

    given()
      .contentType("application/json")
      .body(new CreateShortcutRequest(new URL("http://onet.pl"), new RedirectsExpiryPolicyData(1)))
      .pathParam("accountId", accountId)
      .pathParam("shortcut", shortcut)
      .when()
      .put("/api/accounts/{accountId}/shortcuts/{shortcut}")
      .then()
      .statusCode(201);

    given()
      .contentType("application/json")
      .body(new CreateShortcutRequest(new URL("http://wp.pl"), new RedirectsExpiryPolicyData(1)))
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
    CreateAccountRequest accountRequest = new CreateAccountRequest(name, email, "taxnumber", 1);
    String location = given()
        .contentType("application/json")
        .body(accountRequest)
        .when()
        .post("/api/accounts")
        .then()
        .statusCode(201)
        .header("Location", equalTo("http://localhost:" + port + "/api/accounts/acc123"))
        .extract()
        .header("Location");

    GetAccountResponse accountResponse = when().
        get(URI.create(location))
        .then().
        statusCode(HttpStatus.OK.value())
        .extract()
        .as(GetAccountResponse.class);

    ExtendedAccountResponseAssert
      .assertThat(accountResponse)
      .hasEmail(email)
      .hasName(name)
      .hasNoRegisteredShortcuts();
  }

  private static String generateRandomString() {
    return RandomStringUtils.randomAlphanumeric(5);
  }

}
