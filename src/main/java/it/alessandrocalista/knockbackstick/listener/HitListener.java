package it.alessandrocalista.knockbackstick.listener;

import it.alessandrocalista.knockbackstick.KnockbackStick;
import it.alessandrocalista.knockbackstick.util.VelocityModifier;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HitListener implements Listener {
    private static final double DEFAULT_MULTIPLIER = 1d;
    private final Map<UUID, VelocityModifier> knockedEntities = new HashMap<>();

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

        knockedEntities.put(victim.getUniqueId(), new VelocityModifier(damager.getLocation().getDirection(), strength));
    }

    @EventHandler
    public void onKnockback(PlayerVelocityEvent velocityEvent) {
        Player victim = velocityEvent.getPlayer();
        UUID victimId = victim.getUniqueId();
        if(!knockedEntities.containsKey(victimId)) return;

        VelocityModifier velocityModifier = knockedEntities.get(victimId);
        double strength = velocityModifier.strength();
        Vector attackersDirection = velocityModifier.attackersVector();

        Vector direction = attackersDirection.clone().setY(0).normalize();

        direction.multiply(strength);

        double vLift = Math.log10(strength + 1);
        direction.setY(vLift);

        velocityEvent.setVelocity(direction);
        knockedEntities.remove(victimId);
    }

}
