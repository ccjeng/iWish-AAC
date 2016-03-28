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
import com.ccjeng.iwish.view.base.BaseDialog;

/**
 * Created by andycheng on 2016/3/28.
 */
public class EditDialog extends BaseDialog implements View.OnClickListener {

    private EditText etName;
    private Button btAddData;

    private OnEditClickListener listener;

    public static EditDialog newInstance(String name) {
        EditDialog dialog = new EditDialog();
        Bundle args = new Bundle();
        args.putString("name", name);
        dialog.setArguments(args);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_data, container);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        etName = (EditText) view.findViewById(R.id.et_name);
        etName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        btAddData = (Button) view.findViewById(R.id.bt_edit_data);
        btAddData.setOnClickListener(this);

        //Set Original Name
        String originalName = getArguments().getString("name");
        etName.setText(originalName);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit_data: {
                if (isDataValid()) {

                    listener.OnEditClickListener(etName.getText().toString());
                }
                break;
            }
        }
    }

    private boolean isDataValid() {
        return !etName.getText().toString().isEmpty();
    }

    public void setListener(OnEditClickListener listener) {
        this.listener = listener;
    }

    public interface OnEditClickListener {
        void OnEditClickListener(String name);
    }
}
