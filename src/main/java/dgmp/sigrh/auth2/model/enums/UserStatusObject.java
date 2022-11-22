package dgmp.sigrh.auth2.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public class UserStatusObject
{
    private UserStatus active = UserStatus.ACTIVE;
    private UserStatus blocked = UserStatus.BLOCKED;
    private UserStatus standingForAccountActivation = UserStatus.STANDING_FOR_ACCOUNT_ACTIVATION;
    private UserStatus un_known = UserStatus.UN_KNOWN;
    private UserStatus standingForAccountActivationLink = UserStatus.STANDING_FOR_ACCOUNT_ACTIVATION_LINK;
}
