package dgmp.sigrh.structuremodule.model.dtos.post;

import dgmp.sigrh.emploimodule.controller.repositories.EmploiDAO;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostParamRepo;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostRepo;
import dgmp.sigrh.structuremodule.model.entities.post.Post;
import dgmp.sigrh.structuremodule.model.entities.post.PostParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public abstract class PostMapper
{
    @Autowired protected EmploiDAO empRepo;
    @Autowired protected PostParamRepo ppRepo;
    @Autowired protected PostRepo postRepo;

    @Mapping(target = "nomFonction", source = "post.fonction.nomFonction")
    @Mapping(target = "strName", source = "post.structure.strName")
    @Mapping(target = "strSigle", source = "post.structure.strSigle")
    @Mapping(target = "agentNom", source = "post.agent.nom")
    @Mapping(target = "agentPrenom", source = "post.agent.prenom")
    @Mapping(target = "agentMatricule", source = "post.agent.matricule")
    @Mapping(target = "emploisCompatibles", expression = "java(empRepo.getEmploisCompatiblesByPost(post.getPostId()))")
    public abstract ReadPostDTO mapToReadPostDTO(Post post);
    @Mapping(target = "fonction.idFonction", source = "fonctionId")
    @Mapping(target = "structure.strId", source = "strId")
    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    public abstract Post mapToPost(CreatePostDTO dto);

    public List<PostParam> mapToPostParam(Long postId, Set<Long> empIds)
    {
        Set<Long> oldEmpIds = ppRepo.getEmploiIdsByPost(postId);
        Set<Long> removableEmpIds = oldEmpIds.stream().filter(id0->empIds.stream().noneMatch(id1->id0.longValue() == id1.longValue())).collect(Collectors.toSet());
        Set<Long> newEmpIdsToBeAdded = empIds.stream().filter(id0->oldEmpIds.stream().noneMatch(id1->id0.longValue() == id1.longValue())).collect(Collectors.toSet());

        return Stream.concat
                (
                    removableEmpIds.stream().map(id->new PostParam(postId, id, PersistenceStatus.DELETED)),

                    newEmpIdsToBeAdded.stream().map(id->
                    {
                        if (ppRepo.existsByPostAndEmploi(postId, id, PersistenceStatus.DELETED))
                        {
                            PostParam pp = ppRepo.findByPostAndEmploi(postId, id);
                            pp.setStatus(PersistenceStatus.ACTIVE);
                            return pp;
                        }
                        else
                        {
                            return new PostParam(postId, id, PersistenceStatus.ACTIVE);
                        }
                    })
                ).collect(Collectors.toList());
    }


}