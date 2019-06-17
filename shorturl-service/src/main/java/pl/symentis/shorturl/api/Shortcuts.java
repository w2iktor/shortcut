package pl.symentis.shorturl.api;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static pl.symentis.shorturl.api.ShortcutStatsResponseBuilder.shortcutStatsResponseBuilder;
import static pl.symentis.shorturl.api.StatsBuilder.statsBuilder;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.symentis.shorturl.service.ShortcutStatsService;
import pl.symentis.shorturl.service.ShortcutsService;

@RestController
@RequestMapping(path="/api/accounts/{accountid}/shortcuts")
public class Shortcuts {

	private final ShortcutsService shortcutsService;
  private final ShortcutStatsService shortcutStatsService;

	public Shortcuts(ShortcutsService shortcutsService, ShortcutStatsService shortcutStatsService) {
		this.shortcutsService = shortcutsService;
		this.shortcutStatsService = shortcutStatsService;
	}

	@PostMapping(
	    consumes = MediaType.APPLICATION_JSON_VALUE
	)
	@ApiOperation(
			value="generate shortcut for URL",
			httpMethod="POST"
	)
	@ApiResponses(
			{
				@ApiResponse(code=201,message="URL shortcut created")
			}
	)
	public ResponseEntity<Void> generateURLShortcut(
			@PathVariable("accountid") String accountid,
			@RequestBody CreateShortcutRequest shortcutReqs) throws MalformedURLException {

		try {
			String shortcut = shortcutsService.generate(accountid, shortcutReqs.getUrl(), shortcutReqs.getExpiry());
			return ResponseEntity.created(linkTo(methodOn(Redirects.class).get(shortcut, "", "", null)).toUri()).build();
		} catch (NoSuchAlgorithmException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PutMapping(
	    path = "{shortcut}",
	    consumes = MediaType.APPLICATION_JSON_VALUE,
	    produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(
			value="create shortcut for URL",
			httpMethod="PUT"
	)
	@ApiResponses(
			{
				@ApiResponse(code=201,message="URL shortcut created"),
				@ApiResponse(code=409,message="URL shortcut already exists")
			}
	)
	public ResponseEntity<Void> createURLShortcut(
	    @PathVariable("accountid") String accountid,
			@PathVariable("shortcut") String shortcut, 
			@RequestBody CreateShortcutRequest shortcutReqs) throws MalformedURLException {

		shortcutsService.create(accountid, shortcutReqs.getUrl(), shortcutReqs.getExpiry(), shortcut);
		
		return ResponseEntity
					.created(
							linkTo(methodOn(Redirects.class).get(shortcut, "", "", null)).toUri()
					).build();

	}

	@PutMapping(
	    path = "{shortcut}/validity",
	    consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Void> updateShortcutValidity(
			@PathVariable("shortcut") String shortcut,
			UpdateShortcutValidityRequest updateShortcutValidity) {
		return ResponseEntity.accepted().build();
	}

	@GetMapping(
	    path = "{shortcut}/stats",
	    produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<ShortcutStatsResponse> getShortcutStats(
			@PathVariable("shortcut") String shortcut) {
		List<Stats> agents = shortcutStatsService
				.getStats(shortcut)
				.getAgents()
				.stream()
        .map(statsCounter -> statsBuilder()
						.withId(statsCounter.id)
						.withTotal(statsCounter.total)
						.build())
				.collect(toList());
		ShortcutStatsResponse statsResponse = shortcutStatsResponseBuilder()
				.withClicks(shortcutStatsService.getStats(shortcut).getClicks())
				.withAgents(agents)
				.build();
		return ResponseEntity.ok(statsResponse);
	}

}
