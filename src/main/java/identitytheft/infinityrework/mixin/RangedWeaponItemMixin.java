package identitytheft.infinityrework.mixin;

import identitytheft.infinityrework.config.Config;
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
        if (Config.HANDLER.instance().useScaling) return Config.HANDLER.instance().basePercentage + Config.HANDLER.instance().levelIncrease * (infinityLevel - 1);

        return switch (infinityLevel) {
            case 1 -> Config.HANDLER.instance().infinityOnePercentage;
            case 2 -> Config.HANDLER.instance().infinityTwoPercentage;
            default -> Config.HANDLER.instance().infinityThreePercentage;
        };
    }

    @Redirect(
            method = "getProjectile",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAmmoUse(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;I)I")
    )
    private static int ammoUse(ServerWorld world, ItemStack rangedWeaponStack, ItemStack projectileStack, int baseAmmoUse) {
        int level = EnchantmentHelper.getAmmoUse(world, rangedWeaponStack, projectileStack, 1) - 1;
        if (level <= 0) return 1;

        int returnChance = getArrowChances(level);
        if (returnChance >= 100) return 0;

        int rng = world.getRandom().nextInt(100);
        boolean useArrow = rng > returnChance;

        return useArrow ? 1 : 0;
    }
}
