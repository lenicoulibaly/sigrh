package dgmp.sigrh.structuremodule.controller.repositories.post;

import dgmp.sigrh.structuremodule.model.entities.post.PostHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHistoRepo extends JpaRepository<PostHisto, Long> {
}