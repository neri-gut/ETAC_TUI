package com.etacind.tui.command.igsf.t001;

import com.etacind.tui.dto.igsf.c001.UsersTableRecord;
import com.etacind.tui.dto.igsf.c001.ProductsTableRecord;
import com.etacind.tui.dto.igsf.c001.StatusTableRecord;
import com.etacind.tui.dto.igsf.c001.ActionsTableRecord;
import com.etacind.tui.dto.igsf.c001.HistoryTableRecord;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;
import com.etacind.tui.service.igsf.r002.IGSFR002;
import org.springframework.stereotype.Component;

@Component
public class IGSFT001 {

    private final IGSFR002 igsfR002;

    public IGSFT001(IGSFR002 igsfR002){
        this.igsfR002 = igsfR002;
    }

    public SupabaseApiResponse<UsersTableRecord> createUser(UsersTableRecord request) {
        return igsfR002.execureCreateUser(request);
    }

    public SupabaseApiResponse<UsersTableRecord> getUsers() {
        return igsfR002.executeGetUsers();
    }

    public SupabaseApiResponse<ProductsTableRecord> createProduct(ProductsTableRecord request) {
        return igsfR002.executeCreateProduct(request);
    }

    public SupabaseApiResponse<ProductsTableRecord> getProducts() {
        return igsfR002.executeGetProducts();
    }

    public SupabaseApiResponse<StatusTableRecord> getStatuses() {
        return igsfR002.executeGetStatuses();
    }

    public SupabaseApiResponse<ActionsTableRecord> getActions() {
        return igsfR002.executeGetActions();
    }

    public SupabaseApiResponse<HistoryTableRecord> getHistory() {
        return igsfR002.executeGetHistory();
    }
}
