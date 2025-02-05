package rpg.story;

import rpg.items.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quest {
    public enum QuestType { MAIN, SIDE, REPEATABLE, EVENT }
    public enum ObjectiveType { KILL, COLLECT, TALK, EXPLORE, ESCORT }

    private String id;
    private String title;
    private String description;
    private QuestType type;
    private int chapter;
    private Map<String, Integer> objectives;
    private Map<String, Integer> currentProgress;
    private List<String> requiredQuestIds;
    private int experienceReward;
    private Map<Item, Integer> itemRewards;
    private int goldReward;
    private boolean isCompleted;

    public Quest(String id, String title, QuestType type, int chapter) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.chapter = chapter;
        this.objectives = new HashMap<>();
        this.currentProgress = new HashMap<>();
        this.requiredQuestIds = new ArrayList<>();
        this.itemRewards = new HashMap<>();
        this.isCompleted = false;
    }

    // Builder-style configuration
    public Quest description(String description) {
        this.description = description;
        return this;
    }

    public Quest addObjective(String target, ObjectiveType type, int required) {
        String key = type.name() + ":" + target;
        objectives.put(key, required);
        currentProgress.put(key, 0);
        return this;
    }

    public Quest addRequiredQuest(String questId) {
        requiredQuestIds.add(questId);
        return this;
    }

    public Quest setExperienceReward(int amount) {
        this.experienceReward = amount;
        return this;
    }

    public Quest addItemReward(Item item, int quantity) {
        this.itemRewards.put(item, quantity);
        return this;
    }

    public Quest setGoldReward(int amount) {
        this.goldReward = amount;
        return this;
    }

    // Quest progression
    public void updateProgress(ObjectiveType type, String target, int amount) {
        String key = type.name() + ":" + target;
        if (objectives.containsKey(key)) {
            int current = currentProgress.getOrDefault(key, 0);
            currentProgress.put(key, Math.min(current + amount, objectives.get(key)));
            checkCompletion();
        }
    }

    private void checkCompletion() {
        isCompleted = objectives.entrySet().stream()
            .allMatch(entry -> 
                currentProgress.getOrDefault(entry.getKey(), 0) >= entry.getValue()
            );
    }

    // Validation
    public boolean isCompletable(List<String> completedQuestIds) {
        return requiredQuestIds.stream().allMatch(completedQuestIds::contains);
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public QuestType getType() { return type; }
    public int getChapter() { return chapter; }
    public Map<String, Integer> getObjectives() { return objectives; }
    public int getExperienceReward() { return experienceReward; }
    public Map<Item, Integer> getItemRewards() { return itemRewards; }
    public int getGoldReward() { return goldReward; }
    public boolean isCompleted() { return isCompleted; }
}
