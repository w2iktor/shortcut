package pl.symentis.shorturl.api;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.symentis.shorturl.domain.URLShortcuts;

@Path("accounts")
@Component
@Api
public class Accounts {
	
	private final URLShortcuts urlShortcuts;
	
	@Autowired
	public Accounts(URLShortcuts urlShortcuts) {
		super();
		this.urlShortcuts = urlShortcuts;
	}

	@PUT
	@Path("shortcode")
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
}
