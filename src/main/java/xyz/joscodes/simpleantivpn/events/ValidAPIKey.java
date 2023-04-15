package xyz.joscodes.simpleantivpn.events;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import xyz.joscodes.simpleantivpn.SimpleAntiVPN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ValidAPIKey {

	private static final String PROXYCHECKIO_API_URL = "https://proxycheck.io/v2/";
	private static final String API_KEY_PARAM = "?key=" + SimpleAntiVPN.apiKey;
	private static final String VPN_PARAM = "&vpn=1";
	private static final String USER_AGENT_HEADER = "Mozilla/5.0";
	private static final String ACCEPT_HEADER = "application/json";

	private static String apiUrl = null;

	public ValidAPIKey(String apiKey) {
		apiUrl = PROXYCHECKIO_API_URL + API_KEY_PARAM;
	}

	public static boolean isValidApiKey() {
		try {
			URL url = new URL(apiUrl + VPN_PARAM);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", USER_AGENT_HEADER);
			connection.setRequestProperty("Accept", ACCEPT_HEADER);

			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				Bukkit.getLogger().warning("Invalid API key or URL: Response code " + responseCode);
				return false;
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
			reader.close();

			JsonObject jsonResponse = new Gson().fromJson(response.toString(), JsonObject.class);
			JsonElement jsonProxy = jsonResponse.get("proxy");
			if (jsonProxy != null && jsonProxy.getAsBoolean()) {
				return false;
			}
		} catch (MalformedURLException e) {
			Bukkit.getLogger().warning("Invalid URL: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Bukkit.getLogger().warning("Error while checking API key: " + e.getMessage());
			return false;
		} catch (Exception e) {
			Bukkit.getLogger().warning("Error while parsing JSON response: " + e.getMessage());
			return false;
		}

		return true;
	}
}
