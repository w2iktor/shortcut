package pl.symentis.shorturl.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.jayway.restassured.RestAssured;

import pl.symentis.shorturl.api.URLShortcutRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ShorturlApplicationIT {

	private RestTemplate restTemplate = new RestTemplate();

	@LocalServerPort
	int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
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
		.basePath("/api")
		.contentType("application/json")
		.body(new URLShortcutRequest(new URL("http://onet.pl")))
		.when()
		.put("/accounts/shortcode")
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
	
}
