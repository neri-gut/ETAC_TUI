package com.etacind.tui.dto.igsf.c001;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsersTableRecord(
    String userId,
    String userIdentifier,
    String userName,
    String statusId,
    @JsonProperty("created_at")
    String createdAt
) {

}
