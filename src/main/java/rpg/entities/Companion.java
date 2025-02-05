package rpg.entities;

import java.util.HashMap;
import rpg.combat.Skill;
import rpg.utils.RandomGenerator;

public class Companion extends Character {
    public enum CompanionType { HEALER, TANK, RANGED, MAGE }
    
    private CompanionType type;
    private int loyalty;
    private int maxLoyalty;
    private HashMap<String, Skill> abilities;
    private boolean isActive;
    private int abilityCooldown;

    public Companion(String name, CompanionType type) {
        super(name, 120, 18, 15);
        this.type = type;
        this.loyalty = 50;
        this.maxLoyalty = 100;
        this.abilities = new HashMap<>();
        this.isActive = true;
        this.abilityCooldown = 0;
        initializeCompanion();
    }

    private void initializeCompanion() {
        switch(type) {
            case HEALER:
                maxHealth = 150;
                attack = 12;
                defense = 18;
                addAbility(new Skill("Healing Wave", "Restores party health", 30, 3));
                break;
            case TANK:
                maxHealth = 200;
                attack = 20;
                defense = 25;
                addAbility(new Skill("Shield Wall", "Protects the party", 0, 4));
                break;
            case RANGED:
                maxHealth = 100;
                attack = 25;
                defense = 12;
                addAbility(new Skill("Precision Shot", "High damage single attack", 40, 3));
                break;
            case MAGE:
                maxHealth = 90;
                attack = 30;
                defense = 10;
                addAbility(new Skill("Firestorm", "AoE damage", 50, 5));
                break;
        }
        health = maxHealth;
    }

    public void addAbility(Skill skill) {
        abilities.put(skill.getName(), skill);
    }

    public void useSpecialAbility(Character target) {
        if (abilityCooldown > 0) {
            System.out.println(name + "'s ability is on cooldown!");
            return;
        }

        if (loyalty < 30) {
            System.out.println(name + " is unwilling to help!");
            return;
        }

        Skill ability = getRandomAbility();
        if (ability == null) return;

        System.out.println(name + " uses " + ability.getName() + "!");
        
        switch(ability.getName()) {
            case "Healing Wave":
                healParty(ability.getPower());
                break;
            case "Shield Wall":
                applyPartyBuff("Defense Boost", ability.getPower());
                break;
            case "Precision Shot":
                target.takeDamage(attack + ability.getPower());
                break;
            case "Firestorm":
                applyAoEDamage(ability.getPower());
                break;
        }

        abilityCooldown = ability.getCooldown();
        modifyLoyalty(-5);
    }

    private void healParty(int amount) {
        // Implementation would reference party members
        System.out.println("Party healed for " + amount + " HP!");
    }

    private void applyPartyBuff(String buff, int value) {
        // Implementation would reference party members
        System.out.println("Party gained " + buff + "!");
    }

    private void applyAoEDamage(int damage) {
        // Implementation would reference enemies
        System.out.println("Enemies take " + damage + " damage!");
    }

    public void modifyLoyalty(int amount) {
        loyalty = Math.min(Math.max(loyalty + amount, 0), maxLoyalty);
        if (amount > 0) {
            System.out.println(name + "'s loyalty increases!");
        } else {
            System.out.println(name + "'s loyalty decreases...");
        }
    }

    public void updateCompanion() {
        if (abilityCooldown > 0) abilityCooldown--;
        
        // Random chance for companion to act independently
        if (isActive && RandomGenerator.chance(30)) {
            autonomousAction();
        }
    }

    private void autonomousAction() {
        if (loyalty < 20 && RandomGenerator.chance(40)) {
            System.out.println(name + " refuses to fight!");
            return;
        }

        String[] actions = {
            "guards you closely",
            "scouts ahead",
            "shares supplies",
            "offers encouragement"
        };
        
        System.out.println(name + " " + actions[RandomGenerator.nextInt(actions.length)]);
        modifyLoyalty(2);
    }

    public void interact(String interactionType) {
        switch(interactionType.toLowerCase()) {
            case "praise":
                modifyLoyalty(5);
                System.out.println(name + " appreciates the praise!");
                break;
            case "gift":
                modifyLoyalty(10);
                System.out.println(name + " is pleased with the gift!");
                break;
            case "scold":
                modifyLoyalty(-15);
                System.out.println(name + " looks disappointed...");
                break;
            default:
                System.out.println(name + " doesn't understand that interaction");
        }
    }

    private Skill getRandomAbility() {
        if (abilities.isEmpty()) return null;
        return abilities.values().iterator().next();
    }

    // Getters and Setters
    public CompanionType getType() { return type; }
    public int getLoyalty() { return loyalty; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    @Override
    public void displayStatus() {
        super.displayStatus();
        System.out.println("Loyalty: " + loyalty + "/" + maxLoyalty);
        System.out.println("Type: " + type.toString());
        System.out.println("Abilities: " + abilities.keySet());
    }
}
