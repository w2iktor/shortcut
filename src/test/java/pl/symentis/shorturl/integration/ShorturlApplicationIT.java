package pl.symentis.shorturl.integration;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import java.net.URI;
import java.net.URL;

import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;

import pl.symentis.shorturl.api.AccountResponse;
import pl.symentis.shorturl.api.CreateAccountRequest;
import pl.symentis.shorturl.api.CreateShortcutRequest;
import pl.symentis.shorturl.api.RedirectsExpiryPolicy;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ShorturlApplicationIT {

	private RestTemplate restTemplate = new RestTemplate();

	@LocalServerPort
	int port;
	
	@Autowired
	ObjectMapper objectMapper;

	@Before
	public void setUp() {
		RestAssured.port = port;
		RestAssured.basePath = "/api";
	}

	@Test
	public void swagger_is_accessible() {

		UriComponentsBuilder builder = fromHttpUrl("http://localhost").port(port).path("/api/swagger.json");
		ResponseEntity<String> forEntity = restTemplate.getForEntity(builder.toUriString(), String.class);
		assertThat(forEntity.getStatusCode()).as("swagger.json not found").isEqualTo(HttpStatus.OK);
	}

	@Test
	public void swagger_ui_is_accessible() {
		
		UriComponentsBuilder builder = fromHttpUrl("http://localhost").port(port).path("/swagger-ui/index.html");
		ResponseEntity<String> forEntity = restTemplate.getForEntity(builder.toUriString(), String.class);
		assertThat(forEntity.getStatusCode()).as("swagger-ui not found").isEqualTo(HttpStatus.OK);
		
	}

	@Test
	public void generate_shorturl() throws Exception {
		String location = given()
		.contentType("application/json")
		.body(new CreateShortcutRequest(new URL("http://onet.pl"), new RedirectsExpiryPolicy(1)))
		.when()
		.post("/accounts/1/shortcuts")
		.then()
		.statusCode(201)
		.header("Location", startsWith("http://localhost:"+port))
		.extract()
		.header("Location");
		
		System.out.println(location);
		
		given()
		.redirects().follow(false)
		.when()
		.get(new URL(location))
		.then()
		.statusCode(301)
		.header("Location", equalTo("http://onet.pl"));
	}
	
	@Test
	public void create_shorturl() throws Exception {
		String location = given()
		.basePath("/api")
		.contentType("application/json")
		.body(new CreateShortcutRequest(new URL("http://onet.pl"), new RedirectsExpiryPolicy(1)))
		.when()
		.put("/accounts/1/shortcuts/2")
		.then()
		.statusCode(201)
		.header("Location", startsWith("http://localhost:"+port))
		.extract()
		.header("Location");
		
		given()
		.redirects().follow(false)
		.basePath("/api")
		.when()
		.get(new URL(location))
		.then()
		.statusCode(301)
		.header("Location", equalTo("http://onet.pl"));
	}
	
	@Test
	public void dont_allow_to_create_two_shortcuts() throws Exception {
		given()
		.basePath("/api")
		.contentType("application/json")
		.body(new CreateShortcutRequest(new URL("http://onet.pl"), new RedirectsExpiryPolicy(1)))
		.when()
		.put("/accounts/1/shortcuts/1")
		.then()
		.statusCode(201);
		
		given()
		.basePath("/api")
		.contentType("application/json")
		.body(new CreateShortcutRequest(new URL("http://wp.pl"), new RedirectsExpiryPolicy(1)))
		.when()
		.put("/accounts/1/shortcuts/1")
		.then()
		.statusCode(409);
	}

	@Test
	public void create_new_account() throws Exception {
		CreateAccountRequest accountRequest = new CreateAccountRequest("acc123", "account@account.com", "taxnumber",1);
		String location = given()
		.contentType("application/json")
		.body(accountRequest)
		.when()
		.post("/accounts/")
		.then()
		.statusCode(201)
		.header("Location", equalTo("http://localhost:"+port+"/api/accounts/acc123"))
		.extract()
		.header("Location");
		
		AccountResponse accountResponse = when()
		.get(URI.create(location))
		.then()
		.statusCode(Status.OK.getStatusCode())
		.extract()
		.as(AccountResponse.class);
		
		assertThat(accountResponse).isEqualToIgnoringGivenFields(accountRequest,"currentShortcuts");
	}

}
