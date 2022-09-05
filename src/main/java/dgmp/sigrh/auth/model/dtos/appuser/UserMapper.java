package dgmp.sigrh.auth.model.dtos.appuser;

import dgmp.sigrh.auth.model.entities.AppUser;
import dgmp.sigrh.structuremodule.controller.repositories.StructureDAO;
import dgmp.sigrh.structuremodule.model.dtos.StructureMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper
{
    @Autowired
    protected StructureMapper structureMapper;
    @Autowired
    protected StructureDAO structureDAO;
    @Mappings({

            @Mapping(target="active", expression="java(false)"),
            @Mapping(target="notBlocked", expression="java(false)"),
            @Mapping(target="creationDate", expression="java(java.time.LocalDateTime.now())"),
            @Mapping(target="lastModificationDate", expression="java(java.time.LocalDateTime.now())"),
            @Mapping(target="structure", expression="java(dto.getStrId()==null ? null : new dgmp.sigrh.structuremodule.model.entities.Structure(dto.getStrId()))")
    })
    public abstract AppUser getAppUser(CreateUserDTO dto);
    void foo()
    {
        java.time.LocalDateTime.now();
    }

    @Mappings
    ({
       @Mapping(target="active", expression="java(true)"),
       @Mapping(target="notBlocked", expression="java(true)"),
       @Mapping(target="creationDate", expression="java(java.time.LocalDateTime.now())"),
       @Mapping(target="lastModificationDate", expression="java(java.time.LocalDateTime.now())"),
       @Mapping(target="structure", expression="java(dto.getStrId()==null ? null : new dgmp.sigrh.structuremodule.model.entities.Structure(dto.getStrId()))")
    })
    public abstract AppUser getAppUser(CreateActiveUserDTO dto);

    @Mappings({
            @Mapping(target = "structureDTO", expression = "java(user.getStrId()==null ? null : structureMapper.getStructureDTO(structureDAO.findById(user.getStrId()).orElse(null)))")
    })
    public abstract ReadUserDTO getReadUserDTO(AppUser user);

}
