package dgmp.sigrh.auth.controller.apis;

import dgmp.sigrh.auth.controller.repositories.PrivilegeDAO;
import dgmp.sigrh.auth.controller.services.spec.IPrivilegeService;
import dgmp.sigrh.auth.controller.validators.Exceptions.AppException;
import dgmp.sigrh.auth.model.dtos.appprivilege.CreatePrivilegeDTO;
import dgmp.sigrh.auth.model.dtos.appprivilege.ReadPrivilegeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor @Slf4j
@RequestMapping(path = "/staffadmin/auth-server/privileges")
public class PrivilegeAPI
{
    private final IPrivilegeService privilegeService;
    private final PrivilegeDAO privilegeDAO;

    @PostMapping(path = "/create")
    @PreAuthorize("hasAuthority('CREATE_PRIVILEGE')")
    ReadPrivilegeDTO createPrivilege(@RequestBody CreatePrivilegeDTO dto)
    {
        return privilegeService.createPrivilege(dto);
    }

    @GetMapping(path = "/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    Page<ReadPrivilegeDTO> searchPrivileges(@RequestParam(defaultValue = "0") String searchKey, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "30") int pageSize)
    {
        return privilegeService.searchPrivileges(searchKey, PageRequest.of(pageNum, pageSize));
    }

    @GetMapping(path = "/verify/privilegeCode/isAlreadyUsed")
    boolean existsByCode(@RequestParam String code, @RequestParam Long roleId)
    {
        return privilegeDAO.alreadyExistsByName(code, roleId);
    }

    @GetMapping(path = "/users-revoked-privileges")
    List<ReadPrivilegeDTO> getUsersRevokedPrivileges(@RequestParam Long userId)
    {
        return null;
    }

    @GetMapping(path = "/verify/privilegeName/isAlreadyUsed")
    boolean existsByName(@RequestParam String name, @RequestParam Long roleId)
    {
        return privilegeDAO.alreadyExistsByName(name, roleId);
    }

    @ExceptionHandler
    ResponseEntity<String> exceptionHandler(AppException e)
    {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
