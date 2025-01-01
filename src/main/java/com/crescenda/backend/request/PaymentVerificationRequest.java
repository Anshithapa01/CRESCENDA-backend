package com.crescenda.backend.request;

import lombok.Data;

@Data
public class PaymentVerificationRequest {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private int courseId;
    private int studentId;
    private double amount;
}