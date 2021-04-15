package com.MrEngMan.TeleportAllWithPermission;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import java.io.File;

public class Main extends JavaPlugin implements Listener {

    public static Main plugin;

    // When plugin is first enabled
    @SuppressWarnings("static-access")
    @Override
    public void onEnable() {
        this.plugin = this;

        // Generate the config if need be
        if (!(new File(this.getDataFolder(), "config.yml").exists())) {
            this.saveDefaultConfig();
        }

        // Register stuff
        TeleportCommandHandler teleportCommandHandler = new TeleportCommandHandler();
        this.getCommand("tawp").setExecutor(teleportCommandHandler);

    }

    public void reloadTheConfig() {

        // Generate the config file if it was deleted
        if (!(new File(this.getDataFolder(), "config.yml").exists())) {
            this.saveDefaultConfig();
        }

        // Load new config values
        reloadConfig();

    }

    public class TeleportCommandHandler implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

            // Console always has permission, player must have permission node
            boolean hasPermission = (sender instanceof ConsoleCommandSender) || (sender instanceof Player && sender.hasPermission("tawp.tp"));
            if (hasPermission) {

                boolean inputIsValid = true;
                if(args.length != 1) {
                    inputIsValid = false;
                }
                else {
                    if (!args[0].equalsIgnoreCase("me")){
                        if(getServer().getPlayer(args[0]) == null) inputIsValid = false;
                    } else {
                        if (!(sender instanceof Player)) inputIsValid = false;
                    }
                }

                if(inputIsValid) {

                    Location locationToTeleportTo = args[0].equalsIgnoreCase("me") ? ((Player) sender).getLocation() : getServer().getPlayer(args[0]).getLocation();
                    int numberOfTeleportedPlayers = 0;
                    for(Player player : getServer().getOnlinePlayers()) {
                        if(!player.hasPermission("tawp.me")) continue;

                        player.teleport(locationToTeleportTo);
                        player.sendMessage(Utils.SendChatMessage(plugin.getConfig().getString("TeleportedNotification")));
                        numberOfTeleportedPlayers++;

                    }
                    sender.sendMessage(Utils.SendChatMessage(plugin.getConfig().getString("TeleportedConfirmation").replace("%number%", String.valueOf(numberOfTeleportedPlayers))));

                } else {
                    sender.sendMessage(Utils.SendChatMessage(plugin.getConfig().getString("InvalidSyntaxMessage")));
                }

            } else {
                sender.sendMessage(Utils.SendChatMessage(plugin.getConfig().getString("NoPermissionMessage")));
            }

            return true;
        }

    }

}