package com.crescenda.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.model.Chapter;
import com.crescenda.backend.request.ChapterRequest;
import com.crescenda.backend.service.ChapterService;

@RestController
@RequestMapping("/api/mentor/draft/chapters")
public class ChapterController {
	
    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

	    @GetMapping("/{draftId}")
	    public ResponseEntity<List<Chapter>> getChaptersByDraftId(@PathVariable Long draftId) {
	        List<Chapter> chapters = chapterService.getChaptersByDraftId(draftId);
	        return ResponseEntity.ok(chapters);
	    }
	    
	    @PostMapping("/{draftId}/add")
	    public ResponseEntity<Chapter> addChapter(@PathVariable Long draftId, @RequestBody ChapterRequest chapterRequest) {
	        Chapter newChapter = chapterService.addChapter(draftId, chapterRequest);
	        return ResponseEntity.status(HttpStatus.CREATED).body(newChapter);
	    }


	    @PutMapping("/{chapterId}/update")
	    public ResponseEntity<Chapter> updateChapter(@PathVariable Long chapterId, @RequestBody ChapterRequest chapterRequest) {
	        Chapter updated = chapterService.updateChapter(chapterId, chapterRequest);
	        return ResponseEntity.ok(updated);
	    }


	    @DeleteMapping("/{chapterId}/delete")
	    public ResponseEntity<Void> deleteChapter(@PathVariable Long chapterId) {
	        chapterService.deleteChapter(chapterId);
	        return ResponseEntity.noContent().build();
	    }
	    
	    @GetMapping("/{chapterId}/get")
	    public ResponseEntity<Chapter> getChapterById(@PathVariable Long chapterId) {
	        Chapter chapter = chapterService.getChapterById(chapterId);
	        return ResponseEntity.ok(chapter); 
	    }

}
