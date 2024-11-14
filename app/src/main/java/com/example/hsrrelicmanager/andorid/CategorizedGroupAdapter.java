package com.example.hsrrelicmanager.andorid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hsrrelicmanager.R;
import com.example.hsrrelicmanager.databinding.GroupCardDescriptionBinding;
import com.example.hsrrelicmanager.model.rules.Filter;
import com.example.hsrrelicmanager.model.rules.action.Action;
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction;
import com.example.hsrrelicmanager.model.rules.action.StatusAction;
import com.example.hsrrelicmanager.model.rules.group.ActionGroup;
import com.example.hsrrelicmanager.model.rules.group.FilterGroup;
import com.example.hsrrelicmanager.model.rules.group.Group;

import java.util.ArrayList;
import java.util.List;

public class CategorizedGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ListItem> items;
    private List<Group> groupData;

    public CategorizedGroupAdapter(List<Group> groupData) {
        this.groupData = groupData;

        List<ListItem> enhanceGroups = new ArrayList<>();
        List<ListItem> lockGroups = new ArrayList<>();
        List<ListItem> trashGroups = new ArrayList<>();
        List<ListItem> resetGroups = new ArrayList<>();
        List<ListItem> filterGroups = new ArrayList<>();

        for (Group group : groupData) {
            if (group instanceof FilterGroup) {
                filterGroups.add(new GroupItem(group));
            } else if (group instanceof ActionGroup) {
                Action action = ((ActionGroup) group).getAction();

                if (action instanceof EnhanceAction) {
                    enhanceGroups.add(new GroupItem(group));
                } else if (action instanceof StatusAction) {
                    switch (((StatusAction) action).getTargetStatus()) {
                        case LOCK:
                            lockGroups.add(new GroupItem(group));
                            break;
                        case TRASH:
                            trashGroups.add(new GroupItem(group));
                            break;
                        case DEFAULT:
                            resetGroups.add(new GroupItem(group));
                    }
                }
            }
        }

        items = new ArrayList<>();

        items.add(new HeaderItem("Enhance"));
        items.addAll(enhanceGroups);
        items.add(new DividerItem());

        items.add(new HeaderItem("Lock"));
        items.addAll(lockGroups);
        items.add(new DividerItem());

        items.add(new HeaderItem("Trash"));
        items.addAll(trashGroups);
        items.add(new DividerItem());

        items.add(new HeaderItem("Reset"));
        items.addAll(resetGroups);
        items.add(new DividerItem());

        items.add(new HeaderItem("Filter Groups"));
        items.addAll(filterGroups);
    }

    public List<ListItem> getItems() {
        return items;
    }

    public List<Group> getGroupData() {
        return groupData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case ListItem.TYPE_GROUP:
                view = li.inflate(R.layout.action_group_layout, parent, false);
                return new GroupViewHolder(view);
            case ListItem.TYPE_HEADER:
                view = li.inflate(R.layout.categorized_rules_header, parent, false);
                return new HeaderViewHolder(view);
            case ListItem.TYPE_DIVIDER:
                view = li.inflate(R.layout.categorized_rules_divider, parent, false);
                return new DividerViewHolder(view);
        }

        // Dummy view
        view = li.inflate(R.layout.categorized_rules_divider, parent, false);
        return new DividerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListItem item = items.get(position);

        switch (getItemViewType(position)) {
            case ListItem.TYPE_GROUP:
                GroupItem groupItem = (GroupItem) item;
                GroupViewHolder groupVh = (GroupViewHolder) holder;
                groupVh.updatePosition(groupItem.getGroup().getPosition());
                groupVh.bind(groupItem.getGroup());
                break;
            case ListItem.TYPE_HEADER:
                HeaderItem header = (HeaderItem) item;
                HeaderViewHolder headerVh = (HeaderViewHolder) holder;
                headerVh.bind(header.getText());
                break;
            case ListItem.TYPE_DIVIDER:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    private int getGroupImageResource(Group group) {
        if (group instanceof FilterGroup) {
            return R.drawable.sticker_ppg_11_other_01;
        } else if (group instanceof ActionGroup) {
            Action action = ((ActionGroup) group).getAction();

            if (action instanceof EnhanceAction) {
                return R.drawable.sticker_ppg_09_topaz_and_numby_03;
            } else if (action instanceof StatusAction) {
                switch (((StatusAction) action).getTargetStatus()) {
                    case LOCK:
                        return R.drawable.sticker_ppg_07_pom_pom_04;
                    case TRASH:
                        return R.drawable.sticker_ppg_12_other_01;
                    case DEFAULT:
                        return R.drawable.sticker_ppg_13_acheron_03;
                    default:
                        return -1;
                }
            }
        }

        return -1;
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        private ImageView groupIcon;
        private TextView tvGroupName, tvPosition;
        private ViewGroup filterContainer;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            groupIcon = itemView.findViewById(R.id.groupIcon);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            filterContainer = itemView.findViewById(R.id.filter_container);
        }

        public void bind(Group group) {
            tvGroupName.setText(group.getViewName());
            filterContainer.removeAllViews();
            // check class of group is filter / action group
            for (Filter filter : group.getFilters().values()) {
                GroupCardDescriptionBinding binding = GroupCardDescriptionBinding.inflate(LayoutInflater.from(filterContainer.getContext()), filterContainer, false);
                binding.rowName.setText(filter.getName() + ':');
                binding.rowValue.setText(filter.getDescription());
                filterContainer.addView(binding.getRoot());
                groupIcon.setImageResource(getGroupImageResource(group));
            }
        }

        public void updatePosition(int position) {
            tvPosition.setText(Integer.toString(position + 1));
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHeader = itemView.findViewById(R.id.tvHeader);
        }

        public void bind(String text) {
            tvHeader.setText(text);
        }
    }

    public class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    // Based on link below
    // https://stackoverflow.com/questions/34848401/divide-elements-on-groups-in-recyclerview
    public abstract class ListItem {
        public static final int TYPE_GROUP = 0;
        public static final int TYPE_HEADER = 1;
        public static final int TYPE_DIVIDER = 2;

        abstract public int getType();
    }

    class GroupItem extends ListItem {
        Group group;

        public GroupItem(Group group) {
            this.group = group;
        }

        public Group getGroup() {
            return group;
        }

        @Override
        public int getType() {
            return TYPE_GROUP;
        }
    }

    class HeaderItem extends ListItem {
        String text;

        public HeaderItem(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        @Override
        public int getType() {
            return TYPE_HEADER;
        }
    }

    class DividerItem extends ListItem {
        @Override
        public int getType() {
            return TYPE_DIVIDER;
        }
    }
}
