package dgmp.sigrh.structuremodule.model.dtos.str;

import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.entities.structure.StrHisto;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import dgmp.sigrh.structuremodule.model.events.StrEventType;
import dgmp.sigrh.typemodule.model.entities.Type;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Mapper(componentModel = "spring")
public abstract class StrMapper
{
    @Autowired
    protected StrRepo strRepo;
    @PersistenceContext
    protected EntityManager entityManager;

    @Mapping(target = "strType", source = "str.strType.name")
    @Mapping(target = "parentId", source ="strParent.strId")
    @Mapping(target = "parentName", source ="strParent.strName")
    @Mapping(target = "parentSigle", source ="strParent.strSigle")
    @Mapping(target = "respoId", source ="strRespoPost.agent.agentId")
    @Mapping(target = "respoName", expression ="java(str.getStrRespoPost()==null ? null : str.getStrRespoPost().getAgent() == null ? null : str.getStrRespoPost().getAgent().getNom() + \" \" + str.getStrRespoPost().getAgent().getPrenom())")
    @Mapping(target = "respoMatricule", source ="strRespoPost.agent.matricule")
    public abstract ReadStrDTO mapToReadStrDTO(Structure str);


    @Mapping(target = "strParent.strId", source = "parentId")
    @Mapping(target = "strType.typeId", source = "typeId")
    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    public abstract Structure mapToStructure(CreateStrDTO dto);

    public abstract UpdateStrDTO mapToUpdateStrDTO(Structure str);

    public Structure mapToStructure(UpdateStrDTO dto)
    {
        if(dto==null) return null;
        Structure loadedStructure = strRepo.findById(dto.getStrId()).orElse(null);
        if(loadedStructure == null ) return null;
        entityManager.detach(loadedStructure);
        loadedStructure.setStrName(dto.getStrName());
        loadedStructure.setStrSigle(dto.getStrSigle());
        loadedStructure.setStrTel(dto.getStrTel());
        loadedStructure.setStrAddress(dto.getStrAddress());
        loadedStructure.setSituationGeo(dto.getSituationGeo());
        loadedStructure.setCreationActFile(dto.getCreationActFile());
        return loadedStructure;
    }

    public Structure mapToStructure(ChangeAncrageDTO dto)
    {
        if(dto==null) return null;
        Structure loadedStructure = strRepo.findById(dto.getStrId()).orElse(null);
        if(loadedStructure == null ) return null;
        entityManager.detach(loadedStructure);
        loadedStructure.setStrType(new Type(dto.getNewTypeId()));
        loadedStructure.setStrParent(dto.getNewParentId() == null ? null : new Structure(dto.getNewParentId()));
        return loadedStructure;
    }

    public abstract StrHisto mapToStrHisto(Structure str, StrEventType eventType, EventActorIdentifier eai);
    @Mapping(target = "eai.actionId", source = "actionId")
    @Mapping(target = "eai.mainActionName", source = "mainActionName")
    public abstract StrHisto mapToStrHisto(Structure str, StrEventType eventType, EventActorIdentifier eai, String actionId, String mainActionName);
}