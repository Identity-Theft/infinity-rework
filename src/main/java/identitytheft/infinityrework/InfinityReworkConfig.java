package identitytheft.infinityrework;

import eu.midnightdust.lib.config.MidnightConfig;

public class InfinityReworkConfig extends MidnightConfig {
    @Entry(isSlider = true, min = 1, max = 100) public static int infinityOnePercentage = 25;
    @Entry(isSlider = true, min = 1, max = 100) public static int infinityTwoPercentage = 50;
    @Entry(isSlider = true, min = 1, max = 100) public static int infinityThreePercentage = 75;
    @Entry() public static boolean allowMending = true;
    @Entry() public static boolean tippedArrows = true;
}