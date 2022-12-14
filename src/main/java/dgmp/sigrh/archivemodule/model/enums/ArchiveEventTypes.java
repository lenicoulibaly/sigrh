package dgmp.sigrh.archivemodule.model.enums;


import dgmp.sigrh.shared.model.enums.EventType;

public enum ArchiveEventTypes implements EventType
{
    CREATE_ARCHIVE("Création d'une archive"),
    DELETE_ARCHIVE("Suppression d'une archive"),
    UPDATE_ARCHIVE("Modification d'une archive");

    String event;
    ArchiveEventTypes(String event)
    {
        this.event = event;
    }

    ArchiveEventTypes(){  }

    @Override
    public String toString() {
        return event;
    }
}
