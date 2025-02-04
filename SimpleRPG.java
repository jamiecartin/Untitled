import java.util.Scanner;
import java.util.Random;
import java.util.HashMap;

class Character {
    String name;
    int health;
    int maxHealth;
    int attackDamage;
    int defense;
    int mana;
    int maxMana;
    String element;
    HashMap<String, Integer> inventory;

    public Character(String name, int health, int attackDamage, int defense, String element) {
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.attackDamage = attackDamage;
        this.defense = defense;
        this.element = element;
        this.maxMana = 50;
        this.mana = maxMana;
        this.inventory = new HashMap<>();
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(int damage) {
        health -= Math.max(damage - defense, 0);
        health = Math.min(health, maxHealth);
    }

    public void restoreMana(int amount) {
        mana = Math.min(mana + amount, maxMana);
    }
}

class Player extends Character {
    int experience;
    int level;

    public Player(String name) {
        super(name, 100, 15, 10, "Neutral");
        experience = 0;
        level = 1;
        inventory.put("Potion", 3);
        inventory.put("Ether", 2);
    }

    public void levelUp() {
        level++;
        attackDamage += 5;
        defense += 3;
        maxHealth += 20;
        health = maxHealth;
        maxMana += 10;
        mana = maxMana;
        System.out.println("Level up! You are now level " + level);
    }

    public void learnElementalSkill(String element) {
        this.element = element;
        System.out.println("You've mastered the power of " + element + "!");
    }
}

class Enemy extends Character {
    private String[] elementalPhrases = {
        "The air crackles with energy!",
        "You feel a strange aura...",
        "The ground trembles beneath you!"
    };

    public Enemy(String name, int health, int attackDamage, int defense, String element) {
        super(name, health, attackDamage, defense, element);
    }

    public void elementalRoar() {
        System.out.println(elementalPhrases[new Random().nextInt(elementalPhrases.length)]);
    }
}

public class EnhancedRPG {
    private Player player;
    private Scanner scanner;
    private Random random;

    public EnhancedRPG() {
        scanner = new Scanner(System.in);
        random = new Random();
    }

    public void startGame() {
        System.out.println("=== ELEMENTAL QUEST ===");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        player = new Player(name);
        gameLoop();
    }

