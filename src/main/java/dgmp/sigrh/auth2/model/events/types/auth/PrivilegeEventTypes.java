package dgmp.sigrh.auth2.model.events.types.auth;


import dgmp.sigrh.shared.model.enums.EventType;

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
