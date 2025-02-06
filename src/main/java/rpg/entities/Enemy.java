package rpg.entities;

import rpg.ai.*;
import rpg.ai.states.*;
import rpg.ai.strategies.*;
import rpg.combat.Skill;
import rpg.items.Item;
import rpg.utils.RandomGenerator;
import java.util.*;

public class Enemy extends Character {
    public enum EnemyType { MINION, ELITE, BOSS }
    
    private EnemyType enemyType;
    private AIDecisionMaker decisionMaker;
    private AIBlackboard blackboard;
    private Pathfinder pathfinder;
    private List<Skill> skills;
    private int positionX;
    private int positionY;

    public Enemy(String name, int health, int attack, int defense, EnemyType type) {
        super(name, health, attack, defense);
        this.enemyType = type;
        this.blackboard = new AIBlackboard();
        this.decisionMaker = new AIDecisionMaker(this);
        this.pathfinder = new Pathfinder();
        this.skills = new ArrayList<>();
        initializeEnemy();
    }

    private void initializeEnemy() {
        blackboard.set("patrolRoute", generatePatrolRoute());
        decisionMaker.changeState(PatrolState.class);
    }

    private List<int[]> generatePatrolRoute() {
        List<int[]> route = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            route.add(new int[]{
                positionX + RandomGenerator.randomInt(-5, 5),
                positionY + RandomGenerator.randomInt(-5, 5)
            });
        }
        return route;
    }

    public void updateAI() {
        decisionMaker.update(this);
    }

    public void detectPlayer(Player player) {
        if (calculateDistanceTo(player) < 10) {
            blackboard.set("target", player);
            decisionMaker.changeState(AlertState.class);
        }
    }

    private double calculateDistanceTo(Character other) {
        return Math.sqrt(Math.pow(positionX - other.getPositionX(), 2) + 
                        Math.pow(positionY - other.getPositionY(), 2));
    }

    // Getters and setters
    public AIBlackboard getBlackboard() { return blackboard; }
    public List<Skill> getSkills() { return skills; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public void setPosition(int x, int y) { positionX = x; positionY = y; }
    public EnemyType getEnemyType() { return enemyType; }
}
