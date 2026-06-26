package com.etacind.tui.dto.igsf.c001;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductsTableRecord(
    String productId,
    String productName,
    String productDescription,
    String productQuantity,
    String statusId,
    @JsonProperty("created_at")
    String createdAt
) {

}
