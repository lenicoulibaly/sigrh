package dgmp.sigrh.auth.model.events.types.auth;

import dgmp.sigrh.auth.model.events.types.EventType;

public enum FunctionEventTypes implements EventType
{
    CREATE("CREATE"),
    UPDATE("UPDATE");

    String name;
    FunctionEventTypes(String name)
    {
        this.name = name;
    }
    FunctionEventTypes() { }

    @Override
    public String toString() {
        return name;
    }
}
