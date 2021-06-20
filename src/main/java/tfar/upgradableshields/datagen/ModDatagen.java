package tfar.upgradableshields.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import tfar.upgradableshields.datagen.assets.ModItemModelProvider;
import tfar.upgradableshields.datagen.assets.ModLangProvider;
import tfar.upgradableshields.datagen.data.ModRecipeProvider;

public class ModDatagen {

    public static void start(GatherDataEvent e) {
        DataGenerator dataGenerator = e.getGenerator();
        ExistingFileHelper helper = e.getExistingFileHelper();
        if (e.includeClient()) {
            dataGenerator.addProvider(new ModItemModelProvider(dataGenerator,helper));
            dataGenerator.addProvider(new ModLangProvider(dataGenerator));
        }
        if (e.includeServer()) {
            dataGenerator.addProvider(new ModRecipeProvider(dataGenerator));
        }
    }
}
