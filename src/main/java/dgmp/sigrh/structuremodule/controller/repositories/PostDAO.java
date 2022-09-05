package dgmp.sigrh.structuremodule.controller.repositories;

import dgmp.sigrh.structuremodule.model.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDAO extends JpaRepository<Post, Long> {
}