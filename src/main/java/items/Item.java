package rpg.items;

import rpg.combat.StatusEffect;
import java.util.HashMap;
import java.util.Map;

public class Item implements Cloneable {
    public enum ItemType {
        CONSUMABLE, WEAPON, ARMOR, MATERIAL, QUEST, SPECIAL
    }

    private String id;
    private String name;
    private String description;
    private ItemType type;
    private int maxStack;
    private double weight;
    private int value;
    private Map<String, Integer> attributes;
    private Map<String, Integer> craftingMaterials;
    private StatusEffect statusEffect;

    public Item(String id, String name, ItemType type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.maxStack = type == ItemType.MATERIAL ? 99 : 1;
        this.attributes = new HashMap<>();
        this.craftingMaterials = new HashMap<>();
    }

    // Builder-style methods for fluent configuration
    public Item description(String description) {
        this.description = description;
        return this;
    }

    public Item maxStack(int maxStack) {
        this.maxStack = maxStack;
        return this;
    }

    public Item weight(double weight) {
        this.weight = weight;
        return this;
    }

    public Item value(int value) {
        this.value = value;
        return this;
    }

    public Item addAttribute(String attribute, int value) {
        this.attributes.put(attribute, value);
        return this;
    }

    public Item addCraftingMaterial(String itemId, int quantity) {
        this.craftingMaterials.put(itemId, quantity);
        return this;
    }

    public Item statusEffect(StatusEffect effect) {
        this.statusEffect = effect;
        return this;
    }

    // Core functionality
    public boolean isUsable() {
        return type == ItemType.CONSUMABLE || 
               type == ItemType.SPECIAL ||
               attributes.containsKey("usable");
    }

    public boolean isConsumable() {
        return type == ItemType.CONSUMABLE;
    }

    public boolean isStackable() {
        return maxStack > 1;
    }

    @Override
    public Item clone() {
        try {
            Item cloned = (Item) super.clone();
            cloned.attributes = new HashMap<>(this.attributes);
            cloned.craftingMaterials = new HashMap<>(this.craftingMaterials);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ItemType getType() { return type; }
    public int getMaxStack() { return maxStack; }
    public double getWeight() { return weight; }
    public int getValue() { return value; }
    public Map<String, Integer> getAttributes() { return attributes; }
    public Map<String, Integer> getCraftingMaterials() { return craftingMaterials; }
    public StatusEffect getStatusEffect() { return statusEffect; }
}
