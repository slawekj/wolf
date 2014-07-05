package rule.engine;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class ApiExecutor implements Serializable {
	private static final Logger LOG = Logger.getLogger(ApiExecutor.class);
	private final String USER_AGENT = "Mozilla/5.0";

	// HTTP GET request
	public void sendGet(String url) {
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			LOG.info("Sent request " + url + " with exit status: "
					+ con.getResponseCode());
		} catch (Exception e) {
			LOG.info("could not call api " + url);
		}
	}
}
