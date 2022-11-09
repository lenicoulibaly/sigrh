package dgmp.sigrh.auth2.model.events.types.auth;


import dgmp.sigrh.shared.model.enums.EventType;

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
