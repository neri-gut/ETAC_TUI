package com.etacind.tui.service.igsf.r002;

import com.etacind.tui.dto.igsf.c001.UsersTableRecord;
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
    public SupabaseApiResponse<UsersTableRecord> executeDeleteUserById(String userId) {
        LOGGER.info("Inicia executeDeleteUserById");
        LOGGER.info("userId to delete: {}", userId);
        java.util.Map<String, String> queryParams = java.util.Map.of("userId", "eq." + userId);
        SupabaseApiRequest<Void> supabaseRequest = new SupabaseApiRequest<>(UsersConstants.USERS_TABLE.value(), null, queryParams);
        SupabaseApiResponse<UsersTableRecord> response = igsfR001.executeDelete(supabaseRequest, UsersTableRecord[].class);
        LOGGER.info("Termina executeDeleteUserById");
        return response;
    }

    @Override
    public SupabaseApiResponse<UsersTableRecord> executeDeleteUserByIdentifier(String userIdentifier) {
        LOGGER.info("Inicia executeDeleteUserByIdentifier");
        LOGGER.info("userIdentifier to delete: {}", userIdentifier);
        java.util.Map<String, String> queryParams = java.util.Map.of("userIdentifier", "eq." + userIdentifier);
        SupabaseApiRequest<Void> supabaseRequest = new SupabaseApiRequest<>(UsersConstants.USERS_TABLE.value(), null, queryParams);
        SupabaseApiResponse<UsersTableRecord> response = igsfR001.executeDelete(supabaseRequest, UsersTableRecord[].class);
        LOGGER.info("Termina executeDeleteUserByIdentifier");
        return response;
    }

    @Override
    public SupabaseApiResponse<UsersTableRecord> executeUpdateUser(String userId, UsersTableRecord request) {
        LOGGER.info("Inicia executeUpdateUser");
        LOGGER.info("userId to update: {}", userId);
        java.util.Map<String, String> queryParams = java.util.Map.of("userId", "eq." + userId);
        SupabaseApiRequest<UsersTableRecord> supabaseRequest = new SupabaseApiRequest<>(UsersConstants.USERS_TABLE.value(), request, queryParams);
        SupabaseApiResponse<UsersTableRecord> response = igsfR001.executeUpdate(supabaseRequest, UsersTableRecord[].class);
        LOGGER.info("Termina executeUpdateUser");
        return response;
    }

    @Override
    public SupabaseApiResponse<UsersTableRecord> executeGetUserById(String userId) {
        LOGGER.info("Inicia executeGetUserById");
        LOGGER.info("userId to get: {}", userId);
        java.util.Map<String, String> queryParams = java.util.Map.of("userId", "eq." + userId);
        SupabaseApiRequest<Void> supabaseRequest = new SupabaseApiRequest<>(UsersConstants.USERS_TABLE.value(), null, queryParams);
        SupabaseApiResponse<UsersTableRecord> response = igsfR001.executeSelect(supabaseRequest, UsersTableRecord[].class);
        LOGGER.info("Termina executeGetUserById");
        return response;
    }
}

