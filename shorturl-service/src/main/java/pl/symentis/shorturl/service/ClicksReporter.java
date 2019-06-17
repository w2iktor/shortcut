package pl.symentis.shorturl.service;

import static pl.symentis.shorturl.domain.ClickBuilder.clickBuilder;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import eu.bitwalker.useragentutils.UserAgent;
import pl.symentis.shorturl.dao.ClickRepository;
import pl.symentis.shorturl.domain.Click;

@Component
public class ClicksReporter {

	private final ClickRepository clickRepository;

	public ClicksReporter(ClickRepository clickRepository) {
		this.clickRepository = clickRepository;
	}
	
	@Async
	public CompletableFuture<Click> reportClick(DecodeShortcutRequest decodeShortcutRequest){
		return CompletableFuture.supplyAsync(() -> {
			UserAgent userAgent = new UserAgent(decodeShortcutRequest.getAgent());

			Click click = clickBuilder()
					.withAgent(userAgent.getBrowser().getName())
					.withOs(userAgent.getOperatingSystem().getName())
					.withIpAddress(decodeShortcutRequest.getIpAddress())
					.withLocalDateTime(LocalDateTime.now())
					.withReferer(decodeShortcutRequest.getReferer())
					.withShortcut(decodeShortcutRequest.getShortcut())
					.build();
			clickRepository.save(click);
			return click;
		});
	}

}
