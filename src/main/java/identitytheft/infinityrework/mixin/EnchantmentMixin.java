package identitytheft.infinityrework.mixin;

import identitytheft.infinityrework.config.Config;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.InfinityEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(at = @At("HEAD"), method = "getMaxLevel", cancellable = true)
    public void infinity_rework$getMaxLevel(CallbackInfoReturnable<Integer> cir) {
        if ((Enchantment)(Object)this instanceof InfinityEnchantment) cir.setReturnValue(Config.HANDLER.instance().useScaling ? Config.HANDLER.instance().maxLevel : 3);
    }
}
