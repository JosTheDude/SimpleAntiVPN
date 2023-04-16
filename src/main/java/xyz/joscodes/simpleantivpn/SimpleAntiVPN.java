package xyz.joscodes.simpleantivpn;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public class SimpleAntiVPN extends JavaPlugin implements Listener {

	private String kickMessage;

	private String apiKey;

	@Override
	public void onEnable() {
		// Load configuration values
		getConfig().options().copyDefaults(true);

		String rawKickMessage = getConfig().getString("kickMessage");

		if (rawKickMessage != null) {
			kickMessage = ChatColor.translateAlternateColorCodes('&', rawKickMessage);
		} else {
			kickMessage = ChatColor.translateAlternateColorCodes('&', "&cThis network disallows VPN usage!");
		}

		apiKey = getConfig().getString("apiKey");

		if (apiKey == null) {
			getLogger().log(Level.WARNING, "You have not set your API key in the config.yml file, thus, this plugin will be disabled.");
			getServer().getPluginManager().disablePlugin(this);
		}

		saveConfig();

		// Register event listener
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		String ipAddress = Objects.requireNonNull(event.getPlayer().getAddress()).getAddress().getHostAddress();

		String isVPN = checkVPN(ipAddress);
		boolean blockVPNs = getConfig().getBoolean("blockVPNs");

		if (Objects.equals(isVPN, "yes")) {
			if (!blockVPNs) {
				return;
			}
			Bukkit.getLogger().info("Detected VPN for player: " + event.getPlayer().getName());
			event.getPlayer().kickPlayer(kickMessage);
		}
	}

	private String checkVPN(String ipAddress) {
		try {
			String url = "https://proxycheck.io/v2/" + ipAddress + "?key=" + apiKey + "&vpn=1";

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
