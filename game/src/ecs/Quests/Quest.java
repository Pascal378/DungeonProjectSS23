package ecs.Quests;

public abstract class Quest {

    public abstract void reward();

    public abstract boolean checkProgress();

    public abstract void startQuest();
}
