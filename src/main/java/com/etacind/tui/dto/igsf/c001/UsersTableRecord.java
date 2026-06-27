package com.etacind.tui.dto.igsf.c001;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UsersTableRecord(
    String userId,
    String userIdentifier,
    String userName,
    String statusId,
    @JsonProperty("created_at")
    String createdAt
) {

}
