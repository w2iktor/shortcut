package pl.symentis.shorturl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.symentis.shorturl.domain.ExpiryPolicy;
import pl.symentis.shorturl.domain.Shortcut;

import java.net.URL;
import java.util.Optional;

@Service
public class DefaultRedirectsService implements RedirectsService {

    private final ShortcutsService shortcutsService;
    private final ClicksReporter clicksReporter;

    @Autowired
    DefaultRedirectsService(ShortcutsService shortcutsService, ClicksReporter clicksReporter) {
        this.shortcutsService = shortcutsService;
        this.clicksReporter = clicksReporter;
    }

    @Override
    public Optional<URL> getShortcutTargetUrl(DecodeShortcutRequest decodeShortcutRequest) {
        Optional<URL> shortcutTargetUrl = shortcutsService
                .decode(decodeShortcutRequest.getShortcut())
                .flatMap(this::isValidShortcut)
                .map(Shortcut::getUrl);

        shortcutTargetUrl.ifPresent(url -> clicksReporter.reportClick(decodeShortcutRequest));

        return shortcutTargetUrl;
    }

    private Optional<Shortcut> isValidShortcut(Shortcut shortcut){

        ExpiryPolicy expiryPolicy = shortcut.getExpiryPolicy();

        if(!expiryPolicy.isValidShortcut(shortcut)) {
            return Optional.empty();
        }

        return Optional.of(shortcut);
    }
}
