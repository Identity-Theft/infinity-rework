package identitytheft.infinityrework.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import identitytheft.infinityrework.InfinityRework;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GUI implements ModMenuApi {
    private static Option<Boolean> createOption(String name, boolean def, Supplier<Boolean> getter, Consumer<Boolean> setter)
    {
        return Option.<Boolean>createBuilder()
                .name(Text.translatable(InfinityRework.MOD_ID + "." + name))
                .description(OptionDescription.of(Text.translatable(InfinityRework.MOD_ID + "." + name + ".tooltip")))
                .binding(def, getter, setter)
                .controller(opt -> BooleanControllerBuilder.create(opt)
//                        .formatValue(val -> Text.translatable("gui.yes"))
                        .coloured(true)
                )
                .build();
    }

    private static Option<Integer> createOption(String name, Integer def, Supplier<Integer> getter, Consumer<Integer> setter)
    {
        return Option.<Integer>createBuilder()
                .name(Text.translatable(InfinityRework.MOD_ID + "." + name))
                .description(OptionDescription.of(Text.translatable(InfinityRework.MOD_ID + "." + name + ".tooltip")))
                .binding(def, getter, setter)
                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                        .range(0, 100)
                        .step(1)
                        .formatValue(val -> Text.literal(val + "%"))
                )
                .build();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return parent -> YetAnotherConfigLib.create(
                Config.HANDLER,
                ((defaults, config, builder) -> builder
                        .title(Text.translatable("infinity-rework.title"))
                        .category(ConfigCategory.createBuilder()
                                .name(Text.translatable("infinity-rework.title"))

                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("infinity-rework.behaviour"))
                                        .option(createOption("allow_mending", defaults.allowMending, () -> config.allowMending, newVal -> config.allowMending = newVal))
                                        .option(createOption("all_arrows", defaults.allArrowTypes, () -> config.allArrowTypes, newVal -> config.allArrowTypes = newVal))
                                        .option(createOption("use_scaling", defaults.useScaling, () -> config.useScaling, newVal -> config.useScaling = newVal))
                                        .build()
                                )

                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("infinity-rework.scale"))

                                        .option(Option.<Integer>createBuilder()
                                                .name(Text.translatable("infinity-rework.max_level"))
                                                .description(OptionDescription.of(Text.translatable("infinity-rework.max_level.tooltip")))
                                                .binding(defaults.maxLevel, () -> config.maxLevel, newVal -> config.maxLevel = newVal)
                                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                        .range(1, 5)
                                                        .step(1)
                                                )
                                                .build()
                                        )

                                        .option(createOption("base_percentage", defaults.basePercentage, () -> config.basePercentage, newVal -> config.basePercentage = newVal))
                                        .option(createOption("level_increase", defaults.levelIncrease, () -> config.levelIncrease, newVal -> config.levelIncrease = newVal))
                                        .build()
                                )

                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("infinity-rework.levels"))
                                        .option(createOption("infinity_i_percentage", defaults.infinityOnePercentage, () -> config.infinityOnePercentage, newVal -> config.infinityOnePercentage = newVal))
                                        .option(createOption("infinity_ii_percentage", defaults.infinityTwoPercentage, () -> config.infinityTwoPercentage, newVal -> config.infinityTwoPercentage = newVal))
                                        .option(createOption("infinity_iii_percentage", defaults.infinityThreePercentage, () -> config.infinityThreePercentage, newVal -> config.infinityThreePercentage = newVal))
                                        .build()
                                )

                                .build()
                        )
                )
        ).generateScreen(parent);
    }
}