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

        switch (id)
        {
            case R.id.add_size_item :
                break;

            case R.id.add_item_income_type :
                break;

            case R.id.add_item_outcome_type :
                break;

        }

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

        @InjectView(R.id.fragment_detail_container)
        FrameLayout fragemntContainer;

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
                    configurationInventoryManager.addSizeItem(new SizeModel(0,size));
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
            ButterKnife.inject(this,rootView);
            return rootView;
        }

        @Override public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.reset(this);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.menu_config_fragment,menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()){

                case R.id.add_size_item :
                        showFragment(DialogSizeFragment.createInstace(), DialogSizeFragment.TAG);
                    return true;

                case R.id.add_item_income_type :
                    showFragment(DialogIncomeTypeFragment.createInstace(), DialogIncomeTypeFragment.TAG);
                    return true;

                case R.id.add_item_outcome_type :
                    showFragment(DialogOutcomeTypeFragment.createInstace(), DialogOutcomeTypeFragment.TAG);
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }

        }

        private void showFragment(android.support.v4.app.DialogFragment dialogFragment, String TAG)
        {
            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            dialogFragment.setTargetFragment(PlaceholderFragment.this,0);
            dialogFragment.show(fm,TAG);

        }

        @OnClick({R.id.btn_size, R.id.btn_income_type,R.id.btn_outcome_type})
        public void onClickButtonSize(View view)
        {

            ArrayList<String> items = new ArrayList<>();
            switch (view.getId()){

                case R.id.btn_size :
                    items.add("1");
                    items.add("2");
                    items.add("3");
                    displayItemsInFragment(items);
                    break;
                case R.id.btn_income_type :
                    items.add("4");
                    items.add("5");
                    items.add("6");
                    displayItemsInFragment(items);
                    break;
                case R.id.btn_outcome_type :
                    items.add("7");
                    items.add("8");
                    items.add("9");
                    displayItemsInFragment(items);
                    break;
            }
        }

        private void displayItemsInFragment(ArrayList<String> items)
        {

            ListConfigurationFragment listConfigurationFragment = ListConfigurationFragment.newInstance("","",items);

            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();

            FragmentTransaction transaction = fm.beginTransaction();

            transaction.replace(R.id.fragment_detail_container,listConfigurationFragment);

            transaction.commit();

        }
    }
}
