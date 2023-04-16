# SimpleAntiVPN
## A Simple AntiVPN Plugin that uses ProxyCheckIO's API to check player's for VPN usage.

- This plugin integrates ProxyCheckIO's API with Player Joins, such that it checks a player's IP to see if they're using a VPN and kicks them if they are
- Fully configurable, can disable the blocking & kick message
- Must configure ProxyCheckIO API Key in the config.yml, get one from https://proxycheck.io, they come with 1000 free queries / day

Default `config.yml`:
```
apiKey: YOUR_API_KEY
blockVPNs: true
kickMessage: '&cSorry, VPNs are not allowed on this server.'
```
