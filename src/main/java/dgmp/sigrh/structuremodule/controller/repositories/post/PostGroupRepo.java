package dgmp.sigrh.structuremodule.controller.repositories.post;

import dgmp.sigrh.structuremodule.model.entities.post.PostGroup;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostGroupRepo extends JpaRepository<PostGroup, Long>
{
    @Query("select pg from PostGroup  pg where pg.structure.strId = ?1 and pg.status = 'ACTIVE'")
    List<PostGroup> findByStr(Long strId);

    @Query("select pg.structure from PostGroup pg where pg.postGroupId = ?1")
    Structure findStrByPostGroup(long postGroupId);

    @Query("select pg.structure.strId from PostGroup pg where pg.postGroupId = ?1")
    long findStrIdByPostGroup(long postGroupId);
}