package com.etacind.tui.service.igsf.r004;

import com.etacind.tui.dto.igsf.c001.HistoryTableRecord;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;

public interface IGSFR004 {

    SupabaseApiResponse<HistoryTableRecord> executeSearchAllHistory(HistoryTableRecord request);

    SupabaseApiResponse<HistoryTableRecord> executeSearchHistoryByUserId(HistoryTableRecord request);

    SupabaseApiResponse<HistoryTableRecord> executeSearchHistoryByActionId(HistoryTableRecord request);

    SupabaseApiResponse<HistoryTableRecord> executeSearchHistoryByProductId(HistoryTableRecord request);

    SupabaseApiResponse<HistoryTableRecord> executeSearchHistoryByDate(HistoryTableRecord request);

    SupabaseApiResponse<HistoryTableRecord> executeInsertHistory(HistoryTableRecord request);

}
