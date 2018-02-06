package pl.symentis.shorturl.api;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("redirects")
public class RedirectsExpiryPolicy implements ExpiryPolicy {

	@Range(min = 1)
	private final long max;

	@JsonCreator
	public RedirectsExpiryPolicy(@JsonProperty("max") long max) {
		this.max = max;
	}

	public long getMax() {
		return max;
	}

}
