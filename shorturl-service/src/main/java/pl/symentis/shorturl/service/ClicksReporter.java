package pl.symentis.shorturl.service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import pl.symentis.shorturl.dao.ClickRepository;
import pl.symentis.shorturl.domain.Click;

@Component
public class ClicksReporter {

	private final ClickRepository clickRepository;

	public ClicksReporter(ClickRepository clickRepository) {
		this.clickRepository = clickRepository;
	}
	
	@Async
	public CompletableFuture<Click> reportClick(
			String agent, 
			String ipAddress, 
			URL referer, 
			String shortcut){
		return CompletableFuture.supplyAsync(() -> {
			Click click = new Click();
			click.setAgent(agent);
			click.setIpAddress(ipAddress);
			click.setLocalDateTime(LocalDateTime.now());
			click.setReferer(referer);
			click.setShortcut(shortcut);
			clickRepository.save(click);
			return click;
		});
	}
	
}
