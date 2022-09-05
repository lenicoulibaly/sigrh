package dgmp.sigrh.typemodule.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum TypeGroup
{
    STRUCTURE("TYP_STR", "TYPE-STRUCTURE"),
    AGENT("TYP_AGT", "TYPE-AGENT"),
    DEMANDE("TYP_DMD", "TYPE-DEMANDE"),
    MOUVEMENT("TYP_MVT", "TYPE-MOUVEMENT"),;
    private String groupCode;
    private String groupName;
}
