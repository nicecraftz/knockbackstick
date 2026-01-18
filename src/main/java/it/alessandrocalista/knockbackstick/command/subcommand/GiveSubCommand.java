package it.alessandrocalista.knockbackstick.command.subcommand;

import it.alessandrocalista.knockbackstick.command.SubCommand;
import it.alessandrocalista.knockbackstick.util.StickGenerator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveSubCommand implements SubCommand {
    @Override
    public String permission() {
        return "kbs.give";
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            commandSender.sendRichMessage("<red>Devi specificare il nome di un giocatore!");
            return;
        }

        if (args.length < 2) {
            commandSender.sendRichMessage("<red>Devi specificare il moltiplicatore!");
            return;
        }

        String targetName = args[0];
        Player targetPlayer = Bukkit.getPlayerExact(targetName);

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            commandSender.sendRichMessage("<red>Il giocatore specificato non e' valido!");
            return;
        }

        double multiplier;
        try {
            multiplier = Math.abs(Double.parseDouble(args[1]));
        } catch (NumberFormatException e) {
            commandSender.sendRichMessage("<red>Il moltiplicatore specificato non e' un numero valido!");
            return;
        }



        StickGenerator.createItemThenAddToPlayer(multiplier, targetPlayer);
        targetPlayer.sendRichMessage("<green>Un Knockback Stick con moltiplicatore <gold>" + multiplier + "x <green>e' stato aggiunto al tuo inventario");
        commandSender.sendRichMessage("<green>Aggiunto Knockback Stick con moltplicatore <gold>" + multiplier + "x <green> con successo a <white>" + targetName);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers()
                    .parallelStream()
                    .map(Player::getName)
                    .filter(s -> s.startsWith(args[0]))
                    .toList();
        }

        return null;
    }
}
