package dgmp.sigrh.agentmodule.model.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.Arrays;
import java.util.List;

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

    public static List<TypeAgent> getAll()
    {
        return EnumUtils.getEnumList(TypeAgent.class);
    }
}
