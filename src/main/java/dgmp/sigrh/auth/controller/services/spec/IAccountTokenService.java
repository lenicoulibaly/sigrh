package dgmp.sigrh.auth.controller.services.spec;

import dgmp.sigrh.auth.model.entities.AccountToken;
import dgmp.sigrh.auth.model.entities.AppUser;

public interface IAccountTokenService
{
    AccountToken createAccountToken(AppUser appUser);
    AccountToken createAccountToken(Long agentId);
}
