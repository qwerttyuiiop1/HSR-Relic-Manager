package com.example.hsrrelicmanager.model.rules.action;

import com.example.hsrrelicmanager.model.relics.Relic;

public interface Action {
    Relic apply(Relic relic);
}
