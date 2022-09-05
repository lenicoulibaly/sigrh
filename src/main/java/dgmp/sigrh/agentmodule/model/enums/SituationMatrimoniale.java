package dgmp.sigrh.agentmodule.model.enums;

import java.util.Arrays;

public enum SituationMatrimoniale
{
    CELIBATAIRE("Célibataire"), MARIE("Marié(e)"), VOEUF("Voeuf ou Voeuve"), MARIE_TRADITITIONNEL("Marié(e) traditionnellement");
    private final String name;
    SituationMatrimoniale(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean existsByValue(String value)
    {
        return Arrays.stream(SituationMatrimoniale.values()).anyMatch(val->value.equalsIgnoreCase(val.toString()));
    }
}
