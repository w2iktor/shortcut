package pl.symentis.shorturl.domain;

import java.net.URL;
import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@TypeAlias("click")
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
	
	Click() {
	}

	Click(ObjectId id, String shortcut, LocalDateTime localDateTime, String ipAddress, String os, String agent, URL referer) {
		this.id = id;
		this.shortcut = shortcut;
		this.localDateTime = localDateTime;
		this.ipAddress = ipAddress;
		this.os = os;
		this.agent = agent;
		this.referer = referer;
	}

	public ObjectId getId() {
		return id;
	}

	public String getShortcut() {
		return shortcut;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getOs() {
		return os;
	}

	public String getAgent() {
		return agent;
	}

	public URL getReferer() {
		return referer;
	}
}
