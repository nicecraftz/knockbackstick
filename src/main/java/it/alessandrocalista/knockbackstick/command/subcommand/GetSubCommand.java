package it.alessandrocalista.knockbackstick.command.subcommand;

import it.alessandrocalista.knockbackstick.command.SubCommand;
import it.alessandrocalista.knockbackstick.util.StickGenerator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetSubCommand implements SubCommand {
    @Override
    public String permission() {
        return "kbs.get";
    }

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;
        if (args.length == 0) {
            player.sendRichMessage("<red>Devi specificare un moltiplicatore!");
            return;
        }

        try {
            double parsedValueFromCommandArg = Math.abs(Double.parseDouble(args[0]));
            StickGenerator.createItemThenAddToPlayer(parsedValueFromCommandArg, player);
            player.sendRichMessage("<green>Il Knockback Stick con moltiplicatore <gold>" + parsedValueFromCommandArg + "x <green> e' stato aggiunto al tuo inventario!");
        } catch (NumberFormatException e) {
            player.sendRichMessage("<red>Il moltiplicatore specificato non e' un numero valido!");
        }
    }

    @Override
    public boolean playerOnly() {
        return true;
    }
}
