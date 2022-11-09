package dgmp.sigrh.grademodule.model.events;

import dgmp.sigrh.shared.model.enums.EventType;
import lombok.Getter;

public enum GradeEventType implements EventType
{
    CREATE_GRADE("Création d'un nouveau grade"),
    UPDATE_GRADE("Modification d'un grade"),
    DELETE_GRADE("Suppression d'un grade"),
    RESTORE_GRADE("Restoration d'un grade");

    @Getter
    public String name;
    GradeEventType(String name)
    {
        this.name = name;
    }

    GradeEventType(){  }

    @Override
    public String toString() {
        return name;
    }
}
