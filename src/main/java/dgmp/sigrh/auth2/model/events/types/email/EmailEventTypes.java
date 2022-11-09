package dgmp.sigrh.auth2.model.events.types.email;


import dgmp.sigrh.shared.model.enums.EventType;

public enum EmailEventTypes implements EventType
{
    ACTIVATE_ACCOUNT_REQUEST("ACTIVATE_ACCOUNT_REQUEST"),
    REINITIALISE_PASSWORD_REQUEST("REINITIALISE_PASSWORD_REQUEST"),
    EMAIL_SENT("EMAIL_SENT");
    String name;
    EmailEventTypes(){}
    EmailEventTypes(String name){this.name = name;}
}
