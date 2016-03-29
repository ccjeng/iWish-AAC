package com.ccjeng.iwish.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Item;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/3/26.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private OnItemClickListener onItemClickListener;

    private RealmResults<Item> items;
    public ItemAdapter(RealmResults<Item> items) {
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_main, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.tvName.setText(items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            //tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);

        }

        @Override
        public void onClick(View v) {
            Item item = items.get(getAdapterPosition());
            onItemClickListener.onItemClick(item.getId(), item.getName());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(String id, String name);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
