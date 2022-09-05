package dgmp.sigrh.auth.model.events.types.auth;

import dgmp.sigrh.auth.model.events.types.EventType;

public enum UserEventTypes implements EventType
{
    CREATE_USER("CREATE_USER"),
    CREATE_ACTIVE_USER("CREATE_ACTIVE_USER"),
    CHANGE_PASSWORD("CHANGE_PASSWORD"),
    REINITIALISE_PASSWORD("REINITIALISE_PASSWORD"),
    CHANGE_USERNAME("CHANGE_USERNAME"),
    SIMPLE_UPDATE("SIMPLE_UPDATE"),
    ACTIVATE_ACCOUNT("ACTIVATE_ACCOUNT"),
    CLICK_ACTIVATE_ACCOUNT_LINK("CLICK_ACTIVATE_ACCOUNT_LINK"),
    CLICK_REINITIALISE_PASSWORD_LINK("CLICK_REINITIALISE_PASSWORD_LINK"),
    LOGIN("LOGIN"), CHANGE_DEFAULT_ASSIGNATION_ID("CHANGE_DEFAULT_ASSIGNATION_ID");

    String name;
    UserEventTypes(String name)
    {
        this.name = name;
    }

    UserEventTypes(){  }

    @Override
    public String toString() {
        return name;
    }
}
