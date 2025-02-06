package rpg.ai.states;

import rpg.ai.AIState;
import rpg.entities.Enemy;
import rpg.utils.RandomGenerator;

public class AlertState implements AIState {
    @Override
    public void enter(Enemy enemy) {
        System.out.println(enemy.getName() + " is on high alert!");
        enemy.getBlackboard().set("investigationTarget", 
            enemy.getBlackboard().get("lastKnownPlayerPosition", int[].class));
    }

    @Override
    public void update(Enemy enemy) {
        int[] target = enemy.getBlackboard().get("investigationTarget", int[].class);
        
        if (reachedPosition(enemy, target)) {
            if (RandomGenerator.randomBoolean(0.7)) {
                enemy.getDecisionMaker().changeState(PatrolState.class);
            } else {
                enemy.getBlackboard().set("investigationTarget", 
                    new int[]{
                        target[0] + RandomGenerator.randomInt(-5, 5),
                        target[1] + RandomGenerator.randomInt(-5, 5)
                    });
            }
        }
        
        moveTowards(enemy, target);
    }

    private boolean reachedPosition(Enemy enemy, int[] target) {
        return enemy.getPositionX() == target[0] && enemy.getPositionY() == target[1];
    }

    private void moveTowards(Enemy enemy, int[] target) {
        int dx = Integer.compare(target[0], enemy.getPositionX());
        int dy = Integer.compare(target[1], enemy.getPositionY());
        enemy.setPosition(enemy.getPositionX() + dx, enemy.getPositionY() + dy);
    }

    @Override
    public void exit(Enemy enemy) {
        enemy.getBlackboard().remove("investigationTarget");
    }
}
