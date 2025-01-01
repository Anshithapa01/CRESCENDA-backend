package com.crescenda.backend.service;

import java.util.List;

import com.crescenda.backend.model.Draft;
import com.crescenda.backend.request.DraftRequest;
import com.crescenda.backend.response.DraftResponse;

public interface DraftService {

	Draft createDraftFromDTO(DraftRequest draftRequestDTO);

	List<DraftResponse> getDraftsByMentorId(Long mentorId);

	Draft getDraftById(Long draftId);

	void deleteDraftById(Long draftId);

	void publishDraft(Long draftId);

	void updateDraft(Long draftId, DraftRequest draftRequest);

	DraftResponse getDraftResponseById(Long draftId);

	DraftResponse getFirstPendingDraft(Long qaId);

	List<DraftResponse> getApprovedDraftsByMentor(Long mentorId);

}
