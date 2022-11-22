package dgmp.sigrh.fonctionmodule.model.dtos;

import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.fonctionmodule.model.entities.Fonction;
import dgmp.sigrh.fonctionmodule.model.events.FonctionEventType;
import dgmp.sigrh.fonctionmodule.model.histo.FonctionHisto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class FonctionMapper
{
    @Autowired protected AgentRepo agentRepo;

    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    @Mapping(target = "nomFonction", expression = "java(dto.getNomFonction().trim().toUpperCase())")
    public abstract Fonction mapToFonction(CreateFonctionDTO dto);

    @Mapping(target = "nbrAgents", expression = "java(agentRepo.countByFonction(fonction.getIdFonction()))")
    @Mapping(target = "proportion", expression = "java(agentRepo.count() == 0 ? 0 : (agentRepo.countByFonction(fonction.getIdFonction())/agentRepo.count())*100)")
    public abstract ReadFonctionDTO mapToReadFonctionDTO(Fonction fonction);

    public abstract FonctionHisto mapToFonctionHisto(Fonction fonction, FonctionEventType eventType, EventActorIdentifier eai);
    @Mapping(target = "eai.actionId", source = "actionId")
    @Mapping(target = "eai.mainActionName", source = "mainActionName")
    public abstract FonctionHisto mapToFonctionHisto(Fonction fonction, FonctionEventType eventType, EventActorIdentifier eai, String actionId, String mainActionName);
}
