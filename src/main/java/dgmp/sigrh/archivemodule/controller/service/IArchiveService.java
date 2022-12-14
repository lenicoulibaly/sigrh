package dgmp.sigrh.archivemodule.controller.service;

import dgmp.sigrh.archivemodule.model.dtos.CreateArchiveDTO;
import dgmp.sigrh.archivemodule.model.dtos.UpdateArchiveDTO;
import dgmp.sigrh.archivemodule.model.entities.Archive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IArchiveService
{
    Archive createArchive(CreateArchiveDTO dto);
    Archive updateArchive(UpdateArchiveDTO dto);
    Page<Archive> searchArchives(Long agtId, String key, List<Long> typeArchiveIds, Pageable pageable);
}
