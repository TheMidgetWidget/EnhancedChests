package me.lightlord323dev.enhancedchests.command;

import me.lightlord323dev.enhancedchests.item.ECFactory;
import me.lightlord323dev.enhancedchests.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Luda on 7/15/2020.
 */
public class ECCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    MessageUtil.error(sender, "Player not found.");
                    return true;
                }

                if (!isInt(args[2])) {
                    MessageUtil.error(sender, "Invalid size.");
                    return true;
                }

                int size = Integer.parseInt(args[2]);

                target.getInventory().addItem(ECFactory.createECItem(size));
                MessageUtil.success(sender, "An enhanced chest was given to the player.");
                MessageUtil.info(target, "You were granted an enhanced chest.");
            }
            return true;
        }

        sendUsage(sender);
        return true;
    }

    private void sendUsage(CommandSender sender) {
        MessageUtil.error(sender, "\n\nUsage:\n" +
                "/enhancedchests give <player> <size>");
    }

    private boolean isInt(String str) {
        if (str == null)
            return false;
        char[] data = str.toCharArray();
        if (data.length <= 0)
            return false;
        int index = 0;
        if (data[0] == '-' && data.length > 1)
            index = 1;
        for (; index < data.length; index++) {
            if (data[index] < '0' || data[index] > '9')
                return false;
        }
        return true;
    }

}
