package dgmp.sigrh.auth2.controller.services.impl.withoutbroker;

import dgmp.sigrh.auth2.controller.repositories.AccountTokenRepo;
import dgmp.sigrh.auth2.controller.repositories.UserHistoRepo;
import dgmp.sigrh.auth2.controller.repositories.UserRepo;
import dgmp.sigrh.auth2.controller.services.spec.IAccountTokenService;
import dgmp.sigrh.auth2.controller.services.spec.IUserService;
import dgmp.sigrh.auth2.controller.validators.Exceptions.AppException;
import dgmp.sigrh.auth2.model.dtos.appuser.*;
import dgmp.sigrh.auth2.model.entities.AccountToken;
import dgmp.sigrh.auth2.model.entities.AppUser;
import dgmp.sigrh.auth2.model.enums.UserStatus;
import dgmp.sigrh.auth2.model.enums.UserStatusObject;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.UserEventTypes;
import dgmp.sigrh.auth2.model.histo.UserHisto;
import dgmp.sigrh.auth2.security.SecurityConstants;
import dgmp.sigrh.auth2.security.SecurityErrorMsg;
import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import dgmp.sigrh.notificationmodule.controller.dao.EmailNotificationRepo;
import dgmp.sigrh.notificationmodule.controller.services.EmailSenderService;
import dgmp.sigrh.notificationmodule.controller.services.EmailServiceConfig;
import dgmp.sigrh.notificationmodule.model.entities.EmailNotification;
import dgmp.sigrh.shared.utilities.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static dgmp.sigrh.auth2.security.SecurityErrorMsg.INVALID_ACTIVATION_TOKEN_ERROR_MSG;
import static dgmp.sigrh.auth2.security.SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG;

@Service
@RequiredArgsConstructor @Slf4j
public class UserService implements IUserService
{
    private final UserRepo userRepo;
    private final UserHistoRepo userHistoRepo;
    private final IAccountTokenService accountTokenService;
    private final UserMapper userMapper;
    private final ISecurityContextManager scm;
    private final AccountTokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final EmailServiceConfig emailServiceConfig;
    private final EmailNotificationRepo emailRepo;

    @Override @Transactional
    public ReadUserDTO createUser(CreateUserDTO dto) throws IllegalAccessException
    {
        EventActorIdentifier eventIdentifier = scm.getEventActorIdFromSCM();
        AppUser user = userMapper.mapToUser(dto);
        user = userRepo.save(user);
        UserHisto userHisto = userMapper.mapToUserHisto(user, UserEventTypes.CREATE_USER, eventIdentifier);
        userHistoRepo.save(userHisto);
        AccountToken accountToken = new AccountToken(UUID.randomUUID().toString(), user);
        accountToken = tokenRepo.save(accountToken);

        EmailNotification emailNotification = new EmailNotification(user, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, accountToken.getToken(), eventIdentifier.getModifierUserId());
        emailSenderService.sendAccountActivationEmail(user.getEmail(), user.getUsername(), emailServiceConfig.getActivateAccountLink() + "?token=" + accountToken.getToken());
        emailNotification.setSent(true);
        emailRepo.save(emailNotification);

        return userMapper.mapToReadUserDTO(user);
    }

