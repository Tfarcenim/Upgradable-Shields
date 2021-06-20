package tfar.upgradableshields.datagen.data.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Consumer;

public class ModAdvancements implements Consumer<Consumer<Advancement>> {
    @Override
    public void accept(Consumer<Advancement> advancementConsumer) {
        Advancement rootAdvancement = Advancement.Builder.builder().withDisplay(Items.CAKE,
                new TranslationTextComponent("advancements.upgradableshields.root.title"),
                new TranslationTextComponent("advancements.upgradableshields.root.description"),
                new ResourceLocation("textures/gui/advancements/backgrounds/end.png"),
                FrameType.TASK, false, false, false)
                .withCriterion("consume_food", ConsumeItemTrigger.Instance.forItem(Items.APPLE)).register(advancementConsumer, "upgradableshields:root");




    }
}
