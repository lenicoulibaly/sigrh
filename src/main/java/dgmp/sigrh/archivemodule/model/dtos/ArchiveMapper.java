package dgmp.sigrh.archivemodule.model.dtos;

import dgmp.sigrh.agentmodule.model.dtos.RegisterAgentDTO;
import dgmp.sigrh.archivemodule.model.entities.Archive;
import dgmp.sigrh.archivemodule.model.entities.ArchiveHisto;
import dgmp.sigrh.archivemodule.model.enums.ArchiveEventTypes;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ArchiveMapper
{
    @Autowired protected TypeRepo typeRepo;

    @Mapping(target = "agtId", source = "agent.agentId")
    @Mapping(target = "archiveTypeCode", source = "archiveType.uniqueCode")
    public abstract UpdateArchiveDTO mapToUpdateAgentDTO(Archive archive);

    public abstract ArchiveHisto mapToArchiveHisto(Archive archive, ArchiveEventTypes eventType, EventActorIdentifier eai);

    @Mapping(target = "agent", expression = "java(new dgmp.sigrh.agentmodule.model.entities.Agent(dto.getAgtId()))")
    @Mapping(target = "archiveType", expression = "java(new dgmp.sigrh.typemodule.model.entities.Type(typeRepo.findTypeIdByUniqueCode(dto.getArchiveTypeCode())))")
    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    public abstract Archive mapToArchive(CreateArchiveDTO dto);

    @Mapping(target = "description", expression = "java(\"Photo de profil\")")
    @Mapping(target = "archiveTypeCode", expression = "java(\"PRF_PHT\")")
    @Mapping(target = "file", source = "dto.photoFile")
    public abstract CreateArchiveDTO mapToPhotoArchiveDTO(RegisterAgentDTO dto, Long agtId);
}