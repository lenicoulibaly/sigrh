package dgmp.sigrh.auth.model.dtos.asignation;

import dgmp.sigrh.auth.controller.repositories.PrivilegeDAO;
import dgmp.sigrh.auth.controller.repositories.RoleDAO;
import dgmp.sigrh.auth.controller.repositories.UserDAO;
import dgmp.sigrh.auth.model.entities.PrivilegeToRoleAss;
import dgmp.sigrh.auth.model.entities.PrivilegeToUserAss;
import dgmp.sigrh.auth.model.entities.RoleToUserAss;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AssignationMapper
{
    @Autowired protected UserDAO userDAO;
    @Autowired protected RoleDAO roleDAO;
    @Autowired protected PrivilegeDAO privilegeDAO;

    @Mapping(target = "user", expression = "java(dto.getUserId() == null ? null : new dgmp.sigrh.auth.model.entities.AppUser(dto.getUserId()))")
    @Mapping(target = "role", expression = "java(dto.getRoleId() == null ? null : new dgmp.sigrh.auth.model.entities.AppRole(dto.getRoleId()))")
    @Mapping(target = "structure", expression = "java(dto.getStrId() == null ? null : new dgmp.sigrh.structuremodule.model.entities.Structure(dto.getStrId()))")
    public abstract RoleToUserAss mapToRoleToUserAss(RoleToUserDTO dto);

    @Mapping(target = "user", expression = "java(dto.getUserId() == null ? null : new dgmp.sigrh.auth.model.entities.AppUser(dto.getUserId()))")
    @Mapping(target = "privilege", expression = "java(new dgmp.sigrh.auth.model.entities.AppPrivilege(dto.getPrivilegeId()))")
    @Mapping(target = "structure", expression = "java(dto.getStrId() == null ? null : new dgmp.sigrh.structuremodule.model.entities.Structure(dto.getStrId()))")
    public abstract PrivilegeToUserAss mapToPrvToUserAss(PrvToUserDTO dto);

    @Mapping(target = "role", expression = "java(dto.getRoleId() == null ? null : new dgmp.sigrh.auth.model.entities.AppRole(dto.getRoleId()))")
    @Mapping(target = "privilege", expression = "java(new dgmp.sigrh.auth.model.entities.AppPrivilege(dto.getPrivilegeId()))")
    public abstract PrivilegeToRoleAss mapToPrvToRoleAss(PrvToRoleDTO dto);

    public Set<PrvToUserDTO> mapToSetOfPrvToUserDTO(PrvsToUserDTO dto)
    {
        return dto.getPrivilegeIds().stream().map(prvId->
        {
            PrvToUserDTO ptuDto = new PrvToUserDTO();
            BeanUtils.copyProperties(dto, ptuDto);
            ptuDto.setPrivilegeId(prvId);
            return ptuDto;}
        ).collect(Collectors.toSet());
    }

    public Set<PrvToRoleDTO> mapToSetOfPrvToRoleDTO(PrvsToRoleDTO dto)
    {
        return dto.getPrivilegeIds().stream().map(prvId->
                {
                    PrvToRoleDTO ptrDto = new PrvToRoleDTO();
                    BeanUtils.copyProperties(dto, ptrDto);
                    ptrDto.setPrivilegeId(prvId);
                    return ptrDto;}
        ).collect(Collectors.toSet());
    }


    @Mapping(target = "user", expression = "java(userDAO.findById(dto.getUserId()).orElse(null))")
    @Mapping(target = "role", expression = "java(roleDAO.findById(dto.getRoleId()).orElse(null))")
    public abstract RoleToUserAss mapToFullRoleToUserAss(RoleToUserDTO dto);

    @Mapping(target = "user", expression = "java(userDAO.findById(dto.getUserId()).orElse(null))")
    @Mapping(target = "privilege", expression = "java(privilegeDAO.findById(dto.getPrivilegeId()).orElse(null))")
    public abstract PrivilegeToUserAss mapToFullPrvToUserAss(PrvToUserDTO dto);

    @Mapping(target = "privilege", expression = "java(privilegeDAO.findById(dto.getPrivilegeId()).orElse(null))")
    @Mapping(target = "role", expression = "java(roleDAO.findById(dto.getRoleId()).orElse(null))")
    public abstract PrivilegeToRoleAss mapToFullPrvToRoleAss(PrvToRoleDTO dto);


}
