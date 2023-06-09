package ecs.Quests;

/** A class from which new quests can be inherited. */
public abstract class Quest {

    private String questInfo;

    public Quest(String questInfo) {
        this.questInfo = questInfo;
    }

    public abstract void reward();

    public abstract boolean checkProgress();

    public abstract void startQuest();

    public String getQuestInfo() {
        return questInfo;
    }
}
