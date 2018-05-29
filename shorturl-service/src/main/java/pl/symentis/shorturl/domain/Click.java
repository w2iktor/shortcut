package pl.symentis.shorturl.domain;

import java.net.URL;
import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="clicks")
public class Click {


	@Id
	private ObjectId id;
	private String shortcut;
	private LocalDateTime localDateTime;
	private String ipAddress;
	private String os;
	private String agent;
	private URL referer;
	
	public Click() {
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getShortcut() {
		return shortcut;
	}
	
	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public URL getReferer() {
		return referer;
	}

	public void setReferer(URL referer) {
		this.referer = referer;
	}
	
}
