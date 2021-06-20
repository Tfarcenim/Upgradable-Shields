package tfar.upgradableshields.datagen.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import tfar.upgradableshields.ModItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(ModItems.UPGRADABLE_SHIELD).key('W', ItemTags.PLANKS)
                .key('o', Items.DIAMOND).patternLine("WoW").patternLine("WWW").patternLine(" W ").addCriterion("has_diamond", hasItem(Items.DIAMOND)).build(consumer);
    }
}
