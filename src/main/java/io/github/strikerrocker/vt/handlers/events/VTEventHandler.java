package io.github.strikerrocker.vt.handlers.events;

import io.github.strikerrocker.vt.capabilities.SelfPlantingProvider;
import io.github.strikerrocker.vt.compat.baubles.BaubleTools;
import io.github.strikerrocker.vt.handlers.VTConfigHandler;
import io.github.strikerrocker.vt.items.VTItems;
import io.github.strikerrocker.vt.vt;
import io.github.strikerrocker.vt.vtModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockReed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static io.github.strikerrocker.vt.blocks.VTBlocks.*;

/**
 * The event handler for Vanilla Tweaks
 */
public final class VTEventHandler {
    /**
     * Returns if the given chunk is an slime chunk or not
     *
     * @param world World,x int,z int
     */
    static boolean isSlimeChunk(World world, int x, int z) {
        Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
        return chunk.getRandomWithSeed(987234911L).nextInt(10) == 0;
    }


    /**
     * Returns whether the block is harvestable for hoe sickle
     *
     * @param state IBlockState
     */
    static boolean canHarvest(IBlockState state) {
        Block block = state.getBlock();
        return (block instanceof BlockBush && !(block instanceof BlockLilyPad)) || block instanceof BlockReed;
    }

    /**
     * Swaps the items in armour with player's armor
     *
     * @param player     the player
     * @param armorStand the armour stand
     * @param slot       the slots
     */
    static void swapSlot(EntityPlayer player, EntityArmorStand armorStand, EntityEquipmentSlot slot) {
        ItemStack playerItem = player.getItemStackFromSlot(slot);
        ItemStack armorStandItem = armorStand.getItemStackFromSlot(slot);
        player.setItemStackToSlot(slot, armorStandItem);
        armorStand.setItemStackToSlot(slot, playerItem);
    }

    @SubscribeEvent
    public void addItemCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityItem) {
            event.addCapability(new ResourceLocation(vtModInfo.MOD_ID), new SelfPlantingProvider());
        }
    }

    /**
     * Enables binoculars functionality
     *
     * @param event The FOVUpdateEvent
     */
    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent event) {
        if (event.getEntity() != null) {
            if (event.getEntity() instanceof EntityPlayer) {
                ItemStack helmet = event.getEntity().getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!helmet.isEmpty() && helmet.getItem() == VTItems.binocular)
                    event.setNewfov(event.getFov() / VTConfigHandler.binocularZoomAmount);
                if (vt.baubles) if (BaubleTools.hasProbeGoggle(event.getEntity()))
                    event.setNewfov(event.getFov() / VTConfigHandler.binocularZoomAmount);
            }
        }
    }


    /**
     * Prevents potion effects from shifting your inventory to the side.
     *
     * @param event The PotionShiftEvent
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPotionShiftEvent(GuiScreenEvent.PotionShiftEvent event) {
        event.setCanceled(true);
    }

    /**
     * Sets the burn time for various Blocks
     *
     * @param event FurnaceFuelBurnTimeEvent
     */
    @SubscribeEvent
    public void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().getItem() == Item.getItemFromBlock(charcoal))
            event.setBurnTime(16000);
        if (event.getItemStack().getItem() == Item.getItemFromBlock(Blocks.TORCH))
            event.setBurnTime(400);
        if (event.getItemStack().getItem() == Item.getItemFromBlock(acaciabark))
            event.setBurnTime(500);
        if (event.getItemStack().getItem() == Item.getItemFromBlock(birchbark))
            event.setBurnTime(500);
        if (event.getItemStack().getItem() == Item.getItemFromBlock(darkoakbark))
            event.setBurnTime(500);
        if (event.getItemStack().getItem() == Item.getItemFromBlock(junglebark))
            event.setBurnTime(500);
        if (event.getItemStack().getItem() == Item.getItemFromBlock(oakbark))
            event.setBurnTime(500);
        if (event.getItemStack().getItem() == Item.getItemFromBlock(sprucebark))
            event.setBurnTime(500);
    }
}