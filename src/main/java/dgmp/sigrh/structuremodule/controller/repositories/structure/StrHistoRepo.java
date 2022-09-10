package dgmp.sigrh.structuremodule.controller.repositories.structure;

import dgmp.sigrh.structuremodule.model.entities.structure.StructureHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StrHistoRepo extends JpaRepository<StructureHisto, Long> {
}