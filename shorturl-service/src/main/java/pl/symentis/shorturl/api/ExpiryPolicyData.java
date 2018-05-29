package pl.symentis.shorturl.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use=Id.NAME,property="policy")
@JsonSubTypes({
	@Type(value=RedirectsExpiryPolicyData.class),
	@Type(value=DateTimeExpiryPolicyData.class)})
public interface ExpiryPolicyData {

}
