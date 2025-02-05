package rpg.items;

import java.util.HashMap;

public class CraftingSystem {
    private HashMap<String, CraftingRecipe> recipes;

    public CraftingSystem() {
        initializeRecipes();
    }

    private void initializeRecipes() {
        recipes = new HashMap<>();
        // Add recipes
        recipes.put("Health Potion", new CraftingRecipe()
            .addMaterial("Herb", 3)
            .addMaterial("Water", 1)
            .setResult("Health Potion", 1));
    }

    public boolean craftItem(Inventory inventory, String itemName) {
        CraftingRecipe recipe = recipes.get(itemName);
        if (recipe == null) return false;
        
        if (inventory.hasMaterials(recipe.getMaterials())) {
            inventory.removeMaterials(recipe.getMaterials());
            inventory.addItem(recipe.getResultItem(), recipe.getResultQuantity());
            return true;
        }
        return false;
    }
}
