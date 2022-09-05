package dgmp.sigrh.auth.controller.apis;

import dgmp.sigrh.auth.controller.repositories.RoleToUserAssRepo;
import dgmp.sigrh.auth.controller.services.spec.IAssignationService;
import dgmp.sigrh.auth.controller.validators.Exceptions.AppException;
import dgmp.sigrh.auth.model.dtos.asignation.PrvToRoleDTO;
import dgmp.sigrh.auth.model.dtos.asignation.PrvToUserDTO;
import dgmp.sigrh.auth.model.dtos.asignation.RoleToUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/staffadmin/auth-server/assignations")
@RequiredArgsConstructor @Slf4j
public class AssignationAPI
{
    private final IAssignationService assService;
    private final RoleToUserAssRepo rtuRepo;

    @PostMapping(path = "/assign-role-to-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    void assignRoleToUser(@RequestBody @Valid RoleToUserDTO dto)
    {
        if(rtuRepo.userHasAnActiveRoleAss(dto.getUserId())) assService.assignRoleToUser(dto);
        else assService.assignRoleToUserAsDefault(dto);
    }

    @PostMapping(path = "/assign-privilege-to-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    void assignPrivilegeToUser(@RequestBody @Valid PrvToUserDTO dto)
    {
        assService.assignPrivilegeToUser(dto);
    }

    @PostMapping(path = "/assign-privilege-to-role")
    @PreAuthorize("hasAuthority('ADMIN')")
    void assignPrivilegeToRole(@RequestBody @Valid PrvToRoleDTO dto)
    {
        assService.assignPrivilegeToRole(dto);
    }

    @PutMapping(path = "/revoke-role-to-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    void revokeRoleToUser(@RequestBody @Valid RoleToUserDTO dto)
    {
        assService.revokeRoleToUser(dto);
    }

    @PutMapping(path = "/revoke-privilege-to-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    void revokePrivilegeToUser(@RequestBody @Valid PrvToUserDTO dto)
    {
        assService.revokePrivilegeToUser(dto);
    }

    @PutMapping(path = "/revoke-privilege-to-role")
    @PreAuthorize("hasAuthority('ADMIN')")
    void revokePrivilegeToRole(@RequestBody @Valid PrvToRoleDTO dto)
    {
        assService.revokePrivilegeToRole(dto);
    }

    @ExceptionHandler
    ResponseEntity<String> exceptionHandler(AppException e)
    {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
