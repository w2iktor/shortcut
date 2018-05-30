package pl.symentis.shorturl.service;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.symentis.shorturl.api.CreateShortcutRequest;
import pl.symentis.shorturl.api.DateTimeExpiryPolicyData;
import pl.symentis.shorturl.api.ExpiryPolicyData;
import pl.symentis.shorturl.api.RedirectsExpiryPolicyData;
import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.dao.ShortcutRepository;
import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.domain.DateTimeExpiryPolicy;
import pl.symentis.shorturl.domain.ExpiryPolicy;
import pl.symentis.shorturl.domain.RedirectsExpiryPolicy;
import pl.symentis.shorturl.domain.Shortcut;

@Component
public class ShortcutsRegistry {

	private final AccountRepository accountRepository;
	private final ShortcutRepository shortcutRepository;

	@Autowired
	public ShortcutsRegistry(AccountRepository accountRepository, ShortcutRepository shortcutRepository) {
		super();
		this.accountRepository = accountRepository;
		this.shortcutRepository = shortcutRepository;
	}

	public Shortcut create(CreateShortcutRequest shortcutReqs, String accountName, String shortcutCode) {

		Account account = accountRepository.findAccountWithShortcut(accountName, shortcutCode);

		if (account != null) {
			throw new ConflictException(
					format("There is shortcut registered with path %s for another account", shortcutCode));
		}

		ExpiryPolicyData policyData = shortcutReqs.getExpiry();
		
		ExpiryPolicy policy = null;
		if(policyData instanceof RedirectsExpiryPolicyData) {
			policy = new RedirectsExpiryPolicy(((RedirectsExpiryPolicyData)policyData).getMax());
		}else if (policyData instanceof DateTimeExpiryPolicyData) {
			policy = new DateTimeExpiryPolicy(((DateTimeExpiryPolicyData)policyData).getValidUntil());
		}
		
		Shortcut value = new Shortcut(shortcutCode,shortcutReqs.getUrl(), policy);
		shortcutRepository.addShortcut(accountName, shortcutCode, value);

		return value;
	}

	public String generate(CreateShortcutRequest shortcutReqs, String remoteIP, String accountName) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		String s = format("%s;%s;%d", remoteIP, shortcutReqs.getUrl().toExternalForm(), currentTimeMillis());

		byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
		String shortcutCode = new Base32().encodeToString(hash);

		ExpiryPolicy policy = null;
		ExpiryPolicyData policyData = shortcutReqs.getExpiry();
		if(policyData instanceof RedirectsExpiryPolicyData) {
			policy = new RedirectsExpiryPolicy(((RedirectsExpiryPolicyData)policyData).getMax());
		}else if (policyData instanceof DateTimeExpiryPolicyData) {
			policy = new DateTimeExpiryPolicy(((DateTimeExpiryPolicyData)policyData).getValidUntil());
		}

		
		Shortcut value = new Shortcut(shortcutCode,shortcutReqs.getUrl(), policy);
		shortcutRepository.addShortcut(accountName, shortcutCode, value);

		return shortcutCode;

	}

	public Optional<URL> decode(String shortcut) {
		return shortcutRepository.getURL(shortcut).map(Shortcut::getUrl);
	}

	public List<URL> urlsByAccount(String accountName) {
		return Optional.ofNullable(accountRepository.findOne(accountName))
				.map(Account::getShortcuts)
				.orElseGet(Collections::emptyList)
				.stream()
				.map(Shortcut::getUrl)
				.collect(toList());
	}
}
