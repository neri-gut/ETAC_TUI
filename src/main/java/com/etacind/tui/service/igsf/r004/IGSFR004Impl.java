package com.etacind.tui.service.igsf.r004;

import com.etacind.tui.dto.igsf.c001.HistoryTableRecord;
import com.etacind.tui.dto.igsf.c001.request.SupabaseApiRequest;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;
import com.etacind.tui.repository.igsf.r001.IGSFR001;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IGSFR004Impl implements IGSFR004 {

    private final IGSFR001 igsfR001;
    private static final Logger LOGGER = LoggerFactory.getLogger(IGSFR004Impl.class);

    public IGSFR004Impl(IGSFR001 igsfR001) {
        this.igsfR001 = igsfR001;
    }

    @Override
    public SupabaseApiResponse<HistoryTableRecord> executeSearchAllHistory(HistoryTableRecord request) {
        LOGGER.info("Inicia executeSearchAllHistory");
        SupabaseApiRequest<Void> apiRequest = new SupabaseApiRequest<>("history", null, null);
        SupabaseApiResponse<HistoryTableRecord> response = igsfR001.executeSelect(apiRequest, HistoryTableRecord[].class);
        LOGGER.info("Termina executeSearchAllHistory");
        return response;
    }

    @Override
    public SupabaseApiResponse<HistoryTableRecord> executeSearchHistoryByUserId(HistoryTableRecord request) {
        LOGGER.info("Inicia executeSearchHistoryByUserId");
        Map<String, String> queryParams = Map.of("userId", "eq." + request.userId());
        SupabaseApiRequest<Void> apiRequest = new SupabaseApiRequest<>("history", null, queryParams);
        SupabaseApiResponse<HistoryTableRecord> response = igsfR001.executeSelect(apiRequest, HistoryTableRecord[].class);
        LOGGER.info("Termina executeSearchHistoryByUserId");
        return response;
    }

    @Override
    public SupabaseApiResponse<HistoryTableRecord> executeSearchHistoryByActionId(HistoryTableRecord request) {
        LOGGER.info("Inicia executeSearchHistoryByActionId");
        Map<String, String> queryParams = Map.of("actionId", "eq." + request.actionId());
        SupabaseApiRequest<Void> apiRequest = new SupabaseApiRequest<>("history", null, queryParams);
        SupabaseApiResponse<HistoryTableRecord> response = igsfR001.executeSelect(apiRequest, HistoryTableRecord[].class);
        LOGGER.info("Termina executeSearchHistoryByActionId");
        return response;
    }

    @Override
    public SupabaseApiResponse<HistoryTableRecord> executeSearchHistoryByProductId(HistoryTableRecord request) {
        LOGGER.info("Inicia executeSearchHistoryByProductId");
        Map<String, String> queryParams = Map.of("productId", "eq." + request.productId());
        SupabaseApiRequest<Void> apiRequest = new SupabaseApiRequest<>("history", null, queryParams);
        SupabaseApiResponse<HistoryTableRecord> response = igsfR001.executeSelect(apiRequest, HistoryTableRecord[].class);
        LOGGER.info("Termina executeSearchHistoryByProductId");
        return response;
    }

    @Override
    public SupabaseApiResponse<HistoryTableRecord> executeSearchHistoryByDate(HistoryTableRecord request) {
        LOGGER.info("Inicia executeSearchHistoryByDate");
        Map<String, String> queryParams = Map.of("executionDate", "eq." + request.executionDate());
        SupabaseApiRequest<Void> apiRequest = new SupabaseApiRequest<>("history", null, queryParams);
        SupabaseApiResponse<HistoryTableRecord> response = igsfR001.executeSelect(apiRequest, HistoryTableRecord[].class);
        LOGGER.info("Termina executeSearchHistoryByDate");
        return response;
    }

    @Override
    public SupabaseApiResponse<HistoryTableRecord> executeInsertHistory(HistoryTableRecord request) {
        LOGGER.info("Inicia executeInsertHistory");
        SupabaseApiRequest<HistoryTableRecord> apiRequest = new SupabaseApiRequest<>("history", request, null);
        SupabaseApiResponse<HistoryTableRecord> response = igsfR001.executeInsert(apiRequest, HistoryTableRecord[].class);
        LOGGER.info("Termina executeInsertHistory");
        return response;
    }
}
