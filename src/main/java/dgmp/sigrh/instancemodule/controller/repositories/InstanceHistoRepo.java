package dgmp.sigrh.instancemodule.controller.repositories;

import dgmp.sigrh.instancemodule.model.entities.InstanceHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstanceHistoRepo extends JpaRepository<InstanceHisto, Long> {
}