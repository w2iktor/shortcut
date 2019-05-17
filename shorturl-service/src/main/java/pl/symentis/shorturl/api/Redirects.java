package pl.symentis.shorturl.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pl.symentis.shorturl.domain.ExpiryPolicy;
import pl.symentis.shorturl.domain.Shortcut;
import pl.symentis.shorturl.service.ClicksReporter;
import pl.symentis.shorturl.service.ShortcutsRegistry;

@RestController
@RequestMapping(path = "/redirects")
@Api
public class Redirects {
	
	private final ShortcutsRegistry urlShortcuts;
	private final ClicksReporter clicksReporter;
	
	@Autowired
	public Redirects(ShortcutsRegistry urlShortcuts, ClicksReporter clicksReporter) {
		this.urlShortcuts = urlShortcuts;
		this.clicksReporter = clicksReporter;
	}

	@GetMapping ( 
	    path = "{shortcut}"
	)
	@ApiOperation("redirects caller to a URL based in provided short code")
	public ResponseEntity<Void> get(
			@PathVariable("shortcut") String shortcut,
			@RequestHeader("User-Agent") String agent,
			@RequestHeader( name = "Referer", required = false) String referer,
			HttpServletRequest httpRequest
	    ) throws MalformedURLException {

		ResponseEntity<Void> response = urlShortcuts
				.decode(shortcut)
				.flatMap(this::isValidShortcut)
				.map(Shortcut::getUrl)
				.map(url -> ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("Location", url.toString()))
				.orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND))
				.build();
		
		if(response.getStatusCode()==HttpStatus.MOVED_PERMANENTLY) {
			URL refererURL = null;
			if(!Strings.isNullOrEmpty(referer)) {
				refererURL = new URL(referer);
			}
			clicksReporter.reportClick(agent, httpRequest.getRemoteAddr(), refererURL, shortcut);
		}
		
		return response;
	}
	
	private Optional<Shortcut> isValidShortcut(Shortcut shortcut){
		
		ExpiryPolicy expiryPolicy = shortcut.getExpiryPolicy();
		
		if(!expiryPolicy.isValidShortcut(shortcut)) {
			return Optional.empty();
		}
				 
		return Optional.of(shortcut);
	}
	
}
