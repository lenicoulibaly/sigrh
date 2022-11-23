package dgmp.sigrh.instancemodule.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

import java.util.List;

@Getter @AllArgsConstructor
public enum InstanceEventType
{
    CREATE_INSTANCE("Création d'une instance"),
    UPDATE_INSTANCE("modification d'une instance"),
    DELETE_INSTANCE("Suppression d'une instance"),
    RESTORE_INSTANCE("Restoration d'une instance");
    private String event;

    public static List<InstanceEventType> getAll()
    {
        return EnumUtils.getEnumList(InstanceEventType.class);
    }

    public static InstanceEventType getEnum(String name)
    {
        return EnumUtils.getEnum(InstanceEventType.class, name);
    }
}
