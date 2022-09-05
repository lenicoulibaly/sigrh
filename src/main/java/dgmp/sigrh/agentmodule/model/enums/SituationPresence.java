package dgmp.sigrh.agentmodule.model.enums;

import java.util.Arrays;

public enum SituationPresence
{
    PRESENT("Présent"), ABSENT("Absent"), ABSENT_JUSTIFIE("Absent justifié");
    private final String name;
    SituationPresence(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean existsByValue(String value)
    {
        return Arrays.stream(SituationPresence.values()).anyMatch(val->value.equalsIgnoreCase(val.toString()));
    }
}
