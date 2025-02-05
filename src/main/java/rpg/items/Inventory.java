package rpg.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Inventory {
    private Map<String, List<Item>> items;
    private Item equippedWeapon;
    private Item equippedArmor;
    private double maxWeight;
    private int maxSlots;

    public Inventory() {
        this(100, 30); // Default capacity: 100kg weight, 30 slots
    }

    public Inventory(double maxWeight, int maxSlots) {
        this.items = new HashMap<>();
        this.maxWeight = maxWeight;
        this.maxSlots = maxSlots;
    }

    // Core inventory management
    public boolean addItem(Item item) {
        if (getTotalWeight() + item.getWeight() > maxWeight) {
            System.out.println("Too heavy to carry!");
            return false;
        }

        if (getUsedSlots() >= maxSlots && !item.isStackable()) {
            System.out.println("Not enough space!");
            return false;
        }

        String itemId = item.getId();
        if (item.isStackable()) {
            List<Item> stack = items.getOrDefault(itemId, new ArrayList<>());
            if (!stack.isEmpty() && stack.size() < stack.get(0).getMaxStack()) {
                stack.add(item);
                items.put(itemId, stack);
                return true;
            }
        }

        if (items.containsKey(itemId)) {
            items.get(itemId).add(item);
        } else {
            List<Item> newStack = new ArrayList<>();
            newStack.add(item);
            items.put(itemId, newStack);
        }
        return true;
    }

    public boolean removeItem(String itemId, int quantity) {
        if (!items.containsKey(itemId)) return false;
        
        List<Item> stack = items.get(itemId);
        if (stack.size() < quantity) return false;
        
        for (int i = 0; i < quantity; i++) {
            stack.remove(0);
        }
        
        if (stack.isEmpty()) {
            items.remove(itemId);
        }
        return true;
    }

    // Equipment management
    public boolean equipWeapon(Item weapon) {
        if (weapon.getType() != Item.ItemType.WEAPON) return false;
        equippedWeapon = weapon;
        return true;
    }

    public boolean equipArmor(Item armor) {
        if (armor.getType() != Item.ItemType.ARMOR) return false;
        equippedArmor = armor;
        return true;
    }

    // Crafting support
    public boolean hasMaterials(Map<String, Integer> requiredMaterials) {
        return requiredMaterials.entrySet().stream()
            .allMatch(entry -> getItemCount(entry.getKey()) >= entry.getValue());
    }

    // Query methods
    public int getItemCount(String itemId) {
        return items.containsKey(itemId) ? items.get(itemId).size() : 0;
    }

    public double getTotalWeight() {
        return items.values().stream()
            .flatMap(List::stream)
            .mapToDouble(Item::getWeight)
            .sum();
    }

    public int getUsedSlots() {
        return items.values().stream()
            .mapToInt(stack -> stack.get(0).isStackable() ? 1 : stack.size())
            .sum();
    }

    public List<Item> getItemsByType(Item.ItemType type) {
        return items.values().stream()
            .flatMap(List::stream)
            .filter(item -> item.getType() == type)
            .collect(Collectors.toList());
    }

    // Item usage
    public boolean useItem(String itemId) {
        if (!items.containsKey(itemId)) return false;
        
        Item item = items.get(itemId).get(0);
        if (!item.isUsable()) return false;
        
        // Apply item effects here
        if (item.isConsumable()) {
            removeItem(itemId, 1);
        }
        return true;
    }

    // Bulk operations
    public void addItems(Map<String, Integer> itemsToAdd) {
        // Implementation would create Item objects and add them
    }

    public void removeMaterials(Map<String, Integer> materials) {
        materials.forEach(this::removeItem);
    }

    // Getters
    public Item getEquippedWeapon() { return equippedWeapon; }
    public Item getEquippedArmor() { return equippedArmor; }
    public double getMaxWeight() { return maxWeight; }
    public int getMaxSlots() { return maxSlots; }
}
