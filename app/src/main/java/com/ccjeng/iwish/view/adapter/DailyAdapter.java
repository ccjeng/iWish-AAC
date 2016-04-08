package com.ccjeng.iwish.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Daily;

import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/4/8.
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder> {

    private OnItemClickListener onItemClickListener;

    private RealmResults<Daily> dailies;
    private int fontSize;

    public DailyAdapter(RealmResults<Daily> dailies, int fontSize) {
        this.dailies = dailies;
        this.fontSize = fontSize;
    }

    @Override
    public DailyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_main, parent, false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DailyViewHolder holder, int position) {
        holder.tvName.setText(dailies.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return dailies.size();
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName;

        public DailyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        }

        @Override
        public void onClick(View v) {
            Daily daily = dailies.get(getAdapterPosition());
            onItemClickListener.onItemClick(daily.getId(), daily.getName());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(String id, String name);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
