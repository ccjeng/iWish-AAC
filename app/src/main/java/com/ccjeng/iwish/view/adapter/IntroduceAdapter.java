package com.ccjeng.iwish.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Introduce;
import com.ccjeng.iwish.view.adapter.helper.ItemTouchHelperAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by andycheng on 2016/4/22.
 */
public class IntroduceAdapter extends RecyclerView.Adapter<IntroduceAdapter.CustomViewHolder>
        implements ItemTouchHelperAdapter {

    private static final String TAG = IntroduceAdapter.class.getSimpleName();

    private OnItemClickListener onItemClickListener;

    private List<Introduce> mIntroduces;
    private int fontSize;

    public IntroduceAdapter(List<Introduce> introduces, int fontSize) {
        this.mIntroduces = introduces;
        this.fontSize = fontSize;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_main, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.tvName.setText(mIntroduces.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mIntroduces.size();
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mIntroduces, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mIntroduces, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        //categories.remove(position);
        notifyItemRemoved(position);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView tvName;

        public CustomViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        }

        @Override
        public void onClick(View v) {
            Introduce introduce = mIntroduces.get(getAdapterPosition());
            onItemClickListener.onItemClick(getAdapterPosition(), introduce.getId(), introduce.getName());
        }




    }

    public interface OnItemClickListener{
        void onItemClick(int position, String id, String name);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
