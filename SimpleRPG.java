import java.util.Scanner;
import java.util.Random;

// Base Character Class
class Character {
    String name;
    int health;
    int attackDamage;
    int defense;

    public Character(String name, int health, int attackDamage, int defense) {
        this.name = name;
        this.health = health;
        this.attackDamage = attackDamage;
        this.defense = defense;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(int damage) {
        health -= Math.max(damage - defense, 0);
    }
}

// Player Class
class Player extends Character {
    int potions;
    int experience;
    int level;

    public Player(String name) {
        super(name, 100, 15, 10);
        potions = 3;
        experience = 0;
        level = 1;
    }

    public void usePotion() {
        if (potions > 0) {
            health += 30;
            potions--;
            System.out.println("Used potion! Health is now " + health);
        } else {
            System.out.println("No potions left!");
        }
    }

    public void gainExperience(int exp) {
        experience += exp;
        if (experience >= level * 50) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        attackDamage += 5;
        defense += 3;
        health += 20;
        System.out.println("Level up! You are now level " + level);
    }
}

// Enemy Class
class Enemy extends Character {
    public Enemy(String name, int health, int attackDamage, int defense) {
        super(name, health, attackDamage, defense);
    }
}

// Main Game Class
public class SimpleRPG {
    private Player player;
    private Scanner scanner;
    private Random random;

    public SimpleRPG() {
        scanner = new Scanner(System.in);
        random = new Random();
    }

    public void startGame() {
        System.out.println("Welcome to SimpleRPG!");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        player = new Player(name);
        
        gameLoop();
    }

    private void gameLoop() {
        while (player.isAlive()) {
            System.out.println("\n----------------------------");
            System.out.println("What would you like to do?");
            System.out.println("1. Explore");
            System.out.println("2. Check Status");
            System.out.println("3. Quit");
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
                    System.out.println("Thanks for playing!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
        System.out.println("\nGame Over!");
    }

    private void explore() {
        System.out.println("\nYou venture into the wilderness...");
        if (random.nextDouble() < 0.6) {
            encounterEnemy();
        } else {
            System.out.println("You found nothing of interest.");
        }
    }

    private void encounterEnemy() {
        Enemy enemy = generateRandomEnemy();
        System.out.println("A wild " + enemy.name + " appears!");

        while (enemy.isAlive() && player.isAlive()) {
            System.out.println("\n" + player.name + " HP: " + player.health);
            System.out.println(enemy.name + " HP: " + enemy.health);
            System.out.println("1. Attack");
            System.out.println("2. Use Potion");
            System.out.print("Choice: ");
            
            int choice = scanner.nextInt();
            
            switch(choice) {
                case 1:
                    attack(player, enemy);
                    break;
                case 2:
                    player.usePotion();
                    break;
                default:
                    System.out.println("Invalid choice!");
                    continue;
            }
            
            if (enemy.isAlive()) {
                attack(enemy, player);
            }
        }
        
        if (player.isAlive()) {
            int exp = random.nextInt(20) + 10;
            System.out.println("You defeated the " + enemy.name + "! Gained " + exp + " XP");
            player.gainExperience(exp);
        }
    }

    private void attack(Character attacker, Character defender) {
        int damage = attacker.attackDamage + random.nextInt(5);
        defender.takeDamage(damage);
        System.out.println(attacker.name + " attacks for " + damage + " damage!");
    }

    private Enemy generateRandomEnemy() {
        String[] enemies = {"Goblin", "Skeleton", "Orc", "Spider"};
        String name = enemies[random.nextInt(enemies.length)];
        
        return switch (name) {
            case "Goblin" -> new Enemy(name, 40, 10, 5);
            case "Skeleton" -> new Enemy(name, 50, 12, 8);
            case "Orc" -> new Enemy(name, 60, 15, 10);
            case "Spider" -> new Enemy(name, 30, 8, 3);
            default -> new Enemy(name, 50, 10, 5);
        };
    }

    private void showStatus() {
        System.out.println("\n--- Player Status ---");
        System.out.println("Name: " + player.name);
        System.out.println("Level: " + player.level);
        System.out.println("Health: " + player.health);
        System.out.println("Attack: " + player.attackDamage);
        System.out.println("Defense: " + player.defense);
        System.out.println("Potions: " + player.potions);
        System.out.println("Experience: " + player.experience + "/" + (player.level * 50));
    }

    public static void main(String[] args) {
        SimpleRPG game = new SimpleRPG();
        game.startGame();
    }
}
