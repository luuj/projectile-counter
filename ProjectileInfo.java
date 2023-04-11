package net.runelite.client.plugins.projectilecd;

import net.runelite.api.Projectile;
import java.awt.*;

public class ProjectileInfo {
    public Projectile currProjectile;
    public Color color;

    public ProjectileInfo(Projectile currProjectile, Color color) {
        this.currProjectile = currProjectile;
        this.color = color;
    }
}
