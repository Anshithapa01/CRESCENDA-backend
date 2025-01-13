package com.crescenda.backend.service.serviceImpl;


import com.crescenda.backend.model.Chapter;
import com.crescenda.backend.model.CourseQuality;
import com.crescenda.backend.model.Draft;
import com.crescenda.backend.model.Mentor;
import com.crescenda.backend.model.SubCategory;
import com.crescenda.backend.model.Task;
import com.crescenda.backend.repository.CourseQualityRepository;
import com.crescenda.backend.repository.DraftRepository;
import com.crescenda.backend.repository.MentorRepository;
import com.crescenda.backend.repository.SubCategoryRepository;
import com.crescenda.backend.repository.TaskRepository;
import com.crescenda.backend.request.DraftRequest;
import com.crescenda.backend.response.ChapterResponse;
import com.crescenda.backend.response.DraftResponse;
import com.crescenda.backend.response.SubCategoryResponse;
import com.crescenda.backend.response.TaskResponse;
import com.crescenda.backend.service.DraftService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DraftServiceImpl implements DraftService{

    @Autowired
    private DraftRepository draftRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private MentorRepository mentorRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CourseQualityRepository courseQualityRepository;

    

    @Override
    public Draft createDraftFromDTO(DraftRequest draftRequestDTO) {
        Draft draft = new Draft();
        draft.setCourseName(draftRequestDTO.getCourseName());
        draft.setCoursePrice(draftRequestDTO.getCoursePrice());
        draft.setSellingPrice(draftRequestDTO.getSellingPrice());
        draft.setCourseDescription(draftRequestDTO.getCourseDescription());
        draft.setAuthorNote(draftRequestDTO.getAuthorNote());
        draft.setSpecialNote(draftRequestDTO.getSpecialNote());
        draft.setCoursePrerequisite(draftRequestDTO.getCoursePrerequisite());
        draft.setLevel(draftRequestDTO.getLevel());
        draft.setLanguage(draftRequestDTO.getLanguage());
        draft.setType(draftRequestDTO.getType());	
        draft.setThumbnailUrl(draftRequestDTO.getThumbnail());
        draft.setAddedDate(LocalDate.now());
        
        if (draftRequestDTO.getSubCategoryId() != null) {
            SubCategory subCategory = subCategoryRepository.findById(draftRequestDTO.getSubCategoryId())
            		.orElseThrow(() -> new NoSuchElementException("SubCategory not found with ID: " + draftRequestDTO.getSubCategoryId()));
            draft.setSubCategory(subCategory);
        }
        System.out.println("Mentor Id"+draftRequestDTO.getMentorId());
        if (draftRequestDTO.getMentorId() != null) {
        	Mentor mentor=mentorRepository.findById(draftRequestDTO.getMentorId())
            		.orElseThrow(() -> new NoSuchElementException("Mentor not found with ID: " + draftRequestDTO.getMentorId()));
            draft.setMentor(mentor);
        }


        return draftRepository.save(draft);
    }
    
    @Override
    public List<DraftResponse> getDraftsByMentorId(Long mentorId) {
        List<Draft> drafts = draftRepository.findByMentor_MentorId(mentorId);

        return drafts.stream()
        	.filter(draft->draft.getStatus()!=null &&
        			!"approved".equalsIgnoreCase(draft.getStatus()))
        	.map(draft -> {
            DraftResponse response = new DraftResponse();
            response.setDraftId(draft.getDraftId());
            response.setCourseName(draft.getCourseName());
            response.setCourseDescription(draft.getCourseDescription());
            response.setAuthorNote(draft.getAuthorNote());
            response.setSpecialNote(draft.getSpecialNote());
            response.setCoursePrerequisite(draft.getCoursePrerequisite());
            response.setLevel(draft.getLevel());
            response.setLanguage(draft.getLanguage());
            response.setType(draft.getType());
            response.setStatus(draft.getStatus());
            response.setThumbnailUrl(draft.getThumbnailUrl());
            response.setCoursePrice(draft.getCoursePrice());
            response.setSellingPrice(draft.getSellingPrice());
            response.setAddedDate(draft.getAddedDate());

            // Include subcategory and mentor details
            if (draft.getSubCategory() != null) {
                response.setSubCategory(mapToSubCategoryResponse(draft.getSubCategory()));
            }
            if (draft.getMentor() != null) {
                response.setMentorName(draft.getMentor().getFirstName()); 
            }

            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public Draft getDraftById(Long draftId) {
        return draftRepository.findById(draftId)
                .orElseThrow(() -> new IllegalArgumentException("Draft not found"));
    }
    
    
    @Override
    public DraftResponse getDraftResponseById(Long draftId) {
    	System.out.println("draftId "+draftId);
    	Draft draft = draftRepository.findById(draftId)
                .orElseThrow(() -> new IllegalArgumentException("Draft not found"));

        // Create a DraftResponse object and populate it with draft details
        DraftResponse response = new DraftResponse();
        response.setDraftId(draft.getDraftId());
        response.setCourseName(draft.getCourseName());
        response.setCourseDescription(draft.getCourseDescription());
        response.setAuthorNote(draft.getAuthorNote());
        response.setSpecialNote(draft.getSpecialNote());
        response.setCoursePrerequisite(draft.getCoursePrerequisite());
        response.setLevel(draft.getLevel());
        response.setLanguage(draft.getLanguage());
        response.setStatus(draft.getStatus());
        response.setCoursePrice(draft.getCoursePrice());
        response.setSellingPrice(draft.getSellingPrice());
        response.setType(draft.getType());
        response.setChaptersCount(draft.getChapters().size()); 
        response.setThumbnailUrl(draft.getThumbnailUrl());
        int totalMaterials = draft.getChapters().stream()
                .mapToInt(chapter -> chapter.getMaterials().size())
                .sum();
            response.setMaterialsCount(totalMaterials);

        // Include subcategory and mentor details if they exist
        if (draft.getSubCategory() != null) {
            response.setSubCategory(mapToSubCategoryResponse(draft.getSubCategory()));
        }
        if (draft.getChapters() != null) {
            List<ChapterResponse> chapterResponses = draft.getChapters().stream()
                    .map(chapter -> {
                        ChapterResponse chapterResponse = new ChapterResponse();
                        chapterResponse.setChapterId(chapter.getChapterId());
                        chapterResponse.setChapterName(chapter.getChapterName());
                        chapterResponse.setChapterDescription(chapter.getChapterDescription());
                        return chapterResponse;
                    })
                    .collect(Collectors.toList());

            response.setChapters(chapterResponses);
        }
        if (draft.getSubCategory() != null) {
            response.setSubCategoryId(draft.getSubCategory().getSubcategoryId());
        }
        if (draft.getMentor() != null) {
            response.setMentorName(draft.getMentor().getFirstName()); 
            response.setMentorId(draft.getMentor().getMentorId());
        }

    	System.out.println("response "+draftId);
        return response;
    }
    
    public SubCategoryResponse mapToSubCategoryResponse(SubCategory subCategory) {
        SubCategoryResponse response = new SubCategoryResponse();
        response.setSubcategoryId(subCategory.getSubcategoryId());
        response.setSubcategoryName(subCategory.getSubcategoryName());
        response.setSubCategoryDescription(subCategory.getSubCategoryDescription());
        response.setCatetoryName(subCategory.getCategory().getCategoryName());
        response.setIsDeleted(subCategory.getIsDeleted());
        return response;
    }


    @Override
    public void deleteDraftById(Long draftId) {
    	if (!draftRepository.existsById(draftId)) {
            throw new IllegalArgumentException("Draft not found with ID: " + draftId);
        }
        draftRepository.deleteById(draftId);
    }

    @Override
    public void publishDraft(Long draftId) {
        System.out.println("draftId: " + draftId);
        Draft draft = getDraftById(draftId);

        // Change the draft's status
        draft.setStatus("pending");
        draftRepository.save(draft);

        // Check if a task already exists for this draft
        Optional<Task> existingTaskOptional = taskRepository.findByDraftDraftId(draftId);

        if (existingTaskOptional.isPresent()) {
            // Update the existing task
            Task existingTask = existingTaskOptional.get();
            existingTask.setStatus("pending");
            existingTask.setCreatedAt(LocalDate.now());
            taskRepository.save(existingTask);

            // Check if CourseQuality exists for the existing task
            Optional<CourseQuality> existingCourseQualityPresent = courseQualityRepository.findByTaskTaskId(existingTask.getTaskId());
            if (existingCourseQualityPresent.isPresent()) {
                // Update the existing CourseQuality
            	CourseQuality existingCourseQuality=existingCourseQualityPresent.get();
                existingCourseQuality.setStatus("pending");
                existingCourseQuality.setFeedback(null);
                courseQualityRepository.save(existingCourseQuality);
            }else {
            	System.out.println("Course Quality not present");
            }
        } else {
            // Create a new Task if none exists
            Task newTask = new Task();
            newTask.setStatus("pending");
            newTask.setCreatedAt(LocalDate.now());
            newTask.setDraft(draft);
            newTask.setQaExpertUID(null);
            taskRepository.save(newTask);

            // Create a new CourseQuality
            CourseQuality newCourseQuality = new CourseQuality();
            newCourseQuality.setStatus("pending");
            newCourseQuality.setFeedback(null);
            newCourseQuality.setTask(newTask);
            courseQualityRepository.save(newCourseQuality);
        }
    }




    @Override
    public void updateDraft(Long draftId, DraftRequest draftRequest) {
    	Draft draft = getDraftById(draftId);
        draft.setCourseName(draftRequest.getCourseName());
        draft.setCourseDescription(draftRequest.getCourseDescription());
        draft.setCoursePrice(draftRequest.getCoursePrice());
        draft.setSellingPrice(draftRequest.getSellingPrice());
        draft.setAuthorNote(draftRequest.getAuthorNote());
        draft.setSpecialNote(draftRequest.getSpecialNote());
        draft.setCoursePrerequisite(draftRequest.getCoursePrerequisite());
        draft.setLanguage(draftRequest.getLanguage());
        draft.setType(draftRequest.getType());
        draft.setLevel(draftRequest.getLevel());
        draft.setThumbnailUrl(draftRequest.getThumbnail());

        Mentor mentor = mentorRepository.findById(draftRequest.getMentorId())
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found"));
        draft.setMentor(mentor);

        SubCategory subCategory = subCategoryRepository.findById(draftRequest.getSubCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("SubCategory not found"));
        draft.setSubCategory(subCategory);

        draftRepository.save(draft);
    }
    
    
    @Override
    public DraftResponse getFirstPendingDraft(Long qaExpertId) {
        List<Draft> pendingDrafts = draftRepository.findAll().stream()
                .filter(draft -> "pending".equalsIgnoreCase(draft.getStatus()) && draft.getTask() != null)
                .sorted(Comparator.comparing(draft -> draft.getTask().getCreatedAt()))
                .collect(Collectors.toList());

        for (Draft draft : pendingDrafts) {
            Task task = draft.getTask();
            if (task != null) {
                if ("pending".equalsIgnoreCase(task.getStatus())) {
                    return convertToDraftResponse(draft);
                } else if ("picked".equalsIgnoreCase(task.getStatus()) && task.getQa().getQaId() != null) {
                    if (task.getQa().getQaId().equals(qaExpertId)) {
                        return convertToDraftResponse(draft);
                    }
                }
            }
        }
        return null;
    }



    private DraftResponse convertToDraftResponse(Draft draft) {
        DraftResponse response = new DraftResponse();
        response.setDraftId(draft.getDraftId());
        response.setCourseName(draft.getCourseName());
        response.setCourseDescription(draft.getCourseDescription());
        response.setThumbnailUrl(draft.getThumbnailUrl());
        response.setAddedDate(draft.getAddedDate());
        response.setCoursePrice(draft.getCoursePrice());
        response.setSellingPrice(draft.getSellingPrice());
        response.setAuthorNote(draft.getAuthorNote());
        response.setSpecialNote(draft.getSpecialNote());
        response.setCoursePrerequisite(draft.getCoursePrerequisite());
        response.setLevel(draft.getLevel());
        response.setLanguage(draft.getLanguage());
        response.setType(draft.getType());
        response.setChaptersCount(draft.getChapters().size()); 
        response.setStatus(draft.getStatus());
        if (draft.getSubCategory() != null) {
            response.setSubCategory(mapToSubCategoryResponse(draft.getSubCategory()));
        }
        int totalMaterials = draft.getChapters().stream()
                .mapToInt(chapter -> chapter.getMaterials().size())
                .sum();
            response.setMaterialsCount(totalMaterials);
        if (draft.getTask() != null) {
            response.setTask(mapToTaskResponse(draft.getTask()));
        }
        return response; 
    }
    
    public TaskResponse mapToTaskResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setTaskId(task.getTaskId());
        response.setStatus(task.getStatus());
        response.setCreatedAt(task.getCreatedAt());
        response.setQaExpertUID(task.getQaExpertUID());
        return response;
    }
    
    @Override
    public List<DraftResponse> getApprovedDraftsByMentor(Long mentorId) {
        List<Draft> approvedDrafts = draftRepository.findByStatusAndMentorMentorId("approved", mentorId);

        // Convert drafts to DraftResponse DTOs
        return approvedDrafts.stream()
        		.map(draft ->{
        		DraftResponse response = new DraftResponse();
                response.setDraftId(draft.getDraftId());
                response.setCourseName(draft.getCourseName());
                response.setCourseDescription(draft.getCourseDescription());
                response.setAuthorNote(draft.getAuthorNote());
                response.setSpecialNote(draft.getSpecialNote());
                response.setCoursePrerequisite(draft.getCoursePrerequisite());
                response.setLevel(draft.getLevel());
                response.setLanguage(draft.getLanguage());
                response.setType(draft.getType());
                response.setStatus(draft.getStatus());
                response.setThumbnailUrl(draft.getThumbnailUrl());
                response.setCoursePrice(draft.getCoursePrice());
                response.setSellingPrice(draft.getSellingPrice());
                response.setAddedDate(draft.getAddedDate());

                // Include SubCategory and Mentor details if available
                if (draft.getSubCategory() != null) {
                    response.setSubCategory(mapToSubCategoryResponse(draft.getSubCategory()));
                }
                if (draft.getMentor() != null) {
                    response.setMentorName(draft.getMentor().getFirstName());
                }

                return response;
    			})
                .collect(Collectors.toList());
    }

    private int calculateMaterialCount(List<Chapter> chapters) {
        return chapters.stream()
                .mapToInt(chapter -> chapter.getMaterials().size())
                .sum();
    }

    
}
