package com.example.hsrrelicmanager.model.rules.action;

import com.example.hsrrelicmanager.model.relics.Relic;

public class StatusAction implements Action {
    private Relic.Status targetStatus;

    public StatusAction(Relic.Status targetStatus) {
        this.targetStatus = targetStatus;
    }

    public Relic.Status getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(Relic.Status targetStatus) {
        this.targetStatus = targetStatus;
    }

    @Override
    public void apply(Relic relic) {
        // TODO: Do stuff
    }

    @Override
    public String toString() {
        switch (targetStatus) {
            case LOCK:
                return "Lock";
            case TRASH:
                return "Trash";
            case DEFAULT:
                return "Reset";
        }

        return "";
    }
}
