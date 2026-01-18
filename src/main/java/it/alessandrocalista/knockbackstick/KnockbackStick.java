package it.alessandrocalista.knockbackstick;

import it.alessandrocalista.knockbackstick.command.StickPluginCommand;
import it.alessandrocalista.knockbackstick.listener.HitListener;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class KnockbackStick extends JavaPlugin {
    public static final NamespacedKey PLUGIN_KEY = new NamespacedKey("knockbackstick", "multiplier");
    private static KnockbackStick instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new HitListener(), this);
        StickPluginCommand executor = new StickPluginCommand(this);
        getCommand("knockbackstick").setTabCompleter(executor);
        getCommand("knockbackstick").setExecutor(executor);
    }

    public static KnockbackStick getInstance() {
        return instance;
    }
}
