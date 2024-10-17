package com.example.hsrrelicmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RelicAdapter extends RecyclerView.Adapter<RelicAdapter.ViewHolder> {

    private List<Relic> relicData;

    public RelicAdapter(List<Relic> relicData) {
        this.relicData = relicData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_relic_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Relic relic = relicData.get(position);

        holder.getTvSet().setText(relic.getSet());
        holder.getTvMainstat().setText(relic.getMainstat());
        holder.getImgSet().setImageResource(relic.getImage());

        Iterator<Map.Entry<String, Double>> itSubstat = relic.getSubstats().entrySet().iterator();
        Iterator<TextView> itTextView = holder.getTvSubstats().iterator();
        while (itSubstat.hasNext() && itTextView.hasNext()) {
            Map.Entry<String, Double> substat = itSubstat.next();
            TextView textView = itTextView.next();

            textView.setText(substat.getKey() + ": " + substat.getValue().toString());
        }
    }

    @Override
    public int getItemCount() {
        return relicData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSet, tvMainstat, tvSubstat1, tvSubstat2, tvSubstat3, tvSubstat4;
        private ImageView imgSet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSet = itemView.findViewById(R.id.tvSet);
            tvMainstat = itemView.findViewById(R.id.tvMainstat);
            tvSubstat1 = itemView.findViewById(R.id.tvSubstat1);
            tvSubstat2 = itemView.findViewById(R.id.tvSubstat2);
            tvSubstat3 = itemView.findViewById(R.id.tvSubstat3);
            tvSubstat4 = itemView.findViewById(R.id.tvSubstat4);
            imgSet = itemView.findViewById(R.id.imgSet);
        }

        public TextView getTvSet() {
            return tvSet;
        }

        public TextView getTvMainstat() {
            return tvMainstat;
        }

        public List<TextView> getTvSubstats() {
            return Arrays.asList(tvSubstat1, tvSubstat2, tvSubstat3, tvSubstat4);
        }

        public ImageView getImgSet() {
            return imgSet;
        }
    }
}
