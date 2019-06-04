package pl.symentis.shorturl.service;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.toList;
import static pl.symentis.shorturl.domain.ShortcutBuilder.shortcutBuilder;

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

import pl.symentis.shorturl.api.DateTimeExpiryPolicyData;
import pl.symentis.shorturl.api.ExpiryPolicyData;
import pl.symentis.shorturl.api.RedirectsExpiryPolicyData;
import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.dao.ShortcutRepository;
import pl.symentis.shorturl.domain.*;

@Component
public class ShortcutsService {

	private final AccountRepository accountRepository;
	private final ShortcutRepository shortcutRepository;

	@Autowired
	public ShortcutsService(AccountRepository accountRepository, ShortcutRepository shortcutRepository) {
		super();
		this.accountRepository = accountRepository;
		this.shortcutRepository = shortcutRepository;
	}

	public Shortcut create(String accountName, URL targetUrl, ExpiryPolicyData policyData, String shortcutCode) {

		boolean shortcutAlreadyExists = accountRepository.checkIfShortcutExists(shortcutCode);

		if (shortcutAlreadyExists) {
			throw new ConflictException(
					format("There is shortcut registered with path %s for another account", shortcutCode));
		}



		ExpiryPolicy policy = null;
		if(policyData instanceof RedirectsExpiryPolicyData) {
			policy = new RedirectsExpiryPolicy(((RedirectsExpiryPolicyData)policyData).getMax());
		}else if (policyData instanceof DateTimeExpiryPolicyData) {
			policy = new DateTimeExpiryPolicy(((DateTimeExpiryPolicyData)policyData).getValidUntil());
		} else {
			policy = accountRepository.findById(accountName)
					.map(Account::getDefaultExpiryPolicy)
					.orElse(null);
		}
		
		Shortcut value = shortcutBuilder()
			.withShortcut(shortcutCode)
			.withUrl(targetUrl)
			.withExpiryPolicy(policy)
			.build();
		shortcutRepository.addShortcut(accountName, value);

		return value;
	}

	public String generate(String accountName, URL targetUrl, ExpiryPolicyData policyData) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		String s = format("%s;%s;%d", policyData, targetUrl.toExternalForm(), currentTimeMillis());

		byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
		String shortcutCode = new Base32().encodeToString(hash);


		Shortcut shortcut = create(accountName, targetUrl, policyData, shortcutCode);
		return shortcut.getShortcut();

	}

	public Optional<Shortcut> decode(String shortcut) {
		return shortcutRepository
				.findByShortcut(shortcut)
				.filter(Shortcut::isValid);
	}

	public List<URL> urlsByAccount(String accountName) {
		return accountRepository.findById(accountName)
				.map(Account::getShortcuts)
				.orElseGet(Collections::emptyList)
				.stream()
				.map(Shortcut::getUrl)
				.collect(toList());
	}
}
