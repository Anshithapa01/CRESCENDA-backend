package com.crescenda.backend.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockRequest {
	@JsonProperty("isDeleted")
    private boolean isDeleted;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}