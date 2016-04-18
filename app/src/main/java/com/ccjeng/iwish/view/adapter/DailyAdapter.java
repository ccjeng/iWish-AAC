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

    private final static String TAG = DailyAdapter.class.getSimpleName();

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
    public void onBindViewHolder(final DailyViewHolder holder, int position) {
        holder.tvName.setText(dailies.get(position).getName());
        holder.tvAddition.setText(dailies.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return dailies.size();
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName;
        TextView tvAddition;

        public DailyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAddition = (TextView) itemView.findViewById(R.id.tv_addition);
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            tvAddition.setVisibility(View.VISIBLE);

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
