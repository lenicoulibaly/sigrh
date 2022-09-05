package dgmp.sigrh.auth.model.events.types.auth;

import dgmp.sigrh.auth.model.events.types.EventType;

public enum StructureEventTypes implements EventType
{
    CREATE("CREATE"),
    SIMPLE_UPDATE("SIMPLE_UPDATE"),
    CHANGE_PARENT("CHANGE_PARENT");

    String name;
    StructureEventTypes(String name)
    {
        this.name = name;
    }
    StructureEventTypes() { }

    @Override
    public String toString() {
        return name;
    }
}
