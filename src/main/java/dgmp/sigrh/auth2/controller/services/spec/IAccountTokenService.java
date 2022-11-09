package dgmp.sigrh.auth2.controller.services.spec;

import dgmp.sigrh.auth2.model.entities.AccountToken;
import dgmp.sigrh.auth2.model.entities.AppUser;

public interface IAccountTokenService
{
    AccountToken createAccountToken(AppUser appUser);
    AccountToken createAccountToken(Long agentId);
}
