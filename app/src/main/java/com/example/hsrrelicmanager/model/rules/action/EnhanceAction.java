package com.example.hsrrelicmanager.model.rules.action;

import com.example.hsrrelicmanager.Relic;

public class EnhanceAction implements Action {
    private int targetLevel;

    public EnhanceAction(int targetLevel) {
        this.targetLevel = targetLevel;
    }

    public int getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(int targetLevel) {
        this.targetLevel = targetLevel;
    }

    @Override
    public void apply(Relic relic) {
        // TODO: Do stuff
    }

    @Override
    public String toString() {
        return "Enhance (Lvl " + targetLevel + ")";
    }
}
