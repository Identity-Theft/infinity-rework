package identitytheft.infinityrework.mixin;

import identitytheft.infinityrework.InfinityRework;
import identitytheft.infinityrework.InfinityReworkConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BowItem.class, priority = 1001)
public class BowItemMixin {
    @Unique
    private boolean returnedArrow = false;

    @Unique
    private static int getArrowChances(int infinityLevel) {
        if (InfinityReworkConfig.useScaling) return InfinityReworkConfig.basePercentage + InfinityReworkConfig.increasePerLevel * (infinityLevel - 1);

        return switch (infinityLevel) {
            case 1 -> InfinityReworkConfig.infinityOnePercentage;
            case 2 -> InfinityReworkConfig.infinityTwoPercentage;
            case 3 -> InfinityReworkConfig.infinityThreePercentage;
            default -> 0;
        };
    }

    @ModifyVariable(
            method = "onStoppedUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",
            at = @At("STORE"),
            index = 10
    )
    private boolean shouldReturnArrow(boolean value, ItemStack stack, World world, LivingEntity shooter, int remainingUseTicks) {
        ItemStack projectileStack = shooter.getProjectileType(stack);

        int returnChance = getArrowChances(EnchantmentHelper.getLevel(Enchantments.INFINITY, stack));
        int rng = shooter.getRandom().nextInt(100);

        returnedArrow = rng <= returnChance && (projectileStack.isOf(Items.ARROW) || InfinityReworkConfig.tippedArrows &&
                (projectileStack.isOf(Items.TIPPED_ARROW) || projectileStack.isOf(Items.SPECTRAL_ARROW)));

        if (!world.isClient) InfinityRework.LOGGER.info("Return Chance: {}, RNG: {}, Returned: {}", returnChance, rng, returnedArrow);

        return returnedArrow;
    }

    @Redirect(
            method = "onStoppedUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    boolean updateArrowPickupType(World instance, Entity entity, ItemStack stack, World world, LivingEntity shooter,
                                  int remainingUseTicks) {
        PlayerEntity player = (PlayerEntity) shooter;
        PersistentProjectileEntity persistentProjectileEntity = (PersistentProjectileEntity) entity;

        if (returnedArrow || player.getAbilities().creativeMode) {
            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        } else {
            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        }

        return world.spawnEntity(persistentProjectileEntity);
    }
}
