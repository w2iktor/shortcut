package pl.symentis.shorturl.service;

import pl.symentis.shorturl.api.ExpiryPolicyData;
import pl.symentis.shorturl.domain.Shortcut;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public interface ShortcutsService {

	Shortcut create(String accountName, URL targetUrl, ExpiryPolicyData policyData, String shortcutCode);

	String generate(String accountName, URL targetUrl, ExpiryPolicyData policyData) throws NoSuchAlgorithmException ;

	Optional<Shortcut> decode(String shortcut) ;
	List<URL> urlsByAccount(String accountName) ;
}
