package pl.symentis.shorturl.domain;

import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("redirect")
public class RedirectsExpiryPolicy implements ExpiryPolicy {

	@Range(min = 1)
	private final long max;

	public RedirectsExpiryPolicy(long max) {
		this.max = max;
	}

	public long getMax() {
		return max;
	}

}
