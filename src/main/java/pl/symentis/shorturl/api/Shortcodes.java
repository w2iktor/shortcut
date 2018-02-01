package pl.symentis.shorturl.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pl.symentis.shorturl.domain.URLShortcuts;

@Component
@Path("shortcodes")
@Api
public class Shortcodes {
	
	private final URLShortcuts urlShortcuts;
	
	@Autowired
	public Shortcodes(URLShortcuts urlShortcuts) {
		this.urlShortcuts = urlShortcuts;
	}

	@GET
	@Path("{shortcode}")
	@ApiOperation("redirects caller to a URL based in provided short code")
	public Response get(@PathParam("shortcode") String shortcut) {
		return urlShortcuts
				.decode(shortcut)
				.map(url -> Response.status(Status.MOVED_PERMANENTLY).header("Location", url))
				.orElseGet(()->Response.status(Status.NOT_FOUND))
				.build();
	}
}
