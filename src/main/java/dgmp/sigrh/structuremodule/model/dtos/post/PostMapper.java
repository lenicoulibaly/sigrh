package dgmp.sigrh.structuremodule.model.dtos.post;

import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.emploimodule.controller.repositories.EmploiDAO;
import dgmp.sigrh.fonctionmodule.model.entities.Fonction;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostGroupRepo;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostParamRepo;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostRepo;
import dgmp.sigrh.structuremodule.model.entities.post.*;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import dgmp.sigrh.structuremodule.model.events.PostEventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PostMapper
{
    @Autowired protected EmploiDAO empRepo;
    @Autowired protected PostParamRepo ppRepo;
    @Autowired protected PostRepo postRepo;
    @Autowired private PostGroupRepo pgRepo;
    @PersistenceContext protected EntityManager entityManager;

    @Mapping(target = "nomFonction", source = "postGroup.fonction.nomFonction")
    @Mapping(target = "strName", source = "postGroup.structure.strName")
    @Mapping(target = "strSigle", source = "postGroup.structure.strSigle")
    @Mapping(target = "agentNom", source = "agent.nom")
    @Mapping(target = "agentPrenom", source = "agent.prenom")
    @Mapping(target = "agentMatricule", source = "agent.matricule")
    @Mapping(target = "emploisCompatibles", expression = "java(empRepo.getEmploisCompatiblesByPostGroup(post.getPostGroup().getPostGroupId()))")
    @Mapping(target = "nbrPosts", expression = "java(postRepo.countByPostGroupActive(post.getPostGroup().getPostGroupId()))")
    @Mapping(target = "nbrPostsVacants", expression = "java(postRepo.countVacantPostsByPostGroup(post.getPostGroup().getPostGroupId()))")
    @Mapping(target = "nbrPostsOccupes", expression = "java(postRepo.countNoneVacantPostsByPostGroup(post.getPostGroup().getPostGroupId()))")
    @Mapping(target = "postGroupId", source = "postGroup.postGroupId")
    public abstract ReadPostDTO mapToReadPostDTO(Post post);

    @Mapping(target = "nomFonction", source = "fonction.nomFonction")
    @Mapping(target = "strName", source = "structure.strName")
    @Mapping(target = "strSigle", source = "structure.strSigle")
    @Mapping(target = "emploisCompatibles", expression = "java(empRepo.getEmploisCompatiblesByPostGroup(pg.getPostGroupId()))")
    @Mapping(target = "nbrPosts", expression = "java(postRepo.countByPostGroupActive(pg.getPostGroupId()))")
    @Mapping(target = "nbrPostsVacants", expression = "java(postRepo.countVacantPostsByPostGroup(pg.getPostGroupId()))")
    @Mapping(target = "nbrPostsOccupes", expression = "java(postRepo.countNoneVacantPostsByPostGroup(pg.getPostGroupId()))")
    public abstract ReadPostDTO mapToReadPostDTO(PostGroup pg);

    @Mapping(target = "fonction.idFonction", source = "fonctionId")
    @Mapping(target = "structure.strId", source = "strId")
    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    public abstract PostGroup mapToPostGroup(CreatePostDTO dto);

    @Mapping(target = "postGroup.postGroupId", source = "postGroupId")
    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    @Mapping(target = "vacant", expression = "java(true)")
    public abstract Post mapToPost(PostGroup postGroup);

    public SetPostParamsDTO mapTSetPostParamsDTO(Long postGroupId, Set<Long> empIds)
    {
        java.util.UUID.randomUUID().toString();
        Set<Long> oldEmpIds = ppRepo.getEmploiIdsByPost(postGroupId);
        Set<Long> removableEmpIds = oldEmpIds.stream().filter(id0->empIds.stream().noneMatch(id1->id0.longValue() == id1.longValue())).collect(Collectors.toSet());
        Set<Long> newEmpIdsToBeAdded = empIds.stream().filter(id0->oldEmpIds.stream().noneMatch(id1->id0.longValue() == id1.longValue())).collect(Collectors.toSet());
        return new SetPostParamsDTO(removableEmpIds, newEmpIdsToBeAdded);
    }

    public PostGroup mapToPostGroup(UpdatePostDTO dto)
    {
        if(dto == null) return null;
        PostGroup postGroup = pgRepo.findById(dto.getPostGroupId()).orElse(null);
        if(postGroup == null) return null;
        entityManager.detach(postGroup);
        postGroup.setPostDescription(dto.getPostDescription());
        postGroup.setIntitule(dto.getIntitule());
        return postGroup;
    }

    public abstract PostGroupHisto mapToPostGroupHisto(PostGroup postGroup, PostEventType eventType, EventActorIdentifier eai);
    @Mapping(target = "eai.actionId", source = "actionId")
    @Mapping(target = "eai.mainActionName", source = "mainActionName")
    public abstract PostGroupHisto mapToPostGroupHisto(PostGroup postGroup, PostEventType eventType, EventActorIdentifier eai, String actionId, String mainActionName);

    public abstract PostHisto mapToPostHisto(Post post, PostEventType eventType, EventActorIdentifier eai);
    @Mapping(target = "eai.actionId", source = "actionId")
    @Mapping(target = "eai.mainActionName", source = "mainActionName")
    public abstract PostHisto mapToPostHisto(Post post, PostEventType eventType, EventActorIdentifier eai, String actionId, String mainActionName);

    public abstract PostParamHisto mapToPostParamHisto(PostParam postParam, PostEventType eventType, EventActorIdentifier eai);
    @Mapping(target = "eai.actionId", source = "actionId")
    @Mapping(target = "eai.mainActionName", source = "mainActionName")
    public abstract PostParamHisto mapToPostParamHisto(PostParam postParam, PostEventType eventType, EventActorIdentifier eai, String actionId, String mainActionName);

    private Fonction fonction;
    private String intitule;
    @ManyToOne
    @JoinColumn(name = "ID_UNITE_ADMIN")
    private Structure structure;
}