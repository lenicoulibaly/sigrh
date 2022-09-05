package dgmp.sigrh.agentmodule.model.enums;

import java.util.Arrays;

public enum Civility
{
    MONSIEUR("M."), MADAME("Mme."), MADEMOISELLE("Mlle");
    private final String name;
    Civility(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean existsByValue(String value)
    {
        return Arrays.stream(Civility.values()).anyMatch(val->value.equalsIgnoreCase(val.toString()));
    }
}
