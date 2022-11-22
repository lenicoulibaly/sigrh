package dgmp.sigrh.auth2.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.EnumUtils;

import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor
public enum UserStatus
{
    ACTIVE("Actif"),
    BLOCKED("Bloqué"),
    STANDING_FOR_ACCOUNT_ACTIVATION("En attente d'activation du compte"),
    UN_KNOWN("Inconnu"),
    STANDING_FOR_ACCOUNT_ACTIVATION_LINK("En attente d'un lien d'activation");

    private String status;

    public UserStatus getUserStatus(String statusName)
    {
        return EnumUtils.getEnum(UserStatus.class, statusName);
    }

    public List<UserStatus> getAllUserStatus(String statusName)
    {
        return EnumUtils.getEnumList(UserStatus.class);
    }
}
