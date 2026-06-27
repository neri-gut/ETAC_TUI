package com.etacind.tui.service.igsf.r002;

import com.etacind.tui.dto.igsf.c001.UsersTableRecord;
import com.etacind.tui.dto.igsf.c001.ProductsTableRecord;
import com.etacind.tui.dto.igsf.c001.StatusTableRecord;
import com.etacind.tui.dto.igsf.c001.ActionsTableRecord;
import com.etacind.tui.dto.igsf.c001.HistoryTableRecord;
import com.etacind.tui.dto.igsf.c001.request.SupabaseApiRequest;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;
import com.etacind.tui.repository.igsf.r001.IGSFR001;
import com.etacind.tui.service.igsf.r002.constants.UsersConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IGSFR002Impl implements IGSFR002 {

    private final IGSFR001 igsfR001;

    public IGSFR002Impl(IGSFR001 igsfR001){
        this.igsfR001 = igsfR001;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(IGSFR002Impl.class);

    @Override
    public SupabaseApiResponse<UsersTableRecord> execureCreateUser(UsersTableRecord request) {
        LOGGER.info("Inicia executeCreateUser");
        LOGGER.info("request: {}", request);
        SupabaseApiRequest<UsersTableRecord> supabaseRequest = new SupabaseApiRequest<>(UsersConstants.USERS_TABLE.value(), request, null);
        SupabaseApiResponse<UsersTableRecord> response = igsfR001.executeInsert(supabaseRequest, UsersTableRecord[].class);
        LOGGER.info("Termina executeCreateUser");
        return response;
    }

    @Override
    public SupabaseApiResponse<UsersTableRecord> executeGetUsers() {
        LOGGER.info("Inicia executeGetUsers");
        SupabaseApiRequest<Void> supabaseRequest = new SupabaseApiRequest<>(UsersConstants.USERS_TABLE.value(), null, null);
        SupabaseApiResponse<UsersTableRecord> response = igsfR001.executeSelect(supabaseRequest, UsersTableRecord[].class);
        LOGGER.info("Termina executeGetUsers");
        return response;
    }

    @Override
    public SupabaseApiResponse<ProductsTableRecord> executeCreateProduct(ProductsTableRecord request) {
        LOGGER.info("Inicia executeCreateProduct");
        LOGGER.info("request: {}", request);
        SupabaseApiRequest<ProductsTableRecord> supabaseRequest = new SupabaseApiRequest<>("products", request, null);
        SupabaseApiResponse<ProductsTableRecord> response = igsfR001.executeInsert(supabaseRequest, ProductsTableRecord[].class);
        LOGGER.info("Termina executeCreateProduct");
        return response;
    }

    @Override
    public SupabaseApiResponse<ProductsTableRecord> executeGetProducts() {
        LOGGER.info("Inicia executeGetProducts");
        SupabaseApiRequest<Void> supabaseRequest = new SupabaseApiRequest<>("products", null, null);
        SupabaseApiResponse<ProductsTableRecord> response = igsfR001.executeSelect(supabaseRequest, ProductsTableRecord[].class);
        LOGGER.info("Termina executeGetProducts");
        return response;
    }

    @Override
    public SupabaseApiResponse<StatusTableRecord> executeGetStatuses() {
        LOGGER.info("Inicia executeGetStatuses");
        SupabaseApiRequest<Void> supabaseRequest = new SupabaseApiRequest<>("status", null, null);
        SupabaseApiResponse<StatusTableRecord> response = igsfR001.executeSelect(supabaseRequest, StatusTableRecord[].class);
        LOGGER.info("Termina executeGetStatuses");
        return response;
    }

    @Override
    public SupabaseApiResponse<ActionsTableRecord> executeGetActions() {
        LOGGER.info("Inicia executeGetActions");
        SupabaseApiRequest<Void> supabaseRequest = new SupabaseApiRequest<>("actions", null, null);
        SupabaseApiResponse<ActionsTableRecord> response = igsfR001.executeSelect(supabaseRequest, ActionsTableRecord[].class);
        LOGGER.info("Termina executeGetActions");
        return response;
    }

    @Override
    public SupabaseApiResponse<HistoryTableRecord> executeGetHistory() {
        LOGGER.info("Inicia executeGetHistory");
        SupabaseApiRequest<Void> supabaseRequest = new SupabaseApiRequest<>("history", null, null);
        SupabaseApiResponse<HistoryTableRecord> response = igsfR001.executeSelect(supabaseRequest, HistoryTableRecord[].class);
        LOGGER.info("Termina executeGetHistory");
        return response;
    }
}

