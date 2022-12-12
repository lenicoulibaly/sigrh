package dgmp.sigrh.agentmodule.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor @Getter
public enum EtatRecrutement
{
    ENREGISTRE("Enregistré",true, true),ATTENTE_AFFECTATION("En attente d'affectation", true, true), ATTENTE_PRISE_SERVICE("En attende de prise de service", true, true),
    EN_SERVICE("En service", true, true), EN_DETACHEMENT("En détachement",true, false), DISPONIBILITE("Disponibilité", false, false),
    SOUS_DRAPEAUX("Sous les drapeaux", true, false), RETRAITE("Retraité", false, false), DEMISSIONNAIRE("Démissionnaire", false, false),
    DECEDE("Décédé", false, false);
    public final String name;
    public final boolean withUs;
    public final boolean present;

    public static List<EtatRecrutement> getWithUs(boolean withUs)
    {
        return EnumUtils.getEnumList(EtatRecrutement.class).stream().filter(e->e.withUs == withUs).collect(Collectors.toList());
    }
    public static List<EtatRecrutement> getPresent(boolean present)
    {
        return EnumUtils.getEnumList(EtatRecrutement.class).stream().filter(e->e.present == present).collect(Collectors.toList());
    }

    public static List<EtatRecrutement> getAll()
    {
        return EnumUtils.getEnumList(EtatRecrutement.class);
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