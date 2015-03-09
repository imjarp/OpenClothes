package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.ConfigurationInventoryManager;
import com.mx.kanjo.openclothes.model.IncomeType;
import com.mx.kanjo.openclothes.model.OutcomeType;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.ui.DialogIncomeTypeFragment;
import com.mx.kanjo.openclothes.ui.DialogOutcomeTypeFragment;
import com.mx.kanjo.openclothes.ui.DialogSizeFragment;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ListConfigurationFragment extends ListFragment implements AdapterView.OnItemLongClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TITLE = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM_ITEMS = "configuration_items";
    private static final String ARG_PARAM_CONFIG = "config_type";

    private static final int REQUEST_SIZE = 0 ;

    private static final int REQUEST_INCOME_TYPE = 1 ;

    private static final int REQUEST_OUTCOME_TYPE = 2 ;


    // TODO: Rename and change types of parameters
    private String mParamTitle;
    private String mParam2;
    private ArrayList<String> configurationItems;

    int currentConfigType = -1;

    private static final int SIZE_CONFIG = 0;
    private static final int INCOME_CONFIG = 1;
    private static final int OUTCOME_CONFIG = 2;

    private ConfigurationInventoryManager mConfigurationInventoryManager;


    View headerView;

    private OnFragmentInteractionListener mListener;

    private String valueItemOnLongClick = "";
    private int positionItemOnLongClick = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         return super.onCreateView(inflater, container, savedInstanceState);

    }

    // TODO: Rename and change types of parameters
    public static ListConfigurationFragment newInstance(String param1, int configType, ArrayList<String> items) {
        ListConfigurationFragment fragment = new ListConfigurationFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, param1);
        args.putStringArrayList(ARG_PARAM_ITEMS, items);
        args.putInt(ARG_PARAM_CONFIG,  configType);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListConfigurationFragment() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( Activity.RESULT_OK != resultCode )
            return;

        processResult(requestCode,data);


    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParamTitle = getArguments().getString(ARG_TITLE);
            mParam2 = getArguments().getString(ARG_PARAM2);
            currentConfigType = getArguments().getInt(ARG_PARAM_CONFIG);
            configurationItems = getArguments().getStringArrayList(ARG_PARAM_ITEMS);
        }




    }

    @Override
    public void onResume() {
        super.onResume();
        if(null == configurationItems || configurationItems.isEmpty()) {
            setEmptyText(getString(R.string.empty_configuration_list));
        }

        if(TextUtils.isEmpty(mParamTitle))
            return;

        headerView = getActivity().getLayoutInflater().inflate( R.layout.title_configuration_item, getListView(), false);

        TextView titleView = (TextView) headerView.findViewById(R.id.title_configuration_list_view );

        titleView.setText(mParamTitle);


        if(null == getListView().findViewById(R.id.id_header_view_size_config))
            getListView().addHeaderView(headerView);

        // TODO: Change Adapter to display your content
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, configurationItems));

        getListView().setOnItemLongClickListener(this);

        mConfigurationInventoryManager = new ConfigurationInventoryManager( getActivity() );

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        if(R.id.id_header_view_size_config == view.getId())
            return false;

        valueItemOnLongClick = (String) getListAdapter().getItem( position-1 );
        positionItemOnLongClick = position;

        if(currentConfigType == SIZE_CONFIG){
            showDialogFragment(DialogSizeFragment.createInstaceForUpdate(), DialogSizeFragment.TAG, REQUEST_SIZE);
            return true;
        }
        if(currentConfigType == INCOME_CONFIG){
            showDialogFragment(DialogIncomeTypeFragment.createInstaceForUpdate(), DialogIncomeTypeFragment.TAG, REQUEST_INCOME_TYPE);
            return true;

        }
        if(currentConfigType == OUTCOME_CONFIG){
            showDialogFragment(DialogOutcomeTypeFragment.createInstaceForUpdate(), DialogOutcomeTypeFragment.TAG, REQUEST_OUTCOME_TYPE);
            return true;
        }

        return false;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


    private void showDialogFragment(android.support.v4.app.DialogFragment dialogFragment, String TAG, int requestCode)
    {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        dialogFragment.setTargetFragment( this, requestCode);
        dialogFragment.show(fm, TAG);

    }

    private void processResult(int requestCode, Intent data) {

        String newValue = null;

        if(REQUEST_SIZE ==requestCode){

            SizeModel s =  mConfigurationInventoryManager.findSizeByDescription(valueItemOnLongClick);

            if( null != s )
            {
                newValue = data.getStringExtra(DialogSizeFragment.EXTRA_SIZE);

                mConfigurationInventoryManager.modifySize(s.getIdSize(),newValue);
            }

        }
        else if(REQUEST_INCOME_TYPE ==requestCode){

            IncomeType i = mConfigurationInventoryManager.findIncomeTypeByDescription(valueItemOnLongClick);
            if( null != i  ){

                newValue = data.getStringExtra(DialogIncomeTypeFragment.EXTRA_INCOME_TYPE);
                mConfigurationInventoryManager.modifyIncomeType(i.getIdIncome(),newValue);
            }


        }
        else if(REQUEST_OUTCOME_TYPE ==requestCode){

            OutcomeType o = mConfigurationInventoryManager.findOutcomeTypeByDescription(valueItemOnLongClick);
            if( null != o  ){

                newValue = data.getStringExtra(DialogIncomeTypeFragment.EXTRA_INCOME_TYPE);
                mConfigurationInventoryManager.modifyOutcomeType(o.getIdOutcome(), newValue);
            }

        }

        if( null != newValue)
        {
            configurationItems.set(positionItemOnLongClick-1,newValue);

            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, configurationItems));



        }



    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
