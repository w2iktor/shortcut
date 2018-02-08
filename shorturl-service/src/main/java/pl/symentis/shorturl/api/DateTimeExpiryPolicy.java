package pl.symentis.shorturl.api;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("datetime")
public class DateTimeExpiryPolicy implements ExpiryPolicy {

	private final LocalDateTime validUntil;

	@JsonCreator
	public DateTimeExpiryPolicy(@JsonProperty("validUntil") LocalDateTime validUntil) {
		this.validUntil = validUntil;
	}

	public LocalDateTime getValidUntil() {
		return validUntil;
	}

}
