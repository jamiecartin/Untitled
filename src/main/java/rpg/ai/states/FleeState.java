package rpg.ai.states;

import rpg.ai.AIState;
import rpg.entities.Enemy;
import rpg.utils.RandomGenerator;

public class FleeState implements AIState {
    @Override
    public void enter(Enemy enemy) {
        System.out.println(enemy.getName() + " starts fleeing!");
        enemy.getBlackboard().set("fleeTarget", findFleePosition(enemy));
    }

    @Override
    public void update(Enemy enemy) {
        if (enemy.getHealthPercentage() > 0.5 || RandomGenerator.randomBoolean(0.3)) {
            enemy.getDecisionMaker().changeState(CombatState.class);
            return;
        }
        
        int[] target = enemy.getBlackboard().get("fleeTarget", int[].class);
        if (reachedPosition(enemy, target)) {
            enemy.getBlackboard().remove("fleeTarget");
            enter(enemy);
        }
        
        moveTowards(enemy, target);
    }

    private int[] findFleePosition(Enemy enemy) {
        return new int[]{
            enemy.getPositionX() + RandomGenerator.randomInt(-10, 10),
            enemy.getPositionY() + RandomGenerator.randomInt(-10, 10)
        };
    }

    private boolean reachedPosition(Enemy enemy, int[] target) {
        return enemy.getPositionX() == target[0] && enemy.getPositionY() == target[1];
    }

    private void moveTowards(Enemy enemy, int[] target) {
        // Simplified movement logic
        int dx = Integer.compare(target[0], enemy.getPositionX());
        int dy = Integer.compare(target[1], enemy.getPositionY());
        enemy.setPosition(enemy.getPositionX() + dx, enemy.getPositionY() + dy);
    }

    @Override
    public void exit(Enemy enemy) {
        enemy.getBlackboard().remove("fleeTarget");
    }
}
