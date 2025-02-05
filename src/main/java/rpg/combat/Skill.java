package rpg.combat;

public class Skill {
    private String name;
    private String description;
    private int power;
    private int cooldown;
    
    public Skill(String name, String description, int power, int cooldown) {
        this.name = name;
        this.description = description;
        this.power = power;
        this.cooldown = cooldown;
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPower() { return power; }
    public int getCooldown() { return cooldown; }
}
