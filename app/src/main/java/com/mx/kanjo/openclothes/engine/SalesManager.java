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

    public void convertPromiseToSale(PromiseSale promiseSale)
    {

    }

    public void modifyPromise(int idPromiseSale, final Set<StockItem> itemsToDelete, final Set<StockItem> itemsToUpdate)
    {

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


    private static NotificationOrderRequest createPromise(PromiseSale promise, ContentResolver resolver, ConfigurationOrder configurationOrder)
    {
        NotificationOrderRequest result = verifyOrderInStock(promise.getStockItems(), resolver);

        if( !configurationOrder.TransactIncompleteOrder && !result.isCompleteOrder() )
            return result;

        //TODO: put this on a transaction

        promise = createPromiseHeader(promise, resolver);

        for(Map.Entry<Integer,StockItem> item : promise.getStockItems().entrySet())
        {
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

}
