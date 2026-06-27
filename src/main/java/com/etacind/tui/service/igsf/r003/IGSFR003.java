package com.etacind.tui.service.igsf.r003;

import com.etacind.tui.dto.igsf.c001.ProductsTableRecord;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;

public interface IGSFR003 {

     SupabaseApiResponse<ProductsTableRecord> executeCreateProduct(ProductsTableRecord request);
     
     SupabaseApiResponse<ProductsTableRecord> executeSelectAllProducts(ProductsTableRecord request);

     SupabaseApiResponse<ProductsTableRecord> executeSearchProductById(ProductsTableRecord request);

     SupabaseApiResponse<ProductsTableRecord> executeSearchProductByName(ProductsTableRecord request);

     SupabaseApiResponse<ProductsTableRecord> executeUpdateProduct(ProductsTableRecord request);

     SupabaseApiResponse<ProductsTableRecord> executeDeleteProduct(ProductsTableRecord request);

}
