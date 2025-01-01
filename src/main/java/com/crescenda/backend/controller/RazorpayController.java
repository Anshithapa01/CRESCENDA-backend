package com.crescenda.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.request.PaymentRequest;
import com.crescenda.backend.request.PaymentVerificationRequest;
import com.crescenda.backend.service.EnrollmentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@RestController
@RequestMapping("/api")
public class RazorpayController {
	
	@Autowired
    private EnrollmentService enrollmentService;
	
	@PostMapping("/create-order")
	public ResponseEntity<Map<String, Object>> createOrder(@RequestBody PaymentRequest request) {
	    try {
	        RazorpayClient razorpay = new RazorpayClient("rzp_test_y8yagsN9W3YyKJ", "cX3rnwciccdWXuT1JUvQ8ywd");
	        
	        JSONObject orderRequest = new JSONObject();
	        orderRequest.put("amount", request.getAmount() * 100); // Amount in paisa (₹1 = 100 paisa)
	        orderRequest.put("currency", "INR");
	        orderRequest.put("receipt", "order_rcptid_" + request.getUserId());
	        
	        Order order = razorpay.orders.create(orderRequest);
	        Map<String, Object> response = new HashMap();
	        response.put("id", order.get("id"));
	        response.put("amount", order.get("amount"));
	        response.put("currency", order.get("currency"));
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@PostMapping("/verify-payment")
	public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest request) {
	    try {
	        boolean isVerified = enrollmentService.verifyPayment(
	            request.getRazorpayOrderId(),
	            request.getRazorpayPaymentId(),
	            request.getRazorpaySignature(),
	            request.getCourseId(),
	            request.getStudentId(),
	            request.getAmount()
	        );

	        if (isVerified) {
	            return ResponseEntity.ok("Payment verified and enrollment created/updated.");
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment verification failed.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during payment verification.");
	    }
	}




}
