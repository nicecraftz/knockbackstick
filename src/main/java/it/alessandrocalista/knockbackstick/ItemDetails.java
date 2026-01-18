package it.alessandrocalista.knockbackstick;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public record ItemDetails(
        String name,
        List<String> lore
) {

    public void apply(ItemMeta itemMeta, double multiplier) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        itemMeta.displayName(miniMessage.deserialize(name));
        itemMeta.lore(lore.stream()
                .map(s -> s.replace("%multiplier%", String.valueOf(multiplier)))
                .map(miniMessage::deserialize)
                .toList());
    }

    public static ItemDetails fromConfig(ConfigurationSection section) {
        return new ItemDetails(section.getString("name"), section.getStringList("lore"));
    }
}
