package com.example.hsrrelicmanager.model.rules.group;

import android.annotation.SuppressLint;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.example.hsrrelicmanager.model.rules.action.Action;

@SuppressLint("ParcelCreator")
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
