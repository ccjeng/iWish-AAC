package com.ccjeng.iwish.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.FrequencyList;

import java.util.List;

/**
 * Created by andycheng on 2016/4/10.
 */
public class FrequencyAdapter extends RecyclerView.Adapter<FrequencyAdapter.FrequencyViewHolder> {

    private OnItemClickListener onItemClickListener;

    private List<FrequencyList> frequencies;
    private int fontSize;

    public FrequencyAdapter(List<FrequencyList> frequencies, int fontSize) {
        this.frequencies = frequencies;
        this.fontSize = fontSize;
    }

    @Override
    public FrequencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_main, parent, false);
        return new FrequencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FrequencyViewHolder holder, int position) {
        holder.tvName.setText(frequencies.get(position).getName());
        holder.tvAddition.setText(Long.toString(frequencies.get(position).getCount()));
    }

    @Override
    public int getItemCount() {
        return frequencies.size();
    }

    public class FrequencyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName;
        TextView tvAddition;

        public FrequencyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAddition = (TextView) itemView.findViewById(R.id.tv_addition);
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            tvAddition.setVisibility(View.VISIBLE);

        }

        @Override
        public void onClick(View v) {

            onItemClickListener.onItemClick(frequencies.get(getAdapterPosition()).getName());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(String name);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
