[![Discord](https://badgen.net/discord/online-members/QWJxfb4zQZ?icon=discord&label=Discord&list=what)](https://discord.gg/QWJxfb4zQZ)

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/mat0u5)

# Past Life
This mod is a one-to-one recreation of [Grian](https://www.youtube.com/c/Grian)'s Past Life mod.<br>
**If you want to play the other seasons, use my [Life Series](https://modrinth.com/mod/life-series) mod instead.**<br>

Since modern modding Fabric is not available for Minecraft Beta and other old versions that Past Life is played on, this mod is made for [OrniteMC](https://ornithemc.net).

**This mod has different features for each version of Minecraft** (matching the features of the sessions). So for example, the Boogeyman will be only available in Beta 1.7.3, and none of the other versions, just like Grian had it.

The earliest versions have a separate jar file for the client and the server, so make sure to download the correct one you want.

---------

## How to play:
### Client:
1. Install the https://ornithemc.net installer.
2. Open it, select the **client** environment, and install the desired version of Minecraft (based on the session you're on). **If in mc version beta 1.2_02 or beta 1.7.3:**  Make sure to select check "Historical Versions" otherwise the beta versions won't show up.
3. Open folder where you installed it, and add the mod jar file to the `mods` folder.
4. Open the Minecraft launcher, select the Ornithe profile, and run the game.

### Server:
1. Install the https://ornithemc.net installer.
2. Open it, select the **server** environment, and install the desired version of Minecraft (based on the session you're on). **If in mc version beta 1.2_02 or beta 1.7.3:** Make sure to select check "Historical Versions" otherwise the beta versions won't show up.
3. Open the folder where you installed the server and run the `fabric-server-launch.jar` file to first start the server.
4. **If in mc version beta 1.2_02 or beta 1.7.3:** Open server.properties and **set "online-mode" to false**. This is required, as online-mode does NOT work in the oldest versions
5. Add the mod jar file to the `mods` folder in the server directory.
6. That's it, just run the server (`fabric-server-launch.jar`) and you're done!

### Notes:
When updating the server, make sure you keep the `_pastlife_lives.txt` file, as that contains the info about players' lives.<br>
This mod does not contain Simple Voice Chat.<br>
This mod (obviously) does not handle updating the world to newer versions, so you're going to have to do that manually (and handle any issues with it).<br>
After updating the skins in the minecraft launcher, it takes a while for the skins to update in the game (like 20 minutes) because of the custom implementation of how skins work, so be patient.

## Features:
| MC Version     | Status      |                                                                                                    Modifications                                                                                                     |
|:---------------|-------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| Beta 1.2_02    | Finished    | Fixed player skins not rendering properly.<br/>Improved front-facing third-person perspective.<br/>Added the world border.<br/>Added the ability to zoom.<br/>Added the custom TNT recipe.<br/>Added Death Messages. |
| Beta 1.7.3     | Finished    |                                                                                    Added Keep Inventory.<br/>Added the Boogeyman.                                                                                    |
| Release 1.1    | In Progress |                                            Tab list shows lives.<br>Ender Dragon drops diamonds.<br>Clamped enchantments to level 1.<br>Colored names in death messages.                                             |
| Release 1.4.7  | TODO        |                                                                                                          -                                                                                                           |
| Release 1.9.4  | TODO        |                                                                                                          -                                                                                                           |
| Release 1.14.4 | TODO        |                                                                                                          -                                                                                                           |


---------

### Beta 1.2_02:
This version has no special features.
### Beta 1.7.3:
This version contains the **Boogeyman**. Here's how it works:
- An admin must run the `/boogeyman choose` command, which will randomly select the boogeymen.
- Boogeyman succeeding / failing is not tracked automatically, so the player that is the Boogeyman must run the `/boogeyman succeed` or `/boogeyman fail` command to say if they have succeeded or failed.
- They can run that command at any time, but it also sends a public message in chat to prevent any cheating.
- The console also logs the information about who got picked as the Boogeyman, and if they failed or succeeded, so you can also handle any possible cheating with that.

---------

### All Commands:
Make sure you're opped to be able to use most of the commands below.<br>
<details open>
<summary>/lives</summary>

- `/lives` - Shows you the amount of lives you have. *No permissions required.*
- `/lives add <player> [amount]` - Adds `[amount]` lives to `<player>`. If `[amount]` is not specified, it defaults to 1.
- `/lives remove <player> [amount]` - Removes `[amount]` lives from `<player>`. If `[amount]` is not specified, it defaults to 1.
- `/lives set <player> <amount>` - Sets `<player>`'s lives to `<amount>`.
- `/lives get <player>` - Shows you how many lives `<player>` has.
</details>

<details open>
<summary>/boogeyman - Only available in session 2 (Minecraft Beta 1.7.3)</summary>

- `/boogeyman choose` - Randomly selects the Boogeymen.
- `/boogeyman succeed` - Ran by the Boogeyman to say they have succeeded in killing a player. *No permissions required.*
- `/boogeyman fail` - Ran by the Boogeyman to say they have failed in killing a player. *No permissions required.*
</details>