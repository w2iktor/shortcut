package pl.symentis.shorturl.api;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pl.symentis.shorturl.service.ClicksReporter;
import pl.symentis.shorturl.service.ShortcutsRegistry;

@Component
@Path("/")
@Api
public class Redirects {
	
	private final ShortcutsRegistry urlShortcuts;
	private final ClicksReporter clicksReporter;
	
	@Autowired
	public Redirects(ShortcutsRegistry urlShortcuts, ClicksReporter clicksReporter) {
		this.urlShortcuts = urlShortcuts;
		this.clicksReporter = clicksReporter;
	}

	@GET
	@Path("shortcuts/{shortcut}")
	@ApiOperation("redirects caller to a URL based in provided short code")
	public Response get(
			@PathParam("shortcut") String shortcut,
			@HeaderParam("User-Agent") String agent,
			@HeaderParam("Referer") String referer,			
			@Context HttpServletRequest httpRequest) throws MalformedURLException {

		Response response = urlShortcuts
				.decode(shortcut)
				.map(url -> Response.status(Status.MOVED_PERMANENTLY).header("Location", url))
				.orElseGet(()->Response.status(Status.NOT_FOUND))
				.build();
		
		if(response.getStatusInfo()==Status.MOVED_PERMANENTLY) {
			URL refererURL = null;
			if(!Strings.isNullOrEmpty(referer)) {
				refererURL = new URL(referer);
			}
			clicksReporter.reportClick(agent, httpRequest.getRemoteAddr(), refererURL, shortcut);
		}
		
		return response;
	}
	
	
}
