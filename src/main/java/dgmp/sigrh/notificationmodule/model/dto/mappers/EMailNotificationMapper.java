package dgmp.sigrh.notificationmodule.model.dto.mappers;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.email.EmailEventTypes;
import dgmp.sigrh.notificationmodule.model.dto.EmailNotificationDTO;
import dgmp.sigrh.notificationmodule.model.entities.EmailNotification;
import dgmp.sigrh.notificationmodule.model.entities.EmailNotificationHisto;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface EMailNotificationMapper
{

    EmailNotification mapToNotification(EmailNotificationDTO dto);

    EmailNotificationHisto mapToEmailNotificationHisto(EmailNotification str, EmailEventTypes eventType, EventActorIdentifier eai);

    @Mapping(target = "eai.actionId", source = "actionId")
    @Mapping(target = "eai.mainActionName", source = "mainActionName")
    @Mapping(target = "eai.modificationDate", source = "eai.modificationDate")
    @Mapping(target = "eai.modifierUserId", source = "eai.modifierUserId")
    @Mapping(target = "eai.modifierUsername", source = "eai.modifierUsername")
    @Mapping(target = "eai.modifierAssId", source = "eai.modifierAssId")
    @Mapping(target = "eai.modifierStrId", source = "eai.modifierStrId")
    @Mapping(target = "eai.modifierAgentId", source = "eai.modifierAgentId")
    @Mapping(target = "eai.modifierNom", source = "eai.modifierNom")
    @Mapping(target = "eai.modifierPrenom", source = "eai.modifierPrenom")
    @Mapping(target = "eai.modifierMatricule", source = "eai.modifierMatricule")
    EmailNotificationHisto mapToEmailNotificationHisto(Structure str, EmailEventTypes eventType, EventActorIdentifier eai, String actionId, String mainActionName);
}
