package com.crescenda.backend.service;

import java.util.List;

import com.crescenda.backend.response.PurchasedCourseResponse;

public interface EnrollmentService {

	List<PurchasedCourseResponse> getPurchasedCoursesByStudentId(int studentId);

	boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature, int courseId,
			int studentId, double amount);

	boolean isStudentEnrolled(int courseId, int studentId);

}
