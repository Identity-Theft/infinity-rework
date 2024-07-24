package identitytheft.infinityrework.mixin;

import identitytheft.infinityrework.InfinityReworkConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RangedWeaponItem.class)
public abstract class RangedWeaponItemMixin {

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

    @Redirect(
            method = "getProjectile",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAmmoUse(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;I)I")
    )
    private static int ammoUse(ServerWorld world, ItemStack rangedWeaponStack, ItemStack projectileStack, int baseAmmoUse) {

        int level = EnchantmentHelper.getAmmoUse(world, rangedWeaponStack, projectileStack, 1) - 1;
        if (level == 0) return 1;

        int returnChance = getArrowChances(level);
        if (returnChance >= 100) return 0;

        int rng = world.getRandom().nextInt(100);
        boolean useArrow = rng > returnChance;

        return useArrow ? 1 : 0;
    }
}
