package com.mx.kanjo.openclothes.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.mx.kanjo.openclothes.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by JARP on 2/5/15.
 */
public class DialogSizeFragment extends DialogFragment {


    public static final String TAG = DialogSizeFragment.class.getSimpleName();

    public static final String EXTRA_SIZE = "com.mx.kanjo.openclothes.ui.DialogSizeFragment";

    @InjectView(R.id.et_size)
    public EditText mSize ;

    public static DialogSizeFragment createInstace()
    {
        return new DialogSizeFragment();
    }

    @OnClick({R.id.btn_add_size,R.id.btn_discard_size})
    public void onClickEvent(Button button)
    {
        switch (button.getId())
        {
            case R.id.btn_add_size :
                sendResult(Activity.RESULT_OK);
                break;

            case R.id.btn_discard_size :
                sendResult(Activity.RESULT_CANCELED);
                break;
        }
    }

    private void sendResult(int resultCode) {

        if (null == getTargetFragment())
            return;

        Intent i  = new Intent();

        i.putExtra(EXTRA_SIZE,mSize.getText().toString());

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_size,container,false);
        ButterKnife.inject(this,view);
        return view;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }




}
