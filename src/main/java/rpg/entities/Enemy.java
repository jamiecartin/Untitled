package rpg.entities;

import rpg.combat.Skill;
import rpg.items.Item;
import rpg.utils.RandomGenerator;
import java.util.*;

public class Enemy extends Character {
    public enum EnemyType { MINION, ELITE, BOSS }
    public enum AIBehavior { AGGRESSIVE, DEFENSIVE, SUPPORT, FLEEING }
    public enum Element { FIRE, WATER, EARTH, AIR, LIGHT, DARK, NONE }

    private EnemyType enemyType;
    private AIBehavior aiBehavior;
    private Map<Element, Double> elementalResistances;
    private Map<Element, Double> elementalWeaknesses;
    private List<LootEntry> lootTable;
    private List<Skill> enemySkills;
    private int minGold;
    private int maxGold;
    private AIDecisionMaker decisionMaker;
    private AIPerception perception;
    private AIStrategy currentStrategy;
    private int positionX;
    private int positionY;
    private Character target;

    public Enemy(String name, int health, int attack, int defense, EnemyType type) {
        super(name, health, attack, defense);
        this.enemyType = type;
        this.aiBehavior = AIBehavior.AGGRESSIVE;
        this.elementalResistances = new EnumMap<>(Element.class);
        this.elementalWeaknesses = new EnumMap<>(Element.class);
        this.lootTable = new ArrayList<>();
        this.enemySkills = new ArrayList<>();
        initializeEnemyStats();
    }

    private void initializeEnemyStats() {
        switch(enemyType) {
            case MINION:
                this.minGold = 5;
                this.maxGold = 15;
                this.experienceValue = 50;
                break;
            case ELITE:
                this.minGold = 25;
                this.maxGold = 50;
                this.experienceValue = 150;
                this.health += 50;
                break;
            case BOSS:
                this.minGold = 100;
                this.maxGold = 200;
                this.experienceValue = 500;
                this.health *= 2;
                this.attack += 5;
                this.defense += 5;
                break;
        }
    }

    // Combat AI
    public void decideAction(Character target) {
        if (health < maxHealth * 0.3 && RandomGenerator.randomBoolean(0.3)) {
            aiBehavior = AIBehavior.FLEEING;
        }

        switch(aiBehavior) {
            case AGGRESSIVE:
                performAttack(target);
                break;
            case DEFENSIVE:
                if (RandomGenerator.randomBoolean(0.4)) {
                    defend();
                } else {
                    performAttack(target);
                }
                break;
            case SUPPORT:
                // Implement support skills
                break;
            case FLEEING:
                attemptEscape();
                break;
        }
    }

    private void performAttack(Character target) {
        if (!enemySkills.isEmpty() && RandomGenerator.randomBoolean(0.3)) {
            useSkill(target);
        } else {
            basicAttack(target);
        }
    }

    private void useSkill(Character target) {
        Skill skill = enemySkills.get(RandomGenerator.randomInt(0, enemySkills.size()-1));
        System.out.println(name + " uses " + skill.getName() + "!");
        // Implement skill effects
    }

    private void defend() {
        System.out.println(name + " takes defensive stance!");
        defense += 5;
    }

    private void attemptEscape() {
        System.out.println(name + " tries to flee!");
        if (RandomGenerator.randomBoolean(0.5)) {
            // Successful escape logic
        }
    }

    // Elemental System
    public void addResistance(Element element, double percentage) {
        elementalResistances.put(element, percentage);
    }

    public void addWeakness(Element element, double percentage) {
        elementalWeaknesses.put(element, percentage);
    }

    @Override
    public void takeDamage(int damage, Element element) {
        double multiplier = 1.0;
        if (elementalResistances.containsKey(element)) {
            multiplier -= elementalResistances.get(element);
        }
        if (elementalWeaknesses.containsKey(element)) {
            multiplier += elementalWeaknesses.get(element);
        }
        
        int finalDamage = (int) (damage * multiplier);
        super.takeDamage(finalDamage);
        System.out.println(name + " takes " + finalDamage + " " + element.name().toLowerCase() + " damage!");
    }

    // Loot System
    public static class LootEntry {
        Item item;
        double dropChance;

        public LootEntry(Item item, double dropChance) {
            this.item = item;
            this.dropChance = dropChance;
        }
    }

    public void addLoot(Item item, double dropChance) {
        lootTable.add(new LootEntry(item, dropChance));
    }

    public List<Item> generateLoot() {
        List<Item> droppedItems = new ArrayList<>();
        for (LootEntry entry : lootTable) {
            if (RandomGenerator.randomBoolean(entry.dropChance)) {
                droppedItems.add(entry.item.clone());
            }
        }
        return droppedItems;
    }

    public int generateGold() {
        return RandomGenerator.randomInt(minGold, maxGold);
    }

    // Enemy Skills
    public void learnSkill(Skill skill) {
        enemySkills.add(skill);
    }

    // Cloning for spawn system
    @Override
    public Enemy clone() {
        Enemy clone = new Enemy(name, maxHealth, attack, defense, enemyType);
        clone.elementalResistances = new EnumMap<>(this.elementalResistances);
        clone.elementalWeaknesses = new EnumMap<>(this.elementalWeaknesses);
        clone.lootTable = new ArrayList<>(this.lootTable);
        clone.enemySkills = new ArrayList<>(this.enemySkills);
        return clone;
    }

    @Override
    public void displayStatus() {
        super.displayStatus();
        System.out.println("Type: " + enemyType);
        System.out.println("Behavior: " + aiBehavior);
    }

    // Getters and Setters
    public EnemyType getEnemyType() { return enemyType; }
    public AIBehavior getAiBehavior() { return aiBehavior; }
    public void setAiBehavior(AIBehavior behavior) { aiBehavior = behavior; }
}
