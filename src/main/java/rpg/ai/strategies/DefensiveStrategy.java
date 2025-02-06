package rpg.ai.strategies;

import rpg.entities.Enemy;
import rpg.utils.RandomGenerator;

public class DefensiveStrategy implements AIStrategy {
    @Override
    public void execute(Enemy enemy) {
        if (enemy.getHealthPercentage() < 0.6 && RandomGenerator.randomBoolean(0.8)) {
            enemy.defend();
        } else {
            enemy.basicAttack(enemy.getBlackboard().get("target", Character.class));
        }
        
        if (shouldUseHealingSkill(enemy)) {
            enemy.useSkill("Heal");
        }
    }

    private boolean shouldUseHealingSkill(Enemy enemy) {
        return enemy.getHealthPercentage() < 0.4 && 
               enemy.getSkills().stream().anyMatch(s -> s.getName().equals("Heal"));
    }
}
