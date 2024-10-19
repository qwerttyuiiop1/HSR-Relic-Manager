package com.example.hsrrelicmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hsrrelicmanager.databinding.GroupCardDescriptionBinding;
import com.example.hsrrelicmanager.model.rules.Filter;
import com.example.hsrrelicmanager.model.rules.group.Group;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private List<Group> groupData;

    public GroupAdapter(List<Group> groupData) {
        this.groupData = groupData;
    }

    @NonNull
    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.action_group_layout, parent, false);

        return new GroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.ViewHolder holder, int position) {
        Group group = groupData.get(position);
        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return groupData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView groupIcon;
        private TextView tvGroupName;
        private ViewGroup filterContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            groupIcon = itemView.findViewById(R.id.groupIcon);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            filterContainer = itemView.findViewById(R.id.filter_container);
        }

        public ImageView getGroupIcon() {
            return groupIcon;
        }

        public TextView getTvGroupName() {
            return tvGroupName;
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

                switch (group.getViewName().replaceAll(" .*", "")) {
                    case "Filter":
                        groupIcon.setImageResource(R.drawable.filter_group);
                        break;
                    case "Lock":
                        groupIcon.setImageResource(R.drawable.lock);
                        break;
                    case "Reset":
                        groupIcon.setImageResource(R.drawable.reset);
                        break;
                    case "Enhance":
                        groupIcon.setImageResource(R.drawable.enhance);
                        break;
                    case "Trash":
                        groupIcon.setImageResource(R.drawable.trash);
                        break;
                }
            }
        }
    }
}
