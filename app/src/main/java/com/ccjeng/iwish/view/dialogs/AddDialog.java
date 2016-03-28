package com.ccjeng.iwish.view.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.view.base.BaseDialog;

/**
 * Created by andycheng on 2016/3/28.
 */
public class AddDialog extends BaseDialog implements View.OnClickListener {

    private EditText etName;
    private Button btAddData;

    private OnAddClickListener listener;

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

                    listener.OnAddClickListener(etName.getText().toString());
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
        void OnAddClickListener(String name);
    }
}
