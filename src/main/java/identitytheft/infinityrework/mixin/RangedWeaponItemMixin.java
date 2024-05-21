package identitytheft.infinityrework.mixin;

import identitytheft.infinityrework.InfinityRework;
import identitytheft.infinityrework.InfinityReworkConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
    @Unique
    private static boolean returnedArrow = false;

    @Unique
    private static int getArrowChances(int infinityLevel) {
        if (infinityLevel == 1) return InfinityReworkConfig.infinityOnePercentage;
        if (infinityLevel == 2) return InfinityReworkConfig.infinityTwoPercentage;
        if (infinityLevel == 3) return InfinityReworkConfig.infinityThreePercentage;

        return 0;
    }

    @Redirect(
            method = "getProjectile",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;isInfinity(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;Z)Z")
    )
    private static boolean shouldReturnArrow(ItemStack weaponStack, ItemStack projectileStack, boolean creative) {
        int returnChance = getArrowChances(EnchantmentHelper.getLevel(Enchantments.INFINITY, weaponStack));

        Random random = Random.create();
        int rng = random.nextInt(100);

        boolean returnArrow = rng <= returnChance;

//        InfinityRework.LOGGER.info("Return Chance: {}, RNG: {}, Returned: {}", returnChance, rng, returnArrow);

        returnedArrow = creative || returnArrow && (projectileStack.isOf(Items.ARROW) || InfinityReworkConfig.tippedArrows &&
                (projectileStack.isOf(Items.TIPPED_ARROW) || projectileStack.isOf(Items.SPECTRAL_ARROW)));

        return returnedArrow;
    }

    @Redirect(
            method = "shootAll",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    boolean updateArrowPickupType(World instance, Entity entity) {
        PersistentProjectileEntity persistentProjectileEntity = (PersistentProjectileEntity) entity;

        if (returnedArrow) {
            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        } else {
            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        }

        return instance.spawnEntity(persistentProjectileEntity);
    }
}
