package com.crescenda.backend.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Chapter;
import com.crescenda.backend.model.Draft;
import com.crescenda.backend.repository.ChapterRepository;
import com.crescenda.backend.repository.DraftRepository;
import com.crescenda.backend.request.ChapterRequest;
import com.crescenda.backend.service.ChapterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChapterServiceImpl implements ChapterService{

	 	@Autowired
	    private ChapterRepository chapterRepository;
	 	@Autowired
	    private DraftRepository draftRepository;

	 	@Override
	    public List<Chapter> getChaptersByDraftId(Long draftId) {
	        return chapterRepository.findByDraftDraftId(draftId);
	    }
	 	
	 	@Override
	 	public Chapter addChapter(Long draftId, ChapterRequest chapterRequest) {
	 	    // Find the draft
	 	    Draft draft = draftRepository.findById(draftId)
	 	            .orElseThrow(() -> new IllegalArgumentException("Draft not found with ID: " + draftId));
	 	    
	 	    // Create a new Chapter object from the request
	 	    Chapter chapter = new Chapter();
	 	    chapter.setChapterName(chapterRequest.getChapterName());
	 	    chapter.setChapterDescription(chapterRequest.getChapterDescription());
	 	    chapter.setPosition(chapterRequest.getPosition());
	 	    chapter.setCreatedDate(java.time.LocalDateTime.now());
	 	    chapter.setDraft(draft);
	 	    
	 	    // Save the chapter
	 	    return chapterRepository.save(chapter);
	 	}


	 	@Override
	 	public Chapter updateChapter(Long chapterId, ChapterRequest chapterRequest) {
	 	    // Fetch the existing chapter
	 	    Chapter existingChapter = chapterRepository.findById(chapterId)
	 	            .orElseThrow(() -> new IllegalArgumentException("Chapter not found with ID: " + chapterId));

	 	    // Update the fields using the ChapterRequest data
	 	    existingChapter.setChapterName(chapterRequest.getChapterName());
	 	    existingChapter.setChapterDescription(chapterRequest.getChapterDescription());
	 	    existingChapter.setPosition(chapterRequest.getPosition());

	 	    // Save the updated chapter
	 	    return chapterRepository.save(existingChapter);
	 	}


	    @Override
	    public void deleteChapter(Long chapterId) {
	        if (!chapterRepository.existsById(chapterId)) {
	            throw new IllegalArgumentException("Chapter not found with ID: " + chapterId);
	        }
	        chapterRepository.deleteById(chapterId);
	    }
	    
	    @Override
	    public Chapter getChapterById(Long chapterId) {
	        return chapterRepository.findById(chapterId)
	                .orElseThrow(() -> new IllegalArgumentException("Chapter not found with ID: " + chapterId));
	    }

}
