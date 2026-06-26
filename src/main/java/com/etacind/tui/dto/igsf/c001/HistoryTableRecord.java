package com.etacind.tui.dto.igsf.c001;

public record HistoryTableRecord(
    String historyId,
    String userId,        
    String actionId,
    String productId,
    String executionDate
) {

}
