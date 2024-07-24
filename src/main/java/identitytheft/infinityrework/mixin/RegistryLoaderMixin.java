package identitytheft.infinityrework.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.llamalad7.mixinextras.sugar.Local;
import identitytheft.infinityrework.InfinityReworkConfig;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;

@Mixin(RegistryLoader.class)
public abstract class RegistryLoaderMixin {
    @Inject(method="parseAndAdd(Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Lnet/minecraft/registry/RegistryOps;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/resource/Resource;Lnet/minecraft/registry/entry/RegistryEntryInfo;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/gson/JsonParser;parseReader(Ljava/io/Reader;)Lcom/google/gson/JsonElement;", remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
    private static <E> void parseAndAdd(MutableRegistry<E> registry, com.mojang.serialization.Decoder<E> decoder, RegistryOps<com.google.gson.JsonElement> ops, RegistryKey<E> key, Resource resource, RegistryEntryInfo entryInfo, CallbackInfo cir, @Local JsonElement jsonElement) throws IOException {
        Identifier wholeRegistryIdentifier = registry.getKey().getValue();
        String wholeRegistryIdentifierString = wholeRegistryIdentifier.getNamespace() + ":" + wholeRegistryIdentifier.getPath();

        if (wholeRegistryIdentifierString.equals("minecraft:enchantment")) {
            Identifier individualEnchantmentIdentifier = key.getValue();
            String individualEnchantmentIdentifierString = individualEnchantmentIdentifier.getNamespace() + ":" + individualEnchantmentIdentifier.getPath();

            if (individualEnchantmentIdentifierString.equals("minecraft:infinity")) {
                var items = InfinityReworkConfig.tippedArrows ? "#minecraft:arrows" : "minecraft:arrow";

                ((JsonObject) jsonElement).add("effects", JsonParser.parseString("{\n" +
                    "    \"minecraft:ammo_use\": [\n" +
                    "      {\n" +
                    "        \"effect\": {\n" +
                    "          \"type\": \"minecraft:add\",\n" +
                    "          \"value\": {\n" +
                    "            \"type\": \"minecraft:linear\",\n" +
                    "            \"base\": 1.0,\n" +
                    "            \"per_level_above_first\": 1.0\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"requirements\": {\n" +
                    "          \"condition\": \"minecraft:match_tool\",\n" +
                    "          \"predicate\": {\n" +
                    "            \"items\": \"" + items + "\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }"
                    )
                );

                ((JsonObject)jsonElement).add("max_cost", JsonParser.parseString("""
                    {
                        "base": 55,
                        "per_level_above_first": 8
                      }"""
                    )
                );

                ((JsonObject)jsonElement).add("min_cost", JsonParser.parseString("""
                    {
                        "base": 5,
                        "per_level_above_first": 8
                      }"""
                    )
                );

                ((JsonObject)jsonElement).addProperty("max_level", 3);

                if (InfinityReworkConfig.allowMending) ((JsonObject) jsonElement).remove("exclusive_set");
            }
        }
    }
}