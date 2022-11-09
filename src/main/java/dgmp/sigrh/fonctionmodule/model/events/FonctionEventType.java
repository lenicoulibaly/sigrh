package dgmp.sigrh.fonctionmodule.model.events;

import dgmp.sigrh.shared.model.enums.EventType;

public enum FonctionEventType implements EventType
{
    CREATE_FONCTION("Création d'une nouvelle fonction"),
    UPDATE_FONCTION("Modification d'une fonction"),
    DELETE_FONCTION("Suppression d'une fonction"),
    RESTORE_FONCTION("Restoration d'une fonction");

    public String name;
    FonctionEventType(String name)
    {
        this.name = name;
    }

    FonctionEventType(){  }

    @Override
    public String toString() {
        return name;
    }
}