    private void gameLoop() {
        while (player.isAlive()) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Explore");
            System.out.println("2. Check Status");
            System.out.println("3. View Inventory");
            System.out.println("4. Quit");
            System.out.print("Choice: ");
            
            int choice = scanner.nextInt();
            
            switch(choice) {
                case 1:
                    explore();
                    break;
                case 2:
                    showStatus();
                    break;
                case 3:
                    showInventory();
                    break;
                case 4:
                    System.out.println("Thanks for playing!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
        System.out.println("\nGAME OVER!");
    }

    private void explore() {
        System.out.println("\nYou venture into the unknown...");
        int encounter = random.nextInt(100);
        
        if (encounter < 50) {
            encounterEnemy();
        } else if (encounter < 75) {
            findTreasure();
        } else {
            System.out.println("You discover an ancient shrine...");
            discoverShrine();
        }
    }

    private void encounterEnemy() {
        Enemy enemy = generateRandomEnemy();
        System.out.println("\nA wild " + enemy.name + " (" + enemy.element + ") appears!");
        combatMenu(player, enemy);
        
        if (player.isAlive()) {
            int exp = random.nextInt(20) + 10;
            System.out.println("You gained " + exp + " XP!");
            player.gainExperience(exp);
            if (player.experience >= player.level * 50) {
                player.levelUp();
            }
        }
    }

    private Enemy generateRandomEnemy() {
        String[] enemies = {"Fire Imp", "Water Nymph", "Earth Golem", "Storm Wolf"};
        String name = enemies[random.nextInt(enemies.length)];
        
        return switch (name) {
            case "Fire Imp" -> new Enemy(name, 50, 18, 8, "Fire");
            case "Water Nymph" -> new Enemy(name, 60, 15, 12, "Water");
            case "Earth Golem" -> new Enemy(name, 80, 20, 15, "Earth");
            case "Storm Wolf" -> new Enemy(name, 55, 22, 10, "Air");
            default -> new Enemy(name, 60, 18, 10, "Neutral");
        };
    }

    private void combatMenu(Player player, Enemy enemy) {
        while (enemy.isAlive() && player.isAlive()) {
            System.out.println("\n" + player.name + " HP: " + player.health + "/" + player.maxHealth + " | MP: " + player.mana + "/" + player.maxMana);
            System.out.println(enemy.name + " HP: " + enemy.health + "/" + enemy.maxHealth);
            System.out.println("1. Attack");
            System.out.println("2. Elemental Skill");
            System.out.println("3. Use Item");
            System.out.println("4. Attempt Flee");
            System.out.print("Choice: ");
            
            int choice = scanner.nextInt();
            
            switch(choice) {
                case 1:
                    attack(player, enemy);
                    break;
                case 2:
                    useElementalSkill(player, enemy);
                    break;
                case 3:
                    useItem(player);
                    break;
                case 4:
                    if (attemptFlee()) return;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
            
            if (enemy.isAlive()) {
                enemy.elementalRoar();
                attack(enemy, player);
            }
        }
    }

    private void attack(Character attacker, Character defender) {
        int baseDamage = attacker.attackDamage + random.nextInt(5);
        double elementMultiplier = calculateElementMultiplier(attacker.element, defender.element);
        boolean isCritical = random.nextDouble() < 0.15;
        
        int finalDamage = (int)(baseDamage * elementMultiplier);
        if (isCritical) {
            finalDamage *= 2;
            System.out.println("Critical hit!");
        }
        
        defender.takeDamage(finalDamage);
        System.out.printf("%s attacks with %s energy for %d damage!%n",
            attacker.name, attacker.element, finalDamage);
        
        applyElementalEffect(attacker.element, defender);
    }

    private double calculateElementMultiplier(String attackerElement, String defenderElement) {
        HashMap<String, String> weaknesses = new HashMap<>();
        weaknesses.put("Fire", "Earth");
        weaknesses.put("Water", "Fire");
        weaknesses.put("Earth", "Water");
        weaknesses.put("Air", "Earth");

        if (weaknesses.get(attackerElement) != null && 
            weaknesses.get(attackerElement).equals(defenderElement)) return 1.5;
        if (weaknesses.get(defenderElement) != null && 
            weaknesses.get(defenderElement).equals(attackerElement)) return 0.75;
        return 1.0;
    }

    private void applyElementalEffect(String element, Character target) {
        if (random.nextDouble() < 0.3) {
            switch(element) {
                case "Fire":
                    System.out.println(target.name + " is burning!");
                    target.health -= 5;
                    break;
                case "Water":
                    System.out.println(target.name + " is soaked and vulnerable!");
                    target.defense -= 2;
                    break;
                case "Earth":
                    System.out.println(target.name + " is staggered!");
                    target.attackDamage -= 3;
                    break;
                case "Air":
                    System.out.println(target.name + " is disoriented!");
                    target.mana -= 10;
                    break;
            }
        }
    }

    private void useElementalSkill(Player player, Enemy enemy) {
        if (player.mana < 20) {
            System.out.println("Not enough mana!");
            return;
        }
        
        int skillDamage = (int)(player.attackDamage * 1.5);
        System.out.println("You unleash " + player.element + " fury!");
        enemy.takeDamage(skillDamage);
        player.mana -= 20;
        
        if (player.element.equals("Water")) {
            player.health = Math.min(player.health + 10, player.maxHealth);
            System.out.println("Healing waters restore 10 HP!");
        }
    }

    private void discoverShrine() {
        String[] elements = {"Fire", "Water", "Earth", "Air"};
        String element = elements[random.nextInt(elements.length)];
        
        System.out.println("You find a " + element + " Shrine!");
        System.out.println("1. Touch the shrine");
        System.out.println("2. Leave it alone");
        int choice = scanner.nextInt();
        
        if (choice == 1) {
            player.learnElementalSkill(element);
            System.out.println("Your attacks now have " + element + " affinity!");
        }
    }

    private void findTreasure() {
        String[] treasures = {"Potion", "Ether", "Fire Stone", "Water Crystal", "Earth Medallion"};
        String item = treasures[random.nextInt(treasures.length)];
        
        player.inventory.put(item, player.inventory.getOrDefault(item, 0) + 1);
        System.out.println("Found a " + item + "! Added to inventory.");
    }

    private void useItem(Player player) {
        System.out.println("\nInventory:");
        player.inventory.forEach((item, count) -> System.out.println("- " + item + " (" + count + ")"));
        System.out.print("Choose item: ");
        String item = scanner.next();
        
        if (player.inventory.getOrDefault(item, 0) > 0) {
            switch(item) {
                case "Potion":
                    player.health = Math.min(player.health + 30, player.maxHealth);
                    System.out.println("Restored 30 HP!");
                    break;
                case "Ether":
                    player.mana = Math.min(player.mana + 25, player.maxMana);
                    System.out.println("Restored 25 MP!");
                    break;
                case "Fire Stone":
                    player.attackDamage += 5;
                    System.out.println("Attack boosted by 5 points!");
                    break;
                case "Water Crystal":
                    player.defense += 5;
                    System.out.println("Defense boosted by 5 points!");
                    break;
                case "Earth Medallion":
                    player.maxHealth += 20;
                    System.out.println("Max HP increased by 20!");
                    break;
                default:
                    System.out.println("Can't use that item here!");
                    return;
            }
            player.inventory.put(item, player.inventory.get(item) - 1);
        } else {
            System.out.println("Item not found!");
        }
    }

    private boolean attemptFlee() {
        if (random.nextDouble() < 0.4) {
            System.out.println("Escaped successfully!");
            return true;
        }
        System.out.println("Escape failed!");
        return false;
    }

    private void showStatus() {
        System.out.println("\n=== PLAYER STATUS ===");
        System.out.println("Name: " + player.name);
        System.out.println("Level: " + player.level);
        System.out.println("Health: " + player.health + "/" + player.maxHealth);
        System.out.println("Mana: " + player.mana + "/" + player.maxMana);
        System.out.println("Attack: " + player.attackDamage);
        System.out.println("Defense: " + player.defense);
        System.out.println("Element: " + player.element);
        System.out.println("XP: " + player.experience + "/" + (player.level * 50));
    }

    private void showInventory() {
        System.out.println("\n=== INVENTORY ===");
        player.inventory.forEach((item, count) -> {
            if (count > 0) System.out.println("- " + item + " (" + count + ")");
        });
    }

    public static void main(String[] args) {
        EnhancedRPG game = new EnhancedRPG();
        game.startGame();
    }
}
