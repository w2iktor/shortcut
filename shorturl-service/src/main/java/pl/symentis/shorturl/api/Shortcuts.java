package pl.symentis.shorturl.api;

import static java.util.stream.Collectors.toList;
import static pl.symentis.shorturl.api.ShortcutStatsResponseBuilder.shortcutStatsResponseBuilder;
import static pl.symentis.shorturl.api.StatsBuilder.statsBuilder;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.service.ShortcutStatsService;
import pl.symentis.shorturl.service.ShortcutsRegistry;

public class Shortcuts {

	private final ShortcutsRegistry shortcutRegitry;
	private final ShortcutStatsService shortcutStatsService;
	private final Account account;

	public Shortcuts(Account account, ShortcutsRegistry shortcutRegistry, ShortcutStatsService shortcutStatsService) {
		this.account = account;
		this.shortcutRegitry = shortcutRegistry;
		this.shortcutStatsService = shortcutStatsService;
	}

	/**
	 * An example of context parameters injections,
	 * like HTTP request or URI info.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value="generate shortcut for URL",
			httpMethod="POST"
			)
	@ApiResponses(
			{
				@ApiResponse(code=201,message="URL shortcut created")
			}
			)
	public Response generateURLShortcut(
			@Context HttpServletRequest httpRequest,
			@Context UriInfo uriInfo,
			CreateShortcutRequest shortcutReqs) {

		try {
			String shortcut = shortcutRegitry.generate(shortcutReqs, httpRequest.getRemoteAddr(),account.getName());
			return Response.created(uriInfo.getRequestUriBuilder().replacePath("/api/shortcuts/{shortcut}").build(shortcut)).build();
		} catch (NoSuchAlgorithmException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * An example of context parameters injections,
	 * like HTTP request or URI info.
	 */
	@PUT
	@Path("{shortcut}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
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
	public Response createURLShortcut(
			@Context HttpServletRequest httpRequest, 
			@Context UriInfo uriInfo,
			@PathParam("shortcut") String shortcut, 
			CreateShortcutRequest shortcutReqs) {

		shortcutRegitry.create(shortcutReqs, account.getName(), shortcut);
		
		return Response
					.created(
							uriInfo.getRequestUriBuilder().replacePath("/api/shortcuts/{shortcut}")
							.build(shortcut)
					)
					.build();

	}

	/**
	 * An example how to deal with partial state updates,
	 * which have some logic in it, like checking if we are not setting validity in the past
	 */
	@PUT
	@Path("{shortcut}/validity")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateShortcutValidity(
			@PathParam("shortcut") String shortcut,
			UpdateShortcutValidityRequest updateShortcutValidity) {
		return Response.accepted().build();
	}

	@GET
	@Path("{shortcut}/stats")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getShortcutStats(
			@PathParam("shortcut") String shortcut) {
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

		return Response.ok(statsResponse).build();
	}

}
