package it.alessandrocalista.knockbackstick.util;

import it.alessandrocalista.knockbackstick.ItemDetails;
import it.alessandrocalista.knockbackstick.KnockbackStick;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static it.alessandrocalista.knockbackstick.KnockbackStick.PLUGIN_KEY;

public class StickGenerator {
    private static final KnockbackStick INSTANCE = KnockbackStick.getInstance();

    public static void createItemThenAddToPlayer(double multiplier, Player player) {
        ItemStack item = createItem(multiplier);
        player.getInventory().addItem(item);
    }

    private static ItemStack createItem(double multiplier) {
        ItemStack itemStack = new ItemStack(Material.STICK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.addEnchant(Enchantment.UNBREAKING, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        ItemDetails pluginItemDetails = getPluginItemDetails();
        pluginItemDetails.apply(itemMeta, multiplier);

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(PLUGIN_KEY, PersistentDataType.DOUBLE, multiplier);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static ItemDetails getPluginItemDetails() {
        ConfigurationSection stick = INSTANCE.getConfig().getConfigurationSection("stick");
        if (stick == null) return new ItemDetails("Config Errato", List.of("La configurazione del plugin e' errata."));
        return ItemDetails.fromConfig(stick);
    }
}
