package com.example.hsrrelicmanager.model.rules.group;

import com.example.hsrrelicmanager.model.rules.Filter;

import java.util.EnumMap;
import java.util.Map;

public abstract class Group {
    protected Map<Filter.Type, Filter> filters;
    protected int position;

    public Group() {
        this.filters = new EnumMap<>(Filter.Type.class);
    }

    public Map<Filter.Type, Filter> getFilters() {
        return filters;
    }

    public void setFilters(Map<Filter.Type, Filter> filters) {
        this.filters = filters;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public abstract String getViewName();
}
