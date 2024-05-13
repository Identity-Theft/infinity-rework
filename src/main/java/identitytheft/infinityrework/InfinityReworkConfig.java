package identitytheft.infinityrework;

import eu.midnightdust.lib.config.MidnightConfig;

public class InfinityReworkConfig extends MidnightConfig {
    @Entry(category = "infinity", isSlider = true, min = 1, max = 100) public static int infinityOnePercentage = 25;
    @Entry(category = "infinity", isSlider = true, min = 1, max = 100) public static int infinityTwoPercentage = 50;
    @Entry(category = "infinity", isSlider = true, min = 1, max = 100) public static int infinityThreePercentage = 75;
    @Entry(category = "mending") public static boolean allowMending = true;
}