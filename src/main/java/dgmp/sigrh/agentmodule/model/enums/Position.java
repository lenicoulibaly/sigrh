package dgmp.sigrh.agentmodule.model.enums;

import java.util.Arrays;

public enum Position
{
    ACTIVITE("Activite"), DETACHEMENT("Détachement"), DISPONIBILITE("Disponibilité"), SOUS_DRAPEAUX("Sous les drapeaux");
    private final String name;
    Position(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean existsByValue(String value)
    {
        return Arrays.stream(Position.values()).anyMatch(val->value.equalsIgnoreCase(val.toString()));
    }
}