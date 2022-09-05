package dgmp.sigrh.auth.controller.apis;

import dgmp.sigrh.auth.controller.repositories.UserDAO;
import dgmp.sigrh.auth.controller.services.spec.IUserService;
import dgmp.sigrh.auth.controller.validators.Exceptions.AppException;
import dgmp.sigrh.auth.model.dtos.ActivateAccountDTO;
import dgmp.sigrh.auth.model.dtos.appuser.*;
import dgmp.sigrh.auth.security.services.IAuthoritiesService;
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
@Slf4j @RequiredArgsConstructor
@RequestMapping(path = "/staffadmin/auth-server/users")
public class UserAPI
{
    private final IUserService userService;
    private final UserDAO userDAO;
    private final IAuthoritiesService authoritiesService;

    @PostMapping(path = "/create")
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public ReadUserDTO createUser(@RequestBody CreateUserDTO dto)
    {
        return userService.createUser(dto);
    }

    @PostMapping(path = "/create-test-user")
    @PreAuthorize("hasAuthority('CREATE_TEST_USER')")
    public ReadUserDTO createTestUser(@RequestBody CreateUserDTO dto, @RequestParam String password)
    {
        return userService.createTestUser(dto, password);
    }

    @PutMapping(path = "/update")
    @PreAuthorize("#dto.username == authentication.principal.username")
    public ReadUserDTO update(@RequestBody UpdateUserDTO dto)
    {
        return userService.updateUser(dto);
    }

    @PostMapping(path = "/create-active-user")
    public ReadUserDTO createActiveUser(@RequestBody CreateActiveUserDTO dto)
    {
        return userService.createActiveUser(dto);
    }

    @PutMapping(path = "/change-username")
    @PreAuthorize("#dto.oldUsername == authentication.principal.username")
    public ReadUserDTO ChangeUsername(@RequestBody ChangeUsernameDTO dto)
    {
        return userService.changeUsername(dto);
    }

    @PutMapping(path = "/change-password")
    @PreAuthorize("#dto.username == authentication.principal.username")
    public ReadUserDTO changePassword(@RequestBody ChangePasswordDTO dto)
    {
        return userService.changePassword(dto);
    }

    @PutMapping(path = "/reinit-password")
    public ReadUserDTO ReinitPassword(@RequestBody ReinitialisePasswordDTO dto)
    {
        return userService.reinitialisePassword(dto);
    }

    @GetMapping(path = "/username/{username}")
    public ReadUserDTO findByUsername(@PathVariable String username)
    {
        return userService.findByUsername(username);
    }

    @GetMapping(path = "/email/{email}")
    ReadUserDTO findByEmail(@PathVariable String email)
    {
        return userService.findByEmail(email);
    }

    @GetMapping(path = "/tel/{tel}")
    ReadUserDTO findByTel(@PathVariable String tel)
    {
        return userService.findByTel(tel);
    }

    @GetMapping(path = "/structure/{strId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    List<ReadUserDTO> findByStructure(@PathVariable Long strId)
    {
        return userService.findUsersByStructureRecursive(strId);
    }

    @GetMapping(path = "/structure")
    @PreAuthorize("hasAuthority('ADMIN')")
    List<ReadUserDTO> searchUserByStructure(@RequestParam Long strId, @RequestParam(defaultValue = "") String searchKey)
    {
        return userService.searchUsersByStructureRecursive(strId, searchKey);
    }

    @GetMapping(path = "/page")
    @PreAuthorize("hasAuthority('ADMIN')")
    Page<ReadUserDTO> getUserPage(@RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "30")int pageSize)
    {
        return userService.getUserPage(PageRequest.of(pageNum, pageSize));
    }

    @PutMapping(path = "/account-activation")
    @PreAuthorize("isAnonymous()")
    public ReadUserDTO activateDgmpUserAccount(@RequestBody ActivateAccountDTO dto)
    {
        return this.userService.activateAccount(dto);
    }

    @GetMapping(path = "/click-account-activation-link")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<Void>  clickAccountActivationLink(@RequestParam String token)
    {
        return userService.clickAccountActivationLink(token);
    }

    @GetMapping(path = "/click-reinitialise-password-link")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void>  clickReinitialisePasswordLink(@RequestParam String token)
    {
        return userService.clickReinitialisePasswordLink(token);
    }

    @GetMapping(path = "/send-account-activation-link")
    @PreAuthorize("isAnonymous()")
    public void sendAccountActivationLink(@RequestParam String username, @RequestParam String email)
    {
        this.userService.resendAccountActivationEmail(username, email);
    }

    @GetMapping(path = "/send-reinitialise-password-link")
    @PreAuthorize("isAuthenticated()")
    public void sendReinitialisePasswordLink(@RequestParam String username, @RequestParam String email)
    {
        this.userService.sendReinitialisePasswordEmail(username, email);
    }

    @GetMapping(path = "/verify/username/isAlreadyUsed")
    public boolean isUsernameAlreadyUsed(@RequestParam(defaultValue = "0") Long userId, @RequestParam String username) {
        return userDAO.alreadyExistsByUsername(username, userId);
    }

    @GetMapping(path = "/verify/email/isAlreadyUsed")
    public boolean isEmailAlreadyUsed(@RequestParam(defaultValue = "0") Long userId, @RequestParam String email) {
        return userDAO.alreadyExistsByUsername(email, userId);
    }

    @GetMapping(path = "/verify/tel/isAlreadyUsed")
    public boolean isTelAlreadyUsed(@RequestParam(defaultValue = "0") Long userId, @RequestParam String tel) {
        return userDAO.alreadyExistsByUsername(tel, userId);
    }

    @ExceptionHandler
    ResponseEntity<String> exceptionHandler(AppException e)
    {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}