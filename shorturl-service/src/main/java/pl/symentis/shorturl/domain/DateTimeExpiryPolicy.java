package pl.symentis.shorturl.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("datetime")
public class DateTimeExpiryPolicy implements ExpiryPolicy {

	private final LocalDateTime validUntil;

	public DateTimeExpiryPolicy(LocalDateTime validUntil) {
		this.validUntil = validUntil;
	}

	public LocalDateTime getValidUntil() {
		return validUntil;
	}

}
