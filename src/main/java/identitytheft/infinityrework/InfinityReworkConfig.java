package identitytheft.infinityrework;

import eu.midnightdust.lib.config.MidnightConfig;

public class InfinityReworkConfig extends MidnightConfig {
    @Comment(centered = true) public static Comment behaviour;
    @Entry() public static boolean allowMending = true;
    @Entry() public static boolean tippedArrows = true;
    @Entry() public static boolean useScaling = false;
    @Comment(centered = true) public static Comment set;
    @Entry(isSlider = true, min = 1, max = 100) public static int infinityOnePercentage = 25;
    @Entry(isSlider = true, min = 1, max = 100) public static int infinityTwoPercentage = 50;
    @Entry(isSlider = true, min = 1, max = 100) public static int infinityThreePercentage = 75;
    @Comment(centered = true) public static Comment scale;
    @Entry(isSlider = true, min = 1, max = 100) public static int basePercentage = 25;
    @Entry(isSlider = true, min = 1, max = 100) public static int increasePerLevel = 25;
}
