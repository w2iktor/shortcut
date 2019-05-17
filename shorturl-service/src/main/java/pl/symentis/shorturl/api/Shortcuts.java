package pl.symentis.shorturl.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import pl.symentis.shorturl.service.ShortcutsRegistry;

@RestController
@RequestMapping(path="/api/accounts/{accountid}/shortcuts")
public class Shortcuts {

	private final ShortcutsRegistry shortcutRegitry;
  private final ShortcutStatsService shortcutStatsService;

	public Shortcuts(ShortcutsRegistry shortcutRegistry, ShortcutStatsService shortcutStatsService) {
		this.shortcutRegitry = shortcutRegistry;
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
			@RequestBody CreateShortcutRequest shortcutReqs,
			HttpServletRequest httpRequest) throws MalformedURLException {

		try {
			String shortcut = shortcutRegitry.generate(shortcutReqs, httpRequest.getRemoteAddr(),accountid);
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

		shortcutRegitry.create(shortcutReqs, accountid, shortcut);
		
		return ResponseEntity
					.created(
							linkTo(methodOn(Redirects.class).get(shortcut, "", "", null)).toUri()
					).build();

	}
//
//	/**
//	 * An example how to deal with partial state updates,
//	 * which have some logic in it, like checking if we are not setting validity in the past
//	 */
//	@PUT
//	@Path("{shortcut}/validity")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response updateShortcutValidity(
//			@PathParam("shortcut") String shortcut,
//			UpdateShortcutValidityRequest updateShortcutValidity) {
//		return Response.accepted().build();
//	}
//
//	@GET
//	@Path("{shortcut}/stats")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getShortcutStats(
//			@PathParam("shortcut") String shortcut) {
//		List<Stats> agents = shortcutStatsService
//				.getStats(shortcut)
//				.getAgents()
//				.stream()
//				.map(statsCounter -> statsBuilder()
//						.withId(statsCounter.id)
//						.withTotal(statsCounter.total)
//						.build())
//				.collect(toList());
//		ShortcutStatsResponse statsResponse = shortcutStatsResponseBuilder()
//				.withClicks(shortcutStatsService.getStats(shortcut).getClicks())
//				.withAgents(agents)
//				.build();
//
//		return Response.ok(statsResponse).build();
//	}

}
