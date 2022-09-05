package dgmp.sigrh.structuremodule.model.dtos;

import dgmp.sigrh.structuremodule.model.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PostMapper
{
    @Mappings({
        @Mapping(target = "idFonction", source = "post.fonction.idFonction"),
        @Mapping(target = "nomFonction", source = "post.fonction.nomFonction"),
        @Mapping(target = "strId", source = "post.structure.strId"),
        @Mapping(target = "strName", source = "post.structure.strName"),
        @Mapping(target = "strSigle", source = "post.structure.strSigle"),
        @Mapping(target = "idAgent", source = "post.agent.idAgent"),
        @Mapping(target = "nom", source = "post.agent.nom"),
        @Mapping(target = "prenom", source = "post.agent.prenom"),
        @Mapping(target = "matricule", source = "post.agent.matricule")
    })
    ReadPostDTO getReadPostDTO(Post post);
}