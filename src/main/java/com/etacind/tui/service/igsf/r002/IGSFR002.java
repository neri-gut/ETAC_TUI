package com.etacind.tui.service.igsf.r002;

import com.etacind.tui.dto.igsf.c001.UsersTableRecord;
import com.etacind.tui.dto.igsf.c001.ProductsTableRecord;
import com.etacind.tui.dto.igsf.c001.StatusTableRecord;
import com.etacind.tui.dto.igsf.c001.ActionsTableRecord;
import com.etacind.tui.dto.igsf.c001.HistoryTableRecord;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;

public interface IGSFR002 {

    SupabaseApiResponse<UsersTableRecord> execureCreateUser(UsersTableRecord request);
    
    SupabaseApiResponse<UsersTableRecord> executeGetUsers();
    
    SupabaseApiResponse<ProductsTableRecord> executeCreateProduct(ProductsTableRecord request);
    
    SupabaseApiResponse<ProductsTableRecord> executeGetProducts();
    
    SupabaseApiResponse<StatusTableRecord> executeGetStatuses();
    
    SupabaseApiResponse<ActionsTableRecord> executeGetActions();
    
    SupabaseApiResponse<HistoryTableRecord> executeGetHistory();
}

