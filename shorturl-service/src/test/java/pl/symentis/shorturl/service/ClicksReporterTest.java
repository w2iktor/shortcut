package pl.symentis.shorturl.service;

import com.devskiller.jfairy.Fairy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.symentis.shorturl.dao.ClickRepository;
import pl.symentis.shorturl.domain.Click;
import pl.symentis.shorturl.domain.Shortcut;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.with;
import static org.mockito.Mockito.mock;
import static pl.symentis.shorturl.assertion.Assertions.assertThat;
import static pl.symentis.shorturl.domain.FakeShortcutBuilder.fakeShortcutBuilder;
import static pl.symentis.shorturl.service.DecodeShortcutRequestBuilder.decodeShortcutRequestBuilder;

class ClicksReporterTest {
    private static Fairy fairy;

    @BeforeAll
    static void beforeAll(){
        fairy = Fairy.create();
    }

    @Test
    void report_click_returns_properly_mapped_click() throws MalformedURLException {
        // use Awaitility to wait for a asynchronous operation
    }

}