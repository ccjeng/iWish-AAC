package com.ccjeng.iwish.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Category;
import com.ccjeng.iwish.view.adapter.helper.ItemTouchHelperAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by andycheng on 2016/3/26.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>
        implements ItemTouchHelperAdapter {

    private static final String TAG = CategoryAdapter.class.getSimpleName();

    private OnItemClickListener onItemClickListener;

    private List<Category> categories;
    private int fontSize;

    public CategoryAdapter(List<Category> categories, int fontSize) {
        this.categories = categories;
        this.fontSize = fontSize;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_main, parent, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.tvName.setText(categories.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Log.d(TAG, "onItemMove");
        Collections.swap(categories, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        //categories.remove(position);
        notifyItemRemoved(position);
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView tvName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        }

        @Override
        public void onClick(View v) {
            Category category = categories.get(getAdapterPosition());
            onItemClickListener.onItemClick(category.getId(), category.getName());
        }


    }

    public interface OnItemClickListener{
        void onItemClick(String id, String name);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
