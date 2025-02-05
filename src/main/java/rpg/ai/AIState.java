package rpg.ai;

public interface AIState {
    void enterState(Enemy enemy);
    void updateState(Enemy enemy);
    void exitState(Enemy enemy);
}
