package dgmp.sigrh.auth.model.events.types.email;

import dgmp.sigrh.auth.model.events.types.EventType;

public enum TokenEventTypes implements EventType
{
    CREATE_TOKEN("CREATE_TOKEN"),
    USE_TOKEN("USE_TOKEN");
    String name;
    TokenEventTypes(){}
    TokenEventTypes(String name){this.name = name;}
}
