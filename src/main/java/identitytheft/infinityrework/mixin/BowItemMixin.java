package identitytheft.infinityrework.mixin;

import identitytheft.infinityrework.config.Config;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BowItem.class)
public class BowItemMixin {
    @Unique
    private boolean returnedArrow = false;

    @Unique
    private static int infinity_rework$getArrowChances(int infinityLevel) {
        if (Config.HANDLER.instance().useScaling) return Config.HANDLER.instance().basePercentage + Config.HANDLER.instance().levelIncrease * (infinityLevel - 1);

        return switch (infinityLevel) {
            case 1 -> Config.HANDLER.instance().infinityOnePercentage;
            case 2 -> Config.HANDLER.instance().infinityTwoPercentage;
            default -> Config.HANDLER.instance().infinityThreePercentage;
        };
    }

    @ModifyVariable(
            method = "onStoppedUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",
            at = @At("STORE"),
            index = 10
    )
    private boolean infinity_rework$shouldReturnArrow(boolean value, ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (((PlayerEntity)user).getAbilities().creativeMode) return true;

        ItemStack projectileStack = user.getProjectileType(stack);

        int returnChance = infinity_rework$getArrowChances(EnchantmentHelper.getLevel(Enchantments.INFINITY, stack));
        int rng = user.getRandom().nextInt(100);

        returnedArrow = rng <= returnChance && ((Config.HANDLER.instance().allArrowTypes && projectileStack.isIn(ItemTags.ARROWS)) || projectileStack.isOf(Items.ARROW));

        return returnedArrow;
    }

    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    void infinity_rework$decrementArrow(ItemStack instance, int amount)
    {
        if (!returnedArrow) instance.decrement(amount);
    }

    @Redirect(
            method = "onStoppedUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    boolean infinity_rework$spawnArrow(World instance, Entity entity, ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        PlayerEntity player = (PlayerEntity) user;
        PersistentProjectileEntity persistentProjectileEntity = (PersistentProjectileEntity) entity;

        if (returnedArrow || player.getAbilities().creativeMode) {
            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        } else {
            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        }

        return world.spawnEntity(persistentProjectileEntity);
    }
}
