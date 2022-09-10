package dgmp.sigrh.auth.controller.services.impl.withbroker;

import dgmp.sigrh.auth.controller.repositories.AccountTokenDAO;
import dgmp.sigrh.auth.controller.repositories.UserDAO;
import dgmp.sigrh.auth.controller.services.impl.StructureService;
import dgmp.sigrh.auth.controller.services.spec.IAccountTokenService;
import dgmp.sigrh.auth.controller.services.spec.IBrokerMessageSender;
import dgmp.sigrh.auth.controller.services.spec.IUserService;
import dgmp.sigrh.auth.controller.validators.Exceptions.AppException;
import dgmp.sigrh.auth.model.dtos.ActivateAccountDTO;
import dgmp.sigrh.auth.model.dtos.appuser.*;
import dgmp.sigrh.auth.model.dtos.emailNotification.EmailNotification;
import dgmp.sigrh.auth.model.entities.AccountToken;
import dgmp.sigrh.auth.model.entities.AppUser;
import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.auth.model.events.types.auth.UserEventTypes;
import dgmp.sigrh.auth.model.events.types.email.EmailEventTypes;
import dgmp.sigrh.auth.security.SecurityConstants;
import dgmp.sigrh.auth.security.SecurityErrorMsg;
import dgmp.sigrh.auth.security.services.ISecurityContextManager;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static dgmp.sigrh.auth.controller.validators.Exceptions.ErrorMsg.UNKNOWN_ACCOUNT_TOKEN_ERROR_MSG;
import static dgmp.sigrh.auth.model.events.types.auth.UserEventTypes.ACTIVATE_ACCOUNT;
import static dgmp.sigrh.auth.security.SecurityConstants.TOKEN_TOPIC;
import static dgmp.sigrh.auth.security.SecurityConstants.USER_TOPIC;
import static dgmp.sigrh.auth.security.SecurityErrorMsg.INVALID_ACTIVATION_TOKEN_ERROR_MSG;
import static dgmp.sigrh.auth.security.SecurityErrorMsg.USERNAME_NOT_FOUND_ERROR_MSG;

@Service
@RequiredArgsConstructor @Slf4j
@Profile("withbroker")
public class UserService implements IUserService
{
    private final UserDAO userDAO;
    private final IAccountTokenService accountTokenService;
    private final UserMapper userMapper;
    private final ISecurityContextManager scm;
    private final StrRepo strDAO;
    private final StructureService strService;
    private final AccountTokenDAO accountTokenDAO;
    private final IBrokerMessageSender<Object> brokerMessageSender;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;

    @Override @Transactional
    public ReadUserDTO createUser(CreateUserDTO dto)
    {
        return createUserAfterValidation(userMapper.getAppUser(dto), UserEventTypes.CREATE_USER);
    }

