package xyz.joscodes.simpleantivpn;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;

public class SimpleAntiVPN extends JavaPlugin implements Listener {
	private String kickMessage;

	private final String apiKey = getConfig().getString("apiKey");

	@Override
	public void onEnable() {
		// Load configuration values
		getConfig().options().copyDefaults(true);
		saveConfig();
		kickMessage = getConfig().getString("kickMessage");

		// Register event listener
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		String ipAddress = Objects.requireNonNull(event.getPlayer().getAddress()).getAddress().getHostAddress();

		String isVPN = checkVPN(ipAddress);
		boolean blockVPNs = getConfig().getBoolean("blockVPNs");

		System.out.println("VPN: " + isVPN);

		if (isVPN == "yes") {
			if (!blockVPNs) {
				return;
			}
			event.getPlayer().kickPlayer(kickMessage);
		}
	}

	private String checkVPN(String ipAddress) {
		try {
			URL url = new URL("https://proxycheck.io/v2/" + ipAddress + "?key=" + apiKey + "&vpn=1");

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				JSONObject jsonObject = new JSONObject(response.toString());

				getLogger().log(Level.INFO, "Response: " + response);

				if (jsonObject.has(ipAddress)) {
					JSONObject ipObject = jsonObject.getJSONObject(ipAddress);
					if (ipObject.has("proxy")) {
						return ipObject.getString("proxy");
					} else {
						getLogger().log(Level.WARNING, "Response from Proxy Check for " + ipAddress + " does not contain the 'proxy' key.");
					}
				} else {
					getLogger().log(Level.WARNING, "Response from Proxy Check does not contain the IP address: " + ipAddress);
				}
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Failed to get proxy value for " + ipAddress, e);
		}
		return null;
	}

}
