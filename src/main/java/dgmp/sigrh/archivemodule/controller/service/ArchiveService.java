package dgmp.sigrh.archivemodule.controller.service;

import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import dgmp.sigrh.archivemodule.controller.repositories.ArchiveHistoRepo;
import dgmp.sigrh.archivemodule.controller.repositories.ArchiveRepo;
import dgmp.sigrh.archivemodule.model.dtos.ArchiveMapper;
import dgmp.sigrh.archivemodule.model.dtos.CreateArchiveDTO;
import dgmp.sigrh.archivemodule.model.dtos.UpdateArchiveDTO;
import dgmp.sigrh.archivemodule.model.entities.Archive;
import dgmp.sigrh.archivemodule.model.entities.ArchiveHisto;
import dgmp.sigrh.archivemodule.model.enums.ArchiveEventTypes;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
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

    @Override @Transactional(rollbackFor = {Exception.class})
    public Archive createArchive(CreateArchiveDTO dto)
    {
        EventActorIdentifier eai = scm.getEventActorIdFromSCM();
        Archive archive = archiveMapper.mapToArchive(dto);
        archive = archiveRepo.save(archive);
        archive.setPath(filesManager.generatePath(dto.getFile(), dto.getArchiveTypeCode(), agentRepo.getNameToSnackFormatByAgtId(dto.getAgtId()), dto.getAgtId(), archive.getArchiveId()));
        filesManager.uploadFile(archive.getFile(), archive.getPath());
        ArchiveHisto archiveHisto = archiveMapper.mapToArchiveHisto(archive, ArchiveEventTypes.CREATE_ARCHIVE, eai);
        archiveHistoRepo.save(archiveHisto);
        return archive;
    }

    @Override
    public Archive updateArchive(UpdateArchiveDTO dto) {
        return null;
    }

    @Override
    public Page<Archive> searchArchives(Long agtId, String key, List<Long> typeArchiveIds, Pageable pageable)
    {
        List<Long> allArchiveTypeIds = typeRepo.getTypeIdsByTypeGroup(TypeGroup.ARCHIVE.name());
        key = key==null ? "" : key.trim().equals("") ? "" : key;
        typeArchiveIds = typeArchiveIds == null ? allArchiveTypeIds : typeArchiveIds.isEmpty() ? typeArchiveIds : typeArchiveIds;
        return archiveRepo.searchArchives(agtId, StringUtils.stripAccentsToUpperCase(key), typeArchiveIds, pageable);
    }
}
