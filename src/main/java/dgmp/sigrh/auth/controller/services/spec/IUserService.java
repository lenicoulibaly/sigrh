package dgmp.sigrh.auth.controller.services.spec;

import dgmp.sigrh.auth.model.dtos.ActivateAccountDTO;
import dgmp.sigrh.auth.model.dtos.appuser.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService
{
    ReadUserDTO createUser(CreateUserDTO dto);
    ReadUserDTO createTestUser(CreateUserDTO dto, String password);
    ReadUserDTO createActiveUser(CreateActiveUserDTO dto);
    ReadUserDTO updateUser(UpdateUserDTO dto);
    ReadUserDTO changePassword(ChangePasswordDTO dto);
    ReadUserDTO changeUsername(ChangeUsernameDTO dto);
    ReadUserDTO reinitialisePassword(ReinitialisePasswordDTO dto);
    ReadUserDTO activateAccount(ActivateAccountDTO dto);

    ReadUserDTO findByUsername(String username);
    ReadUserDTO findByEmail(String email);
    ReadUserDTO findByTel(String tel);
    Page<ReadUserDTO> getUserPage(PageRequest request);
    List<ReadUserDTO> findUsersByStructureRecursive(Long strId);

    List<ReadUserDTO> searchUsersByStructureRecursive(Long strId, String searchKey);

    void resendAccountActivationEmail(String username, String email);

    void sendReinitialisePasswordEmail(String username, String email);

    ResponseEntity<Void> clickAccountActivationLink(String token);

    ResponseEntity<Void> clickReinitialisePasswordLink(String token);
}
