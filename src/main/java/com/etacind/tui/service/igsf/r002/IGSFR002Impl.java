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

        SupabaseApiResponse<UsersTableRecord> response = null;
        LOGGER.info("Inicia executeCreateUser");
        LOGGER.info("request: {}", request);

        if (request != null
            && !request.userIdentifier().isEmpty()
            && !request.userName().isEmpty()
            && !request.statusId().isEmpty()){
            LOGGER.info("Paso If");
                SupabaseApiRequest<UsersTableRecord> supabaseRequest = new SupabaseApiRequest<>(UsersConstants.USERS_TABLE.value(), request, null);
                response = igsfR001.executeInsert(supabaseRequest, UsersTableRecord[].class);
                LOGGER.info("response data: {}", response.data());
        }

        return response;
    }

    @Override
    public SupabaseApiResponse<UsersTableRecord> executeSelectUserById(UsersTableRecord request){
        SupabaseApiResponse<UsersTableRecord> response = null;



    }

}
