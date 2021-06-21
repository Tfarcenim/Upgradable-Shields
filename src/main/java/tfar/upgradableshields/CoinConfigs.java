package tfar.upgradableshields;

import net.minecraftforge.common.ForgeConfigSpec;

public class CoinConfigs {

    public static ForgeConfigSpec.IntValue reflection;
    public static ForgeConfigSpec.IntValue lightning;
    public static ForgeConfigSpec.IntValue charge_dash;
    public static ForgeConfigSpec.IntValue jump;
    public static ForgeConfigSpec.IntValue summon;
    public static ForgeConfigSpec.IntValue ender;
    public static ForgeConfigSpec.IntValue arrow;
    public static ForgeConfigSpec.IntValue ultimate;

    public static CoinConfigs build(ForgeConfigSpec.Builder b) {
        b.push("general");
        arrow = b.defineInRange("arrow_cost",30,0,Integer.MAX_VALUE);
        reflection = b.defineInRange("reflection_cost",50,0,Integer.MAX_VALUE);
        summon = b.defineInRange("summon_cost",120,0,Integer.MAX_VALUE);
        lightning = b.defineInRange("lightning_cost",200,0,Integer.MAX_VALUE);
        charge_dash = b.defineInRange("charge_dash_cost",310,0,Integer.MAX_VALUE);
        jump = b.defineInRange("jump_cost",420,0,Integer.MAX_VALUE);
        ender = b.defineInRange("ender_cost",450,0,Integer.MAX_VALUE);
        ultimate = b.defineInRange("ultimate_cost",500,0,Integer.MAX_VALUE);

        b.pop();
        return null;
    }

}
