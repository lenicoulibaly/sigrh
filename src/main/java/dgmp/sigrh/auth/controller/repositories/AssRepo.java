package dgmp.sigrh.auth.controller.repositories;

import dgmp.sigrh.auth.model.entities.Assignation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssRepo extends JpaRepository<Assignation, Long>
{
}
