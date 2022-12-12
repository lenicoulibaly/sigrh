package dgmp.sigrh.emploimodule.model.dtos;

import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.emploimodule.controller.repositories.EmploiRepo;
import dgmp.sigrh.emploimodule.model.entities.Emploi;
import dgmp.sigrh.emploimodule.model.events.EmploiEventType;
import dgmp.sigrh.emploimodule.model.histo.EmploiHisto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class EmploiMapper
{
    @Autowired protected EmploiRepo emploiRepo;
    @Autowired protected AgentRepo agentRepo;

    @Mapping(target = "nbrAgent", expression = "java(agentRepo.countByEmploi(emploi.getIdEmploi()))")
    @Mapping(target = "proportion", expression = "java(agentRepo.count() == 0 ? 0 : (agentRepo.countByEmploi(emploi.getIdEmploi())/agentRepo.count())*100)")
    public abstract ReadEmploiDTO mapToReadEmploiDTO(Emploi emploi);

    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    @Mapping(target = "nomEmploi", expression = "java(dto.getNomEmploi().trim().toUpperCase())")
    public abstract Emploi mapToEmploi(CreateEmploiDTO dto);

    @Mapping(target = "eai.actionId", source = "actionId")
    @Mapping(target = "eai.mainActionName", source = "mainActionName")
    public abstract EmploiHisto mapToEmploiHisto(Emploi emploi, EmploiEventType eventType, EventActorIdentifier eventActorIdentifier, String actionId, String mainActionName);
    public abstract EmploiHisto mapToEmploiHisto(Emploi emploi, EmploiEventType eventType, EventActorIdentifier eventActorIdentifier);
}