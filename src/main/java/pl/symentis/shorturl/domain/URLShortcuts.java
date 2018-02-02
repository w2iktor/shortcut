package pl.symentis.shorturl.domain;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Component
public class URLShortcuts {

	private final Cache<String, URL> cache;

	public URLShortcuts() {
		cache = CacheBuilder.newBuilder().build();
	}

	public String encode(URL url, String remoteIP) throws NoSuchAlgorithmException {

		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		String s = format("%s;%s;%d", remoteIP, url.toExternalForm(), currentTimeMillis());

		byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
		String shortcut = new Base32().encodeToString(hash);

		cache.put(shortcut, url);

		return shortcut;

	}

	public Optional<URL> decode(String shortcut) {
		return Optional.ofNullable(cache.getIfPresent(shortcut));
	}
}
