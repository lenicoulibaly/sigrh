package dgmp.sigrh.structuremodule.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum StrEventType
{
    CREATE_STR("Création d'une structure"),
    UPDATE_STR("modification d'une structure"),
    DELETE_STR("Suppression d'une structure"),
    RESTORE_STR("Restoration d'une structure"),
    CHANGE_STR_ANCHOR("Changement d'ancrage institutionel");
    private String event;
}
