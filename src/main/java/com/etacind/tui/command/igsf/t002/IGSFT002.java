package com.etacind.tui.command.igsf.t002;

import com.etacind.tui.dto.igsf.c001.ProductsTableRecord;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;
import com.etacind.tui.service.igsf.r003.IGSFR003;
import org.springframework.stereotype.Component;

@Component
public class IGSFT002 {

    private final IGSFR003 igsfR003;

    public IGSFT002(IGSFR003 igsfR003){
        this.igsfR003 = igsfR003;
    }

    public SupabaseApiResponse<ProductsTableRecord> createProduct(ProductsTableRecord request) {
        return igsfR003.executeCreateProduct(request);
    }

    public SupabaseApiResponse<ProductsTableRecord> getProducts() {
        return igsfR003.executeSelectAllProducts(null);
    }

    public SupabaseApiResponse<ProductsTableRecord> getProductById(String productId) {
        ProductsTableRecord request = new ProductsTableRecord(productId, null, null, null, null, null);
        return igsfR003.executeSearchProductById(request);
    }

    public SupabaseApiResponse<ProductsTableRecord> getProductByName(String productName) {
        ProductsTableRecord request = new ProductsTableRecord(null, productName, null, null, null, null);
        return igsfR003.executeSearchProductByName(request);
    }

    public SupabaseApiResponse<ProductsTableRecord> updateProduct(ProductsTableRecord request) {
        return igsfR003.executeUpdateProduct(request);
    }

    public SupabaseApiResponse<ProductsTableRecord> deleteProduct(String productId) {
        ProductsTableRecord request = new ProductsTableRecord(productId, null, null, null, null, null);
        return igsfR003.executeDeleteProduct(request);
    }
}
