package dgmp.sigrh.auth2.model.dtos.asignation;

import dgmp.sigrh.auth2.controller.repositories.*;
import dgmp.sigrh.auth2.model.entities.PrincipalAss;
import dgmp.sigrh.auth2.model.entities.PrvAss;
import dgmp.sigrh.auth2.model.entities.PrvToRoleAss;
import dgmp.sigrh.auth2.model.entities.RoleAss;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.AssignationEventTypes;
import dgmp.sigrh.auth2.model.histo.PrincipalAssHisto;
import dgmp.sigrh.auth2.model.histo.PrvAssHisto;
import dgmp.sigrh.auth2.model.histo.PrvToRoleAssHisto;
import dgmp.sigrh.auth2.model.histo.RoleAssHisto;
import dgmp.sigrh.structuremodule.controller.service.str.IHierarchySiglesGenerator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AssignationMapper {
    @Autowired protected UserRepo userRepo;
    @Autowired protected RoleRepo roleRepo;
    @Autowired protected PrvRepo prvRepo;
    @Autowired protected PrvToRoleAssRepo prvToRoleAssRepo;
    @Autowired protected  RoleAssRepo roleAssRepo;
    @Autowired protected PrvAssRepo prvAssRepo;
    @Autowired protected IHierarchySiglesGenerator hierarchySiglesGenerator;

    @Mapping(target = "role", expression = "java(dto.getRoleId() == null ? null : new dgmp.sigrh.auth2.model.entities.AppRole(dto.getRoleId()))")
    @Mapping(target = "privilege", expression = "java(new dgmp.sigrh.auth2.model.entities.AppPrivilege(dto.getPrivilegeId()))")
    @Mapping(target = "ass.startsAt", source = "startsAt")
    @Mapping(target = "ass.endsAt", source = "endsAt")
    public abstract PrvToRoleAss mapToPrvToRoleAss(PrvToRoleDTO dto);



    @Mapping(target = "privilege", expression = "java(prvRepo.findById(dto.getPrivilegeId()).orElse(null))")
    @Mapping(target = "role", expression = "java(roleRepo.findById(dto.getRoleId()).orElse(null))")
    public abstract PrvToRoleAss mapToFullPrvToRoleAss(PrvToRoleDTO dto);

    public abstract PrincipalAssHisto mapToPrincipalAssHisto(PrincipalAss principalAss, AssignationEventTypes eventType, EventActorIdentifier eai);

    public abstract RoleAssHisto mapToRoleAssHisto(RoleAss roleAss, AssignationEventTypes eventType, EventActorIdentifier eai);

    public abstract PrvAssHisto mapToPrvAssHisto(PrvAss prvAss, AssignationEventTypes eventType, EventActorIdentifier eai);

    public abstract PrvToRoleAssHisto mapToPrvToRoleAssHisto(PrvToRoleAss prvToRoleAss, AssignationEventTypes eventType, EventActorIdentifier eai);


    @Mapping(target = "user", expression = "java(dto.getUserId() == null ? null : new dgmp.sigrh.auth2.model.entities.AppUser(dto.getUserId()))")
    @Mapping(target = "structure", expression = "java(dto.getStrId() == null ? null : new dgmp.sigrh.structuremodule.model.entities.structure.Structure(dto.getStrId()))")
    @Mapping(target = "ass.startsAt", source = "startsAt")
    @Mapping(target = "ass.endsAt", source = "endsAt")
    @Mapping(target = "ass.assStatus", expression = "java(2)")
    public abstract PrincipalAss mapToPrincipalAss(CreatePrincipalAssDTO dto);

    @Mapping(target = "startsAt", source = "ass.startsAt")
    @Mapping(target = "endsAt", source = "ass.endsAt")
    @Mapping(target = "assStatus", source = "ass.assStatus")
    @Mapping(target = "username", source = "ass.user.username")
    @Mapping(target = "strName", source = "ass.structure.strName")
    @Mapping(target = "strSigle", source = "ass.structure.strSigle")
    @Mapping(target = "hierarchySigles", expression = "java(hierarchySiglesGenerator.getHierarchySigles(ass.getStructure().getStrId()))")
    public abstract ReadPrincipalAssDTO mapToReadPrincipalAssDTO(PrincipalAss ass);

    @Mapping(target = "principalAssId", source = "assId")
    public abstract SetAuthoritiesToPrincipalAssDTO mapTo_SetAuthoritiesToPrincipalAssDTO(UpdatePrincipalAssDTO dto);

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "strId", source = "structure.strId")
    @Mapping(target = "startsAt", source = "ass.startsAt")
    @Mapping(target = "endsAt", source = "ass.endsAt")
    public abstract UpdatePrincipalAssDTO mapToUpdatePrincipalAssDTO(PrincipalAss ass);



    public RoleAssSpliterDTO mapToRoleAssSpliterDTO(Set<Long> roleIds, Long principalAssId, LocalDate startsAt, LocalDate endsAt, boolean removable)
    {
        //On l'a mais non pr??sent dans les id envoy??s
        Set<Long> roleIdsToBeRemoved = !removable ? new HashSet<>() :
        roleAssRepo.findActiveRoleIdsBelongingToPrincipalAss(principalAssId).stream().filter(id0->roleIds.stream().noneMatch(id1->id0.equals(id1))).collect(Collectors.toSet()); //

        //On ne l'a pas mais pr??sent dans les id envoy??s
        Set<Long> roleIdsToBeAddedAsNew = roleAssRepo.findRoleIdsNotBelongingToPrincipalAss(principalAssId).stream().filter(id0->roleIds.stream().anyMatch(id1->id0.equals(id1))).collect(Collectors.toSet());

        //On l'a, il est actif mais les dates ne sont pas identiques (pr??sent dans les id envoy??s)
        Set<Long> roleIdsToChangeTheDates = roleAssRepo.findActiveRoleIdsBelongingToPrincipalAss_otherDates(principalAssId, startsAt, endsAt).stream().filter(id0->roleIds.stream().anyMatch(id1->id0.equals(id1))).collect(Collectors.toSet());

        //On l'a, il n'est pas actif et les dates ne sont pas identiques (pr??sent dans les id envoy??s)
        Set<Long> roleIdsToActivateAndChangeTheDates = roleAssRepo.findNoneActiveRoleIdsBelongingToPrincipalAss_otherDates(principalAssId , startsAt, endsAt).stream().filter(id0->roleIds.stream().anyMatch(id1->id0.equals(id1))).collect(Collectors.toSet());

        //On l'a, il n'est pas actif mais les dates sont pas identiques (pr??sent dans les id envoy??s)
        Set<Long> roleIdsToActivate = roleAssRepo.findNoneActiveRoleIdsBelongingToPrincipalAss_sameDates(principalAssId , startsAt, endsAt).stream().filter(id0->roleIds.stream().anyMatch(id1->id0.equals(id1))).collect(Collectors.toSet());

        return new RoleAssSpliterDTO(roleIdsToBeRemoved, roleIdsToBeAddedAsNew, roleIdsToChangeTheDates, roleIdsToActivateAndChangeTheDates, roleIdsToActivate);
    }

    public PrvAssSpliterDTO mapToPrvAssSpliterDTO(Set<Long> prvIds, Set<Long> roleIds, Long principalAssId, LocalDate startsAt, LocalDate endsAt, boolean removable)
    {
        Set<Long> prvIdsToBeRemoved = !removable ? new HashSet<>() :
                prvAssRepo.getPrincipalAssPrvIds(principalAssId).stream().filter(prvId->!prvIds.contains(prvId) && (roleIds.stream().anyMatch(roleId->prvToRoleAssRepo.roleHasPrivilege(roleId, prvId)) || prvAssRepo.existsByPrincipalAssAndPrvAndStatus(principalAssId, prvId, 1))).collect(Collectors.toSet()); //

        Set<Long> prvIdsToBeAddedAsNew = prvIds.stream().filter(prvId->!prvAssRepo.existsByPrincipalAssAndPrv(principalAssId, prvId) && roleIds.stream().noneMatch(roleId->prvToRoleAssRepo.roleHasPrivilege(roleId, prvId))).collect(Collectors.toSet());

        Set<Long> prvIdsToChangeTheDates = prvIds.stream().filter(prvId->prvAssRepo.existsActiveByPrincipalAssAndPrv_OtherDates(principalAssId, prvId, startsAt, endsAt) && roleIds.stream().noneMatch(roleId->prvToRoleAssRepo.roleHasPrivilege(roleId, prvId))).collect(Collectors.toSet());

        Set<Long> prvIdsToActivateAndChangeTheDates = prvIds.stream().filter(prvId->prvAssRepo.existsNoneActiveByPrincipalAssAndPrv_OtherDates(principalAssId, prvId, startsAt, endsAt)).collect(Collectors.toSet());

        Set<Long> prvIdsToActivate = prvIds.stream().filter(prvId->prvAssRepo.existsNoneActiveByPrincipalAssAndPrv_SameDates(principalAssId, prvId, startsAt, endsAt)).collect(Collectors.toSet());

        return new PrvAssSpliterDTO(prvIdsToBeRemoved, prvIdsToBeAddedAsNew, prvIdsToChangeTheDates, prvIdsToActivateAndChangeTheDates, prvIdsToActivate);
    }

    public RoleAssSpliterDTO mapToRoleAssSpliterDTO(Set<Long> roleIds, Long principalAssId, LocalDate startsAt, LocalDate endsAt)
    {

        return this.mapToRoleAssSpliterDTO(roleIds, principalAssId, startsAt, endsAt, false);
    }
}