package rpg.ai;

import rpg.entities.Enemy;
import java.util.*;

public class AIDecisionMaker {
    private final Map<Class<? extends AIState>, AIState> states = new HashMap<>();
    private AIState currentState;

    public AIDecisionMaker(Enemy enemy) {
        registerState(new CombatState());
        registerState(new PatrolState());
        registerState(new FleeState());
    }

    private void registerState(AIState state) {
        states.put(state.getClass(), state);
    }

    public void changeState(Class<? extends AIState> newState, Enemy enemy) {
        if (currentState != null) {
            currentState.exitState(enemy);
        }
        currentState = states.get(newState);
        currentState.enterState(enemy);
    }

    public void update(Enemy enemy) {
        currentState.updateState(enemy);
    }
}
