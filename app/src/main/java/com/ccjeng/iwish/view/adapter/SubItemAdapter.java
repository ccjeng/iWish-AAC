package com.ccjeng.iwish.view.adapter;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Item;
import com.ccjeng.iwish.model.SubItem;
import com.ccjeng.iwish.view.adapter.helper.ItemTouchHelperAdapter;
import com.ccjeng.iwish.view.adapter.helper.OnStartDragListener;
import com.ccjeng.iwish.view.base.BaseApplication;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by andycheng on 2016/3/28.
 */
public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder>
        implements ItemTouchHelperAdapter {

    private OnItemClickListener onItemClickListener;

    private RealmList<SubItem> items;
    private int fontSize;

    private OnStartDragListener onStartDragListener;
    private Realm realm;

    public SubItemAdapter(RealmList<SubItem> items, int fontSize, OnStartDragListener onStartDragListener) {
        this.items = items;
        this.fontSize = fontSize;
        this.onStartDragListener = onStartDragListener;

        realm = Realm.getInstance(BaseApplication.realmConfiguration);

    }

    @Override
    public SubItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_main, parent, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubItemViewHolder holder, int position) {
        holder.tvName.setText(items.get(position).getName());

        holder.tvName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //Collections.swap(dailies, fromPosition, toPosition);
        //Log.d(TAG, fromPosition + " " + toPosition);
        realm.beginTransaction();
        items.move(fromPosition, toPosition);
        realm.commitTransaction();
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }


    public class SubItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName;

        public SubItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

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
