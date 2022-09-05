package dgmp.sigrh.auth.model.events.types.auth;

import dgmp.sigrh.auth.model.events.types.EventType;

public enum PrivilegeEventTypes implements EventType
{
    CREATE("CREATE");
    String name;
    PrivilegeEventTypes(String name)
    {
        this.name = name;
    }
    PrivilegeEventTypes() { }

    @Override
    public String toString() {
        return name;
    }
}
