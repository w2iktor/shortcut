package pl.symentis.shorturl.domain;

import org.springframework.data.annotation.TypeAlias;

import java.util.StringJoiner;

@TypeAlias("redirect")
public class RedirectsExpiryPolicy implements ExpiryPolicy {

	private final long max;

	public RedirectsExpiryPolicy(long max) {
		this.max = max;
	}

	public long getMax() {
		return max;
	}

	@Override
	public boolean isValidShortcut(Shortcut shortcut) {
		return shortcut.getDecodeCounter()<=getMax();
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", RedirectsExpiryPolicy.class.getSimpleName() + "[", "]")
				.add("max=" + max)
				.toString();
	}
}
