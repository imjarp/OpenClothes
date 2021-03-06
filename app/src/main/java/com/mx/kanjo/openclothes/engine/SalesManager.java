package com.mx.kanjo.openclothes.engine;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.mx.kanjo.openclothes.engine.creators.PromiseHeaderCreator;
import com.mx.kanjo.openclothes.engine.creators.PromiseItemCreator;
import com.mx.kanjo.openclothes.engine.creators.SaleHeaderCreator;
import com.mx.kanjo.openclothes.engine.creators.SaleItemCreator;
import com.mx.kanjo.openclothes.model.ConfigurationOrder;
import com.mx.kanjo.openclothes.model.NotificationOrderRequest;
import com.mx.kanjo.openclothes.model.OutcomeModel;
import com.mx.kanjo.openclothes.model.OutcomeType;
import com.mx.kanjo.openclothes.model.OutcomeTypeSale;
import com.mx.kanjo.openclothes.model.PromiseSale;
import com.mx.kanjo.openclothes.model.SaleModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.util.Lists;
import com.mx.kanjo.openclothes.util.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by JARP on 12/10/14.
 */
public class SalesManager {

    private Context mContext;
    private ContentResolver resolver;
    private InventoryManager inventoryManager;

    public SalesManager(Context context)
    {
        if(context == null) {
            throw new IllegalArgumentException("context");
        }

        mContext = context;
        resolver = context.getContentResolver();
        inventoryManager = new InventoryManager(context);
    }

    public NotificationOrderRequest createNewSale(SaleModel sale, ConfigurationOrder configurationOrder)
    {
        return createNewSale(sale, resolver, configurationOrder ,false );
    }

    public NotificationOrderRequest createPromise(PromiseSale promiseSale, ConfigurationOrder configurationOrder)
    {
        return createPromise(promiseSale, resolver, configurationOrder);
    }

    public Collection<PromiseSale> findPromiseByDate()
    {
        return null;
    }

    public  Collection<PromiseSale> findPromiseByCustomer(String customer)
    {
        return findPromiseSaleByCustomer(customer,resolver);
    }


    public NotificationOrderRequest convertPromiseToSale(PromiseSale promiseSale,ConfigurationOrder configurationOrder)
    {
        String today = OpenClothesContract.getDbDateString(new Date());

        //TODO : Add a status to sale, so we will not remove the promise

        //Create Sale
        SaleModel saleModel = new SaleModel(promiseSale.getStockItems(),today,0,0,promiseSale.getCustomer());

        NotificationOrderRequest result = this.createNewSale(saleModel, resolver, configurationOrder, true);

        //Remove Promise
        removePromise(promiseSale);


        return result;

    }

    public void modifyPromise(int idPromise, final Set<StockItem> itemsToDelete, final Set<StockItem> newItems)
    {

        //TODO : create transaction for this operation

        /*
            Remove promise Items involves to return to the stock , and delete from promise items
        */
        Map<Integer, StockItem> stockItems = Maps.newHashMap();

        for(StockItem item : itemsToDelete)
        {
            stockItems.put(item.getIdProduct(),item);

            //Add to the stock
            inventoryManager.addItemToStock(item);
        }

        deletePromiseItems(stockItems,idPromise);

        for(StockItem item : newItems)
        {
            //Remove from the stock
            inventoryManager.removeItemFromStock(item);

            //Create promise item
            createPromiseItem(item,idPromise,resolver);
        }



    }

    private void removePromise(PromiseSale promiseSale) {

        deletePromiseItems(promiseSale.getStockItems(), promiseSale.getId());

        deletePromiseHeader(promiseSale);

    }

    private void deletePromiseHeader(PromiseSale promiseSale) {

        Uri promiseUri = OpenClothesContract.Promise.buildPromiseUri(promiseSale.getId());

        final String columnIdPromise = OpenClothesContract.Promise._ID + " = ?";

        int rowsDeleted = resolver.delete(promiseUri, columnIdPromise ,new String[]{String.valueOf(promiseSale.getId())});

        //TODO : this should be an exception
        /*if(rowsDeleted<1)
            throw new Exception()*/

    }

    private void deletePromiseItems(Map<Integer, StockItem> stockItems, int idPromise) {

        Uri promiseItemsUri = OpenClothesContract.PromiseItem.buildPromiseItemWithHeader(idPromise,false);

        final String columnIdPromise = OpenClothesContract.PromiseItem.ID_PROMISE + " = ?";

        int rowsDeleted = resolver.delete(promiseItemsUri, columnIdPromise ,new String[]{String.valueOf(idPromise)});

        //This should be an exception
        //if(stockItems.size()!=rowsDeleted)
    }

