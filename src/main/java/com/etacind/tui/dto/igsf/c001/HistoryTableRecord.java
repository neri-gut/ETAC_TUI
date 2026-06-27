package com.etacind.tui.dto.igsf.c001;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HistoryTableRecord(
    String historyId,
    String userId,        
    String actionId,
    String productId,
    String executionDate
) {

}
