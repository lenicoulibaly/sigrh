package dgmp.sigrh.auth2.model.events.types.auth;

import dgmp.sigrh.shared.model.enums.EventType;

public enum TokenEventTypes implements EventType
{
    CREATE_TOKEN("CREATE_TOKEN"),
    USE_TOKEN("USE_TOKEN");
    String name;
    TokenEventTypes(){}
    TokenEventTypes(String name){this.name = name;}
}
