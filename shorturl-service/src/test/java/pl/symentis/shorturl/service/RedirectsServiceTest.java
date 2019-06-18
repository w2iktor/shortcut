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

    // using spy
    @Test
    void click_was_reported_and_pass_to_repository_after_properly_shortcut_decode(){
        // given
        Shortcut shortcut = fakeShortcutBuilder()
                .withValidExpiryPolicy()
                .build();
        DecodeShortcutRequest decodeShortcutRequest = decodeShortcutRequestBuilder()
                .withShortcut(shortcut.getShortcut())
                .build();

        ClickRepository clickRepository = mock(ClickRepository.class);
        ClicksReporter spyClicksReporter = spy(new ClicksReporter(clickRepository));
        ShortcutsService shortcutsService = mock(ShortcutsService.class);

        when(shortcutsService.decode(decodeShortcutRequest.getShortcut()))
                .thenReturn(Optional.of(shortcut));

        RedirectsService sut = new DefaultRedirectsService(shortcutsService, spyClicksReporter);

        // when
        sut.getShortcutTargetUrl(decodeShortcutRequest);

        // then
        verify(spyClicksReporter)
                .reportClick(decodeShortcutRequest);

        // and
        verify(clickRepository, timeout(Duration.ofSeconds(5).toMillis()))
                .save(any());
    }

    @Test
    void click_was_reported_with_given_decode_request_after_properly_shortcut_decode(){
        // given
        Shortcut shortcut = fakeShortcutBuilder()
                .withValidExpiryPolicy()
                .build();
        DecodeShortcutRequest decodeShortcutRequest = decodeShortcutRequestBuilder()
                .withShortcut(shortcut.getShortcut())
                .build();

        ClicksReporter clicksReporter = mock(ClicksReporter.class);
        ShortcutsService shortcutsService = mock(ShortcutsService.class);

        RedirectsService sut = new DefaultRedirectsService(shortcutsService, clicksReporter);

        when(shortcutsService.decode(decodeShortcutRequest.getShortcut()))
                .thenReturn(Optional.of(shortcut));

        // when
        sut.getShortcutTargetUrl(decodeShortcutRequest);
        ArgumentCaptor<DecodeShortcutRequest> argumentCaptor = ArgumentCaptor.forClass(DecodeShortcutRequest.class);


        // then
        verify(clicksReporter)
                .reportClick(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue())
                .isSameAs(decodeShortcutRequest);
    }

}