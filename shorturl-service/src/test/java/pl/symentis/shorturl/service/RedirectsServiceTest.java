package pl.symentis.shorturl.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pl.symentis.shorturl.dao.ClickRepository;
import pl.symentis.shorturl.domain.Shortcut;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.symentis.shorturl.domain.FakeShortcutBuilder.fakeShortcutBuilder;
import static pl.symentis.shorturl.service.DecodeShortcutRequestBuilder.decodeShortcutRequestBuilder;

class RedirectsServiceTest {

    @Test
    void click_was_reported_and_pass_to_repository_after_properly_shortcut_decode(){
        // use spy on repository
    }

    @Test
    void click_was_reported_with_given_decode_request_after_properly_shortcut_decode(){
        // use argument capture to verify click reporting
    }

}