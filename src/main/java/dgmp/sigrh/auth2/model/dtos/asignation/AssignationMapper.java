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

    public RoleAssSpliterDTO mapToRoleAssSpliterDTO(Set<Long> roleIds, Long principalAssId, LocalDate startsAt, LocalDate endsAt, boolean removable)
    {
        //On l'a mais non présent dans les id envoyés
        Set<Long> roleIdsToBeRemoved = removable ? new HashSet<>() :
        roleAssRepo.findActiveRoleIdsBelongingToPrincipalAss(principalAssId).stream().filter(id0->roleIds.stream().noneMatch(id1->id0.equals(id1))).collect(Collectors.toSet()); //

        //On ne l'a pas mais présent dans les id envoyés
        Set<Long> roleIdsToBeAddedAsNew = roleAssRepo.findRoleIdsNotBelongingToPrincipalAss(principalAssId).stream().filter(id0->roleIds.stream().anyMatch(id1->id0.equals(id1))).collect(Collectors.toSet());

        //On l'a, il est actif mais les dates ne sont pas identiques (présent dans les id envoyés)
        Set<Long> roleIdsToChangeTheDates = roleAssRepo.findActiveRoleIdsBelongingToPrincipalAss_otherDates(principalAssId, startsAt, endsAt).stream().filter(id0->roleIds.stream().anyMatch(id1->id0.equals(id1))).collect(Collectors.toSet());

        //On l'a, il n'est pas actif et les dates ne sont pas identiques (présent dans les id envoyés)
        Set<Long> roleIdsToActivateAndChangeTheDates = roleAssRepo.findNoneActiveRoleIdsBelongingToPrincipalAss_otherDates(principalAssId , startsAt, endsAt).stream().filter(id0->roleIds.stream().anyMatch(id1->id0.equals(id1))).collect(Collectors.toSet());

        //On l'a, il n'est pas actif mais les dates sont pas identiques (présent dans les id envoyés)
        Set<Long> roleIdsToActivate = roleAssRepo.findNoneActiveRoleIdsBelongingToPrincipalAss_sameDates(principalAssId , startsAt, endsAt).stream().filter(id0->roleIds.stream().anyMatch(id1->id0.equals(id1))).collect(Collectors.toSet());

        return new RoleAssSpliterDTO(roleIdsToBeRemoved, roleIdsToBeAddedAsNew, roleIdsToChangeTheDates, roleIdsToActivateAndChangeTheDates, roleIdsToActivate);
    }

    public RoleAssSpliterDTO mapToRoleAssSpliterDTO(Set<Long> roleIds, Long principalAssId, LocalDate startsAt, LocalDate endsAt)
    {

        return this.mapToRoleAssSpliterDTO(roleIds, principalAssId, startsAt, endsAt, false);
    }
}