package com.etacind.tui.command.igsf.t003;

import com.etacind.tui.dto.igsf.c001.HistoryTableRecord;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;
import com.etacind.tui.service.igsf.r004.IGSFR004;
import org.springframework.stereotype.Component;

@Component
public class IGSFT003 {

    private final IGSFR004 igsfR004;

    public IGSFT003(IGSFR004 igsfR004) {
        this.igsfR004 = igsfR004;
    }

    public SupabaseApiResponse<HistoryTableRecord> getAllHistory() {
        return igsfR004.executeSearchAllHistory(null);
    }

    public SupabaseApiResponse<HistoryTableRecord> getHistoryByUserId(String userId) {
        HistoryTableRecord request = new HistoryTableRecord(null, userId, null, null, null);
        return igsfR004.executeSearchHistoryByUserId(request);
    }

    public SupabaseApiResponse<HistoryTableRecord> getHistoryByActionId(String actionId) {
        HistoryTableRecord request = new HistoryTableRecord(null, null, actionId, null, null);
        return igsfR004.executeSearchHistoryByActionId(request);
    }

    public SupabaseApiResponse<HistoryTableRecord> getHistoryByProductId(String productId) {
        HistoryTableRecord request = new HistoryTableRecord(null, null, null, productId, null);
        return igsfR004.executeSearchHistoryByProductId(request);
    }

    public SupabaseApiResponse<HistoryTableRecord> getHistoryByDate(String executionDate) {
        HistoryTableRecord request = new HistoryTableRecord(null, null, null, null, executionDate);
        return igsfR004.executeSearchHistoryByDate(request);
    }

    public void insertHistory(String userId, String actionId, String productId) {
        HistoryTableRecord record = new HistoryTableRecord(null, userId, actionId, productId, null);
        igsfR004.executeInsertHistory(record);
    }
}
