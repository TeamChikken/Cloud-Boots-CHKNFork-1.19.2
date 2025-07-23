package com.tiviacz.cloudboots;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GoldenFeatherItem extends Item
{
    public GoldenFeatherItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        if (!(entity instanceof ServerPlayerEntity player)) return;

        // Only act if this is the specific item *currently* being ticked
        // (not just another feather in inventory)
        if (!player.getInventory().contains(stack) && !stack.equals(player.getEquippedStack(EquipmentSlot.MAINHAND)) && !stack.equals(player.getEquippedStack(EquipmentSlot.OFFHAND))) {
            return;
        }

        if (entity.fallDistance >= 3.0F) {
            stack.damage(1, player, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND)); // Slot info is just for animation

            entity.fallDistance = 0.0F;

            if (!world.isClient && world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.CLOUD,
                        entity.getX(), entity.getY(), entity.getZ(),
                        3, 0, 0, 0, (world.random.nextFloat() - 0.5F));
            }
        }
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
    {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.cloudboots.negates_fall_damage").formatted(Formatting.BLUE));
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient)
    {
        return ingredient.getItem() == Items.GOLD_INGOT;
    }
}