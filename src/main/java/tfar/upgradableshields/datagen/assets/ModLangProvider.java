package tfar.upgradableshields.datagen.assets;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import tfar.upgradableshields.ModItems;
import tfar.upgradableshields.UpgradableShields;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(DataGenerator gen) {
        super(gen, UpgradableShields.MODID,"en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(() -> ModItems.COIN,"Coin");
        addItem(() -> ModItems.UPGRADABLE_SHIELD,"Upgradable Shield");
        addItem(() -> ModItems.UPGRADE_BOOK,"Upgrade Book");
        addItem(()-> ModItems.WALLET,"Wallet");
    }
}
