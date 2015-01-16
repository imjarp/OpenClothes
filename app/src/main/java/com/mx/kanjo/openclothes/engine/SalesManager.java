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
import com.mx.kanjo.openclothes.model.PromiseSale;
import com.mx.kanjo.openclothes.model.SaleModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.util.Maps;

import java.util.Date;
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

    public NotificationOrderRequest  createNewSale(SaleModel sale, ConfigurationOrder configurationOrder)
    {
        return createNewSale(sale, resolver, configurationOrder);
    }

    public NotificationOrderRequest createPromise(PromiseSale promiseSale, ConfigurationOrder configurationOrder)
    {
        return createPromise(promiseSale, resolver, configurationOrder);
    }

    public  PromiseSale findPromiseByDate()
    {
        return null;
    }


    public NotificationOrderRequest convertPromiseToSale(PromiseSale promiseSale,ConfigurationOrder configurationOrder)
    {
        String today = OpenClothesContract.getDbDateString(new Date());

        //TODO : Add a status to sale, so we will not remove the promise

        //Create Sale
        SaleModel saleModel = new SaleModel(promiseSale.getStockItems(),today,0,0);

        NotificationOrderRequest result = createNewSale(saleModel, configurationOrder);

        if (!configurationOrder.TransactIncompleteOrder && !result.isCompleteOrder())
            return result;

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

    private NotificationOrderRequest createNewSale(SaleModel sale, ContentResolver resolver, ConfigurationOrder configurationOrder) {

        //Check the inventory
        NotificationOrderRequest result = verifyOrderInStock(sale.getSaleItems(), resolver);

        OutcomeModel outcomeModel = new OutcomeModel();

        OutcomeType saleOutcomeType  = new OutcomeType();

        String today = OpenClothesContract.getDbDateString(new Date());

        if (!configurationOrder.TransactIncompleteOrder && !result.isCompleteOrder())
            return result;

        // TODO: put this on a transaction
        sale = createSaleHeader(sale,resolver);

        for (Map.Entry<Integer,StockItem> item : sale.getSaleItems().entrySet())
        {
            inventoryManager.removeItemFromStock(item.getValue());

            createSaleItem(item.getValue(),sale.getId(),resolver);

            createOutcome(outcomeModel, saleOutcomeType, today, item);

            inventoryManager.addOutcome(outcomeModel);

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

    private static void createSaleItem(StockItem item , int idSale, ContentResolver resolver)
    {
        ContentValues values = SaleItemCreator.createSaleModel(item, idSale);

        resolver.insert(OpenClothesContract.SaleItem.CONTENT_URI, values);

    }

    //TODO :  check if this method is feasible or useful
    /*
    private void deletePromiseItem(Map.Entry<Integer, StockItem> stockItemEntry, int idPromise) {}
    */

}
