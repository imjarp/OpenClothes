package com.mx.kanjo.openclothes;

import android.content.ContentUris;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.mx.kanjo.openclothes.engine.CatalogueManager;
import com.mx.kanjo.openclothes.engine.InventoryManager;
import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

import java.util.Date;
import java.util.Set;

/**
 * Created by JARP on 1/7/15.
 */
public class TestInventory extends AndroidTestCase {

    public static final String TAG = TestInventory.class.getSimpleName();

    ProductModel model1;
    ProductModel model2;
    private Set<ProductModel> productCataloge;

    @Override
    public void setUp() throws Exception {

        super.setUp();

        TestProvider testProvider = new TestProvider();

        testProvider.deleteAllRecords();
    }

    public void TestAddTwoProducts()
    {
        CatalogueManager catalogueManager = new CatalogueManager(mContext);

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

        productCataloge = catalogueManager.getCatalogue();

        assertTrue( productCataloge.size() == 2 );


    }

    public void TestAddSizeItems()
    {

    }

    public void TestAddProductsToStock() {

        InventoryManager inventoryManager = new InventoryManager(mContext);







    }

    

    public static  ProductModel createProductInstance(int idProduct, String name, String description, String model, String date, Uri imagePath, boolean isActive, int price, int cost)
    {
        return new ProductModel(idProduct,name,description,model,date,imagePath,isActive,price,cost);
    }




}
