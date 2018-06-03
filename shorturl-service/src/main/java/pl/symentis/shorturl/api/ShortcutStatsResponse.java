package pl.symentis.shorturl.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ShortcutStatsResponse {
	private final int clicks;
	private final List<Stats> agents;
	private final List<Stats> oses;

	@JsonCreator()
	public ShortcutStatsResponse(
			@JsonProperty("clicks") int clicks,
			@JsonProperty("statsCounters") List<Stats> agents,
			@JsonProperty("oses") List<Stats> oses) {
		super();
		this.clicks = clicks;
		this.agents = agents;
		this.oses = oses;
	}


	public List<Stats> getAgents() {
		return agents;
	}

	public int getClicks() {
		return clicks;
	}

	public List<Stats> getOses() {
		return oses;
	}
}
