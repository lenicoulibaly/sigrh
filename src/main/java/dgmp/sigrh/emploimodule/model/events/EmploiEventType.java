package dgmp.sigrh.emploimodule.model.events;

import dgmp.sigrh.auth.model.events.types.EventType;

public enum EmploiEventType implements EventType
{
    CREATE_EMPLOI("Création d'un nouvel emploi"),
    UPDATE_EMPLOI("Modification d'un emploi"),
    DELETE_EMPLOI("Suppression d'un emploi"),
    RESTORE_EMPLOI("Restoration d'un emploi");

    String name;
    EmploiEventType(String name)
    {
        this.name = name;
    }

    EmploiEventType(){  }

    @Override
    public String toString() {
        return name;
    }
}