    @Override
    public ReadUserDTO createTestUser(CreateUserDTO dto, String password)
    {
        AppUser user = userMapper.getAppUser(dto);
        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);
        user.setNotBlocked(true);
        user = userDAO.save(user);
        ReadUserDTO readUserDTO = userMapper.getReadUserDTO(user);
        brokerMessageSender.sendEvent(USER_TOPIC, UserEventTypes.CREATE_USER, user);
        return readUserDTO;
    }

    @Override @Transactional
    public ReadUserDTO createActiveUser(CreateActiveUserDTO dto)
    {
        AppUser user = userMapper.getAppUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActive(true);
        user.setNotBlocked(true);
        return createUserAfterValidation(user, UserEventTypes.CREATE_ACTIVE_USER);
    }

    @Override @Transactional
    public ReadUserDTO updateUser(UpdateUserDTO dto)
    {
        AppUser user = userDAO.findById(dto.getUserId()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        user.setEmail(dto.getEmail());
        user.setTel(dto.getTel());
        user = userDAO.save(user);
        ReadUserDTO readUserDTO = userMapper.getReadUserDTO(user);
        brokerMessageSender.sendEvent(USER_TOPIC, UserEventTypes.SIMPLE_UPDATE, user);
        return readUserDTO;
    }

    @Override @Transactional
    public ReadUserDTO changePassword(ChangePasswordDTO dto)
    {
        AppUser user = userDAO.findById(dto.getUserId()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user = userDAO.save(user);
        ReadUserDTO readUserDTO = userMapper.getReadUserDTO(user);
        brokerMessageSender.sendEvent(USER_TOPIC, UserEventTypes.CHANGE_PASSWORD, user);
        return readUserDTO;
    }

    @Override @Transactional
    public ReadUserDTO changeUsername(ChangeUsernameDTO dto)
    {
        AppUser user = userDAO.findById(dto.getUserId()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        user.setUsername(dto.getNewUsername());
        user = userDAO.save(user);
        ReadUserDTO readUserDTO = userMapper.getReadUserDTO(user);
        brokerMessageSender.sendEvent(USER_TOPIC, UserEventTypes.CHANGE_USERNAME, user);
        return readUserDTO;
    }

    @Override @Transactional
    public ReadUserDTO reinitialisePassword(ReinitialisePasswordDTO dto)
    {
        AppUser user = userDAO.findByUsername(dto.getUsername()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user = userDAO.save(user);
        ReadUserDTO readUserDTO = userMapper.getReadUserDTO(user);
        brokerMessageSender.sendEvent(USER_TOPIC, UserEventTypes.REINITIALISE_PASSWORD, user);
        return readUserDTO;
    }

    @Override @Transactional
    public ReadUserDTO activateAccount(ActivateAccountDTO dto)
    {
        AppUser user = userDAO.findByUsername(dto.getUsername()).orElseThrow(()->new AppException(USERNAME_NOT_FOUND_ERROR_MSG));
        user.setActive(true);
        user.setNotBlocked(true);
        user.setLastModificationDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = userDAO.save(user);
        AccountToken token = accountTokenDAO.findByToken(dto.getActivationToken()).orElseThrow(()->new AppException(INVALID_ACTIVATION_TOKEN_ERROR_MSG));
        token.setUsageDate(LocalDateTime.now());
        token.setAlreadyUsed(true);
        accountTokenDAO.save(token);
        ReadUserDTO readUserDTO = userMapper.getReadUserDTO(user);
        brokerMessageSender.sendEvent(USER_TOPIC, ACTIVATE_ACCOUNT, user);
        return readUserDTO;
    }

    @Override
    public ReadUserDTO findByUsername(String username)
    {
        AppUser user = userDAO.findByUsername(username).orElse(null);
        return user == null ? null : userMapper.getReadUserDTO(user);
    }

    @Override
    public ReadUserDTO findByEmail(String email)
    {
        AppUser user = userDAO.findByEmail(email).orElse(null);
        return user == null ? null : userMapper.getReadUserDTO(user);
    }

    @Override
    public ReadUserDTO findByTel(String tel)
    {
        AppUser user = userDAO.findByTel(tel).orElse(null);
        return user == null ? null : userMapper.getReadUserDTO(user);
    }

    @Override
    public Page<ReadUserDTO> getUserPage(PageRequest request)
    {
        Page<AppUser> userPage = userDAO.getUsersPage(request);
        List<ReadUserDTO> readUserDTOS = userPage.stream().map(userMapper::getReadUserDTO).collect(Collectors.toList());
        return new PageImpl<>(readUserDTOS, request, userPage.getTotalElements());
    }

    @Override
    public List<ReadUserDTO> findUsersByStructureRecursive(Long strId)
    {
        return !strDAO.existsById(strId) ? Collections.emptyList() : strService.getStructureChildrenStream(strId).flatMap(str->userDAO.findByStructure(str.getStrId()).stream()).map(userMapper::getReadUserDTO).collect(Collectors.toList());
    }

    @Override
    public List<ReadUserDTO> searchUsersByStructureRecursive(Long strId, String searchKey)
    {
        return findUsersByStructureRecursive(strId).stream()
                .filter(user->user.getUsername().toUpperCase(Locale.ROOT).contains(searchKey.toUpperCase(Locale.ROOT)) ||
                        user.getEmail().toUpperCase(Locale.ROOT).contains(searchKey.toUpperCase(Locale.ROOT)) ||
                        user.getTel().toUpperCase(Locale.ROOT).contains(searchKey.toUpperCase(Locale.ROOT)) ||
                        user.getUserId().toString().equals(searchKey.toUpperCase(Locale.ROOT))
                       )
                .collect(Collectors.toList());
    }

    @Override
    public void resendAccountActivationEmail(String username, String email)
    {
        AppUser loadedUser = this.userDAO.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(SecurityErrorMsg.USERNAME_NOT_FOUND_ERROR_MSG));
        if(!loadedUser.getEmail().equals(email)) {throw new AppException(SecurityErrorMsg.INVALID_USERNAME_OR_EMAIL_ERROR_MSG);}
        if(loadedUser.isActive() && loadedUser.isNotBlocked()) {throw new AppException(SecurityErrorMsg.ALREADY_ACTIVATED_ACCOUNT_ERROR_MSG);}
        AccountToken accountToken = accountTokenService.createAccountToken(loadedUser);

        EventActorIdentifier eventIdentifier = scm.getEventActorIdFromSCM();
        EmailNotification emailNotification = new EmailNotification(SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, loadedUser, eventIdentifier, accountToken.getToken());
        MutatedEventPayload<EmailEventTypes, EmailNotification> emailRequestedEvent = new MutatedEventPayload<>(EmailEventTypes.ACTIVATE_ACCOUNT_REQUEST, emailNotification, eventIdentifier);

        this.brokerMessageSender.sendNotificationEvent(emailRequestedEvent);
    }

    @Override
    public void sendReinitialisePasswordEmail(String username, String email)
    {
        AppUser loadedUser = this.userDAO.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(SecurityErrorMsg.USERNAME_NOT_FOUND_ERROR_MSG));
        if(!loadedUser.getEmail().equals(email)) {throw new AppException(SecurityErrorMsg.INVALID_USERNAME_OR_EMAIL_ERROR_MSG);}

        AccountToken accountToken = accountTokenService.createAccountToken(loadedUser);

        EventActorIdentifier eventIdentifier = scm.getEventActorIdFromSCM();
        EmailNotification emailNotification = new EmailNotification(SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, loadedUser, eventIdentifier, accountToken.getToken());
        MutatedEventPayload<EmailEventTypes, EmailNotification> emailRequestedEvent = new MutatedEventPayload(EmailEventTypes.REINITIALISE_PASSWORD_REQUEST, emailNotification, eventIdentifier);

        this.brokerMessageSender.sendNotificationEvent(emailRequestedEvent);
    }

    @Override
    public ResponseEntity<Void> clickAccountActivationLink(String token)
    {
        Optional<AccountToken> accountToken$ = accountTokenDAO.findByToken(token);
        if(!accountToken$.isPresent()) throw new AppException(UNKNOWN_ACCOUNT_TOKEN_ERROR_MSG);
        brokerMessageSender.sendEvent(TOKEN_TOPIC, UserEventTypes.CLICK_ACTIVATE_ACCOUNT_LINK, token, false);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(env.getProperty("client.server.address") + "/activate-account?token="+token));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Override
    public ResponseEntity<Void> clickReinitialisePasswordLink(String token)
    {
        Optional<AccountToken> accountToken$ = accountTokenDAO.findByToken(token);
        if(!accountToken$.isPresent()) throw new AppException(UNKNOWN_ACCOUNT_TOKEN_ERROR_MSG);
        brokerMessageSender.sendEvent(TOKEN_TOPIC, UserEventTypes.CLICK_REINITIALISE_PASSWORD_LINK, token, false);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(env.getProperty("client.server.address") + "/reinitialise-password?token="+token));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    private ReadUserDTO createUserAfterValidation(AppUser user, UserEventTypes eventType)
    {
        user = userDAO.save(user);
        brokerMessageSender.sendEvent(USER_TOPIC,eventType, user);

        EventActorIdentifier identifier = scm.getEventActorIdFromSCM();
        String token = UUID.randomUUID().toString();
        AccountToken accountToken = new AccountToken(token, user);
        accountTokenDAO.save(accountToken);
        EmailNotification emailNotification = new EmailNotification(SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, user, identifier, token);

        MutatedEventPayload<EmailEventTypes, EmailNotification> emailRequestedEvent = new MutatedEventPayload(EmailEventTypes.ACTIVATE_ACCOUNT_REQUEST, emailNotification, identifier);

        brokerMessageSender.sendNotificationEvent(emailRequestedEvent);
        return userMapper.getReadUserDTO(user);
    }
}
