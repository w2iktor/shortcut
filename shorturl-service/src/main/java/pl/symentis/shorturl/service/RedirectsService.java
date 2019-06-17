package pl.symentis.shorturl.service;

import java.net.URL;
import java.util.Optional;

public interface RedirectsService {
    Optional<URL> getShortcutTargetUrl(DecodeShortcutRequest decodeShortcutRequest);
}
