package it.alessandrocalista.knockbackstick.command.subcommand;

import it.alessandrocalista.knockbackstick.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadSubCommand implements SubCommand {
    private final JavaPlugin plugin;

    public ReloadSubCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String permission() {
        return "kbs.reload";
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        plugin.reloadConfig();
        commandSender.sendRichMessage("<green> Configurazione del plugin ricaricata!");
    }
}
