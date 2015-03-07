package com.mx.kanjo.openclothes.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.ConfigurationInventoryManager;
import com.mx.kanjo.openclothes.model.IncomeType;
import com.mx.kanjo.openclothes.model.OutcomeType;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.ui.fragments.ListConfigurationFragment;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ConfigurationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        configureToolbar();

    }

    private void configureToolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(getString(R.string.title_tool_bar_config_activity));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuration_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final int REQUEST_SIZE = 0 ;

        private static final int REQUEST_INCOME_TYPE = 1 ;

        private static final int REQUEST_OUTCOME_TYPE = 2 ;

        private int currentConfigurationItem = -1;

        ArrayList<String> items = new ArrayList<>();

        @InjectView(R.id.fragment_detail_container)
        FrameLayout fragmentContainer;

        ConfigurationInventoryManager configurationInventoryManager ;

        public PlaceholderFragment() {
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

            if(Activity.RESULT_OK != resultCode)
                return;

            if( REQUEST_SIZE == requestCode  )
            {
                String size = data.getStringExtra(DialogSizeFragment.EXTRA_SIZE);
                if( !TextUtils.isEmpty( size ) )
                {
                    configurationInventoryManager.addSizeItem(new SizeModel(0, size));

                }

            }

            else if( REQUEST_INCOME_TYPE == requestCode )
            {

                String description = data.getStringExtra( DialogIncomeTypeFragment.EXTRA_INCOME_TYPE );
                if( !TextUtils.isEmpty( description ) )
                {
                    configurationInventoryManager.addIncomeType( new IncomeType( 0 , description ) );
                }

            }

            else if( REQUEST_OUTCOME_TYPE == requestCode )
            {

                String description = data.getStringExtra( DialogOutcomeTypeFragment.EXTRA_OUTCOME_TYPE );
                if( !TextUtils.isEmpty( description ) )
                {
                    configurationInventoryManager.addOutcomeType( new OutcomeType( 0, description ) );
                }
            }
            updateCurrentFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            setHasOptionsMenu(true);

            configurationInventoryManager = new ConfigurationInventoryManager(getActivity());


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, 
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_configuration, container, false);

            ButterKnife.inject(this, rootView);

            return rootView;
        }

        @Override public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.reset(this);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.menu_config_fragment, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()){

                case R.id.add_size_item :
                    showFragment(DialogSizeFragment.createInstace(), DialogSizeFragment.TAG, REQUEST_SIZE);
                    return true;

                case R.id.add_item_income_type :
                    showFragment(DialogIncomeTypeFragment.createInstace(), DialogIncomeTypeFragment.TAG, REQUEST_INCOME_TYPE);
                    return true;

                case R.id.add_item_outcome_type :
                    showFragment(DialogOutcomeTypeFragment.createInstace(), DialogOutcomeTypeFragment.TAG, REQUEST_OUTCOME_TYPE);
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }

        }

        private void showFragment(android.support.v4.app.DialogFragment dialogFragment, String TAG, int requestCode)
        {
            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            dialogFragment.setTargetFragment(PlaceholderFragment.this, requestCode);
            dialogFragment.show(fm, TAG);

        }

        private void showSizeItems(ArrayList<String> items )
        {
            Iterator<SizeModel> iteratorSize =  configurationInventoryManager.getSizeCatalogue().iterator();

            if( null != iteratorSize ) {
                while (iteratorSize.hasNext()) {
                    items.add(iteratorSize.next().getSizeDescription());
                }
            }
            displayItemsInFragment(getString(R.string.title_list_configuration_size_items), items,"SIZE");
        }

        private void showIncomeTypeItems(ArrayList<String> items) {
            Iterator<IncomeType> iteratorIncome =  configurationInventoryManager.getIncomeTypes().iterator();
            if( null != iteratorIncome ) {
                while (iteratorIncome.hasNext()) {
                    items.add(iteratorIncome.next().getDescription());
                }
            }
            displayItemsInFragment(getString(R.string.title_list_configuration_income_type_items), items, "INCOME");
        }

        private void showOutcomeTypeItems(ArrayList<String> items) {
            Iterator<OutcomeType> iteratorOutcome  =  configurationInventoryManager.getOutcomeTypes().iterator();
            if( null != iteratorOutcome ) {
                while (iteratorOutcome.hasNext()) {
                    items.add(iteratorOutcome.next().getDescription());
                }
            }
            displayItemsInFragment(getString(R.string.title_list_configuration_outcome_type_items), items,"OUTGOING");
        }


        private void updateCurrentFragment()
        {
            items.clear();

            if(currentConfigurationItem==0)
                showSizeItems(items);
            else if(currentConfigurationItem==1)
                showIncomeTypeItems(items);
            else if(currentConfigurationItem==2)
                showOutcomeTypeItems(items);
        }

        @OnClick({R.id.btn_size, R.id.btn_income_type, R.id.btn_outcome_type})
        public void onClickButton(View view)
        {
            items.clear();

            switch (view.getId()){
                case R.id.btn_size :
                        showSizeItems(items);
                        currentConfigurationItem=0;
                    break;
                case R.id.btn_income_type :
                        showIncomeTypeItems(items);
                        currentConfigurationItem=1;
                    break;
                case R.id.btn_outcome_type :
                        showOutcomeTypeItems(items);
                        currentConfigurationItem=2;
                    break;
            }
        }

        private void displayItemsInFragment(String title , ArrayList<String> items, String TAG)
        {

            ListConfigurationFragment listConfigurationFragment = ListConfigurationFragment.newInstance(title, "", items);

            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();

            if( null  != fm.findFragmentByTag(TAG))
            {

                fm.beginTransaction().remove(fm.findFragmentByTag(TAG)).commit();
            }

            FragmentTransaction transaction = fm.beginTransaction();

            transaction.replace(R.id.fragment_detail_container, listConfigurationFragment,TAG);

            transaction.commit();

        }
    }
}
