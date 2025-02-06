package rpg.ai.strategies;

import rpg.entities.Enemy;
import rpg.utils.RandomGenerator;

public class SupportStrategy implements AIStrategy {
    @Override
    public void execute(Enemy enemy) {
        Character ally = enemy.getBlackboard().get("ally", Character.class);
        if (ally != null && ally.getHealthPercentage() < 0.5) {
            if (RandomGenerator.randomBoolean(0.7)) {
                enemy.useSkill("Heal", ally);
            }
        }
        
        if (RandomGenerator.randomBoolean(0.4)) {
            enemy.useSkill("Buff", enemy);
        } else {
            enemy.basicAttack(enemy.getBlackboard().get("target", Character.class));
        }
    }
}
