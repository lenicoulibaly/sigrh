package dgmp.sigrh.structuremodule.controller.repositories.post;

import dgmp.sigrh.structuremodule.model.entities.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long>
{

}