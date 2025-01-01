package com.crescenda.backend.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequest {
	
	private Long chapterId;
	private Long materialId;
    private String materialName;
    private String materialType;
    private String materialUrl;
}
