package dgmp.sigrh.agentmodule.model.enums;

import java.util.Arrays;

public enum EtatRecrutement
{
    ATTENTE_AFFECTATION("En attente d'affectation"), ATTENTE_PRISE_SERVICE("En attende de prise de service"), EN_SERVICE("En service");
    private final String name;
    EtatRecrutement(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean existsByValue(String value)
    {
        return Arrays.stream(EtatRecrutement.values()).anyMatch(val->value.equalsIgnoreCase(val.toString()));
    }
}
