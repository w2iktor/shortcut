package pl.symentis.shorturl.domain;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Component
public class ShortcutsRegistry {

	class Shortcut {

		URL url;
		String account;

		public Shortcut(URL url, String account) {
			this.url = url;
			this.account = account;
		}

	}

	private final Cache<String, Shortcut> cache;

	public ShortcutsRegistry() {
		cache = CacheBuilder.newBuilder().build();
	}

	public int putOnPath(URL url, String account, String userShortcut){
		String shortcutAccount = ofNullable(cache.getIfPresent(userShortcut))
				.map(s -> s.account)
				.orElse(null);
		if (shortcutAccount != null && ! StringUtils.equals(account, shortcutAccount)){
			throw new ConflictException(format("There is shortcut register with path %s for another account", userShortcut));
		}
		cache.put(userShortcut, new Shortcut(url, account));

		if(shortcutAccount == null){
			return HttpStatus.CREATED.value();
		}
		return HttpStatus.OK.value();
	}

	public String encode(URL url, String account, String remoteIP) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		String s = format("%s;%s;%d", remoteIP, url.toExternalForm(), currentTimeMillis());

		byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
		String shortcut = new Base32().encodeToString(hash);

		cache.put(shortcut, new Shortcut(url, account));

		return shortcut;

	}

	public Optional<URL> decode(String shortcut) {
		return ofNullable(cache.getIfPresent(shortcut)).map(s -> s.url);
	}

	public List<URL> byAccount(String account) {
		return cache
				.asMap()
				.values()
				.stream()
				.filter( s-> s.account.equals(account))
				.map( s-> s.url).collect(toList());
	}
}
