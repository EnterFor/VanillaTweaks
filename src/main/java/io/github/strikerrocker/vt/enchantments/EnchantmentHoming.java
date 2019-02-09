package io.github.strikerrocker.vt.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

/**
 * Makes arrows shot out of bows track down their targets
 */
@EntityTickingEnchantment
public class EnchantmentHoming extends VTEnchantmentBase {
    private static String name = "homing";

    EnchantmentHoming() {
        super(name, Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.BOW, EntityEquipmentSlot.MAINHAND);
        this.setRegistryName(name);
        this.setName(name);
    }

    @Override
    public void performAction(Entity entity, Event baseEvent) {
        if (entity instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) entity;
            EntityLivingBase shooter = (EntityLivingBase) arrow.shootingEntity;
            if (shooter != null && this.getEnchantmentLevel(shooter.getHeldItemMainhand()) > 0) {
                int homingLevel = this.getEnchantmentLevel(shooter.getHeldItemMainhand());
                double distance = Math.pow(2, homingLevel - 1) * 32;
                World world = arrow.world;
                List<EntityLivingBase> livingEntities = world.getEntities(EntityLivingBase.class, EntitySelectors.NOT_SPECTATING);
                EntityLivingBase target = null;
                for (EntityLivingBase livingEntity : livingEntities) {
                    double distanceToArrow = livingEntity.getDistance(arrow);
                    if (distanceToArrow < distance && shooter.canEntityBeSeen(livingEntity) && !livingEntity.getPersistentID().equals(shooter.getPersistentID())) {
                        distance = distanceToArrow;
                        target = livingEntity;
                    }
                }
                if (target != null) {
                    double x = target.posX - arrow.posX;
                    double y = target.getEntityBoundingBox().minY + target.height / 2 - (arrow.posY + arrow.height / 2);
                    double z = target.posZ - arrow.posZ;
                    arrow.shoot(x, y, z, MathHelper.sqrt(arrow.motionX * arrow.motionX + arrow.motionY * arrow.motionY + arrow.motionZ * arrow.motionZ), 0);
                }
            }
        }
    }

    @Override
    public int getMinimumEnchantability(int enchantmentLevel) {
        return (enchantmentLevel - 1) * 10 + 10;
    }

    @Override
    public int getMaximumEnchantability(int enchantmentLevel) {
        return enchantmentLevel * 10 + 51;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

}
