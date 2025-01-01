package com.crescenda.backend.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private double amount;
    private long userId;
}
