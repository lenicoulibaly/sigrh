package dgmp.sigrh.auth.controller.repositories;

import dgmp.sigrh.auth.model.entities.PrivilegeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeGroupDAO extends JpaRepository<PrivilegeGroup, Long> {
}