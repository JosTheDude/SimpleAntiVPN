package xyz.joscodes.simpleantivpn.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.joscodes.simpleantivpn.SimpleAntiVPN;
import xyz.joscodes.simpleantivpn.checks.CheckVPN;

import java.util.Objects;

import static xyz.joscodes.simpleantivpn.SimpleAntiVPN.kickMessage;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		String ipAddress = Objects.requireNonNull(event.getPlayer().getAddress()).getAddress().getHostAddress();

		String isVPN = CheckVPN.checkVPN(ipAddress);
		boolean blockVPNs = SimpleAntiVPN.blockVPNs;

		if (Objects.equals(isVPN, "yes")) {
			if (!blockVPNs) {
				return;
			}
			Bukkit.getLogger().info("Detected VPN for player: " + event.getPlayer().getName());
			event.getPlayer().kickPlayer(kickMessage);
		}
	}

}
