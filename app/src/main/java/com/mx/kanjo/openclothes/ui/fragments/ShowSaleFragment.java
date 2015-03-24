package com.mx.kanjo.openclothes.ui.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.SalesManager;
import com.mx.kanjo.openclothes.model.SaleModel;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowSaleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowSaleFragment extends Fragment {

    private static final String ARG_ID_SALE = "paramIdSale";

    private int idSale;

    private AsyncTask<Void, Void,SaleModel> mFetchSaleTask;

    private Context mContext;

    private SalesManager mSalesManager ;

    @InjectView(R.id.tv_customer_name)  TextView mTvCustomer;
    @InjectView(R.id.tv_sale_date)  TextView mTvDateSale;
    @InjectView(R.id.text_total_sale) TextView mTvTotalSale;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param paramIdSale sale id.
     * @return A new instance of fragment ShowSaleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowSaleFragment newInstance(int paramIdSale) {
        ShowSaleFragment fragment = new ShowSaleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_SALE, paramIdSale);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowSaleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mSalesManager = new SalesManager(mContext);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idSale= getArguments().getInt(ARG_ID_SALE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fetchSale(idSale);
        View view = inflater.inflate(R.layout.fragment_show_sale, container, false);
        ButterKnife.inject(this,view);
        return view;
    }



    private void fetchSale(final int idSale){

        mFetchSaleTask = new AsyncTask<Void, Void, SaleModel>() {

            @Override
            protected SaleModel doInBackground(Void... params) {
                if( idSale <= 0 )
                return null;
                return  mSalesManager.getSale(idSale);
            }

            @Override
            protected void onPostExecute(SaleModel saleModel) {
                fillView(saleModel);
            }
        }.execute();
    }


    private void fillView(SaleModel saleModel){
        mTvCustomer.setText(saleModel.getCustomer());
        mTvDateSale.setText(saleModel.getDate());
        mTvTotalSale.setText("$" + saleModel.getTotal());
    }


}
