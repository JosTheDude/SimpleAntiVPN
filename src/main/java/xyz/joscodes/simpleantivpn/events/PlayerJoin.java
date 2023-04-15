package xyz.joscodes.simpleantivpn.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.joscodes.simpleantivpn.SimpleAntiVPN;

import java.util.Objects;

public class PlayerJoin {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String ip = Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();

		// Check if IP is a VPN
		boolean isVPN = CheckVPN.checkVPN(ip);

		// Block player if VPN and block-vpn is enabled
		if (isVPN && SimpleAntiVPN.blockVPN) {
			player.kickPlayer("VPN usage is not allowed on this server.");
		}
	}


}
