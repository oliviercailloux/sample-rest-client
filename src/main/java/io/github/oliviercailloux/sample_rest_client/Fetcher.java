package io.github.oliviercailloux.sample_rest_client;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

import java.time.ZonedDateTime;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fetcher {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(Fetcher.class);

	public static void main(String[] args) {
		Fetcher fetcher = new Fetcher();
		fetcher.fromJsonPlaceHolder();
		fetcher.fromWikipedia();
	}

	public void fromJsonPlaceHolder() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("https://jsonplaceholder.typicode.com").path("{name}").path("{id}");
		WebTarget resolvedTarget = target.resolveTemplate("name", "comments").resolveTemplate("id", "2");
		/** https://jsonplaceholder.typicode.com/comments/2 */
		String result = resolvedTarget.request(MediaType.TEXT_PLAIN).get(String.class);
		client.close();
		LOGGER.info(result);
	}

	public void fromWikipedia() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("https://en.wikipedia.org/w/api.php");
		WebTarget resolvedTarget = target.queryParam("action", "query").queryParam("titles", "Bertrand Russell")
				.queryParam("prop", "revisions").queryParam("rvprop", "ids|timestamp|user")
				.queryParam("rvstart", ZonedDateTime.of(2017, 8, 1, 0, 0, 0, 0, UTC).format(ISO_DATE_TIME))
				.queryParam("rvend", ZonedDateTime.of(2017, 7, 29, 0, 0, 0, 0, UTC).format(ISO_DATE_TIME))
				.queryParam("format", "json");
		/**
		 * https://en.wikipedia.org/w/api.php?action=query&titles=Bertrand%20Russell&prop=revisions&rvprop=ids|timestamp|user
		 */
		LOGGER.info("Querying: " + resolvedTarget + ".");
		String result = resolvedTarget.request(MediaType.TEXT_PLAIN).get(String.class);
		client.close();
		LOGGER.info(result);
	}
}