    private NotificationOrderRequest createNewSale(SaleModel sale, ContentResolver resolver, ConfigurationOrder configurationOrder, boolean isPromiseSale) {

        //Check the inventory
        NotificationOrderRequest result  = null;

        //If is promiseSale the products have been already taken from stock
        if( ! isPromiseSale ) {
            result = verifyOrderInStock(sale.getSaleItems(), resolver);
        }


        OutcomeModel outcomeModel = null;

        final OutcomeType saleOutcomeType  = OutcomeTypeSale.OutcomeTypeSale();

        String today = OpenClothesContract.getDbDateString(new Date());

        if ( ! isPromiseSale && !configurationOrder.TransactIncompleteOrder && ! result.isCompleteOrder() )
            return result;

        // TODO: put this on a transaction
        sale = createSaleHeader(sale,resolver);
        if( null == result ) {
            result = new NotificationOrderRequest();
            result.AvailableProducts = Lists.newArrayList();
        }

        for (Map.Entry<Integer,StockItem> item : sale.getSaleItems().entrySet())
        {
            createSaleItem(item.getValue(),sale.getId(),resolver);

            outcomeModel = new OutcomeModel(0,
                                            item.getValue(),
                                            item.getValue().getSize(),
                                            item.getValue().getQuantity(),
                                            saleOutcomeType,
                                            today);

            createOutcome(outcomeModel, saleOutcomeType, today, item);

            inventoryManager.addOutcome(outcomeModel);

            inventoryManager.removeItemFromStock(item.getValue());

            result.AvailableProducts.add(item.getValue());

        }

        return result;

    }

    private void createOutcome(OutcomeModel outcomeModel, OutcomeType saleOutcomeType, String today, Map.Entry<Integer, StockItem> item) {

        outcomeModel.setDateOperation(today);

        outcomeModel.setSize(item.getValue().getSize());

        outcomeModel.setIdProduct(item.getValue().getIdProduct());

        outcomeModel.setQuantity(item.getValue().getQuantity());

        outcomeModel.setDescription(item.getValue().getDescription());

        outcomeModel.setOutcomeType(saleOutcomeType);

        outcomeModel.setCost(item.getValue().getCost());

        outcomeModel.setModel(item.getValue().getModel());

        outcomeModel.setName(item.getValue().getName());

        outcomeModel.setDescription(item.getValue().getDescription());
    }

    private  NotificationOrderRequest createPromise(PromiseSale promise, ContentResolver resolver, ConfigurationOrder configurationOrder)
    {
        NotificationOrderRequest result = verifyOrderInStock(promise.getStockItems(), resolver);

        if( !configurationOrder.TransactIncompleteOrder && !result.isCompleteOrder() )
            return result;

        //TODO: put this on a transaction

        promise = createPromiseHeader(promise, resolver);

        for(Map.Entry<Integer,StockItem> item : promise.getStockItems().entrySet())
        {
            //First Remove from inventory
            inventoryManager.removeItemFromStock(item.getValue());

            //create promise item
            createPromiseItem(item.getValue(), promise.getId(), resolver);

        }

        return result;
    }

    private static NotificationOrderRequest verifyOrderInStock(Map<Integer, StockItem> items, ContentResolver resolver)
    {
        NotificationOrderRequest result = new NotificationOrderRequest();
        Uri uriItem;
        Cursor cursor = null;

        for(Map.Entry<Integer, StockItem> item : items.entrySet())
        {
            uriItem = OpenClothesContract.Stock.buildStockUriWithProductIdAndSizeId(item.getValue().getIdProduct(),
                    item.getValue().getSize().getIdSize());
            cursor = resolver.query(uriItem,null,null,null,null);

            if(cursor.getCount()==0) {
                result.UnavailableProducts.add(item.getValue());
                continue;
            }

            result.AvailableProducts.add(item.getValue());
        }

        cursor.close();

        return result;

    }

    private static PromiseSale createPromiseHeader(PromiseSale promiseSale, ContentResolver resolver)
    {
        ContentValues values = PromiseHeaderCreator.createPromiseHeader(promiseSale);

        Uri uri = resolver.insert(OpenClothesContract.Promise.CONTENT_URI,values);

        int id = (int) ContentUris.parseId(uri);

        promiseSale.setId(id);

        return promiseSale;
    }

    private static void createPromiseItem(StockItem item, int idPromise, ContentResolver resolver) {

        ContentValues values = PromiseItemCreator.createPromiseItem(item,idPromise);

        resolver.insert(OpenClothesContract.PromiseItem.CONTENT_URI,values);

    }

