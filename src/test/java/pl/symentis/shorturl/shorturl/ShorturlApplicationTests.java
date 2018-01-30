package pl.symentis.shorturl.shorturl;

import org.assertj.core.api.Assertions;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ShorturlApplicationTests {

	
	RestTemplate restTemplate = new RestTemplate();
	
	
	@LocalServerPort
	int port;
	
	@Test
	public void contextLoads() {
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost").port(port).path("/api/swagger.json");
		
		ResponseEntity<String> forEntity = restTemplate.getForEntity(builder.toUriString(),String.class);
		Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

}
