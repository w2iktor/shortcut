package pl.symentis.shorturl.domain;

import java.net.URL;
import java.util.Objects;
import java.util.StringJoiner;

import org.springframework.data.mongodb.core.mapping.Field;

public class Shortcut {

	private String shortcut;
	private URL url;
	private ExpiryPolicy expiryPolicy;
	@Field("counter")
	private long decodeCounter;

	Shortcut(String shortcut, URL url, ExpiryPolicy expiryPolicy) {
		this.shortcut = shortcut;
		this.url = url;
		this.expiryPolicy = expiryPolicy;
		this.decodeCounter = 0L;
	}

	public String getShortcut() {
		return shortcut;
	}

	public URL getUrl() {
		return url;
	}

	public ExpiryPolicy getExpiryPolicy() {
		return expiryPolicy;
	}

	public long getDecodeCounter() {
		return decodeCounter;
	}

	public void incrementDecodeCounter() {
		this.decodeCounter++;
	}

	public boolean isValid() {
		return expiryPolicy.isValidShortcut(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Shortcut)) return false;
		Shortcut shortcut1 = (Shortcut) o;
		return getDecodeCounter() == shortcut1.getDecodeCounter() &&
			Objects.equals(getShortcut(), shortcut1.getShortcut()) &&
			getUrl().equals(shortcut1.getUrl()) &&
			getExpiryPolicy().equals(shortcut1.getExpiryPolicy());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getShortcut(), getUrl(), getExpiryPolicy(), getDecodeCounter());
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Shortcut.class.getSimpleName() + "[", "]")
			.add("shortcut='" + shortcut + "'")
			.add("url=" + url)
			.add("expiryPolicy=" + expiryPolicy)
			.add("decodeCounter=" + decodeCounter)
			.toString();
	}
}