package dgmp.sigrh.structuremodule.model.dtos.str;

import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadStrDTO
{
    private Long strId;
    private String strCode;
    private String strName;
    private String strSigle;

    private String strType;
    private String strTel;
    private String strAddress;
    private String situationGeo;

    private Long parentId;
    private String parentName;
    private String parentSigle;

    private Long respoId;
    private String respoName;
    private String respoMatricule;
    private List<Structure> hierarchy;
    private String hierarchySigles;

    @Override
    public String toString()
    {
        return this.strName + (this.strSigle == null ? "" : " ("+this.strSigle + ")");
    }
}