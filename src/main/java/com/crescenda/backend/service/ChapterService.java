package com.crescenda.backend.service;

import java.util.List;

import com.crescenda.backend.model.Chapter;
import com.crescenda.backend.request.ChapterRequest;

public interface ChapterService {

	List<Chapter> getChaptersByDraftId(Long draftId);
	Chapter addChapter(Long draftId, ChapterRequest chapterRequest);
    Chapter updateChapter(Long chapterId, ChapterRequest updatedChapter);
    void deleteChapter(Long chapterId);
	Chapter getChapterById(Long chapterId);

}
