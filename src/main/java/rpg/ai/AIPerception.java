package rpg.ai;

import rpg.entities.Character;
import rpg.utils.RandomGenerator;

public class AIPerception {
    private static final int DETECTION_RANGE = 8;
    private static final double HEARING_THRESHOLD = 0.7;

    public boolean detectPlayer(Enemy enemy, Character player) {
        return isInVision(enemy, player) || canHearPlayer(enemy, player);
    }

    private boolean isInVision(Enemy enemy, Character player) {
        // Simplified line-of-sight check
        return calculateDistance(enemy, player) <= DETECTION_RANGE &&
               RandomGenerator.randomBoolean(0.8);
    }

    private boolean canHearPlayer(Enemy enemy, Character player) {
        return calculateDistance(enemy, player) <= DETECTION_RANGE/2 &&
               RandomGenerator.randomBoolean(HEARING_THRESHOLD);
    }

    private double calculateDistance(Enemy enemy, Character player) {
        // Mock position system - implement actual positioning as needed
        return Math.abs(enemy.getPositionX() - player.getPositionX()) + 
               Math.abs(enemy.getPositionY() - player.getPositionY());
    }
}
