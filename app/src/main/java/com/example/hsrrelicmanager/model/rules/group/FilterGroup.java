package com.example.hsrrelicmanager.model.rules.group;

import java.util.ArrayList;
import java.util.List;

public class FilterGroup extends Group {
    private List<ActionGroup> actionGroupList;

    public FilterGroup() {
        actionGroupList = new ArrayList<>();
    }

    public List<ActionGroup> getActionGroupList() {
        return actionGroupList;
    }

    @Override
    public String getViewName() {
        return "Filter Group";
    }
}
