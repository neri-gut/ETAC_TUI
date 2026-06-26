package com.etacind.tui.dto.igsf.c001;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ActionsTableRecord(
    String actionId,
    String actionName,
    String actionDescription,
    @JsonProperty("created_at") 
    String createdAt
) {

}
