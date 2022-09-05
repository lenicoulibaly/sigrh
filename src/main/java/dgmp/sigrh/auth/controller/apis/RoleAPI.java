package dgmp.sigrh.auth.controller.apis;

import dgmp.sigrh.auth.controller.repositories.RoleDAO;
import dgmp.sigrh.auth.controller.services.spec.IRoleService;
import dgmp.sigrh.auth.controller.validators.Exceptions.AppException;
import dgmp.sigrh.auth.model.dtos.approle.CreateRoleDTO;
import dgmp.sigrh.auth.model.dtos.approle.ReadRoleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor @Slf4j
@RequestMapping(path = "/staffadmin/auth-server/roles")
public class RoleAPI
{
    private final IRoleService roleService;
    private final RoleDAO roleDAO;

    @PostMapping(path = "/create")
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    ReadRoleDTO createRole(@RequestBody CreateRoleDTO dto)
    {
        return roleService.createRole(dto);
    }

    @GetMapping(path = "/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    Page<ReadRoleDTO> searchRoles(@RequestParam(defaultValue = "") String searchKey, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "30") int pageSize)
    {
        return roleService.searchRoles(searchKey, PageRequest.of(pageNum, pageSize));
    }

    @GetMapping(path = "/verify/roleCode/isAlreadyUsed")
    boolean existsByCode(@RequestParam String code, @RequestParam Long roleId)
    {
        return roleDAO.alreadyExistsByName(code, roleId);
    }

    @GetMapping(path = "/verify/roleName/isAlreadyUsed")
    boolean existsByName(@RequestParam String name, @RequestParam Long roleId)
    {
        return roleDAO.alreadyExistsByName(name, roleId);
    }

    @ExceptionHandler
    ResponseEntity<String> exceptionHandler(AppException e)
    {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
