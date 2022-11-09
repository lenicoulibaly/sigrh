package dgmp.sigrh.auth2.model.events.types.auth;

import dgmp.sigrh.shared.model.enums.EventType;

public enum RoleEventTypes implements EventType
{
    CREATE("CREATE");
    String name;
    RoleEventTypes(String name)
    {
        this.name = name;
    }
    RoleEventTypes() { }

    @Override
    public String toString() {
        return name;
    }
}
