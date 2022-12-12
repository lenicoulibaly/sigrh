package dgmp.sigrh.agentmodule.model.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.Arrays;
import java.util.List;

public enum Civility
{
    MONSIEUR("M."), MADAME("Mme."), MADEMOISELLE("Mlle");
    private final String name;
    Civility(String name)
    {
        this.name = name;
    }

    public static List<Civility> getAll()
    {
        return EnumUtils.getEnumList(Civility.class);
    }

    @Override
    public String toString() {
        return this.name();
    }

    public static boolean existsByValue(String value)
    {
        return Arrays.stream(Civility.values()).anyMatch(val->value.equalsIgnoreCase(val.toString()));
    }
}
