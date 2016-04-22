package com.ccjeng.iwish.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccjeng.iwish.R;

import java.util.List;

/**
 * Created by andycheng on 2016/4/22.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private static final String TAG = MainAdapter.class.getSimpleName();

    private OnItemClickListener onItemClickListener;

    private List<String> items;
    private int fontSize;

    public MainAdapter(List<String> items, int fontSize) {
        this.items = items;
        this.fontSize = fontSize;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_main, parent, false);

        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.tvName.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class MainViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView tvName;

        public MainViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition(), items.get(getAdapterPosition()));
        }


    }

    public interface OnItemClickListener{
        void onItemClick(int position, String name);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
