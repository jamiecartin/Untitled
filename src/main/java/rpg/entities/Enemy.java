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
    private Pathfinder.Grid navigationGrid;
    private List<int[]> currentPath;
    private int pathIndex;
    private List<Skill> skills;
    private int positionX;
    private int positionY;
    private Character target;
    private AIStrategy currentStrategy;

    public Enemy(String name, int health, int attack, int defense, 
                EnemyType type, Pathfinder.Grid grid) {
        super(name, health, attack, defense);
        this.enemyType = type;
        this.blackboard = new AIBlackboard();
        this.decisionMaker = new AIDecisionMaker(this);
        this.pathfinder = new Pathfinder();
        this.navigationGrid = grid;
        this.currentPath = new ArrayList<>();
        this.pathIndex = 0;
        this.skills = new ArrayList<>();
        this.currentStrategy = new AggressiveStrategy();
        initializeEnemy();
    }

    private void initializeEnemy() {
        // Initialize AI components
        blackboard.set("patrolRoute", generatePatrolRoute());
        decisionMaker.changeState(PatrolState.class);
        
        // Initialize type-specific properties
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
                this.maxHealth += 50;
                this.health = this.maxHealth;
                break;
            case BOSS:
                this.minGold = 100;
                this.maxGold = 200;
                this.experienceValue = 500;
                this.maxHealth *= 2;
                this.health = this.maxHealth;
                this.attack += 5;
                this.defense += 5;
                break;
        }
    }

    public void updateAI() {
        // Process AI state machine
        decisionMaker.update(this);
        
        // Execute current strategy
        if(target != null) {
            currentStrategy.execute(this);
        }
        
        // Follow calculated path
        if(!currentPath.isEmpty() && pathIndex < currentPath.size()) {
            followPath();
        }
    }

    private List<int[]> generatePatrolRoute() {
        List<int[]> route = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            route.add(new int[]{
                positionX + RandomGenerator.randomInt(-5, 5),
                positionY + RandomGenerator.randomInt(-5, 5)
            });
        }
        return route;
    }

    public void calculatePathTo(int targetX, int targetY) {
        currentPath = pathfinder.findPath(
            positionX, 
            positionY, 
            targetX, 
            targetY, 
            navigationGrid
        );
        pathIndex = 0;
    }

    private void followPath() {
        int[] nextStep = currentPath.get(pathIndex);
        if(positionX == nextStep[0] && positionY == nextStep[1]) {
            pathIndex++;
            if(pathIndex >= currentPath.size()) {
                currentPath.clear();
            }
            return;
        }

        // Calculate movement direction
        int dx = Integer.compare(nextStep[0], positionX);
        int dy = Integer.compare(nextStep[1], positionY);
        
        // Update position
        positionX += dx;
        positionY += dy;
    }

    public boolean detectPlayer(Character player) {
        double distance = Math.hypot(
            positionX - player.getPositionX(),
            positionY - player.getPositionY()
        );
        return distance < 10 && hasLineOfSightTo(player);
    }

    private boolean hasLineOfSightTo(Character target) {
        // Bresenham's line algorithm for LOS check
        int x0 = positionX;
        int y0 = positionY;
        int x1 = target.getPositionX();
        int y1 = target.getPositionY();
        
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        
        while(true) {
            if(x0 == x1 && y0 == y1) break;
            if(!navigationGrid.isWalkable(x0, y0)) return false;
            
            int e2 = 2 * err;
            if(e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if(e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
        return true;
    }

    public void useSkill(String skillName) {
        skills.stream()
            .filter(s -> s.getName().equalsIgnoreCase(skillName))
            .findFirst()
            .ifPresent(skill -> {
                System.out.println(name + " uses " + skill.getName() + "!");
                // Implement skill effects
            });
    }

    // Getters and setters
    public AIBlackboard getBlackboard() { return blackboard; }
    public List<Skill> getSkills() { return skills; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public void setPosition(int x, int y) { 
        positionX = x; 
        positionY = y; 
    }
    public EnemyType getEnemyType() { return enemyType; }
    public Character getTarget() { return target; }
    public void setTarget(Character target) { this.target = target; }
    public AIStrategy getCurrentStrategy() { return currentStrategy; }
    public void setCurrentStrategy(AIStrategy strategy) { currentStrategy = strategy; }
    public List<int[]> getCurrentPath() { return currentPath; }
    public Pathfinder.Grid getNavigationGrid() { return navigationGrid; }
}
