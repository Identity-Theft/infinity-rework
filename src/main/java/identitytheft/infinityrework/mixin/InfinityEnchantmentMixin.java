package identitytheft.infinityrework.mixin;

import identitytheft.infinityrework.InfinityReworkConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InfinityEnchantment.class)
public class InfinityEnchantmentMixin {
	@Unique
	public int getMaxLevel() {
		return 3;
	}

	@Inject(at = @At("HEAD"), method = "getMinPower", cancellable = true)
	private void getMinPower(int level, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(level * 10);
	}

	@Inject(at = @At("HEAD"), method = "getMaxPower", cancellable = true)
	private void getMaxPower(int level, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(level * 20);
	}

	@Inject(at = @At("HEAD"), method = "canAccept", cancellable = true)
	private void canAccept(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
		if (InfinityReworkConfig.allowMending) {
			//noinspection ConstantConditions
			cir.setReturnValue((Object) this != other);
		}
	}
}