package com.etacind.tui.service.igsf.r002;

import com.etacind.tui.dto.igsf.c001.UsersTableRecord;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;

public interface IGSFR002 {

    SupabaseApiResponse<UsersTableRecord> execureCreateUser(UsersTableRecord request);
    
    SupabaseApiResponse<UsersTableRecord> executeGetUsers();

    SupabaseApiResponse<UsersTableRecord> executeDeleteUserById(String userId);

    SupabaseApiResponse<UsersTableRecord> executeDeleteUserByIdentifier(String userIdentifier);

    SupabaseApiResponse<UsersTableRecord> executeUpdateUser(String userId, UsersTableRecord request);

    SupabaseApiResponse<UsersTableRecord> executeGetUserById(String userId);
}

