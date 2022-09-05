package dgmp.sigrh.structuremodule.model.dtos;

import dgmp.sigrh.structuremodule.controller.repositories.StructureDAO;
import dgmp.sigrh.structuremodule.model.entities.Structure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class StructureMapper
{
    @Autowired
    protected StructureDAO structureDAO;

    @Mappings({
            @Mapping(target = "parentId", expression = "java(str.getStrParent().getStrId())"),
            @Mapping(target = "parentName", expression = "java(str.getStrParent().getStrName())"),
            @Mapping(target = "parentSigle", expression = "java(str.getStrParent().getStrSigle())")
    })
    public abstract StructureDTO getStructureDTO(Structure str);

    @Mapping(target = "strParent", expression = "java(new dgmp.sigrh.structuremodule.model.entities.Structure(dto.getParentId()))")
    public abstract Structure getStructure(StructureDTO dto);
}
