package pl.symentis.shorturl.shorturl.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;

@Component
@Api
@Path("shortcodes")
public class Shortcodes {
	
	@GET
	public Response get() {
		return Response.ok("ok").build();			
	}
}
