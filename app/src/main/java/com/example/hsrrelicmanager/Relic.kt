package com.example.hsrrelicmanager;

import java.util.Map;

public class Relic {
    private String set;
    private String slot;
    private int rarity;
    private int level;
    private String mainstat;
    private Map<String, Double> substats;
    private Status status;
    private int image;

    enum Status {
        DEFAULT,
        LOCK,
        TRASH
    }

    public Relic(String set, String slot, int rarity, int level, String mainstat, Map<String, Double> substats, Status status, int image) {
        this.set = set;
        this.slot = slot;
        this.rarity = rarity;
        this.level = level;
        this.mainstat = mainstat;
        this.substats = substats;
        this.status = status;
        this.image = image;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getMainstat() {
        return mainstat;
    }

    public void setMainstat(String mainstat) {
        this.mainstat = mainstat;
    }

    public Map<String, Double> getSubstats() {
        return substats;
    }

    public void setSubstats(Map<String, Double> substats) {
        this.substats = substats;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
