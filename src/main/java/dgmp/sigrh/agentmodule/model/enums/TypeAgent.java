package dgmp.sigrh.agentmodule.model.enums;

import java.util.Arrays;

public enum TypeAgent
{
    FONCTIONNAIRE("Fonctionnaire"), CONTRACTUEL("Contractuel"), CONTRACTUEL_ASSIMILE("Contractuel assimilé"), STAGIAIRE("Stagiaire");
    private final String name;
    TypeAgent(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean existsByValue(String value)
    {
        return Arrays.stream(TypeAgent.values()).anyMatch(val->value.equalsIgnoreCase(val.toString()));
    }
}
