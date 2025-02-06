package rpg.ai.states;

import rpg.ai.AIState;
import rpg.entities.Enemy;
import rpg.utils.RandomGenerator;

public class PatrolState implements AIState {
    private int currentWaypoint = 0;
    private List<int[]> patrolRoute;

    @Override
    public void enterState(Enemy enemy) {
        generatePatrolRoute(enemy);
        System.out.println(enemy.getName() + " begins patrolling");
    }

    @Override
    public void updateState(Enemy enemy) {
        if (enemy.detectPlayer()) {
            enemy.getDecisionMaker().changeState(AlertState.class, enemy);
            return;
        }

        // Move to next waypoint
        if (reachedWaypoint(enemy)) {
            currentWaypoint = (currentWaypoint + 1) % patrolRoute.size();
        }
        moveTowardsWaypoint(enemy);
    }

    private void generatePatrolRoute(Enemy enemy) {
        // Generate random patrol route
        patrolRoute = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            patrolRoute.add(new int[]{
                enemy.getPositionX() + RandomGenerator.randomInt(-5, 5),
                enemy.getPositionY() + RandomGenerator.randomInt(-5, 5)
            });
        }
    }

    private boolean reachedWaypoint(Enemy enemy) {
        int[] waypoint = patrolRoute.get(currentWaypoint);
        return enemy.getPositionX() == waypoint[0] && 
               enemy.getPositionY() == waypoint[1];
    }

    private void moveTowardsWaypoint(Enemy enemy) {
        // Simplified movement logic
        int[] waypoint = patrolRoute.get(currentWaypoint);
        enemy.setPositionX(moveCoordinate(enemy.getPositionX(), waypoint[0]));
        enemy.setPositionY(moveCoordinate(enemy.getPositionY(), waypoint[1]));
    }

    private int moveCoordinate(int current, int target) {
        return current + Integer.compare(target, current);
    }

    @Override
    public void exitState(Enemy enemy) {
        patrolRoute.clear();
    }
}
