package it.alessandrocalista.knockbackstick.listener;

import it.alessandrocalista.knockbackstick.KnockbackStick;
import it.alessandrocalista.knockbackstick.util.KnockbackUtil;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class HitListener implements Listener {
    private static final double DEFAULT_MULTIPLIER = 1d;

    @EventHandler
    public void onHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Entity damagerEntity = entityDamageByEntityEvent.getDamager();
        Entity victimEntity = entityDamageByEntityEvent.getEntity();
        if (!(damagerEntity instanceof Player damager) || !(victimEntity instanceof Player victim)) return;

        ItemStack itemInMainHand = damager.getInventory().getItemInMainHand();
        if (itemInMainHand.getType() != Material.STICK) return;

        ItemMeta itemMeta = itemInMainHand.getItemMeta();
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        boolean isPluginStick = dataContainer.has(KnockbackStick.PLUGIN_KEY);

        if (!isPluginStick) return;

        double strength = dataContainer.getOrDefault(
                KnockbackStick.PLUGIN_KEY,
                PersistentDataType.DOUBLE,
                DEFAULT_MULTIPLIER
        );

        Vector direction = damager.getLocation().getDirection();
        KnockbackUtil.applySegmentedKnockback(victim, direction, strength);
    }

}
