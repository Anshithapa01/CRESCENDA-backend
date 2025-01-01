package com.crescenda.backend.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.crescenda.backend.model.Chapter;
import com.crescenda.backend.model.SubCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DraftResponse {
    public DraftResponse(Long draftId, String courseName, String courseDescription, LocalDate addedDate,
			String thumbnailUrl,BigDecimal coursePrice,BigDecimal sellingPrice) {
    	this.draftId=draftId;
    	this.courseName=courseName;
    	this.coursePrice=coursePrice;
    	this.sellingPrice=sellingPrice;
    	this.courseDescription=courseDescription;
    	this.addedDate=addedDate;
    	this.thumbnailUrl=thumbnailUrl;
    	
		// TODO Auto-generated constructor stub
	}
	private Long draftId;
    private String courseName;
    private BigDecimal coursePrice;
    private BigDecimal sellingPrice;
    private String courseDescription;
    private String authorNote;
    private String specialNote;
    private String coursePrerequisite;
    private String level;
    private String language;
    private String status;
    private String type;
    private LocalDate addedDate;
    private String thumbnailUrl;
    private SubCategoryResponse subCategory;  
    private List<ChapterResponse> chapters;
    private TaskResponse task;
    private Long subCategoryId; 
    private int chaptersCount;
    private int materialsCount;
    private String mentorName; 
    private Long mentorId;
} 
