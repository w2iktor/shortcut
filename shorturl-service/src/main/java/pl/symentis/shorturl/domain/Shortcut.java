package pl.symentis.shorturl.domain;

import java.net.URL;

public class Shortcut {

	URL url;
	String account;

	public Shortcut(URL url, String account) {
		this.url = url;
		this.account = account;
	}

}