package dgmp.sigrh.auth2.model.events.types.auth;


import dgmp.sigrh.shared.model.enums.EventType;

public enum UserEventTypes implements EventType
{
    CREATE_USER("CREATE_USER"),
    CREATE_ACTIVE_USER("CREATE_ACTIVE_USER"),
    CHANGE_PASSWORD("CHANGE_PASSWORD"),
    REINITIALISE_PASSWORD("REINITIALISE_PASSWORD"),
    CHANGE_USERNAME("CHANGE_USERNAME"),
    SIMPLE_UPDATE("SIMPLE_UPDATE"),
    ACTIVATE_ACCOUNT("ACTIVATE_ACCOUNT"),
    BLOCK_ACCOUNT("Bloquer un compte"),
    UN_BLOCK_ACCOUNT("Déblouer un compte"),
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
