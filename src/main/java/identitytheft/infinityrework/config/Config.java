package identitytheft.infinityrework.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import identitytheft.infinityrework.InfinityRework;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class Config {
    public static final ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(Identifier.of(InfinityRework.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("infinity-rework.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry public boolean allowMending = false;
    @SerialEntry public boolean allArrowTypes = false;
    @SerialEntry public boolean useScaling = true;

    @SerialEntry public int maxLevel = 3;
    @SerialEntry public int basePercentage = 25;
    @SerialEntry public int levelIncrease = 25;

    @SerialEntry public int infinityOnePercentage = 25;
    @SerialEntry public int infinityTwoPercentage = 50;
    @SerialEntry public int infinityThreePercentage = 75;
}
