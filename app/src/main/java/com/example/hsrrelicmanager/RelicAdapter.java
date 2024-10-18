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
        holder.getTvMainstatVal().setText(relic.getMainstatVal().toString());
        holder.getIvMainstat().setImageResource(getStatResource(relic.getMainstat()));

        holder.getImgSet().setImageResource(relic.getImage());

        int backgroundResource = getRarityResource(relic);
        holder.getImgSet().setBackgroundResource(backgroundResource);

        Iterator<Map.Entry<String, Double>> itSubstat = relic.getSubstats().entrySet().iterator();
        Iterator<TextView> itTextView = holder.getTvSubstats().iterator();
        Iterator<TextView> itTextViewVal= holder.getTvSubstatVals().iterator();
        Iterator<ImageView> itImageView= holder.getIvSubstats().iterator();

        while (itSubstat.hasNext() && itTextView.hasNext() && itTextViewVal.hasNext() && itImageView.hasNext()) {
            Map.Entry<String, Double> substat = itSubstat.next();
            TextView textView = itTextView.next();
            TextView textViewVal = itTextViewVal.next();
            ImageView imageView = itImageView.next();

            textView.setText(substat.getKey());
            textViewVal.setText(substat.getValue().toString());

            imageView.setImageResource(getStatResource(substat.getKey()));
        }
    }

    @Override
    public int getItemCount() {
        return relicData.size();
    }

    private int getRarityResource(Relic relic) {
        int rarity = relic.getRarity();

        switch(rarity) {
            case 1:
                return R.drawable.bg_1_star;
            case 2:
                return R.drawable.bg_2_star;
            case 3:
                return R.drawable.bg_3_star;
            case 4:
                return R.drawable.bg_4_star;
            case 5:
                return R.drawable.bg_5_star;
            default:
                return R.drawable.playfrag_test;
        }
    }

    private int getStatResource(String stat) {
        switch(stat) {
            case "ATK":
            case "ATK%":
                return R.drawable.icon_atk;
            case "DEF":
                return R.drawable.icon_def;
            case "SPD":
                return R.drawable.icon_spd;
            case "CRIT Rate":
                return R.drawable.icon_crit_rate;
            default:
                return R.drawable.playfrag_test;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSet, tvMainstat, tvSubstat1, tvSubstat2, tvSubstat3, tvSubstat4,
                tvMainstatVal, tvSubstat1Val, tvSubstat2Val, tvSubstat3Val, tvSubstat4Val;

        private ImageView imgSet,
                ivMainstat, ivSubstat1, ivSubstat2, ivSubstat3, ivSubstat4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSet = itemView.findViewById(R.id.tvSet);
            tvMainstat = itemView.findViewById(R.id.tvMainstat);
            tvSubstat1 = itemView.findViewById(R.id.tvSubstat1);
            tvSubstat2 = itemView.findViewById(R.id.tvSubstat2);
            tvSubstat3 = itemView.findViewById(R.id.tvSubstat3);
            tvSubstat4 = itemView.findViewById(R.id.tvSubstat4);

            tvMainstatVal = itemView.findViewById(R.id.tvMainstatVal);
            tvSubstat1Val = itemView.findViewById(R.id.tvSubstat1Val);
            tvSubstat2Val = itemView.findViewById(R.id.tvSubstat2Val);
            tvSubstat3Val = itemView.findViewById(R.id.tvSubstat3Val);
            tvSubstat4Val = itemView.findViewById(R.id.tvSubstat4Val);

            ivMainstat = itemView.findViewById(R.id.ivMainstat);
            ivSubstat1 = itemView.findViewById(R.id.ivSubstat1);
            ivSubstat2 = itemView.findViewById(R.id.ivSubstat2);
            ivSubstat3 = itemView.findViewById(R.id.ivSubstat3);
            ivSubstat4 = itemView.findViewById(R.id.ivSubstat4);

            imgSet = itemView.findViewById(R.id.imgSet);
        }

        public TextView getTvSet() {
            return tvSet;
        }

        public TextView getTvMainstat() {
            return tvMainstat;
        }

        public TextView getTvMainstatVal() {
            return tvMainstatVal;
        }

        public List<TextView> getTvSubstats() {
            return Arrays.asList(tvSubstat1, tvSubstat2, tvSubstat3, tvSubstat4);
        }

        public List<TextView> getTvSubstatVals() {
            return Arrays.asList(tvSubstat1Val, tvSubstat2Val, tvSubstat3Val, tvSubstat4Val);
        }

        public ImageView getIvMainstat() {
            return ivMainstat;
        }

        public List<ImageView> getIvSubstats() {
            return Arrays.asList(ivSubstat1, ivSubstat2, ivSubstat3, ivSubstat4);
        }

        public ImageView getImgSet() {
            return imgSet;
        }
    }
}