package com.mx.kanjo.openclothes;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.mx.kanjo.openclothes.engine.CatalogueManager;
import com.mx.kanjo.openclothes.engine.ConfigurationInventoryManager;
import com.mx.kanjo.openclothes.engine.InventoryManager;
import com.mx.kanjo.openclothes.model.IncomeType;
import com.mx.kanjo.openclothes.model.OutcomeType;
import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by JARP on 1/7/15.
 */
public class TestInventory extends AndroidTestCase {

    //public static final String TAG = TestInventory.class.getSimpleName();

    Context context;

    ProductModel model1;
    ProductModel model2;

    SizeModel smallSize ;
    SizeModel mediumSize;

    StockItem dress1Small;
    StockItem dress1Medium;

    StockItem dress2Small;


    private Set<ProductModel> productCatalogue;

    InventoryManager inventoryManager ;

    ConfigurationInventoryManager configInventoryManager;

    CatalogueManager catalogueManager ;

    IncomeType incomeTypeNewEntry;

    IncomeType incomeTypeInventoryError;

    private OutcomeType outcomeTypeNewEntry;

    private OutcomeType outcomeTypeInventoryError;


    public void setUp() {

        context = mContext;
        inventoryManager = new InventoryManager(context);
        configInventoryManager = new ConfigurationInventoryManager(context);
        catalogueManager = new CatalogueManager(mContext);
    }


    public void testAddIncomeTypes() {

        TestProvider.deleteAllRecords(context.getContentResolver());

        incomeTypeNewEntry = new IncomeType(0, "New Entry");

        incomeTypeInventoryError = new IncomeType(0, "Income Inventory Error");

        incomeTypeNewEntry =  configInventoryManager.addIncomeType(incomeTypeNewEntry);

        assertTrue( incomeTypeNewEntry.getIdIncome() > 0 );

        incomeTypeInventoryError = configInventoryManager.addIncomeType(incomeTypeInventoryError);

        assertTrue( incomeTypeInventoryError.getIdIncome() >0 );

        Set<IncomeType> incomeTypes =  configInventoryManager.getIncomeTypes();

        assertTrue( incomeTypes.size() == 2 );

    }

    public void testAddOutcomeTypes() {


        outcomeTypeNewEntry = new OutcomeType(0, "New Entry");

        outcomeTypeInventoryError = new OutcomeType(0, "Outcome Inventory Error");

        outcomeTypeNewEntry =  configInventoryManager.addOutcomeType(outcomeTypeNewEntry);

        assertTrue( outcomeTypeNewEntry.getIdOutcome() > 0 );

        outcomeTypeInventoryError = configInventoryManager.addOutcomeType(outcomeTypeInventoryError);

        assertTrue( outcomeTypeInventoryError.getIdOutcome() >0 );

        Set<OutcomeType> outcomeTypes =  configInventoryManager.getOutcomeTypes();

        assertTrue( outcomeTypes.size() == 2 );

    }

    public void testAddProducts()
    {
        TestProvider.deleteAllRecords(context.getContentResolver());

        model1 =  createProductInstance(0,
                "CB-011",
                "Vesitidi",
                "CB-011",
                OpenClothesContract.getDbDateString(new Date()),
                null,
                true,
                100,
                30);

        model2 = createProductInstance(0,
                "CB-012",
                "Vestido 12",
                "CB-012",
                OpenClothesContract.getDbDateString(new Date()),
                null,
                true,
                100,
                30);

        Uri uriResult = catalogueManager.addNewProduct(model1);

        long id = ContentUris.parseId(uriResult);

        model1.setIdProduct((int) id);

        uriResult = catalogueManager.addNewProduct(model2);

        id = ContentUris.parseId(uriResult);

        model2.setIdProduct((int) id);

        productCatalogue = catalogueManager.getCatalogue();

        assertTrue( productCatalogue.size() == 2 );


    }

    public void testAddSizeItems()
    {
        smallSize = createSizeModel(0,"small");
        mediumSize = createSizeModel(0,"medium");

        smallSize =  configInventoryManager.addSizeItem(smallSize);
        mediumSize =  configInventoryManager.addSizeItem(mediumSize);

        List sizeCatalogue = configInventoryManager.getSizeCatalogue();

        assertTrue( sizeCatalogue.size() == 2 );


    }

    public void testAddStock() {

        model1 = catalogueManager.findProductByModel("CB-011");

        model2 = catalogueManager.findProductByModel("CB-012");

        smallSize = configInventoryManager.findByDescription("small");

        mediumSize = configInventoryManager.findByDescription("medium");

        dress1Small = new StockItem(model1, smallSize, 1);

        dress1Medium = new StockItem(model1, mediumSize, 2);

        dress2Small = new StockItem(model2, smallSize, 1);

        inventoryManager.addItemToStock(dress1Small);

        inventoryManager.addItemToStock(dress1Medium);

        inventoryManager.addItemToStock(dress2Small);

        Set<StockItem> stockItems = inventoryManager.getStock();

        assertTrue( stockItems.size() == 3 );

        StockItem item =  inventoryManager.getStockItemByProductAndSize(dress1Small.getIdProduct(), dress1Small.getSize().getIdSize() );

        assertNotNull(item);

        assertEquals(dress1Small.getIdProduct(), item.getIdProduct());
        assertEquals(dress1Small.getQuantity(), item.getQuantity());
        assertEquals(dress1Small.getSize().getIdSize(), item.getSize().getIdSize());

        item =  inventoryManager.getStockItemByProductAndSize(dress1Medium.getIdProduct(), dress1Medium.getSize().getIdSize());

        assertNotNull(item);

        assertEquals(dress1Medium.getIdProduct(), item.getIdProduct());
        assertEquals(dress1Medium.getQuantity(), item.getQuantity());
        assertEquals(dress1Medium.getSize().getIdSize(), item.getSize().getIdSize());

    }

    public void testCreatePromiseOperations()
    {

        model1 = catalogueManager.findProductByModel("CB-011");

        model2 = catalogueManager.findProductByModel("CB-012");

        smallSize = configInventoryManager.findByDescription("small");

        mediumSize = configInventoryManager.findByDescription("medium");






    }

    public void testCreateSaleOperations()
    {

    }



    

    public static  ProductModel createProductInstance(int idProduct, String name, String description, String model, String date, Uri imagePath, boolean isActive, int price, int cost)
    {
        return new ProductModel(idProduct,name,description,model,date,imagePath,isActive,price,cost);
    }

    public static SizeModel createSizeModel(int idSize, String description)
    {
        return new SizeModel(idSize,description);
    }



}
