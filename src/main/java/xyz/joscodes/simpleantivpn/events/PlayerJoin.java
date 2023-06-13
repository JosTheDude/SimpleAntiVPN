package xyz.joscodes.simpleantivpn.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import xyz.joscodes.simpleantivpn.SimpleAntiVPN;
import xyz.joscodes.simpleantivpn.checks.CheckVPN;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static xyz.joscodes.simpleantivpn.SimpleAntiVPN.kickMessage;

public class PlayerJoin implements Listener {

	private final SimpleAntiVPN plugin;

	public PlayerJoin(SimpleAntiVPN plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void on(AsyncPlayerPreLoginEvent event) {

		String ipAddress = Arrays.toString(Objects.requireNonNull(event.getAddress()).getAddress());

		String isVPN = CheckVPN.checkVPN(ipAddress);
		boolean blockVPNs = SimpleAntiVPN.blockVPNs;

		if (Objects.equals(isVPN, "yes")) {
			if (!blockVPNs) {
				return;
			}

			String playerName = event.getName();
			UUID playerUUID = event.getUniqueId();

			List<String> exemptPlayers = plugin.getExemptPlayers();
			if (exemptPlayers.contains(playerName) || exemptPlayers.contains(playerUUID.toString())) {
				return;
			}

			Bukkit.getLogger().info("Detected VPN for player: " + playerName);
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, kickMessage);
		}
	}
}
