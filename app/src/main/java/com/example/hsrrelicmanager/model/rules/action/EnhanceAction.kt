package com.example.hsrrelicmanager.model.rules.action;

import com.example.hsrrelicmanager.model.relics.Relic;

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
    public Relic apply(Relic relic) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String toString() {
        return "Enhance (Lvl " + targetLevel + ")";
    }
}
