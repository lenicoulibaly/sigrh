package dgmp.sigrh.instancemodule.model.dtos;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.instancemodule.model.entities.Instance;
import dgmp.sigrh.instancemodule.model.entities.InstanceHisto;
import dgmp.sigrh.instancemodule.model.events.InstanceEventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstanceMapper
{
    @Mapping(target = "headId", source = "head.strId")
    @Mapping(target = "headName", source ="head.strName")
    @Mapping(target = "headSigle", source ="head.strSigle")
    @Mapping(target = "rhId", source ="rh.strId")
    @Mapping(target = "rhName", source ="rh.strName")
    @Mapping(target = "rhSigle", source ="rh.strSigle")
    ReadInstanceDTO mapToReadInstanceDTO(Instance instance);

    @Mapping(target = "head.strId", source = "headId")
    @Mapping(target = "rh.strId", source = "rhId")
    Instance mapToInstance(CreateInstanceDTO dto);

    @Mapping(source = "head.strId", target = "headId")
    @Mapping(source = "rh.strId", target = "rhId")
    UpdateInstanceDTO mapToUpdateInstanceDTO(Instance instance);

    Instance mapToStructure(InstanceHisto histo);

    //====================================

    InstanceHisto mapToInstanceHisto(Instance instance, InstanceEventType eventType, EventActorIdentifier eai);
    @Mapping(target = "eai.actionId", source = "actionId")
    @Mapping(target = "eai.mainActionName", source = "mainActionName")
    @Mapping(target = "eai.modificationDate", source = "eai.modificationDate")
    @Mapping(target = "eai.modifierUserId", source = "eai.modifierUserId")
    @Mapping(target = "eai.modifierUsername", source = "eai.modifierUsername")
    @Mapping(target = "eai.modifierAssId", source = "eai.modifierAssId")
    @Mapping(target = "eai.modifierStrId", source = "eai.modifierStrId")
    @Mapping(target = "eai.agentId", source = "eai.agentId")
    @Mapping(target = "eai.nom", source = "eai.nom")
    @Mapping(target = "eai.prenom", source = "eai.prenom")
    @Mapping(target = "eai.matricule", source = "eai.matricule")
    InstanceHisto mapToInstanceHisto(Instance instance, InstanceEventType eventType, EventActorIdentifier eai, String actionId, String mainActionName);
}