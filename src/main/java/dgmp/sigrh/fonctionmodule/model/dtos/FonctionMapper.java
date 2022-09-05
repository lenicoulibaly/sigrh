package dgmp.sigrh.fonctionmodule.model.dtos;

import dgmp.sigrh.agentmodule.controller.repositories.AgentDAO;
import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.fonctionmodule.model.entities.Fonction;
import dgmp.sigrh.fonctionmodule.model.events.FonctionEventType;
import dgmp.sigrh.fonctionmodule.model.histo.FonctionHisto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class FonctionMapper
{
    @Autowired protected AgentDAO agentDAO;

    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    @Mapping(target = "nomFonction", expression = "java(dto.getNomFonction().trim().toUpperCase())")
    public abstract Fonction mapToFonction(CreateFonctionDTO dto);

    @Mapping(target = "nbrAgents", expression = "java(agentDAO.countByFonction(fonction.getIdFonction()))")
    @Mapping(target = "proportion", expression = "java(agentDAO.count() == 0 ? 0 : (agentDAO.countByFonction(fonction.getIdFonction())/agentDAO.count())*100)")
    public abstract ReadFonctionDTO mapToReadFonctionDTO(Fonction fonction);

    public abstract FonctionHisto mapToFonctionHisto(Fonction fonction, FonctionEventType eventType, EventActorIdentifier eventActorIdentifier);
}
