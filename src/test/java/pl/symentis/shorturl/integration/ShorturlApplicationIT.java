package pl.symentis.shorturl.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ShorturlApplicationIT {

	
	private RestTemplate restTemplate = new RestTemplate();
	
	@LocalServerPort
	int port;
	
	@Test
	public void swagger_is_accessible() {
		
		UriComponentsBuilder builder = fromHttpUrl("http://localhost").port(port).path("/api/swagger.json");
		ResponseEntity<String> forEntity = restTemplate.getForEntity(builder.toUriString(),String.class);
		assertThat(forEntity.getStatusCode()).as("swagger.json not found").isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void swagger_ui_is_accessible() {
		
		UriComponentsBuilder builder = fromHttpUrl("http://localhost").port(port).path("/swagger-ui/index.html");
		ResponseEntity<String> forEntity = restTemplate.getForEntity(builder.toUriString(),String.class);
		assertThat(forEntity.getStatusCode()).as("swagger-ui not found").isEqualTo(HttpStatus.OK);
	}

}
