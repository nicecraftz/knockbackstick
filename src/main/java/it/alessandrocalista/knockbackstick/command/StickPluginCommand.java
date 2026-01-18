package it.alessandrocalista.knockbackstick.command;

import it.alessandrocalista.knockbackstick.KnockbackStick;
import it.alessandrocalista.knockbackstick.command.subcommand.GetSubCommand;
import it.alessandrocalista.knockbackstick.command.subcommand.GiveSubCommand;
import it.alessandrocalista.knockbackstick.command.subcommand.ReloadSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickPluginCommand implements TabExecutor {
    private static final List<String> HELP_MESSAGE = List.of(
            "<red>Devi specificare un argomento!",
            "<gray>/kbs get [multiplier]",
            "<white>/kbs give [player] [multiplier]",
            "<gray>/kbs reload"
    );

    private final KnockbackStick plugin;
    private final Map<String, SubCommand> subCommandMap = new HashMap<>();

    public StickPluginCommand(KnockbackStick plugin) {
        this.plugin = plugin;
        registerSubcommand(new ReloadSubCommand(plugin));
        registerSubcommand(new GetSubCommand());
        registerSubcommand(new GiveSubCommand());
    }

    private void registerSubcommand(SubCommand subCommand) {
        subCommandMap.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String @NotNull [] args
    ) {
        if (!sender.hasPermission("kbstick.use")) {
            sender.sendRichMessage("<red>Non hai il permesso per eseguire questo comando!");
            return true;
        }

        if (args.length == 0) {
            HELP_MESSAGE.forEach(sender::sendRichMessage);
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        SubCommand subCommand = subCommandMap.get(subCommandName);

        if (subCommand == null) {
            HELP_MESSAGE.forEach(sender::sendRichMessage);
            return true;
        }

        if(subCommand.playerOnly() && !(sender instanceof Player executor)) {
            sender.sendRichMessage("<red>Solo i giocatori possono eseguire questo comando!");
            return true;
        }

        String[] subcommandArray = new String[args.length - 1];
        System.arraycopy(args, 1, subcommandArray, 0, args.length-1);

        subCommand.execute(sender, subcommandArray);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String @NotNull [] args
    ) {
        if (args.length == 0) return null;
        if (args.length == 1) return subCommandMap.keySet().stream().filter(s -> s.startsWith(args[0])).toList();

        SubCommand subCommand = subCommandMap.get(args[0].toLowerCase());
        if (subCommand == null) return null;

        String[] subcommandArray = new String[args.length - 1];
        System.arraycopy(args, 1, subcommandArray, 0, args.length-1);

        return subCommand.tabComplete(subcommandArray);
    }
}
