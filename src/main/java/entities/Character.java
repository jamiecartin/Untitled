package rpg.entities;

import java.util.HashMap;

public class Character {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int attack;
    protected int defense;
    protected int level;
    protected HashMap<String, Integer> attributes;
    protected HashMap<String, Integer> statusEffects;

    public Character(String name, int health, int attack, int defense) {
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.level = 1;
        this.attributes = new HashMap<>();
        this.statusEffects = new HashMap<>();
        initializeBaseAttributes();
    }

    private void initializeBaseAttributes() {
        attributes.put("Strength", 10);
        attributes.put("Dexterity", 10);
        attributes.put("Constitution", 10);
        attributes.put("Intelligence", 10);
        attributes.put("Wisdom", 10);
        attributes.put("Charisma", 10);
    }

    // Core Health Methods
    public void takeDamage(int damage) {
        int effectiveDamage = Math.max(damage - defense, 0);
        health -= effectiveDamage;
        if (health < 0) health = 0;
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) health = maxHealth;
    }

    public void restoreHealth() {
        health = maxHealth;
    }

    public boolean isAlive() {
        return health > 0;
    }

    // Status Effect Methods
    public void applyStatusEffect(String effect, int duration) {
        statusEffects.put(effect, duration);
    }

    public void removeStatusEffect(String effect) {
        statusEffects.remove(effect);
    }

    public void processStatusEffects() {
        statusEffects.entrySet().removeIf(entry -> {
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                System.out.println(name + " is no longer affected by " + entry.getKey());
                return true;
            }
            entry.setValue(remaining);
            return false;
        });
    }

    // Attribute Management
    public void modifyAttribute(String attribute, int value) {
        attributes.put(attribute, attributes.getOrDefault(attribute, 10) + value);
    }

    public int getAttribute(String attribute) {
        return attributes.getOrDefault(attribute, 10);
    }

    // Level Progression
    public void gainExperience(int exp) {
        // To be overridden in subclasses
    }

    public void levelUp() {
        level++;
        maxHealth += 5 + getAttribute("Constitution") / 2;
        attack += 1 + getAttribute("Strength") / 4;
        defense += 1 + getAttribute("Dexterity") / 4;
        health = maxHealth;
    }

    // Combat Calculations
    public int calculateDamage() {
        return attack + getAttribute("Strength") / 2;
    }

    public int calculateDefense() {
        return defense + getAttribute("Dexterity") / 4;
    }

    // Display Methods
    public void displayStatus() {
        System.out.println("\n=== " + name + " Status ===");
        System.out.println("Level: " + level);
        System.out.println("Health: " + health + "/" + maxHealth);
        System.out.println("Attack: " + attack);
        System.out.println("Defense: " + defense);
        System.out.println("Status Effects: " + statusEffects.keySet());
    }

    public void displayAttributes() {
        System.out.println("\n=== Attributes ===");
        attributes.forEach((key, value) -> 
            System.out.println(key + ": " + value)
        );
    }

    // Getters and Setters
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getLevel() { return level; }
}
