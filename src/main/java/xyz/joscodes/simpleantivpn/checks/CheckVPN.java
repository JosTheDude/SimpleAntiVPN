package xyz.joscodes.simpleantivpn.checks;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import xyz.joscodes.simpleantivpn.SimpleAntiVPN;

import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;

public class CheckVPN {

	public static String checkVPN(String ipAddress) {
		try {
			String url = "https://proxycheck.io/v2/" + ipAddress + "?key=" + SimpleAntiVPN.apiKey + "&vpn=1";

			HttpResponse<JsonNode> response = Unirest.get(url)
					.header("User-Agent", "Mozilla/5.0")
					.asJson();

			int responseCode = response.getStatus();
			if (responseCode == 200) {
				kong.unirest.json.JSONObject jsonObject = response.getBody().getObject();

				if (jsonObject.has(ipAddress)) {
					kong.unirest.json.JSONObject ipObject = jsonObject.getJSONObject(ipAddress);
					if (ipObject.has("proxy")) {
						return ipObject.getString("proxy");
					} else {
						getLogger().log(Level.WARNING, "Response from Proxy Check for " + ipAddress + " does not contain the 'proxy' key.");
					}
				} else {
					getLogger().log(Level.WARNING, "Response from Proxy Check does not contain the IP address: " + ipAddress);
				}
			} else {
				getLogger().log(Level.WARNING, "Proxy Check returned a non-200 response code: " + responseCode);
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Failed to get proxy value for " + ipAddress, e);
		}
		return null;
	}

}
