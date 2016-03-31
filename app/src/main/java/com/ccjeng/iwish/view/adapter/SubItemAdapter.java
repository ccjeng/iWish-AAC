package com.ccjeng.iwish.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Item;
import com.ccjeng.iwish.model.SubItem;

import io.realm.RealmList;

/**
 * Created by andycheng on 2016/3/28.
 */
public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {

    private OnItemClickListener onItemClickListener;

    private RealmList<SubItem> items;
    public SubItemAdapter(RealmList<SubItem> items) {
        this.items = items;
    }

    @Override
    public SubItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_main, parent, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubItemViewHolder holder, int position) {
        holder.tvName.setText(items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class SubItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName;

        public SubItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            //tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);

        }

        @Override
        public void onClick(View v) {
            SubItem subItem = items.get(getAdapterPosition());
            onItemClickListener.onItemClick(subItem.getId(), subItem.getName());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(String id, String name);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}