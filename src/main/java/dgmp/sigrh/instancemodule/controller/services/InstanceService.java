package dgmp.sigrh.instancemodule.controller.services;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import dgmp.sigrh.instancemodule.controller.repositories.InstanceHistoRepo;
import dgmp.sigrh.instancemodule.controller.repositories.InstanceRepo;
import dgmp.sigrh.instancemodule.model.dtos.CreateInstanceDTO;
import dgmp.sigrh.instancemodule.model.dtos.InstanceMapper;
import dgmp.sigrh.instancemodule.model.dtos.ReadInstanceDTO;
import dgmp.sigrh.instancemodule.model.entities.Instance;
import dgmp.sigrh.instancemodule.model.entities.InstanceHisto;
import dgmp.sigrh.instancemodule.model.events.InstanceEventType;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrHistoRepo;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.dtos.str.StrMapper;
import dgmp.sigrh.structuremodule.model.entities.structure.StrHisto;
import dgmp.sigrh.structuremodule.model.events.StrEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service @RequiredArgsConstructor
public class InstanceService implements IInstanceService
{
    private final InstanceRepo instanceRepo;
    private final InstanceMapper instanceMapper;
    private final StrRepo strRepo;
    private final StrMapper strMapper;
    private final ISecurityContextManager scm;
    private final StrHistoRepo strHistoRepo;
    private final InstanceHistoRepo instanceHistoRepo;

    @Override @Transactional
    public ReadInstanceDTO createInstance(CreateInstanceDTO dto)
    {
        Instance instance = instanceMapper.mapToInstance(dto);
        Instance savedInstance = instanceRepo.save(instance);
        String actionId = UUID.randomUUID().toString();
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        strRepo.findAllChildren(dto.getHeadId()).forEach(str->
        {
            str.setInstance(savedInstance);
            StrHisto strHisto = strMapper.mapToStrHisto(str, StrEventType.CHANGE_STR_INSTANCE, eai, actionId, InstanceEventType.CREATE_INSTANCE.getEvent());
            strHistoRepo.save(strHisto);
        });
        InstanceHisto histo = instanceMapper.mapToInstanceHisto(savedInstance, InstanceEventType.CREATE_INSTANCE, eai, actionId, InstanceEventType.CREATE_INSTANCE.getEvent());
        instanceHistoRepo.save(histo);
        return instanceMapper.mapToReadInstanceDTO(instance);
    }
}
