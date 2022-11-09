package dgmp.sigrh.auth2.controller.services.impl.withoutbroker;

import dgmp.sigrh.auth2.controller.repositories.*;
import dgmp.sigrh.auth2.controller.services.spec.IAssignationService;
import dgmp.sigrh.auth2.model.dtos.asignation.*;
import dgmp.sigrh.auth2.model.entities.*;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.AssignationEventTypes;
import dgmp.sigrh.auth2.model.histo.PrincipalAssHisto;
import dgmp.sigrh.auth2.model.histo.PrvAssHisto;
import dgmp.sigrh.auth2.model.histo.PrvToRoleAssHisto;
import dgmp.sigrh.auth2.model.histo.RoleAssHisto;
import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignationService implements IAssignationService
{
    private final AssignationMapper assMapper;
    private final PrvToRoleAssRepo prvToRoleAssRepo;
    private final PrvToRoleAssHistoRepo prvToRoleAssHistoRepo;
    private final PrvAssRepo prvAssRepo;
    private final PrvAssHistoRepo prvAssHistoRepo;
    private final PrincipalAssRepo principalAssRepo;
    private final PrincipalAssHistoRepo principalAssHistoRepo;
    private final RoleAssRepo roleAssRepo;
    private final RoleAssHistoRepo roleAssHistoRepo;
    private final ISecurityContextManager scm;


    @Override @Transactional
    public PrincipalAss CreatePrincipalAss(CreatePrincipalAssDTO dto)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        PrincipalAss principalAss = assMapper.mapToPrincipalAss(dto);
        principalAss = principalAssRepo.save(principalAss);
        PrincipalAssHisto histo = assMapper.mapToPrincipalAssHisto(principalAss, AssignationEventTypes.PRINCIPAL_ASSIGNATION_CREATED, eai);
        principalAssHistoRepo.save(histo);

        Long principalAssId = principalAss.getAssId();
        dto.getRoleIds().forEach(id->
        {
            RoleAss roleAss = new RoleAss();
            roleAss.setAss(new Ass(1, dto.getStartsAt(), dto.getEndsAt()));
            roleAss.setPrincipalAss(new PrincipalAss(principalAssId));
            roleAss.setRole(new AppRole(id));
            roleAss = roleAssRepo.save(roleAss);

            RoleAssHisto roleAssHisto = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.ROLE_ASSIGNED_TO_PRINCIPAL_ASS, eai);
            roleAssHistoRepo.save(roleAssHisto);
        });
        this.treatPrvsAssignation(dto.getRoleIds(), dto.getPrvIds(), principalAssId, dto.getStartsAt(), dto.getEndsAt(), eai);
        return principalAss;
    }



    @Override @Transactional
    public void setPrincipalAssAsDefault(Long principalAssId)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        PrincipalAss principalAss  = principalAssRepo.findById(principalAssId).orElse(null);
        if(principalAss == null) return;
        principalAssRepo.findActiveByUser(principalAss.getUser().getUserId()).forEach(prAss->
        {
            if(!prAss.getAssId().equals(principalAssId))
            {
                prAss.setAss(new Ass(2, prAss.getAss().getStartsAt(), prAss.getAss().getEndsAt()));
                PrincipalAssHisto principalAssHisto = assMapper.mapToPrincipalAssHisto(prAss, AssignationEventTypes.ASSIGNATION_SET_AS_NONE_DEFAULT, eai);
                principalAssHistoRepo.save(principalAssHisto);
            }

        });
        if(principalAss.getAss().getAssStatus() == 1) return;
        principalAss.setAss(new Ass(1, principalAss.getAss().getStartsAt(), principalAss.getAss().getEndsAt()));
        PrincipalAssHisto principalAssHisto = assMapper.mapToPrincipalAssHisto(principalAss, AssignationEventTypes.ASSIGNATION_SET_AS_DEFAULT, eai);
        principalAssHistoRepo.save(principalAssHisto);
    }

    @Override
    public void setPrincipalAssAuthorities(SetAuthoritiesToPrincipalAssDTO dto)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        PrincipalAss principalAss  = principalAssRepo.findById(dto.getPrincipalAssId()).orElse(null);
        if(principalAss == null) return;
        Long principalAssId = principalAss.getAssId();
        Set<Long> roleIds = dto.getRoleIds();
        LocalDate startsAt = dto.getStartsAt(); LocalDate endsAt = dto.getEndsAt();

        RoleAssSpliterDTO roleAssSpliterDTO = assMapper.mapToRoleAssSpliterDTO(roleIds, principalAssId, startsAt, endsAt, true);

        treatRolesAssignation(roleAssSpliterDTO, principalAssId, startsAt, endsAt, eai);

        this.treatPrvsAssignation(dto.getRoleIds(), dto.getPrvIds(), principalAssId, dto.getStartsAt(), dto.getEndsAt(), eai);
    }


    @Override
    public void addRolesToPrincipalAss(RolesAssDTO dto)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        PrincipalAss principalAss  = principalAssRepo.findById(dto.getPrincipalAssId()).orElse(null);
        if(principalAss == null) return;

        RoleAssSpliterDTO roleAssSpliterDTO = assMapper.mapToRoleAssSpliterDTO(dto.getRoleIds(), principalAss.getAssId(), dto.getStartsAt(), dto.getEndsAt());
        this.treatRolesAssignation(roleAssSpliterDTO, principalAss.getAssId(), dto.getStartsAt(), dto.getEndsAt(), eai);
    }

    @Override
    public void removeRolesToPrincipalAss(RolesAssDTO dto)
    {
        dto.getRoleIds().forEach(id->
        {
            RoleAss roleAss = roleAssRepo.findByPrincipalAssAndRole(dto.getPrincipalAssId(), id);
            if(roleAss != null) roleAss.setAss(new Ass(2, roleAss.getAss().getStartsAt(), roleAss.getAss().getEndsAt()));
            else
            {
                roleAss = new RoleAss();
                roleAss.setAss(new Ass(2, dto.getStartsAt(), dto.getEndsAt()));
                roleAss.setPrincipalAss(new PrincipalAss(dto.getPrincipalAssId()));
            }

            roleAss = roleAssRepo.save(roleAss);
            RoleAssHisto histo = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.ROLE_REVOKED_TO_PRINCIPAL_ASS, scm.getEventActorIdFromSCM());
            roleAssHistoRepo.save(histo);
        });
    }

    @Override
    public void addPrivilegesToPrincipalAss(PrvsAssDTO dto)
    {
        assignPrvToPrincipalAss(dto, 1);
    }

    @Override
    public void removePrivilegesToPrincipalAss(PrvsAssDTO dto)
    {
        assignPrvToPrincipalAss(dto, 2);
    }

    @Override
    public void revokePrivilegesToPrincipalAss(PrvsAssDTO dto)
    {
        assignPrvToPrincipalAss(dto, 3);
    }

    @Override
    public void restorePrivilegesToPrincipalAss(PrvsAssDTO dto)
    {
        dto.getPrvIds().forEach(id->
        {
            PrvAss prvAss = prvAssRepo.findByPrincipalAssAndPrv(dto.getPrincipalAssId(), id);
            if(prvAss == null) return;
            if(prvAss.getAss().getAssStatus() != 3) return;
            prvAss.setAss(new Ass(2, prvAss.getAss().getStartsAt(), prvAss.getAss().getEndsAt()));

            prvAss = prvAssRepo.save(prvAss);
            PrvAssHisto histo = assMapper.mapToPrvAssHisto(prvAss, AssignationEventTypes.PRIVILEGE_RESTORED_TO_PRINCIPAL_ASS, scm.getEventActorIdFromSCM());
            prvAssHistoRepo.save(histo);
        });
    }

    @Override @Transactional
    public void setRolePrivileges(PrvsToRoleDTO dto)
    {
        Long roleId = dto.getRoleId(); Set<Long> prvIds = dto.getPrvIds();
        LocalDate startsAt = dto.getStartsAt(); LocalDate endsAt =  dto.getEndsAt();
        Set<Long> prvIdsToBeRemoved = prvToRoleAssRepo.findPrvIdsForRoleNotIn(roleId, prvIds); //
        Set<Long> prvIdsToBeAdded = prvToRoleAssRepo.findPrvIdsNotBelongingToRoleIn(roleId, prvIds);
        //Set<Long> prvIdsToNotBeChanged = prvToRoleAssRepo.findActivePrvIdsForRoleIn_sameDates(roleId, prvIds, startsAt.toLocalDate(), endsAt.toLocalDate());
        Set<Long> prvIdsToChangeTheDates = prvToRoleAssRepo.findActivePrvIdsForRoleIn_otherDates(roleId, prvIds, startsAt, endsAt);
        Set<Long> prvIdsToActivateAndChangeTheDates = prvToRoleAssRepo.findNoneActivePrvIdsForRoleIn(roleId, prvIds);

        prvIdsToBeRemoved.forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(roleId, id);
            prvToRoleAss.setAss(new Ass(2, prvToRoleAss.getAss().getStartsAt(), prvToRoleAss.getAss().getEndsAt()));
            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.PRIVILEGE_REVOKED_TO_ROLE, scm.getEventActorIdFromSCM());
            prvToRoleAssHistoRepo.save(histo);
        });

        prvIdsToBeAdded.forEach(id->
        {
            PrvToRoleAss prvToRoleAss = new PrvToRoleAss(null, new Ass(1, startsAt, endsAt) , new AppPrivilege(id), new AppRole(roleId));
            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);

            PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_PRINCIPAL_ASS, scm.getEventActorIdFromSCM());
            prvToRoleAssHistoRepo.save(histo);
        });

        prvIdsToChangeTheDates.forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(roleId, id);
            prvToRoleAss.setAss(new Ass(prvToRoleAss.getAss().getAssStatus(), startsAt, endsAt));
            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.VALIDITY_PERIOD_CHANGED, scm.getEventActorIdFromSCM());
            prvToRoleAssHistoRepo.save(histo);
        });

        prvIdsToActivateAndChangeTheDates.forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(roleId, id);
            prvToRoleAss.setAss(new Ass(1, startsAt, endsAt));
            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.ASSIGNATION_ACTIVATED, scm.getEventActorIdFromSCM());
            prvToRoleAssHistoRepo.save(histo);
        });
    }

    @Override
    public void addPrivilegesToRole(PrvsToRoleDTO dto)
    {
        dto.getPrvIds().forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(dto.getRoleId(), id);
            if(prvToRoleAss != null)
            {
                if(1 == prvToRoleAss.getAss().getAssStatus()) return;
            }
            else
            {
                prvToRoleAss = new PrvToRoleAss();
                prvToRoleAss.setRole(new AppRole(dto.getRoleId()));
            }
            prvToRoleAss.setAss(new Ass(1, dto.getStartsAt(), dto.getEndsAt()));

            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_ROLE, scm.getEventActorIdFromSCM());
            prvToRoleAssHistoRepo.save(histo);
        });
    }

    @Override
    public void removePrivilegesToRole(PrvsToRoleDTO dto)
    {
        dto.getPrvIds().forEach(id->
        {
            PrvToRoleAss prvToRoleAss = prvToRoleAssRepo.findByRoleAndPrivilege(dto.getRoleId(), id);
            if(prvToRoleAss != null)
            {
                if(2 == prvToRoleAss.getAss().getAssStatus() || 3 == prvToRoleAss.getAss().getAssStatus()) return;
            }
            else
            {
                prvToRoleAss = new PrvToRoleAss();
                prvToRoleAss.setRole(new AppRole(dto.getRoleId()));
            }
            prvToRoleAss.setAss(new Ass(2, dto.getStartsAt(), dto.getEndsAt()));

            prvToRoleAss = prvToRoleAssRepo.save(prvToRoleAss);
            PrvToRoleAssHisto histo = assMapper.mapToPrvToRoleAssHisto(prvToRoleAss, AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_ROLE, scm.getEventActorIdFromSCM());
            prvToRoleAssHistoRepo.save(histo);
        });
    }

    private void treatRolesAssignation(RoleAssSpliterDTO roleAssSpliterDTO, Long principalAssId, LocalDate startsAt, LocalDate endsAt, EventActorIdentifier eai)
    {
        roleAssSpliterDTO.getRoleIdsToBeRemoved().forEach(id->
        {
            RoleAss roleAss = roleAssRepo.findByPrincipalAssAndRole(principalAssId, id);
            roleAss.setAss(new Ass(2, roleAss.getAss().getStartsAt(), roleAss.getAss().getEndsAt()));
            roleAss = roleAssRepo.save(roleAss);
            RoleAssHisto histo = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.ROLE_REVOKED_TO_PRINCIPAL_ASS, eai);
            roleAssHistoRepo.save(histo);
        });

        roleAssSpliterDTO.getRoleIdsToBeAddedAsNew().forEach(id->
        {
            RoleAss roleAss = new RoleAss();
            roleAss.setAss(new Ass(1, roleAss.getAss().getStartsAt(), roleAss.getAss().getEndsAt()));
            roleAss.setRole(new AppRole(id));
            roleAss.setPrincipalAss(new PrincipalAss(principalAssId));
            roleAss = roleAssRepo.save(roleAss);
            RoleAssHisto histo = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.ROLE_ASSIGNED_TO_PRINCIPAL_ASS, eai);
            roleAssHistoRepo.save(histo);
        });

        roleAssSpliterDTO.getRoleIdsToChangeTheDates().forEach(id->
        {
            RoleAss roleAss = roleAssRepo.findByPrincipalAssAndRole(principalAssId, id);
            roleAss.setAss(new Ass(roleAss.getAss().getAssStatus(), startsAt, endsAt));
            roleAss = roleAssRepo.save(roleAss);
            RoleAssHisto histo = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.VALIDITY_PERIOD_CHANGED, eai);
            roleAssHistoRepo.save(histo);
        });

        roleAssSpliterDTO.getRoleIdsToActivate().forEach(id->
        {
            RoleAss roleAss = roleAssRepo.findByPrincipalAssAndRole(principalAssId, id);
            roleAss.setAss(new Ass(1, roleAss.getAss().getStartsAt(), roleAss.getAss().getEndsAt()));
            roleAss = roleAssRepo.save(roleAss);
            RoleAssHisto histo = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.ASSIGNATION_ACTIVATED, eai);
            roleAssHistoRepo.save(histo);
        });

        roleAssSpliterDTO.getRoleIdsToActivateAndChangeTheDates().forEach(id->
        {
            RoleAss roleAss = roleAssRepo.findByPrincipalAssAndRole(principalAssId, id);
            roleAss.setAss(new Ass(1, startsAt, endsAt));
            roleAss = roleAssRepo.save(roleAss);
            RoleAssHisto histo = assMapper.mapToRoleAssHisto(roleAss, AssignationEventTypes.ASSIGNATION_ACTIVATED_AND_VALIDITY_PERIOD_CHANGED, eai);
            roleAssHistoRepo.save(histo);
        });
    }

    private void treatPrvsAssignation(Set<Long> roleIds, Set<Long> prvIds, Long principalAssId, LocalDate startsAt, LocalDate endsAt, EventActorIdentifier eai)
    {
        Set<Long> rolesPrvIds = prvToRoleAssRepo.findActivePrvIdsForRoles(roleIds);
        Set<Long> prvIdsToBeAdded = prvIds.stream().filter(prvId0->rolesPrvIds.stream().noneMatch(prvId1-> Objects.equals(prvId0, prvId1))).collect(Collectors.toSet());
        Set<Long> prvIdsToBeRevoked = rolesPrvIds.stream().filter(id0-> prvIds.stream().noneMatch(id1->Objects.equals(id0, id1))).collect(Collectors.toSet());

        prvIdsToBeAdded.forEach(id->
        {
            PrvAss prvAss = new PrvAss();
            prvAss.setAss(new Ass(1, startsAt, endsAt));
            prvAss.setPrincipalAss(new PrincipalAss(principalAssId));
            prvAss.setPrv(new AppPrivilege(id));
            prvAss = prvAssRepo.save(prvAss);

            PrvAssHisto prvAssHisto = assMapper.mapToPrvAssHisto(prvAss, AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_PRINCIPAL_ASS, eai);
            prvAssHistoRepo.save(prvAssHisto);
        });

        prvIdsToBeRevoked.forEach(id->
        {
            PrvAss prvAss = new PrvAss();
            prvAss.setAss(new Ass(3, startsAt, endsAt));
            prvAss.setPrincipalAss(new PrincipalAss(principalAssId));
            prvAss.setPrv(new AppPrivilege(id));
            prvAss = prvAssRepo.save(prvAss);

            PrvAssHisto prvAssHisto = assMapper.mapToPrvAssHisto(prvAss, AssignationEventTypes.PRIVILEGE_REVOKED_TO_PRINCIPAL_ASS, eai);
            prvAssHistoRepo.save(prvAssHisto);
        });
    }

    private void assignPrvToPrincipalAss(PrvsAssDTO dto, int status)
    {
        dto.getPrvIds().forEach(id->
        {
            PrvAss prvAss = prvAssRepo.findByPrincipalAssAndPrv(dto.getPrincipalAssId(), id);
            if(prvAss != null)
            {
                if(status == prvAss.getAss().getAssStatus()) return;
                prvAss.setAss(new Ass(status, prvAss.getAss().getStartsAt(), prvAss.getAss().getEndsAt()));
            }
            else
            {
                prvAss = new PrvAss();
                prvAss.setAss(new Ass(status, dto.getStartsAt(), dto.getEndsAt()));
                prvAss.setPrincipalAss(new PrincipalAss(dto.getPrincipalAssId()));
            }
            AssignationEventTypes eventType = status == 1 ? AssignationEventTypes.PRIVILEGE_ASSIGNED_TO_PRINCIPAL_ASS :
                    status == 2 ? AssignationEventTypes.PRIVILEGE_REMOVED_TO_PRINCIPAL_ASS : AssignationEventTypes.PRIVILEGE_REVOKED_TO_PRINCIPAL_ASS;
            prvAss = prvAssRepo.save(prvAss);
            PrvAssHisto histo = assMapper.mapToPrvAssHisto(prvAss, eventType, scm.getEventActorIdFromSCM());
            prvAssHistoRepo.save(histo);
        });
    }
}
