package dgmp.sigrh.auth.model.events.types.email;

import dgmp.sigrh.auth.model.events.types.EventType;

public enum EmailEventTypes implements EventType
{
    ACTIVATE_ACCOUNT_REQUEST("ACTIVATE_ACCOUNT_REQUEST"),
    REINITIALISE_PASSWORD_REQUEST("REINITIALISE_PASSWORD_REQUEST"),
    EMAIL_SENT("EMAIL_SENT");
    String name;
    EmailEventTypes(){}
    EmailEventTypes(String name){this.name = name;}
}
