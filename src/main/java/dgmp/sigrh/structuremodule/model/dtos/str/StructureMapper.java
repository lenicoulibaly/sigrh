package dgmp.sigrh.structuremodule.model.dtos.str;

import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class StructureMapper
{
    @Autowired
    protected StrRepo strRepo;

    @Mapping(target = "strType", source = "str.strType.name")
    @Mapping(target = "parentId", source ="strParent.strId")
    @Mapping(target = "parentName", source ="strParent.strName")
    @Mapping(target = "parentSigle", source ="strParent.strSigle")
    @Mapping(target = "respoId", source ="strRespoPost.agent.agentId")
    @Mapping(target = "respoName", expression ="java(str.getStrRespoPost().getAgent().getNom() + \" \" + str.getStrRespoPost().getAgent().getPrenom())")
    @Mapping(target = "respoMatricule", source ="strRespoPost.agent.matricule")
    public abstract ReadStrDTO mapToReadStrDTO(Structure str);


    @Mapping(target = "strParent.strId", source = "parentId")
    @Mapping(target = "strType.typeId", source = "typeId")
    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    public abstract Structure mapToStructure(CreateStrDTO dto);

    public Structure mapToStructure(UpdateStrDTO dto)
    {
        if(dto==null) return null;
        Structure loadedStructure = strRepo.findById(dto.getStrId()).orElse(null);
        if(loadedStructure == null ) return null;
        loadedStructure.setStrName(dto.getStrName());
        loadedStructure.setStrSigle(dto.getStrSigle());
        loadedStructure.setStrTel(dto.getStrTel());
        loadedStructure.setStrAddress(dto.getStrAddress());
        loadedStructure.setSituationGeo(dto.getSituationGeo());
        loadedStructure.setCreationActFile(dto.getCreationActFile());
        return loadedStructure;
    }
}