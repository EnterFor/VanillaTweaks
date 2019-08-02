package io.github.strikerrocker.vt.enchantments;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EnchantmentVeteran extends Enchantment {
    EnchantmentVeteran(String name) {
        super(Rarity.VERY_RARE, EnchantmentType.ARMOR_HEAD, new EquipmentSlotType[]{EquipmentSlotType.HEAD});
        this.setRegistryName(name);
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent event) {
        event.world.getEntities(ExperienceOrbEntity.class, EntityPredicates.IS_ALIVE).forEach(this::attemptToMove);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().world != null)
            Minecraft.getInstance().world.getEntities(ExperienceOrbEntity.class, EntityPredicates.IS_ALIVE).forEach(this::attemptToMove);
    }

    private void attemptToMove(Entity entity) {
        double range = 32;
        PlayerEntity closestPlayer = entity.world.getClosestPlayer(entity, range);
        if (closestPlayer != null && EnchantmentHelper.getEnchantmentLevel(this, closestPlayer.getItemStackFromSlot(EquipmentSlotType.HEAD)) > 0) {
            double xDiff = (closestPlayer.posX - entity.posX) / range;
            double yDiff = (closestPlayer.posY + closestPlayer.getEyeHeight() - entity.posY) / range;
            double zDiff = (closestPlayer.posZ - entity.posZ) / range;
            double movementFactor = Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
            double invertedMovementFactor = 1 - movementFactor;
            if (invertedMovementFactor > 0) {
                Vec3d motion = entity.getMotion();
                invertedMovementFactor *= invertedMovementFactor;
                entity.setMotion(motion.x + xDiff / movementFactor * invertedMovementFactor * 0.1, motion.y + yDiff / movementFactor * invertedMovementFactor * 0.1, motion.z + zDiff / movementFactor * invertedMovementFactor * 0.1);
            }
        }
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 40;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getEquipmentSlot().equals(EquipmentSlotType.HEAD);
    }
}
