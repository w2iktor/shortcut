package pl.symentis.shorturl.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.symentis.shorturl.service.DecodeShortcutRequest;
import pl.symentis.shorturl.service.RedirectsService;

import javax.servlet.http.HttpServletRequest;

import static pl.symentis.shorturl.service.DecodeShortcutRequestBuilder.decodeShortcutRequestBuilder;

@RestController
@RequestMapping(path = "/redirects")
@Api
public class Redirects {

    private final RedirectsService redirectsService;

    @Autowired
    public Redirects(RedirectsService redirectsService) {

        this.redirectsService = redirectsService;
    }

    @GetMapping(
            path = "{shortcut}"
    )
    @ApiOperation("redirects caller to a URL based in provided short code")
    public ResponseEntity<Void> get(
            @PathVariable("shortcut") String shortcut,
            @RequestHeader("User-Agent") String agent,
            @RequestHeader(name = "Referer", required = false) String referer,
            HttpServletRequest httpRequest
    ) {

        DecodeShortcutRequest decodeShortcutRequest = decodeShortcutRequestBuilder()
                .withShortcut(shortcut)
                .withAgent(agent)
                .withIpAddress(httpRequest.getRemoteAddr())
                .withReferer(referer)
                .build();

        return redirectsService
                .getShortcutTargetUrl(decodeShortcutRequest)
                .map(url -> ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("Location", url.toString()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND))
                .build();
    }

}
