package com.etacind.tui.dto.igsf.c001;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatusTableRecord(
    String statusId,
    String statusValue,
    String statusDescription,
    @JsonProperty("created_at")
    String createdAt
) {

}