    private static SaleModel createSaleHeader ( SaleModel saleModel, ContentResolver resolver)
    {
        ContentValues values = SaleHeaderCreator.createSaleModel(saleModel);

        Uri uri = resolver.insert(OpenClothesContract.Sale.CONTENT_URI, values);

        int id = (int) ContentUris.parseId(uri);

        saleModel.setId(id);

        return  saleModel;
    }

    private static long createSaleItem(StockItem item , int idSale, ContentResolver resolver)
    {
        ContentValues values = SaleItemCreator.createSaleModel(item, idSale);

        Uri result = resolver.insert(OpenClothesContract.SaleItem.CONTENT_URI, values);

        long id = ContentUris.parseId(result);

        return id;

    }

    //TODO :  check if this method is feasible or useful
    /*
    private void deletePromiseItem(Map.Entry<Integer, StockItem> stockItemEntry, int idPromise) {}
    */

    private static Collection<PromiseSale> findPromiseSaleByDate(String date,ContentResolver resolver)
    {
        return  null;

    }

    private static List<PromiseSale> findPromiseSaleByCustomer(String customer,ContentResolver resolver) {

        ArrayList<PromiseSale> listPromiseSale = Lists.newArrayList();

        Uri uri = OpenClothesContract.Promise.buildPromiseUriWithCustomer(customer);

        Cursor cursor = null;

        PromiseSale promiseSaleTemp = null;

        Map<Integer,StockItem> stockPromiseItems = null ;

        try {

            cursor = resolver.query(uri, null, null, null, null);

            if (!cursor.moveToFirst()) {
                return listPromiseSale;
            }

            do {

                promiseSaleTemp = PromiseHeaderCreator.createPromiseHeaderFromCursor(cursor);

                listPromiseSale.add(promiseSaleTemp);

            } while (cursor.moveToNext());



        } finally {

            if (null != cursor && !cursor.isClosed())
                cursor.close();


        }

        for (PromiseSale sale : listPromiseSale) {

            stockPromiseItems = findPromiseItemsByIdPromise(sale.getId(), resolver);

            if( stockPromiseItems.size() > 0  ) {
                sale.setStockItems(stockPromiseItems);
            }

        }

            return listPromiseSale;

    }

    public static Map<Integer,StockItem> findPromiseItemsByIdPromise(int idHeaderPromise, ContentResolver resolver)
    {
        Map<Integer,StockItem> promiseItems = Maps.newHashMap();

        StockItem stockItem = null;

        int idx = 0;

        Cursor cursor = null;

        try {

            Uri uri = OpenClothesContract.PromiseItem.buildPromiseItemWithHeader(idHeaderPromise,false);

            cursor = resolver.query(uri,null,null,null,null);

            if( !cursor.moveToFirst() )
                return promiseItems;

            do {

                stockItem = PromiseItemCreator.createPromiseItemFromIdPromise(cursor,resolver);

                promiseItems.put(idx++, stockItem);

            }while (cursor.moveToNext());

        }
        finally {
             if( null != cursor && ! cursor.isClosed())
             {
                cursor.close();;
             }
        }

        return  promiseItems;

    }


    public SaleModel getSale( int idSale ){

        Uri saleUri = OpenClothesContract.Sale.buildSaleUri(idSale);

        Cursor cursor = resolver.query(saleUri,null,null,null,null);

        cursor.moveToFirst();

        SaleModel saleModel =  SaleHeaderCreator.createSaleHeaderFromCursor(cursor);

        cursor.close();

        ArrayList<StockItem> result = getSaleItem(saleModel.getId());

        Map<Integer, StockItem> saleItems = new HashMap<>();

        int index = 0;
        for(StockItem i : result){ saleItems.put( index++,i ); }

        saleModel.setSaleItems(saleItems);

        return saleModel;


    }

    public ArrayList<StockItem> getSaleItem(int idSale){

        Uri saleItemUri = OpenClothesContract.SaleItem.buildSaleItemHeader(idSale,true);

        ArrayList<StockItem> stockItems = Lists.newArrayList();

        Cursor cursor = resolver.query(saleItemUri,null,null,null,null);

        if(!cursor.moveToFirst())
            return stockItems;

        if(cursor.getCount() == 0)
            return  stockItems;


        do{
            stockItems.add(SaleItemCreator.createSaleItemFromCursor(cursor,resolver));
        }while (cursor.moveToNext());

        cursor.close();

        return stockItems;

    }



}
