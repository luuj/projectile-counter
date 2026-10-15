package net.runelite.client.plugins.projectilecd;

import java.awt.Color;
import net.runelite.api.Projectile;

public class ProjectileInfo {
    final Projectile currProjectile;
    Color color;

    public ProjectileInfo(Projectile currProjectile, Color color) {
        this.currProjectile = currProjectile;
        this.color = color;
    }
}
