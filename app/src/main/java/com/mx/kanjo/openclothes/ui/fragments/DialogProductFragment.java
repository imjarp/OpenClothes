package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mx.kanjo.openclothes.R;

import butterknife.ButterKnife;

/**
 * Created by JARP on 2/10/15.
 */
public class DialogProductFragment extends DialogFragment {

    public static final String TAG = "com.mx.kanjo.openclothes.ui.fragments.DialogProductFragment";

    public static DialogProductFragment createInstace()
    {
        return new DialogProductFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.dialog_new_product, container, false );
        ButterKnife.inject(this, view);
        return view;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog( savedInstanceState );
        dialog.getWindow().requestFeature( Window.FEATURE_NO_TITLE );
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