    @Override
    public ReadUserDTO createTestUser(CreateUserDTO dto, String password)
    {
        AppUser user = userMapper.mapToUser(dto);
        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);
        user.setNotBlocked(true);
        user = userRepo.save(user);
        ReadUserDTO readUserDTO = userMapper.mapToReadUserDTO(user);
        return readUserDTO;
    }

    @Override @Transactional
    public ReadUserDTO updateUser(UpdateUserDTO dto)
    {
        EventActorIdentifier eventIdentifier = scm.getEventActorIdFromSCM();
        AppUser user = userRepo.findById(dto.getUserId()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        user.setEmail(dto.getEmail());
        user.setTel(dto.getTel());
        user.setLastModificationDate(LocalDateTime.now());
        UserHisto userHisto = userMapper.mapToUserHisto(user, UserEventTypes.SIMPLE_UPDATE, eventIdentifier);
        userHistoRepo.save(userHisto);
        return userMapper.mapToReadUserDTO(user);
    }

    @Override @Transactional
    public ReadUserDTO changePassword(ChangePasswordDTO dto)
    {
        EventActorIdentifier eventIdentifier = scm.getEventActorIdFromSCM();
        AppUser user = userRepo.findById(dto.getUserId()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setLastModificationDate(LocalDateTime.now());
        UserHisto userHisto = userMapper.mapToUserHisto(user, UserEventTypes.CHANGE_PASSWORD, eventIdentifier);
        userHistoRepo.save(userHisto);
        return userMapper.mapToReadUserDTO(user);
    }

    @Override @Transactional
    public ReadUserDTO changeUsername(ChangeUsernameDTO dto)
    {
        EventActorIdentifier eventIdentifier = scm.getEventActorIdFromSCM();
        AppUser user = userRepo.findById(dto.getUserId()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        user.setUsername(dto.getNewUsername());
        user.setLastModificationDate(LocalDateTime.now());
        UserHisto userHisto = userMapper.mapToUserHisto(user, UserEventTypes.CHANGE_USERNAME, eventIdentifier);
        userHistoRepo.save(userHisto);
        return userMapper.mapToReadUserDTO(user);
    }

    @Override @Transactional
    public ReadUserDTO reinitialisePassword(ReinitialisePasswordDTO dto)
    {
        EventActorIdentifier eventIdentifier = scm.getEventActorIdFromSCM();
        AppUser user = userRepo.findByUsername(dto.getUsername()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setLastModificationDate(LocalDateTime.now());
        UserHisto userHisto = userMapper.mapToUserHisto(user, UserEventTypes.REINITIALISE_PASSWORD, eventIdentifier);
        userHistoRepo.save(userHisto);
        AccountToken token = tokenRepo.findByToken(dto.getPasswordReinitialisationToken()).orElseThrow(()->new AppException(INVALID_ACTIVATION_TOKEN_ERROR_MSG));
        token.setUsageDate(LocalDateTime.now());
        token.setAlreadyUsed(true);
        return userMapper.mapToReadUserDTO(user);
    }

    @Override @Transactional
    public ReadUserDTO activateAccount(ActivateAccountDTO dto)
    {
        EventActorIdentifier eventIdentifier = scm.getEventActorIdFromSCM();
        AppUser user = userRepo.findById(dto.getUserId()).orElseThrow(()->new AppException(USER_ID_NOT_FOUND_ERROR_MSG));
        user.setActive(true);
        user.setNotBlocked(true);
        user.setUsername(dto.getUsername());
        user.setLastModificationDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        UserHisto userHisto = userMapper.mapToUserHisto(user, UserEventTypes.ACTIVATE_ACCOUNT, eventIdentifier);
        userHistoRepo.save(userHisto);

        AccountToken token = tokenRepo.findByToken(dto.getActivationToken()).orElseThrow(()->new AppException(INVALID_ACTIVATION_TOKEN_ERROR_MSG));
        token.setUsageDate(LocalDateTime.now());
        token.setAlreadyUsed(true);

        ReadUserDTO readUserDTO = userMapper.mapToReadUserDTO(user);
        return readUserDTO;
    }

    @Override @Transactional
    public void blockAccount(Long userId)
    {
        EventActorIdentifier eventIdentifier = scm.getEventActorIdFromSCM();
        AppUser user = userRepo.findById(userId).orElse(null);
        if(user == null) return;
        if(!user.isNotBlocked()) return;
        user.setNotBlocked(false);
        UserHisto userHisto = userMapper.mapToUserHisto(user, UserEventTypes.BLOCK_ACCOUNT, eventIdentifier);
        userHistoRepo.save(userHisto);
    }

    @Override @Transactional
    public void unBlockAccount(Long userId)
    {
        EventActorIdentifier eventIdentifier = scm.getEventActorIdFromSCM();
        AppUser user = userRepo.findById(userId).orElse(null);
        if(user == null) return;
        if(user.isNotBlocked()) return;
        user.setNotBlocked(true);
        UserHisto userHisto = userMapper.mapToUserHisto(user, UserEventTypes.UN_BLOCK_ACCOUNT, eventIdentifier);
        userHistoRepo.save(userHisto);
    }

    @Override
    public ReadUserDTO findByUsername(String username)
    {
        AppUser user = userRepo.findByUsername(username).orElse(null);
        return user == null ? null : userMapper.mapToReadUserDTO(user);
    }

    @Override
    public ReadUserDTO findByEmail(String email)
    {
        AppUser user = userRepo.findByEmail(email).orElse(null);
        return user == null ? null : userMapper.mapToReadUserDTO(user);
    }

    @Override
    public ReadUserDTO findByTel(String tel)
    {
        AppUser user = userRepo.findByTel(tel).orElse(null);
        return user == null ? null : userMapper.mapToReadUserDTO(user);
    }

    @Override
    public Page<ReadUserDTO> getUserPage(PageRequest request)
    {
        Page<AppUser> userPage = userRepo.getUsersPage(request);
        List<ReadUserDTO> readUserDTOS = userPage.stream().map(userMapper::mapToReadUserDTO).collect(Collectors.toList());
        return new PageImpl<>(readUserDTOS, request, userPage.getTotalElements());
    }


    @Override //@Transactional
    public void sendAccountActivationEmail(String email, String username) throws IllegalAccessException
    {
        AppUser loadedUser = this.userRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(SecurityErrorMsg.USERNAME_NOT_FOUND_ERROR_MSG));
        if(!loadedUser.getEmail().equals(email)) {throw new AppException(SecurityErrorMsg.INVALID_USERNAME_OR_EMAIL_ERROR_MSG);}
        if(loadedUser.isActive() && loadedUser.isNotBlocked()) {throw new AppException(SecurityErrorMsg.ALREADY_ACTIVATED_ACCOUNT_ERROR_MSG);}
        AccountToken accountToken = accountTokenService.createAccountToken(loadedUser);

        EventActorIdentifier eventIdentifier = scm.getEventActorIdFromSCM();
        EmailNotification emailNotification = new EmailNotification(loadedUser, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, accountToken.getToken(), eventIdentifier.getModifierUserId());
        emailSenderService.sendAccountActivationEmail(email, username, emailServiceConfig.getActivateAccountLink() + "?token=" + accountToken.getToken());
        emailNotification.setSent(true);
        emailRepo.save(emailNotification);
    }

    @Override @Transactional
    public void sendAccountActivationEmail(Long userId) throws IllegalAccessException
    {
        AppUser loadedUser = this.userRepo.findById(userId).orElseThrow(()->new UsernameNotFoundException(USER_ID_NOT_FOUND_ERROR_MSG));
        //if(!loadedUser.getEmail().equals(email)) {throw new AppException(SecurityErrorMsg.INVALID_USERNAME_OR_EMAIL_ERROR_MSG);}
        if(loadedUser.isActive() && loadedUser.isNotBlocked()) {throw new AppException(SecurityErrorMsg.ALREADY_ACTIVATED_ACCOUNT_ERROR_MSG);}
        AccountToken accountToken = accountTokenService.createAccountToken(loadedUser);

        EventActorIdentifier eventIdentifier = scm.getEventActorIdFromSCM();
        EmailNotification emailNotification = new EmailNotification(loadedUser, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, accountToken.getToken(), eventIdentifier.getModifierUserId());
        emailSenderService.sendAccountActivationEmail(loadedUser.getEmail(), loadedUser.getUsername(), emailServiceConfig.getActivateAccountLink() + "?token=" + accountToken.getToken());
        emailNotification.setSent(true);
        accountToken.setEmailSent(true);
        emailRepo.save(emailNotification);
    }

    @Override
    public void sendReinitialisePasswordEmail(String email, String username) throws IllegalAccessException {
        AppUser loadedUser = this.userRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(SecurityErrorMsg.USERNAME_NOT_FOUND_ERROR_MSG));
        if(!loadedUser.getEmail().equals(email)) {throw new AppException(SecurityErrorMsg.INVALID_USERNAME_OR_EMAIL_ERROR_MSG);}

        AccountToken accountToken = accountTokenService.createAccountToken(loadedUser);

        EmailNotification emailNotification = new EmailNotification(loadedUser, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, accountToken.getToken(), scm.getAuthUserId());
        emailSenderService.sendReinitialisePasswordEmail(email, username, emailServiceConfig.getReinitPasswordLink());
        emailNotification.setSent(true);
        emailRepo.save(emailNotification);
    }

    @Override @Transactional
    public void clickLink(String token)
    {
        Optional<AccountToken> accountToken$ = tokenRepo.findByToken(token);
        if(!accountToken$.isPresent()) return; //throw new AppException(UNKNOWN_ACCOUNT_TOKEN_ERROR_MSG);
        EmailNotification notification = emailRepo.findByToken(accountToken$.get().getToken()).orElse(null);
        if(notification != null) {
            notification.setSeen(true);
            notification.setSent(true);
        }
    }

    @Override
    public UserStatus getUserStatus(Long userId)
    {
        AppUser user = userRepo.findById(userId).orElse(null);
        if(user == null) return  UserStatus.UN_KNOWN;
        if(user.isActive() && user.isNotBlocked()) return UserStatus.ACTIVE;
        if(user.isActive() && !user.isNotBlocked()) return UserStatus.BLOCKED;
        if(!user.isActive() && user.isNotBlocked() && tokenRepo.hasValidActivationToken(userId)) return UserStatus.STANDING_FOR_ACCOUNT_ACTIVATION;
        if(!user.isActive() && user.isNotBlocked()  && tokenRepo.lastActivationTokenHasExpired(userId)) return UserStatus.STANDING_FOR_ACCOUNT_ACTIVATION_LINK;
        return  UserStatus.UN_KNOWN;
    }

    @Override
    public Page<ReadUserDTO> searchUsers(String key, Long strId, Pageable pageable)
    {
        Page<AppUser>  users = strId == null ? new PageImpl<>(new ArrayList<>()): this.userRepo.searchUsersUnderStr(StringUtils.stripAccentsToUpperCase(key), strId, pageable);
        return new PageImpl<>(users.stream().map(userMapper::mapToReadUserDTO).peek(u->u.setStatus(this.getUserStatus(u.getUserId()))).collect(Collectors.toList()), pageable, users.getTotalElements());
    }



    @Bean("userStatus")
    public UserStatusObject userStatus()
    {
        return new UserStatusObject();
    }
}
