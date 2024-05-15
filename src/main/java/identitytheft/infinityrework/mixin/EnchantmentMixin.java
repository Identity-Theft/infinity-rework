package identitytheft.infinityrework.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.InfinityEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// There's probably a better to do this
@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(at = @At("HEAD"), method = "getMaxLevel", cancellable = true)
    public void getMaxLevel(CallbackInfoReturnable<Integer> cir) {
        if ((Enchantment)(Object)this instanceof InfinityEnchantment) cir.setReturnValue(3);
    }

    @Inject(at = @At("HEAD"), method = "getMinPower", cancellable = true)
    private void getMinPower(int level, CallbackInfoReturnable<Integer> cir) {
        if ((Enchantment)(Object)this instanceof InfinityEnchantment) cir.setReturnValue(level * 10);
    }

    @Inject(at = @At("HEAD"), method = "getMaxPower", cancellable = true)
    private void getMaxPower(int level, CallbackInfoReturnable<Integer> cir) {
        if ((Enchantment)(Object)this instanceof InfinityEnchantment) cir.setReturnValue(level * 20);
    }
}
