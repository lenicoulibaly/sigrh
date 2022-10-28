package dgmp.sigrh.structuremodule.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

import java.util.List;

@Getter @AllArgsConstructor
public enum StrEventType
{
    CREATE_STR("Création d'une structure"),
    UPDATE_STR("modification d'une structure"),
    DELETE_STR("Suppression d'une structure"),
    RESTORE_STR("Restoration d'une structure"),
    CHANGE_STR_ANCHOR("Changement d'ancrage institutionel"),
    CHANGE_STR_CODE("Modification du code structure");
    private String event;

    public static List<StrEventType> getAll()
    {
        return EnumUtils.getEnumList(StrEventType.class);
    }

    public static StrEventType getEnum(String name)
    {
        return EnumUtils.getEnum(StrEventType.class, name);
    }
}
