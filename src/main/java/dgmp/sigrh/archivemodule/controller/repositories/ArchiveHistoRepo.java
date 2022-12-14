package dgmp.sigrh.archivemodule.controller.repositories;

import dgmp.sigrh.archivemodule.model.entities.ArchiveHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveHistoRepo extends JpaRepository<ArchiveHisto, Long> {
}