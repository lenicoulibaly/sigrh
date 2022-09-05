package dgmp.sigrh.structuremodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Builder @Data
public class ReadStructureDTO
{
    private Long strId;
    private String strName;
    private String strSigle;
    private Long parentId;
    private Long parentName;
    private Long parentSigle;
    private long strLevel;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;
}
