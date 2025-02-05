package rpg.entities;

import rpg.items.Inventory;
import rpg.story.Quest;
import java.util.ArrayList;
import java.util.List;

public class Player extends Character {
    private Inventory inventory;
    private List<Companion> companions;
    private Companion activeCompanion;
    private List<Quest> activeQuests;
    private List<Quest> completedQuests;
    private int experience;
    private int skillPoints;

    public Player(String name) {
        super(name, 100, 15, 10);
        this.inventory = new Inventory();
        this.companions = new ArrayList<>();
        this.activeQuests = new ArrayList<>();
        this.completedQuests = new ArrayList<>();
        this.experience = 0;
        this.skillPoints = 0;
        initializeStartingInventory();
    }

    private void initializeStartingInventory() {
        inventory.addItem("Health Potion", 3);
        inventory.addItem("Mana Potion", 2);
        inventory.addItem("Basic Sword", 1);
        inventory.addItem("Leather Armor", 1);
    }

    @Override
    public void gainExperience(int exp) {
        this.experience += exp;
        System.out.println("Gained " + exp + " experience points!");
        checkLevelUp();
    }

    private void checkLevelUp() {
        int requiredExp = calculateRequiredExperience();
        if (experience >= requiredExp) {
            levelUp();
            experience -= requiredExp;
            skillPoints += 2;
            System.out.println("Gained 2 skill points!");
        }
    }

    private int calculateRequiredExperience() {
        return 100 * level + (int) Math.pow(level, 2) * 50;
    }

    @Override
    public void levelUp() {
        super.levelUp();
        maxHealth += 10 + getAttribute("Constitution");
        attack += 2 + getAttribute("Strength") / 4;
        defense += 1 + getAttribute("Dexterity") / 4;
        health = maxHealth;
        System.out.println("Reached level " + level + "!");
    }

    // Companion Management
    public void addCompanion(Companion companion) {
        companions.add(companion);
        if (activeCompanion == null) {
            activeCompanion = companion;
        }
        System.out.println(companion.getName() + " joined your party!");
    }

    public void removeCompanion(Companion companion) {
        companions.remove(companion);
        if (activeCompanion == companion) {
            activeCompanion = companions.isEmpty() ? null : companions.get(0);
        }
    }

    public void switchActiveCompanion(int index) {
        if (index >= 0 && index < companions.size()) {
            activeCompanion = companions.get(index);
            System.out.println("Active companion: " + activeCompanion.getName());
        }
    }

    // Quest Management
    public void startQuest(Quest quest) {
        activeQuests.add(quest);
        System.out.println("Started quest: " + quest.getTitle());
    }

    public void completeQuest(Quest quest) {
        if (activeQuests.remove(quest)) {
            completedQuests.add(quest);
            gainExperience(quest.getExperienceReward());
            inventory.addItems(quest.getItemRewards());
            System.out.println("Completed quest: " + quest.getTitle());
        }
    }

    // Inventory Management
    public void useItem(String itemName) {
        if (inventory.useItem(itemName)) {
            switch (itemName) {
                case "Health Potion":
                    heal(50);
                    break;
                case "Mana Potion":
                    // Implement mana system
                    break;
            }
        }
    }

    public boolean craftItem(String itemName) {
        // Integrate with CraftingSystem
        return false;
    }

    // Skill System
    public void improveAttribute(String attribute, int points) {
        if (skillPoints >= points) {
            modifyAttribute(attribute, points);
            skillPoints -= points;
            System.out.println(attribute + " increased by " + points);
        }
    }

    // Enhanced Display Methods
    @Override
    public void displayStatus() {
        super.displayStatus();
        System.out.println("Experience: " + experience + "/" + calculateRequiredExperience());
        System.out.println("Skill Points: " + skillPoints);
        System.out.println("Active Companion: " + 
            (activeCompanion != null ? activeCompanion.getName() : "None"));
    }

    public void displayQuests() {
        System.out.println("\n=== Active Quests ===");
        activeQuests.forEach(quest -> 
            System.out.println("- " + quest.getTitle() + ": " + quest.getDescription())
        );
        
        System.out.println("\n=== Completed Quests ===");
        completedQuests.forEach(quest -> 
            System.out.println("- " + quest.getTitle())
        );
    }

    // Getters
    public Inventory getInventory() { return inventory; }
    public List<Companion> getCompanions() { return companions; }
    public Companion getActiveCompanion() { return activeCompanion; }
    public List<Quest> getActiveQuests() { return activeQuests; }
    public int getExperience() { return experience; }
    public int getSkillPoints() { return skillPoints; }
}
