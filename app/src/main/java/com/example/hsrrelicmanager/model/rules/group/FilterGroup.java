package com.example.hsrrelicmanager.model.rules.group;

import android.annotation.SuppressLint;
import android.os.Parcel;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class FilterGroup extends Group {
    private List<Group> groupList;

    public FilterGroup() {
        groupList = new ArrayList<>();
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    @Override
    public String getViewName() {
        return "Filter Group";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
