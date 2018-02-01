package pl.symentis.shorturl.api;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class URLShortcutRequest {

	private final URL url;

	@JsonCreator
	public URLShortcutRequest(@JsonProperty("url") URL url) {
		super();
		this.url = url;
	}

	public URL getUrl() {
		return url;
	}

}
