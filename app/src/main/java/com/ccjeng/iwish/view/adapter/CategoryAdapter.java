package com.ccjeng.iwish.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Category;

import java.util.Collections;

import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/3/26.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>
        implements ItemTouchHelperAdapter {

    private OnItemClickListener onItemClickListener;

    private RealmResults<Category> categories;
    private int fontSize;

    public CategoryAdapter(RealmResults<Category> categories, int fontSize) {
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


        //holder.itemView.setLongClickable(true);
        //holder.itemView.setTag(new ContextMenuRecyclerView.RecyclerItemMarker(position, categories.get(position)));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    @Override
    public void onViewRecycled(CategoryViewHolder holder) {
        super.onViewRecycled(holder);
        holder.itemView.setOnCreateContextMenuListener(null);
    }


    @Override
    public void onItemDismiss(int position) {
        categories.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(categories, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(categories, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener{

        TextView tvName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

            //itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {
            Category category = categories.get(getAdapterPosition());
            onItemClickListener.onItemClick(category.getId(), category.getName());
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            //menuInfo is null
            menu.add(Menu.NONE, R.id.edit,
                    Menu.NONE, R.string.edit);
            menu.add(Menu.NONE, R.id.delete,
                    Menu.NONE, R.string.delete);

        }


    }

    public interface OnItemClickListener{
        void onItemClick(String id, String name);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
