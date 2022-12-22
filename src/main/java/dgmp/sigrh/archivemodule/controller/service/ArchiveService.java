package dgmp.sigrh.archivemodule.controller.service;

import dgmp.sigrh.agentmodule.controller.repositories.AgentHistoRepo;
import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import dgmp.sigrh.agentmodule.model.dtos.AgentMapper;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.agentmodule.model.histo.AgentHisto;
import dgmp.sigrh.archivemodule.controller.repositories.ArchiveHistoRepo;
import dgmp.sigrh.archivemodule.controller.repositories.ArchiveRepo;
import dgmp.sigrh.archivemodule.model.dtos.ArchiveMapper;
import dgmp.sigrh.archivemodule.model.dtos.ArchiveReqDTO;
import dgmp.sigrh.archivemodule.model.entities.Archive;
import dgmp.sigrh.archivemodule.model.entities.ArchiveHisto;
import dgmp.sigrh.archivemodule.model.enums.ArchiveEventTypes;
import dgmp.sigrh.archivemodule.model.enums.ArchiveTypes;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.agent.AgentEventTypes;
import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import dgmp.sigrh.shared.utilities.StringUtils;
import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import dgmp.sigrh.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class ArchiveService implements IArchiveService
{
    private final IFilesManager filesManager;
    private final ArchiveRepo archiveRepo;
    private final ArchiveHistoRepo archiveHistoRepo;
    private final ArchiveMapper archiveMapper;
    private final ISecurityContextManager scm;
    private final AgentRepo agentRepo;
    private final TypeRepo typeRepo;
    private final AgentMapper agentMapper;
    private final AgentHistoRepo agentHistoRepo;

    @Override @Transactional(rollbackFor = {Exception.class})
    public Archive createArchive(ArchiveReqDTO dto)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        Archive archive = archiveMapper.mapToArchive(dto);
        archive = archiveRepo.save(archive);
        archive.setPath(filesManager.generatePath(dto.getFile(), dto.getArchiveTypeCode(), agentRepo.getNameToSnackFormatByAgtId(dto.getAgtId()), dto.getAgtId(), archive.getArchiveId()));
        filesManager.uploadFile(archive.getFile(), archive.getPath());
        ArchiveHisto archiveHisto = archiveMapper.mapToArchiveHisto(archive, ArchiveEventTypes.CREATE_ARCHIVE, eai);
        archiveHistoRepo.save(archiveHisto);
        if(typeRepo.getUniqueCode(archive.getArchiveType().getTypeId()).equals("PRF_PHT")) this.setAgentProfilePhoto(archive);

        return archive;
    }

    @Override @Transactional
    public void setAgentProfilePhoto(Archive archive)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        if(archive == null) return;
        if(archive.getArchiveType() == null) return;
        if(!typeRepo.getUniqueCode(archive.getArchiveType().getTypeId()).equals("PRF_PHT")) return;
        Agent agent = agentRepo.findById(archive.getAgent().getAgentId()).orElse(null);
        if(agent == null) return;
        if(agent.getNomPhoto() != null) return;
        agent.setNomPhoto(archive.getPath());

        AgentHisto histo = agentMapper.mapToAgentHisto(agent, AgentEventTypes.ADD_PROFILE_PHOTO, eai);
        agentHistoRepo.save(histo);
    }

    @Override @Transactional(rollbackFor = Exception.class)
    public Archive updateArchive(ArchiveReqDTO dto)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        Archive archive = archiveMapper.mapToArchive(archiveRepo.findById(dto.getArchiveId()).orElse(null), dto);
        archive = archiveRepo.save(archive);
        ArchiveHisto archiveHisto = archiveMapper.mapToArchiveHisto(archive, ArchiveEventTypes.UPDATE_ARCHIVE, eai);
        archiveHisto = archiveHistoRepo.save(archiveHisto);

        if(dto.getFile() != null) // Historize the old file under a new name and upload the new one
        {
            if(!dto.getFile().getOriginalFilename().equals(""))
            {
                String histoPath = filesManager.generateHistoPath(dto.getFile(), dto.getArchiveTypeCode(), agentRepo.getNameToSnackFormatByAgtId(dto.getAgtId()), dto.getAgtId(), archive.getArchiveId(), archiveHisto.getHistoId());
                archiveHisto.setHistoPath(histoPath);
                filesManager.renameFile(archive.getPath(), histoPath);
                filesManager.uploadFile(dto.getFile(), archive.getPath());
            }
        }
        return archive;
    }

    @Override
    public Page<Archive> searchArchives(Long agtId, String key, List<Long> typeArchiveIds, Pageable pageable)
    {
        List<Long> allArchiveTypeIds = typeRepo.getTypeIdsByTypeGroup(TypeGroup.ARCHIVE.name());
        key = key==null ? "" : key.trim().equals("") ? "" : key;
        typeArchiveIds = typeArchiveIds == null ? allArchiveTypeIds : typeArchiveIds.isEmpty() ? typeArchiveIds : typeArchiveIds;
        return archiveRepo.searchArchives(agtId, StringUtils.stripAccentsToUpperCase(key), typeArchiveIds, pageable);
    }

    @Override
    public boolean isSeveralPerAgent(String archTypeCode)
    {
        return ArchiveTypes.getByUniqueCode(archTypeCode) == null ? false : ArchiveTypes.getByUniqueCode(archTypeCode).isSeveralPerAgent();
    }

    @Override
    public List<ArchiveTypes> getAllArchiveTypes()
    {
        return ArchiveTypes.getAll();
    }

    public boolean agentCouldHaveNewArchive(Long agtId, String archiveUniqueCode)
    {
        return !archiveRepo.existsByAgtIdAndTyCode(agtId, archiveUniqueCode) || this.isSeveralPerAgent(archiveUniqueCode);
    }
}
