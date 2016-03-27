package com.ccjeng.iwish.view.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Item;

/**
 * Created by andycheng on 2016/3/26.
 */
public class AddItemDialog extends DialogFragment implements View.OnClickListener {

    private EditText etName;
    private Button btAddData;

    private OnAddClickListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AlertDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_data, container);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        etName = (EditText) view.findViewById(R.id.et_name);
        etName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        btAddData = (Button) view.findViewById(R.id.bt_add_data);
        btAddData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_data: {
                if (isDataValid()) {
                    Item item = new Item();
                    item.setName(etName.getText().toString());
                    listener.OnAddClickListener(item);
                }
                break;
            }
        }
    }

    private boolean isDataValid() {
        return !etName.getText().toString().isEmpty();
    }

    public void setListener(OnAddClickListener listener) {
        this.listener = listener;
    }

    public interface OnAddClickListener {
        void OnAddClickListener(Item item);
    }
}
