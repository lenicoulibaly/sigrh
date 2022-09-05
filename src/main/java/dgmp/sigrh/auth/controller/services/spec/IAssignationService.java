package dgmp.sigrh.auth.controller.services.spec;

import dgmp.sigrh.auth.model.dtos.asignation.*;
import dgmp.sigrh.auth.model.entities.Assignation;

public interface IAssignationService
{
    Assignation assignRoleToUserAsDefault(RoleToUserDTO dto);
    Assignation assignRoleToUser(RoleToUserDTO dto);
    Assignation assignPrivilegeToUser(PrvToUserDTO dto);
    Assignation assignPrivilegeToRole(PrvToRoleDTO dto);

    void assignPrivilegesToUser(PrvsToUserDTO dto);
    void assignPrivilegesToRole(PrvsToRoleDTO dto);

    Assignation revokePrivilegeToUser(PrvToUserDTO dto);

    Assignation revokePrivilegeToRole(PrvToRoleDTO dto);

    Assignation revokeRoleToUser(RoleToUserDTO dto);

    Assignation setAssignationAsDefaultForUser(Long assId);
}
