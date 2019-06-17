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
import java.util.concurrent.ExecutionException;

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
    void report_click_returns_properly_mapped_click() throws ExecutionException, InterruptedException, MalformedURLException {
        // given
        String agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Shortcut shortcut = fakeShortcutBuilder()
                .withValidExpiryPolicy()
                .build();


        String ipAddress = null;
        String referer = null;
        CompletableFuture<Click> reportClickResult = null;



        Click reportedClick = reportClickResult.getNow(null);
        assertThat(reportedClick)
                .hasAgent("Chrome")
                .hasOs("Windows 10")
                .hasIpAddress(ipAddress)
                .hasReferer(new URL(referer));
    }

}