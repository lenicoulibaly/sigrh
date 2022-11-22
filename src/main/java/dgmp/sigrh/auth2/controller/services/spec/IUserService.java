package dgmp.sigrh.auth2.controller.services.spec;

import dgmp.sigrh.auth2.model.dtos.appuser.*;
import dgmp.sigrh.auth2.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface IUserService
{
    ReadUserDTO createUser(CreateUserDTO dto) throws IllegalAccessException;
    ReadUserDTO createTestUser(CreateUserDTO dto, String password);
    //ReadUserDTO createActiveUser(CreateActiveUserDTO dto);
    ReadUserDTO updateUser(UpdateUserDTO dto);
    ReadUserDTO changePassword(ChangePasswordDTO dto);
    ReadUserDTO changeUsername(ChangeUsernameDTO dto);
    ReadUserDTO reinitialisePassword(ReinitialisePasswordDTO dto);
    ReadUserDTO activateAccount(ActivateAccountDTO dto);

    ReadUserDTO findByUsername(String username);
    ReadUserDTO findByEmail(String email);
    ReadUserDTO findByTel(String tel);
    Page<ReadUserDTO> getUserPage(PageRequest request);


    //@Transactional
    void sendAccountActivationEmail(String username, String email) throws IllegalAccessException;
    void sendAccountActivationEmail(Long userId) throws IllegalAccessException;

    //void resendAccountActivationEmail(String username, String email) throws IllegalAccessException;

    void sendReinitialisePasswordEmail(String username, String email) throws IllegalAccessException;

    void clickLink(String token);

    UserStatus getUserStatus(Long userId);

    Page<ReadUserDTO> searchUsers(String key, Long strId, Pageable pageable);

    void blockAccount(Long userId);

    void unBlockAccount(Long userId);
}
