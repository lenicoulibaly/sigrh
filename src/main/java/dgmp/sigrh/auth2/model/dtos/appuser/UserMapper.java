package dgmp.sigrh.auth2.model.dtos.appuser;

import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import dgmp.sigrh.auth2.model.entities.AppUser;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.UserEventTypes;
import dgmp.sigrh.auth2.model.histo.UserHisto;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.dtos.str.StrMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper
{
    @Autowired protected StrMapper structureMapper;
    @Autowired protected StrRepo strRepo;
    @Autowired protected AgentRepo agentRepo;

    @Mapping(target="active", expression="java(false)")
    @Mapping(target="notBlocked", expression="java(true)")
    @Mapping(target="creationDate", expression="java(java.time.LocalDateTime.now())")
    @Mapping(target="lastModificationDate", expression="java(java.time.LocalDateTime.now())")
    @Mapping(target="structure", expression="java(dto.getStrId()==null ? null : new dgmp.sigrh.structuremodule.model.entities.structure.Structure(dto.getStrId()))")
    @Mapping(target = "username", source="email")
    public abstract AppUser mapToUser(CreateUserDTO dto);

    @Mapping(target="active", expression="java(true)")
    @Mapping(target="notBlocked", expression="java(true)")
    @Mapping(target="creationDate", expression="java(java.time.LocalDateTime.now())")
    @Mapping(target="lastModificationDate", expression="java(java.time.LocalDateTime.now())")
    @Mapping(target="structure", expression="java(dto.getStrId()==null ? null : new dgmp.sigrh.structuremodule.model.entities.structure.Structure(dto.getStrId()))")
    public abstract AppUser mapToUser(CreateActiveUserDTO dto);

    public abstract UserHisto mapToUserHisto(AppUser user, UserEventTypes eventType, EventActorIdentifier eai);

    @Mapping(target = "strId", source = "structure.strId")
    @Mapping(target = "strName", source = "structure.strName")
    @Mapping(target = "strSigle", source = "structure.strSigle")
    @Mapping(target = "strParentId", source = "structure.strParent.strId")
    @Mapping(target = "strHierarchySigle", expression = "java(strRepo.getHierarchySigle(user.getStructure().getStrId()))")
    @Mapping(target = "nom", expression = "java(agentRepo.getFullNameByUserId(user.getUserId()))")
    @Mapping(target = "matricule", expression = "java(agentRepo.getMatriculeUserId(user.getUserId()))")
    public abstract ReadUserDTO mapToReadUserDTO(AppUser user);
}