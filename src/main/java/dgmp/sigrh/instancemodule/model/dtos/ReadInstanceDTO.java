package dgmp.sigrh.instancemodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadInstanceDTO
{
    private Long instanceId;
    private String name;
    private Long headId;
    private String headName;
    private String headSigle;
    private String headHierarchySigle;
    private Long rhId;
    private String rhName;
    private String rhSigle;
    private String rhHierarchySigle;
}
