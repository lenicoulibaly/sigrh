package dgmp.sigrh.auth.controller.services.impl.withoutbroker;

import dgmp.sigrh.auth.controller.repositories.*;
import dgmp.sigrh.auth.controller.services.spec.IAssignationService;
import dgmp.sigrh.auth.controller.validators.Exceptions.AppException;
import dgmp.sigrh.auth.model.dtos.asignation.*;
import dgmp.sigrh.auth.model.entities.Assignation;
import dgmp.sigrh.auth.model.entities.PrivilegeToRoleAss;
import dgmp.sigrh.auth.model.entities.PrivilegeToUserAss;
import dgmp.sigrh.auth.model.entities.RoleToUserAss;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Profile("withoutbroker")
public class AssignationService implements IAssignationService
{
    private final UserDAO userRepo;
    private final AssignationMapper assMapper;
    private final RoleToUserAssRepo rtuRepo;
    private final PrivilegeToUserAssRepo ptuRepo;
    private final PrivilegeToRoleAssRepo ptrRepo;
    private final PrivilegeDAO prvRepo;
    //private final IBrokerMessageSender<Assignation> messageSender;
    //private final IBrokerMessageSender<AppUser> userMessageSender;

    @Override @Transactional
    public Assignation assignRoleToUserAsDefault(RoleToUserDTO dto)
    {
        Assignation roleToUserAss = this.assignRoleToUser(dto);
        return this.setAssignationAsDefaultForUser(roleToUserAss.getAssignationId());
    }
    @Override @Transactional
    public Assignation assignRoleToUser(RoleToUserDTO dto)
    {
        Long userId = dto.getUserId(); Long roleId = dto.getRoleId(); Long strId = dto.getStrId();
        RoleToUserAss roleToUserAss;

        if(rtuRepo.existsByUserAndRoleAndStr(userId, roleId, strId)) //Si l'assignation existe on la charge dépuis la base
        {
            roleToUserAss = rtuRepo.findByUserAndRoleAndStr(userId, roleId, strId);
            int oldAssStatus = roleToUserAss.getAssStatus(); LocalDateTime oldEndsAt = roleToUserAss.getEndsAt();
            roleToUserAss.setEndsAt(dto.getEndsAt());
            roleToUserAss.setAssStatus(oldAssStatus == 3 ? 2 : oldAssStatus); //Si l'assignation était une révocation on la passe à une assignation inactive, sinon on garde l'acien status;

            if(oldAssStatus != roleToUserAss.getAssStatus() || !oldEndsAt.isEqual(dto.getEndsAt())) //Si l'assignation a été modifiée on met sa date de dernière modif à maintenant
            {
                roleToUserAss.setLastModificationDate(LocalDateTime.now());
                roleToUserAss = rtuRepo.save(roleToUserAss);
                //messageSender.sendEvent(ASSIGNATION_TOPIC, AssignationEventTypes.ASSIGNATION_UPDATED, roleToUserAss);
            }
        }
        else //Sinon on crée une nouvelle assignation de role à partir du mapper et du dto
        {
            roleToUserAss = assMapper.mapToRoleToUserAss(dto);
            roleToUserAss.setAssStatus(2);
            roleToUserAss.setCreationDate(LocalDateTime.now());
            roleToUserAss.setLastModificationDate(LocalDateTime.now());
            roleToUserAss = rtuRepo.save(roleToUserAss);
            //messageSender.sendEvent(ASSIGNATION_TOPIC, AssignationEventTypes.ROLE_ASSIGNED_TO_USER, roleToUserAss);
        }
        return roleToUserAss;
    }

    @Override @Transactional
    public Assignation assignPrivilegeToUser(PrvToUserDTO dto)
    {
        Long userId = dto.getUserId(); Long prvId = dto.getPrivilegeId(); Long strId = dto.getStrId();
        PrivilegeToUserAss privilegeToUserAss = ptuRepo.existsByUserAndPrvAndStr(userId,prvId, strId) ?
                ptuRepo.findByUserAndPrvAndStr(userId,prvId, strId) :
                assMapper.mapToPrvToUserAss(dto);

        privilegeToUserAss.setAssStatus(1);
        privilegeToUserAss.setEndsAt(dto.getEndsAt());
        privilegeToUserAss = ptuRepo.save(privilegeToUserAss);
        //messageSender.sendEvent(ASSIGNATION_TOPIC, AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_USER, privilegeToUserAss);
        return privilegeToUserAss;
    }

    @Override @Transactional
    public Assignation assignPrivilegeToRole(PrvToRoleDTO dto)
    {
        Long roleId = dto.getRoleId(); Long prvId = dto.getPrivilegeId();
        PrivilegeToRoleAss privilegeToRoleAss = ptrRepo.existsByRoleAndPrv(roleId,prvId) ?
                ptrRepo.findByRoleAndPrv(roleId,prvId) :
                assMapper.mapToPrvToRoleAss(dto);

        privilegeToRoleAss.setAssStatus(1);
        privilegeToRoleAss = ptrRepo.save(privilegeToRoleAss);
        //messageSender.sendEvent(ASSIGNATION_TOPIC, AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_ROLE, privilegeToRoleAss);
        return privilegeToRoleAss;
    }

