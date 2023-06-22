# SimpleAntiVPN
## A Simple AntiVPN Plugin that uses ProxyCheckIO's API to check player's for VPN usage.

- This plugin integrates ProxyCheckIO's API with Player Joins, such that it checks a player's IP to see if they're using a VPN and kicks them if they are
- Fully configurable, can disable the blocking & kick message
- Must configure ProxyCheckIO API Key in the config.yml, get one from https://proxycheck.io, they come with 1000 free queries / day

Default `config.yml`:
```
apiKey: YOUR_API_KEY # API Key from ProxyCheckIO to check VPNs. This is necessary for function.
blockVPNs: true # Whether this plugin's Anti-VPN system is enabled.
kickMessage: '&cSorry, VPNs are not allowed on this server.' # Kick message when a player has aa VPN
exemptPlayers: # Players who will not be flagged for VPNs by the plugin and kick them, regardless if they have one on or not
  - 'a52e4842-3a09-4cad-8e91-2160399b766f' # This list works with both UUIDs
  - 'SkipTurn' # and player names
```
