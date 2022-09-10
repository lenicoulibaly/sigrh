package dgmp.sigrh.structuremodule.model.dtos.str;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Override
    public String toString()
    {
        return this.strName + " ("+this.strSigle + ")";
    }
}