    @Override
    public void assignPrivilegesToUser(PrvsToUserDTO dto)
    {
        assMapper.mapToSetOfPrvToUserDTO(dto).forEach(ptuDto->
        {
            if(prvRepo.existsById(ptuDto.getPrivilegeId())) this.assignPrivilegeToUser(ptuDto);
        });
    }

    @Override
    public void assignPrivilegesToRole(PrvsToRoleDTO dto) {
        assMapper.mapToSetOfPrvToRoleDTO(dto).forEach(ptrDto->
        {
            if(prvRepo.existsById(ptrDto.getPrivilegeId())) this.assignPrivilegeToRole(ptrDto);
        });
    }


    @Override @Transactional
    public Assignation revokePrivilegeToUser(PrvToUserDTO dto)
    {
        Long userId = dto.getUserId(); Long prvId = dto.getPrivilegeId(); Long strId = dto.getStrId();
        PrivilegeToUserAss privilegeToUserAss = ptuRepo.existsByUserAndPrvAndStr(userId,prvId, strId) ?
                ptuRepo.findByUserAndPrvAndStr(userId,prvId, strId) :
                assMapper.mapToPrvToUserAss(dto);

        privilegeToUserAss.setAssStatus(3);
        privilegeToUserAss = ptuRepo.save(privilegeToUserAss);
        //messageSender.sendEvent(ASSIGNATION_TOPIC, AssignationEventTypes.PRIVILEGE_REVOKED_TO_USER, privilegeToUserAss);
        return privilegeToUserAss;
    }

    @Override @Transactional
    public Assignation revokePrivilegeToRole(PrvToRoleDTO dto)
    {
        Long roleId = dto.getRoleId(); Long prvId = dto.getPrivilegeId();
        PrivilegeToRoleAss privilegeToRoleAss = ptrRepo.existsByRoleAndPrv(roleId,prvId) ?
                ptrRepo.findByRoleAndPrv(roleId,prvId) :
                assMapper.mapToPrvToRoleAss(dto);

        privilegeToRoleAss.setAssStatus(3);
        privilegeToRoleAss = ptrRepo.save(privilegeToRoleAss);
        //messageSender.sendEvent(ASSIGNATION_TOPIC, AssignationEventTypes.PRIVILEGE_REVOKED_TO_USER, privilegeToRoleAss);
        return privilegeToRoleAss;
    }

    @Override @Transactional
    public Assignation revokeRoleToUser(RoleToUserDTO dto)
    {
        Long userId = dto.getUserId(); Long roleId = dto.getRoleId(); Long strId = dto.getStrId();
        RoleToUserAss roleToUserAss = rtuRepo.existsByUserAndRoleAndStr(userId, roleId, strId) ?
                rtuRepo.findByUserAndRoleAndStr(userId, roleId, strId) :
                assMapper.mapToRoleToUserAss(dto);

        roleToUserAss.setAssStatus(3);
        roleToUserAss = rtuRepo.save(roleToUserAss);
        //messageSender.sendEvent(ASSIGNATION_TOPIC, AssignationEventTypes.ROLE_REVOKED_TO_USER, roleToUserAss);
        return roleToUserAss;
    }

    @Override @Transactional
    public Assignation setAssignationAsDefaultForUser(Long assId)
    {
        RoleToUserAss rtuAss = rtuRepo.findById(assId).orElseThrow(()->new AppException("Invalid Assignation ID"));
        if(rtuAss.getAssStatus() != 1)
        {
            rtuRepo.getUsersActiveRoleAss(rtuAss.getUser().getUserId()).forEach(
                rtu->
                {
                    rtu.setAssStatus(2);
                    rtu.setLastModificationDate(LocalDateTime.now());
                    rtuRepo.save(rtu);
                    //messageSender.sendEvent(ASSIGNATION_TOPIC, AssignationEventTypes.ASSIGNATION_SET_AS_NONE_DEFAULT, rtu);
                });
            rtuAss.setAssStatus(1);
            rtuAss.setLastModificationDate(LocalDateTime.now());
            rtuAss = rtuRepo.save(rtuAss);
            //messageSender.sendEvent(ASSIGNATION_TOPIC, AssignationEventTypes.ASSIGNATION_SET_AS_DEFAULT, rtuAss);

            userRepo.changeDefaultAssId(rtuAss.getUser().getUserId(), rtuAss.getAssignationId());
            //userMessageSender.sendEvent(USER_TOPIC, UserEventTypes.CHANGE_DEFAULT_ASSIGNATION_ID, rtuAss.getUser());
        }
        return rtuAss;
    }
}
