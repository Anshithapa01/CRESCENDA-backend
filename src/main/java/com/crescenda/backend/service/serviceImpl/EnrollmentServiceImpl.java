package com.crescenda.backend.service.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Course;
import com.crescenda.backend.model.Draft;
import com.crescenda.backend.model.Mentor;
import com.crescenda.backend.model.MentorPayment;
import com.crescenda.backend.model.Payment;
import com.crescenda.backend.model.Student;
import com.crescenda.backend.model.SubCategory;
import com.crescenda.backend.repository.CourseRepository;
import com.crescenda.backend.repository.MentorPaymentRepository;
import com.crescenda.backend.repository.PaymentRepository;
import com.crescenda.backend.repository.StudentRepository;
import com.crescenda.backend.response.CourseResponse;
import com.crescenda.backend.response.DraftResponse;
import com.crescenda.backend.response.PurchasedCourseResponse;
import com.crescenda.backend.response.StudentResponse;
import com.crescenda.backend.response.SubCategoryResponse;
import com.crescenda.backend.service.EnrollmentService;
import com.crescenda.backend.service.StudentService;

@Service
public class EnrollmentServiceImpl implements EnrollmentService{

	@Autowired
    private PaymentRepository paymentRepository;
	@Autowired
	private StudentService studentService;
	@Autowired
	StudentRepository studentRepository;
	@Autowired
	CourseRepository courseRepository;
	@Autowired
	MentorPaymentRepository mentorPaymentRepository;
	

    @Override
    public List<PurchasedCourseResponse> getPurchasedCoursesByStudentId(int studentId) {
        List<Payment> enrollments = paymentRepository.findByStudentStudentId(studentId);
        
        return enrollments.stream()
                .map(enrollment -> {
                    CourseResponse courseResponse = mapToCourseResponse(enrollment.getCourse());
                    StudentResponse studentResponse=studentService.mapToStudentResponse(enrollment.getStudent());
                    
                    return new PurchasedCourseResponse(
                            enrollment.getId(),
                            enrollment.getPaymentDate(),
                            enrollment.getAmount(),
                            enrollment.getPaymentStatus(),
                            enrollment.getPaymentId(),
                            courseResponse,
                            studentResponse
                    );
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean isStudentEnrolled(int courseId, int studentId) {
        return paymentRepository.existsByCourseCourseIdAndStudentStudentId(courseId, studentId);
    }
    
    @Override
    public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature, int courseId, int studentId, double amount) {
        // Step 1: Verify Razorpay signature
        boolean isPaymentVerified = verifyRazorpaySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);

        if (isPaymentVerified) {
            // Step 2: Check if an enrollment already exists
            Payment existingEnrollment = paymentRepository.findByPaymentId(razorpayOrderId);

            if (existingEnrollment == null) {
                // Step 3: Create a new enrollment record if it doesn't exist
                Payment newEnrollment = new Payment();
                newEnrollment.setPaymentId(razorpayOrderId);
                newEnrollment.setPaymentStatus("Completed");
                newEnrollment.setAmount(amount);

                // Fetch student and course entities
                Student student = studentRepository.findByStudentId(studentId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid student ID: " + studentId));
                Course course = courseRepository.findByCourseId(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + courseId));

                newEnrollment.setStudent(student);
                newEnrollment.setCourse(course);

                // Save the new enrollment
                paymentRepository.save(newEnrollment);
                updateMentorPayment(course.getDraft().getMentor(), amount);
            } else {
                // Update existing enrollment if found
                existingEnrollment.setPaymentStatus("Completed");
                paymentRepository.save(existingEnrollment);
            }
            return true;
        }
        return false;
    }


    private boolean verifyRazorpaySignature(String orderId, String paymentId, String signature) {
        try {
            // Add Razorpay HMAC signature verification logic here
            String secretKey = "cX3rnwciccdWXuT1JUvQ8ywd"; // Replace with your Razorpay key
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            String generatedSignature = new String(Hex.encodeHex(mac.doFinal(payload.getBytes())));

            return generatedSignature.equals(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateMentorPayment(Mentor mentor, double amount) {

        double mentorShare = amount * 0.55; 
        double commission = amount * 0.45;

        MentorPayment mentorPayment = mentorPaymentRepository.findTopByMentorAndPaymentStatusOrderByPaymentDueDateDesc(mentor, "due")
                .orElseThrow(() -> new IllegalStateException("No 'due' payment record found for mentor: " + mentor.getMentorId()));

        mentorPayment.setTotalAmount(mentorPayment.getTotalAmount().add(BigDecimal.valueOf(mentorShare)));
        mentorPayment.setCommissionDeducted(mentorPayment.getCommissionDeducted().add(BigDecimal.valueOf(commission)));
		mentorPayment.setLastPaymentDate(LocalDate.now());

        mentorPaymentRepository.save(mentorPayment);
    }

    private CourseResponse mapToCourseResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setCourseId(course.getCourseId());
        response.setBlocked(course.isBlocked());

        if (course.getDraft() != null) {
            DraftResponse draftResponse = new DraftResponse();
            Draft draft = course.getDraft();
            draftResponse.setDraftId(draft.getDraftId());
            draftResponse.setCourseName(draft.getCourseName());
            draftResponse.setCourseDescription(draft.getCourseDescription());
            draftResponse.setLevel(draft.getLevel());
            draftResponse.setType(draft.getType());
            draftResponse.setThumbnailUrl(draft.getThumbnailUrl());
            draftResponse.setCoursePrice(draft.getCoursePrice());
            draftResponse.setSellingPrice(draft.getSellingPrice());
            // Include SubCategory and Mentor details if available
            if (draft.getSubCategory() != null) {
                draftResponse.setSubCategory(mapToSubCategoryResponse(draft.getSubCategory()));
            }
            if (draft.getMentor() != null) {
                draftResponse.setMentorName(draft.getMentor().getFirstName());
            }

            response.setDraft(draftResponse);
        }
        return response;
    }

    // Map SubCategory to SubCategoryResponse
    private SubCategoryResponse mapToSubCategoryResponse(SubCategory subCategory) {
        SubCategoryResponse response = new SubCategoryResponse();
        response.setSubcategoryName(subCategory.getSubcategoryName());
        response.setCatetoryName(subCategory.getCategory().getCategoryName());
        return response;
    }
}
