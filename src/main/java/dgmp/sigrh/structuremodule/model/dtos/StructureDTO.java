package dgmp.sigrh.structuremodule.model.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StructureDTO
{
    private Long strId;
    private String strName;
    private String strSigle;
    private long strLevel;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;

    private Long parentId;
    private String parentName;
    private String parentSigle;
    public StructureDTO(Long strId)
    {
        this.strId = strId;
    }
}
