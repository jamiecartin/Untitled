package rpg.ai.strategies;

import rpg.entities.Enemy;
import rpg.utils.RandomGenerator;

public class AggressiveStrategy implements AIStrategy {
    @Override
    public void execute(Enemy enemy) {
        if (RandomGenerator.randomBoolean(0.7)) {
            enemy.usePowerAttack();
        } else {
            enemy.useComboAttack();
        }
        
        if (enemy.getHealthPercentage() < 0.4) {
            enemy.activateBerserkMode();
        }
    }
}
