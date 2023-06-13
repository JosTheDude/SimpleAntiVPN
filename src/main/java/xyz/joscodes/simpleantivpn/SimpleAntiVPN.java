package xyz.joscodes.simpleantivpn;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.joscodes.simpleantivpn.events.PlayerJoin;

import java.util.List;
import java.util.logging.Level;

public class SimpleAntiVPN extends JavaPlugin implements Listener {

	public static String kickMessage;

	public static String apiKey;

	public static boolean blockVPNs;

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
			return;
		}

		blockVPNs = getConfig().getBoolean("blockVPNs");

		saveConfig();

		// Register event listener
		getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
	}


	public List<String> getExemptPlayers() {
		return getConfig().getStringList("exemptPlayers");
	}
}
