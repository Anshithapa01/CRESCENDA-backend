package com.crescenda.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialResponse {
    private Long materialId;
    private ChapterResponse chapter;
    private String materialName;
    private String materialType;
    private String materialUrl; 

      
}
