package rpg.story;

import java.util.ArrayList;
import java.util.List;

public class StoryManager {
    private List<Quest> mainStoryQuests;
    private List<Quest> sideQuests;
    private int currentChapter;

    public StoryManager() {
        this.mainStoryQuests = new ArrayList<>();
        this.sideQuests = new ArrayList<>();
        initializeMainStory();
    }

    private void initializeMainStory() {
        mainStoryQuests.add(new Quest("Prologue", "Find the village elder", 1));
        mainStoryQuests.add(new Quest("First Blood", "Defeat 5 forest enemies", 2));
        // Add more story quests
    }

    public void progressStory() {
        currentChapter++;
        // Trigger story events based on chapter
    }
}
