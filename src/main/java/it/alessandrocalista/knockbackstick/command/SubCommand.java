package it.alessandrocalista.knockbackstick.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {

    String permission();

    String getName();

    default boolean playerOnly() {
        return false;
    }

    void execute(CommandSender commandSender, String[] args);

    default List<String> tabComplete(String[] args) {
        return List.of();
    }

}
