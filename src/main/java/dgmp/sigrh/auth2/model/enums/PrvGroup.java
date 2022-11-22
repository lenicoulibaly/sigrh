package dgmp.sigrh.auth2.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.EnumUtils;

import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor
public enum PrvGroup
{
    SECURITY("Sécurité"),
    ADMINISTRATION("Administration"),
    STR_POST("Structures et postes de travail"),
    AGENT("Agents"),
    MOUVEMENT("Mouvement"),
    PROMOTION("Promotion"),
    NOMINATION("Nomination");

    private String group;

    public static List<PrvGroup> getPrvGroups()
    {
        return EnumUtils.getEnumList(PrvGroup.class);
    }
}
