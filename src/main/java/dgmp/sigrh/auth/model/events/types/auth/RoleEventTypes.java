package dgmp.sigrh.auth.model.events.types.auth;

import dgmp.sigrh.auth.model.events.types.EventType;

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
