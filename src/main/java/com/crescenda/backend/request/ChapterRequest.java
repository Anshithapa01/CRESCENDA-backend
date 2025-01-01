package com.crescenda.backend.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterRequest {

	private String chapterName;
    private String chapterDescription;
    private Integer position;
}
