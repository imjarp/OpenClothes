package com.mx.kanjo.openclothes.ui.fragments.dialog;

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
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by JARP on 2/5/15.
 */
public class DialogOutcomeTypeFragment extends DialogFragment {


    public static final String TAG = DialogOutcomeTypeFragment.class.getSimpleName();

    public static final String EXTRA_OUTCOME_TYPE = "com.mx.kanjo.openclothes.ui.fragments.dialog.DialogOutcomeTypeFragment";

    @InjectView(R.id.et_outcome_type)
    public EditText mEditText;

    private static final String KEY_IS_UPDATE = "Update";

    private boolean isForUpdate = false;

    public static DialogOutcomeTypeFragment createInstace()
    {
        return new DialogOutcomeTypeFragment();
    }

    public static DialogOutcomeTypeFragment createInstaceForUpdate()
    {
        DialogOutcomeTypeFragment f = new DialogOutcomeTypeFragment();

        Bundle args  = new Bundle();

        args.putBoolean(KEY_IS_UPDATE,true);

        f.setArguments(args);

        return f;
    }

    @OnClick({R.id.btn_add_outcome_type,R.id.btn_discard_outcome_type})
    public void onClickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_add_outcome_type :
                sendResult(Activity.RESULT_OK);
                break;

            case R.id.btn_discard_outcome_type :
                sendResult(Activity.RESULT_CANCELED);
                break;
        }
    }

    private void sendResult(int resultCode) {

        if ( null == getTargetFragment() )
            return;

        Intent i  = new Intent();

        i.putExtra( EXTRA_OUTCOME_TYPE, mEditText.getText().toString() );

        getTargetFragment().onActivityResult( getTargetRequestCode(), resultCode, i );

        dismiss();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.dialog_new_outcome_type, container, false );
         ButterKnife.inject(this, view );
        if(isForUpdate){
            ((TextView) view.findViewById(R.id.title_outcome_type)).setText(getString(R.string.title_update_outcome_type));
            ((Button) view.findViewById(R.id.btn_add_outcome_type)).setText(getString(R.string.update_action));
        }
        return view;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog( savedInstanceState );
        dialog.getWindow().requestFeature( Window.FEATURE_NO_TITLE );
        return dialog;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset( this );
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if( null  != getArguments()){
            isForUpdate = getArguments().getBoolean(KEY_IS_UPDATE);
        }
    }


}
