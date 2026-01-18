package it.alessandrocalista.knockbackstick.util;

import it.alessandrocalista.knockbackstick.KnockbackStick;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;


public class KnockbackUtil {
    private static final KnockbackStick PLUGIN = KnockbackStick.getInstance();

    public static void applySegmentedKnockback(Player victim, Vector direction, double strength) {
        final double MAX_PER_TICK = 3.5;

        new BukkitRunnable() {
            double remainingStrength = strength;
            boolean firstTick = true;

            @Override
            public void run() {
                if (remainingStrength <= 0 || !victim.isOnline() || victim.isDead()) {
                    this.cancel();
                    return;
                }

                double currentBoost = Math.min(remainingStrength, MAX_PER_TICK);
                Vector boost = direction.clone().multiply(currentBoost);

                if (firstTick) {
                    boost.setY(0.5);
                    firstTick = false;
                }

                Vector currentVelocity = victim.getVelocity();
                victim.setVelocity(currentVelocity.add(boost));

                remainingStrength -= currentBoost;
            }
        }.runTaskTimer(PLUGIN, 0L, 1L);
    }
}
