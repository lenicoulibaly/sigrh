package dgmp.sigrh.grademodule.controller.repositories;

import dgmp.sigrh.grademodule.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, Long> {
}