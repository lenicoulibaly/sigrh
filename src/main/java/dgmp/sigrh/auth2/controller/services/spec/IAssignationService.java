package dgmp.sigrh.auth2.controller.services.spec;

import dgmp.sigrh.auth2.model.dtos.asignation.*;
import dgmp.sigrh.auth2.model.entities.PrincipalAss;

public interface IAssignationService
{
    PrincipalAss CreatePrincipalAss(CreatePrincipalAssDTO dto);
    void setPrincipalAssAsDefault(Long principalAssId);
    void setPrincipalAssAuthorities(SetAuthoritiesToPrincipalAssDTO dto);

    void addRolesToPrincipalAss(RolesAssDTO dto);
    void removeRolesToPrincipalAss(RolesAssDTO dto);

    void addPrivilegesToPrincipalAss(PrvsAssDTO dto);
    void removePrivilegesToPrincipalAss(PrvsAssDTO dto); //assStatus = 2
    void revokePrivilegesToPrincipalAss(PrvsAssDTO dto); //assStatus = 3
    void restorePrivilegesToPrincipalAss(PrvsAssDTO dto); //assStatus = 2

    void setRolePrivileges(PrvsToRoleDTO dto);
    void addPrivilegesToRole(PrvsToRoleDTO dto);
    void removePrivilegesToRole(PrvsToRoleDTO dto);
}
