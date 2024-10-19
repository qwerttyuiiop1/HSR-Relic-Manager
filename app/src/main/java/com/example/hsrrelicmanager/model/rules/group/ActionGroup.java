package com.example.hsrrelicmanager.model.rules.group;

import com.example.hsrrelicmanager.model.rules.action.Action;

public class ActionGroup extends Group {
    private Action action;

    public ActionGroup(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public String getViewName() {
        return action.toString();
    }
}
