package rpg.story;

import rpg.entities.Player;
import rpg.items.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoryManager {
    private int currentChapter;
    private Map<Integer, List<Quest>> chapterQuests;
    private List<Quest> activeQuests;
    private List<String> completedQuestIds;
    private List<Quest> sideQuests;
    private Map<String, Boolean> storyFlags;

    public StoryManager() {
        this.currentChapter = 1;
        this.chapterQuests = new HashMap<>();
        this.activeQuests = new ArrayList<>();
        this.completedQuestIds = new ArrayList<>();
        this.sideQuests = new ArrayList<>();
        this.storyFlags = new HashMap<>();
        initializeQuests();
    }

    private void initializeQuests() {
        // Chapter 1 Main Quests
        Quest prologue = new Quest("ch1_prologue", "Prologue: The Beginning", Quest.QuestType.MAIN, 1)
            .description("Visit Elder Rowan in the village square")
            .addObjective("Elder Rowan", Quest.ObjectiveType.TALK, 1)
            .setExperienceReward(100)
            .addItemReward(new Item("map_old", "Old Map", "Tattered map of the region"), 1);

        Quest firstBlood = new Quest("ch1_first_blood", "First Blood", Quest.QuestType.MAIN, 1)
            .description("Defeat 5 forest enemies")
            .addObjective("Forest Spider", Quest.ObjectiveType.KILL, 3)
            .addObjective("Forest Wolf", Quest.ObjectiveType.KILL, 2)
            .setExperienceReward(300)
            .setGoldReward(50);

        chapterQuests.put(1, List.of(prologue, firstBlood));

        // Side Quests
        Quest lostPendant = new Quest("side_lost_pendant", "Lost Pendant", Quest.QuestType.SIDE, 1)
            .description("Find Amelia's lost pendant in the forest")
            .addObjective("Silver Pendant", Quest.ObjectiveType.COLLECT, 1)
            .addRequiredQuest("ch1_prologue")
            .setExperienceReward(150)
            .addItemReward(new Item("potion_rare", "Rare Potion", "Restores 200 HP"), 2);

        sideQuests.add(lostPendant);
    }

    public void progressStory() {
        currentChapter++;
        System.out.println("=== CHAPTER " + currentChapter + " ===");
        unlockChapterQuests(currentChapter);
        processChapterEvents(currentChapter);
    }

    private void unlockChapterQuests(int chapter) {
        if (chapterQuests.containsKey(chapter)) {
            chapterQuests.get(chapter).forEach(this::offerQuest);
        }
    }

    public void offerQuest(Quest quest) {
        if (!activeQuests.contains(quest) && 
            !completedQuestIds.contains(quest.getId()) &&
            quest.isCompletable(completedQuestIds)) {
            activeQuests.add(quest);
            System.out.println("New Quest Available: " + quest.getTitle());
        }
    }

    public void startQuest(Quest quest, Player player) {
        if (activeQuests.contains(quest) && !quest.isCompleted()) {
            System.out.println("Quest Started: " + quest.getTitle());
            player.getJournal().addQuest(quest);
        }
    }

    public void completeQuest(Quest quest, Player player) {
        if (activeQuests.remove(quest)) {
            quest.checkCompletion();
            completedQuestIds.add(quest.getId());
            applyQuestRewards(quest, player);
            checkChapterCompletion();
            System.out.println("Quest Completed: " + quest.getTitle());
        }
    }

    private void applyQuestRewards(Quest quest, Player player) {
        player.gainExperience(quest.getExperienceReward());
        player.getInventory().addGold(quest.getGoldReward());
        quest.getItemRewards().forEach((item, quantity) -> 
            player.getInventory().addItem(item.clone(), quantity)
        );
    }

    private void checkChapterCompletion() {
        if (chapterQuests.get(currentChapter).stream()
            .allMatch(q -> completedQuestIds.contains(q.getId()))) {
            progressStory();
        }
    }

    public void processChapterEvents(int chapter) {
        switch(chapter) {
            case 1:
                setStoryFlag("met_elder", true);
                break;
            case 2:
                setStoryFlag("castle_unlocked", true);
                break;
        }
    }

    public void updateQuestObjectives(Quest.ObjectiveType type, String target, int amount) {
        activeQuests.forEach(quest -> 
            quest.updateProgress(type, target, amount)
        );
    }

    public void setStoryFlag(String flag, boolean value) {
        storyFlags.put(flag, value);
    }

    public boolean checkStoryFlag(String flag) {
        return storyFlags.getOrDefault(flag, false);
    }

    // Getters
    public int getCurrentChapter() { return currentChapter; }
    public List<Quest> getActiveQuests() { return activeQuests; }
    public List<Quest> getAvailableSideQuests() { 
        return sideQuests.stream()
            .filter(q -> q.isCompletable(completedQuestIds))
            .collect(Collectors.toList());
    }
}
