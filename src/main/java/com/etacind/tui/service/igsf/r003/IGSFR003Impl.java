package com.etacind.tui.service.igsf.r003;

import com.etacind.tui.dto.igsf.c001.ProductsTableRecord;
import com.etacind.tui.dto.igsf.c001.request.SupabaseApiRequest;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;
import com.etacind.tui.repository.igsf.r001.IGSFR001;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IGSFR003Impl implements IGSFR003 {

    private final IGSFR001 igsfR001;
    private static final Logger LOGGER = LoggerFactory.getLogger(IGSFR003Impl.class);

    public IGSFR003Impl(IGSFR001 igsfR001) {
        this.igsfR001 = igsfR001;
    }

    @Override
    public SupabaseApiResponse<ProductsTableRecord> executeCreateProduct(ProductsTableRecord request) {
        LOGGER.info("Inicia executeCreateProduct");
        SupabaseApiRequest<ProductsTableRecord> apiRequest = new SupabaseApiRequest<>("products", request, null);
        SupabaseApiResponse<ProductsTableRecord> response = igsfR001.executeInsert(apiRequest, ProductsTableRecord[].class);
        LOGGER.info("Termina executeCreateProduct");
        return response;
    }

    @Override
    public SupabaseApiResponse<ProductsTableRecord> executeSelectAllProducts(ProductsTableRecord request) {
        LOGGER.info("Inicia executeSelectAllProducts");
        SupabaseApiRequest<Void> apiRequest = new SupabaseApiRequest<>("products", null, null);
        SupabaseApiResponse<ProductsTableRecord> response = igsfR001.executeSelect(apiRequest, ProductsTableRecord[].class);
        LOGGER.info("Termina executeSelectAllProducts");
        return response;
    }

    @Override
    public SupabaseApiResponse<ProductsTableRecord> executeSearchProductById(ProductsTableRecord request) {
        LOGGER.info("Inicia executeSearchProductById");
        Map<String, String> queryParams = Map.of("productId", "eq." + request.productId());
        SupabaseApiRequest<Void> apiRequest = new SupabaseApiRequest<>("products", null, queryParams);
        SupabaseApiResponse<ProductsTableRecord> response = igsfR001.executeSelect(apiRequest, ProductsTableRecord[].class);
        LOGGER.info("Termina executeSearchProductById");
        return response;
    }

    @Override
    public SupabaseApiResponse<ProductsTableRecord> executeSearchProductByName(ProductsTableRecord request) {
        LOGGER.info("Inicia executeSearchProductByName");
        Map<String, String> queryParams = Map.of("productName", "eq." + request.productName());
        SupabaseApiRequest<Void> apiRequest = new SupabaseApiRequest<>("products", null, queryParams);
        SupabaseApiResponse<ProductsTableRecord> response = igsfR001.executeSelect(apiRequest, ProductsTableRecord[].class);
        LOGGER.info("Termina executeSearchProductByName");
        return response;
    }

    @Override
    public SupabaseApiResponse<ProductsTableRecord> executeUpdateProduct(ProductsTableRecord request) {
        LOGGER.info("Inicia executeUpdateProduct");
        Map<String, String> queryParams = Map.of("productId", "eq." + request.productId());
        
        ProductsTableRecord patchBody = new ProductsTableRecord(
                null, 
                request.productName(), 
                request.productDescription(), 
                request.productQuantity(), 
                request.statusId(), 
                null
        );
        SupabaseApiRequest<ProductsTableRecord> apiRequest = new SupabaseApiRequest<>("products", patchBody, queryParams);
        SupabaseApiResponse<ProductsTableRecord> response = igsfR001.executeUpdate(apiRequest, ProductsTableRecord[].class);
        LOGGER.info("Termina executeUpdateProduct");
        return response;
    }

    @Override
    public SupabaseApiResponse<ProductsTableRecord> executeDeleteProduct(ProductsTableRecord request) {
        LOGGER.info("Inicia executeDeleteProduct");
        Map<String, String> queryParams = Map.of("productId", "eq." + request.productId());
        SupabaseApiRequest<Void> apiRequest = new SupabaseApiRequest<>("products", null, queryParams);
        SupabaseApiResponse<ProductsTableRecord> response = igsfR001.executeDelete(apiRequest, ProductsTableRecord[].class);
        LOGGER.info("Termina executeDeleteProduct");
        return response;
    }
}
