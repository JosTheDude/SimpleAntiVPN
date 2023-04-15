package xyz.joscodes.simpleantivpn.events;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static xyz.joscodes.simpleantivpn.SimpleAntiVPN.apiKey;

public class CheckVPN {

	public static boolean checkVPN(String ip) {
		// Make API request to ProxyCheckIO
		String url = "https://proxycheck.io/v2/" + ip + "?vpn=1&key=" + apiKey;
		try {
			URLConnection connection = new URL(url).openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla/5.0");
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				// Parse JSON response and check if IP is a VPN
				JsonObject jsonObject = new JsonParser().parse(inputLine).getAsJsonObject();
				return jsonObject.get(ip).getAsJsonObject().get("proxy").getAsInt() == 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


}
