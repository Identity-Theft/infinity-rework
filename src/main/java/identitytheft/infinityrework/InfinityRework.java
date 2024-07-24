package identitytheft.infinityrework;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.enchantment.Enchantments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinityRework implements ModInitializer {
	public static final String MOD_ID = "infinity-rework";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Starting Infinity Rework");

		MidnightConfig.init(MOD_ID, InfinityReworkConfig.class);
	}
}