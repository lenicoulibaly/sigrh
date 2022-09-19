package dgmp.sigrh.emploimodule.model.dtos;

import dgmp.sigrh.agentmodule.controller.repositories.AgentDAO;
import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.emploimodule.controller.repositories.EmploiDAO;
import dgmp.sigrh.emploimodule.model.entities.Emploi;
import dgmp.sigrh.emploimodule.model.events.EmploiEventType;
import dgmp.sigrh.emploimodule.model.histo.EmploiHisto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class EmploiMapper
{
    @Autowired protected EmploiDAO emploiDAO;
    @Autowired protected AgentDAO agentDAO;

    @Mapping(target = "nbrAgent", expression = "java(agentDAO.countByEmploi(emploi.getIdEmploi()))")
    @Mapping(target = "proportion", expression = "java(agentDAO.count() == 0 ? 0 : (agentDAO.countByEmploi(emploi.getIdEmploi())/agentDAO.count())*100)")
    public abstract ReadEmploiDTO mapToReadEmploiDTO(Emploi emploi);

    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    @Mapping(target = "nomEmploi", expression = "java(dto.getNomEmploi().trim().toUpperCase())")
    public abstract Emploi mapToEmploi(CreateEmploiDTO dto);

    @Mapping(target = "eai.actionId", source = "actionId")
    @Mapping(target = "eai.mainActionName", source = "mainActionName")
    public abstract EmploiHisto mapToEmploiHisto(Emploi emploi, EmploiEventType eventType, EventActorIdentifier eventActorIdentifier, String actionId, String mainActionName);
    public abstract EmploiHisto mapToEmploiHisto(Emploi emploi, EmploiEventType eventType, EventActorIdentifier eventActorIdentifier);
}