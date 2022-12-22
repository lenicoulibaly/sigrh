package dgmp.sigrh.archivemodule.controller.service;

import dgmp.sigrh.archivemodule.model.dtos.ArchiveReqDTO;
import dgmp.sigrh.archivemodule.model.entities.Archive;
import dgmp.sigrh.archivemodule.model.enums.ArchiveTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IArchiveService
{
    Archive createArchive(ArchiveReqDTO dto);

    @Transactional
    void setAgentProfilePhoto(Archive archive);

    Archive updateArchive(ArchiveReqDTO dto);
    Page<Archive> searchArchives(Long agtId, String key, List<Long> typeArchiveIds, Pageable pageable);

    boolean isSeveralPerAgent(String archTypeCode);

    List<ArchiveTypes> getAllArchiveTypes();
    //void addCivilArchives(AddCivilArchivesDTO dto);
}
