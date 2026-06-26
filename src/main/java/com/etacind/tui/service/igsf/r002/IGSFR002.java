package com.etacind.tui.service.igsf.r002;

import com.etacind.tui.dto.igsf.c001.UsersTableRecord;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;

public interface IGSFR002 {

    SupabaseApiResponse<UsersTableRecord> execureCreateUser(UsersTableRecord request);

    SupabaseApiResponse<UsersTableRecord> executeSelectUserById(UsersTableRecord request);

}
