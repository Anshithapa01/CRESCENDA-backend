package com.crescenda.backend.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDetailsResponse {
	private String purchaseId;
	private String thumbnailUrl;
    private String courseName;
    private String customerName;
    private LocalDateTime enrollmentDate;
    private double amount;
}
