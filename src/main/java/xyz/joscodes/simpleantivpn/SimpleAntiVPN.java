package xyz.joscodes.simpleantivpn;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.joscodes.simpleantivpn.events.ValidAPIKey;

public final class SimpleAntiVPN extends JavaPlugin implements Listener {

	public static String apiKey;
	public static boolean blockVPN;

	@Override
	public void onEnable() {
		// Load configuration options
		getConfig().options().copyDefaults(true);
		saveConfig();
		apiKey = getConfig().getString("api-key");
		boolean blockVPN = getConfig().getBoolean("block-vpn");

		// Check API key validity
		if (!ValidAPIKey.isValidApiKey()) {
			getLogger().severe("Invalid API key! Plugin disabled!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// Register listener
		getServer().getPluginManager().registerEvents(this, this);
	}


	@Override
	public void onDisable() {
		Bukkit.getLogger().info("SimpleAntiVPN has been disabled!");
	}
}
