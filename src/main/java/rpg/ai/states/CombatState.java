package rpg.ai.states;

import rpg.ai.AIState;
import rpg.entities.Enemy;
import rpg.utils.RandomGenerator;

public class CombatState implements AIState {
    private static final double SKILL_CHANCE = 0.35;
    private static final double FLEE_THRESHOLD = 0.25;

    @Override
    public void enterState(Enemy enemy) {
        System.out.println(enemy.getName() + " enters combat mode!");
    }

    @Override
    public void updateState(Enemy enemy) {
        if (shouldFlee(enemy)) {
            enemy.getDecisionMaker().changeState(FleeState.class, enemy);
            return;
        }

        if (RandomGenerator.randomBoolean(SKILL_CHANCE) && !enemy.getSkills().isEmpty()) {
            enemy.useSkill(enemy.getTarget());
        } else {
            enemy.basicAttack(enemy.getTarget());
        }
    }

    private boolean shouldFlee(Enemy enemy) {
        return (double)enemy.getHealth() / enemy.getMaxHealth() < FLEE_THRESHOLD &&
               RandomGenerator.randomBoolean(0.6);
    }

    @Override
    public void exitState(Enemy enemy) {
        System.out.println(enemy.getName() + " exits combat mode");
    }
}
