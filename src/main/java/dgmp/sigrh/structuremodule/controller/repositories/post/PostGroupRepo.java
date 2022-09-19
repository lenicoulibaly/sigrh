package dgmp.sigrh.structuremodule.controller.repositories.post;

import dgmp.sigrh.structuremodule.model.entities.post.PostGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostGroupRepo extends JpaRepository<PostGroup, Long> {
}