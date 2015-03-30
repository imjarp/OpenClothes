package com.mx.kanjo.openclothes.ui.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.SalesManager;
import com.mx.kanjo.openclothes.model.SaleModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.ui.adapters.SaleItemAdapter;
import com.mx.kanjo.openclothes.util.ConfigImageHelper;
import com.mx.kanjo.openclothes.util.UiUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowSaleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowSaleFragment extends Fragment {


    private final static String TAG = "ShowSaleFragment";
    private static final String ARG_ID_SALE = "paramIdSale";

    private int idSale;

    private AsyncTask<Void, Void,SaleModel> mFetchSaleTask;

    private Context mContext;

    private SalesManager mSalesManager ;

    SaleItemAdapter mSaleItemAdapter ;

    @InjectView(R.id.tv_customer_name)  TextView mTvCustomer;
    @InjectView(R.id.tv_sale_date)  TextView mTvDateSale;
    @InjectView(R.id.text_total_sale) TextView mTvTotalSale;


    private RecyclerView mRecyclerView;

    protected LayoutManagerType mCurrentLayoutManagerType;

    private LinearLayoutManager mLinearLayoutManager;

    protected RecyclerView.LayoutManager mLayoutManager;

    private int SPAN_COUNT = 1;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param paramIdSale sale id.
     * @return A new instance of fragment ShowSaleFragment.
     */
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

        view.setTag(TAG);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        if(UiUtils.isTablet(getActivity())) {
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER ;
        }else{
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER ;
        }

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        ButterKnife.inject(this,view);

        return view;
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
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
        mTvTotalSale.setText("Total $" + saleModel.getTotal());

        ArrayList<StockItem> saleItems  = new ArrayList<>(saleModel.getSaleItems().values());

        mSaleItemAdapter = new SaleItemAdapter(mContext,saleItems,buildConfiguration());

        mRecyclerView.setAdapter(mSaleItemAdapter);




    }

    private ConfigImageHelper buildConfiguration() {
        Pair<Integer,Integer> sizeImage;

        if( UiUtils.isTablet( getActivity() ) ) {

            sizeImage = new Pair<>(72, 72);

            return new ConfigImageHelper.ConfigImageHelpBuilder(sizeImage)
                    .withRoundImage(true)
                    .build();
        }
        else
        {
            sizeImage = new Pair<>(48, 48);

            return new ConfigImageHelper.ConfigImageHelpBuilder(sizeImage)
                    .withRoundImage(true)
                    .build();

        }
    }


}
