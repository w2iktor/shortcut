package pl.symentis.shorturl.api;

public class InactivateAccountRequest {

	private final String reason;

	public InactivateAccountRequest(String reason) {
		super();
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

}
