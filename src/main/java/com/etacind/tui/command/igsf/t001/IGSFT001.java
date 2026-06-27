package com.etacind.tui.command.igsf.t001;

import com.etacind.tui.dto.igsf.c001.UsersTableRecord;
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

    public SupabaseApiResponse<UsersTableRecord> deleteUserById(String userId) {
        return igsfR002.executeDeleteUserById(userId);
    }

    public SupabaseApiResponse<UsersTableRecord> deleteUserByIdentifier(String userIdentifier) {
        return igsfR002.executeDeleteUserByIdentifier(userIdentifier);
    }

    public SupabaseApiResponse<UsersTableRecord> updateUser(String userId, UsersTableRecord request) {
        return igsfR002.executeUpdateUser(userId, request);
    }

    public SupabaseApiResponse<UsersTableRecord> getUserById(String userId) {
        return igsfR002.executeGetUserById(userId);
    }
}
