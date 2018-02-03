package pl.symentis.shorturl.api;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.symentis.shorturl.domain.ShortcutsRegistry;

public class Shortcuts {

	private final ShortcutsRegistry urlShortcuts;
	private final String accountid;

	public Shortcuts(String accountid, ShortcutsRegistry urlShortcuts) {
		this.accountid = accountid;
		this.urlShortcuts = urlShortcuts;
	}
	
	/**
	 * An example of context parameters injections, 
	 * like HTTP request or URI info.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value="create shortcut for URL",
			httpMethod="PUT"
			)
	@ApiResponses(
			{
				@ApiResponse(code=201,message="URL shortcut created")
			}
			)
	public Response createURLShortcut(
			@Context HttpServletRequest httpRequest,
			@Context UriInfo uriInfo,
			URLShortcutRequest shortcutReqs) {
		
		try {
			String shortcut = urlShortcuts.encode(shortcutReqs.getUrl(), httpRequest.getRemoteAddr());
			return Response.created(uriInfo.getRequestUriBuilder().replacePath("/api/shortcodes/{shortcut}").build(shortcut)).build();
		} catch (NoSuchAlgorithmException e) {
			return Response.serverError().build();
		}
		
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

}
