package com.example.hsrrelicmanager.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hsrrelicmanager.R;
import com.example.hsrrelicmanager.databinding.GroupCardDescriptionBinding;
import com.example.hsrrelicmanager.model.rules.Filter;
import com.example.hsrrelicmanager.model.rules.group.ActionGroup;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private List<ActionGroup> groupData;

    public GroupAdapter(List<ActionGroup> groupData) {
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
        ActionGroup group = groupData.get(position);
        holder.updatePosition(position);
        holder.bind(group);

        holder.card.setBackgroundResource(R.drawable.bg_dark);
        holder.ivTrash.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return groupData.size();
    }

    public List<ActionGroup> getGroupData() {
        return groupData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView groupIcon;
        private TextView tvGroupName, tvPosition;
        private ViewGroup filterContainer;
        private LinearLayout card;
        private ImageView ivTrash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            groupIcon = itemView.findViewById(R.id.groupIcon);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            filterContainer = itemView.findViewById(R.id.filter_container);
            card = itemView.findViewById(R.id.group_card);
            ivTrash = itemView.findViewById(R.id.trash_icon);
        }

        public void bind(ActionGroup group) {
            tvGroupName.setText(group.getViewName());
            filterContainer.removeAllViews();
            // check class of group is filter / action group
            for (Filter filter : group.getFilters().values()) {
                if (filter != null) {
                    GroupCardDescriptionBinding binding = GroupCardDescriptionBinding.inflate(LayoutInflater.from(filterContainer.getContext()), filterContainer, false);
                    binding.rowName.setText(filter.getName() + ':');
                    binding.rowValue.setText(filter.getDescription());
                    filterContainer.addView(binding.getRoot());
                    groupIcon.setImageResource(group.getImageResource());
                }
//                switch (group.getViewName().replaceAll(" .*", "")) {
//                    case "Filter":
//                        groupIcon.setImageResource(R.drawable.filter_group);
//                        break;
//                    case "Lock":
//                        groupIcon.setImageResource(R.drawable.lock);
//                        break;
//                    case "Reset":
//                        groupIcon.setImageResource(R.drawable.reset);
//                        break;
//                    case "Enhance":
//                        groupIcon.setImageResource(R.drawable.enhance);
//                        break;
//                    case "Trash":
//                        groupIcon.setImageResource(R.drawable.trash);
//                        break;
//                }
            }
        }

        public void updatePosition(int position) {
            tvPosition.setText(Integer.toString(position + 1));
        }
    }
}
