package dgmp.sigrh.structuremodule.controller.repositories.post;

import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.model.entities.post.PostParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PostParamRepo extends JpaRepository<PostParam, Long>
{
    @Query("select (count(p) > 0) from PostParam p where p.postGroupId = ?1 and p.emploiId = ?2")
    boolean existsByPostAndEmploi(Long postGroupId, Long emploiId);

    @Query("select (count(p) > 0) from PostParam p where p.postGroupId = ?1 and p.emploiId = ?2 and p.status = 'ACTIVE'")
    boolean existsByPostAndEmploiActive(Long postGroupId, Long emploiId, PersistenceStatus status);

    @Query("select (count(p) > 0) from PostParam p where p.postGroupId = ?1 and p.emploiId = ?2 and p.status = ?3")
    boolean existsByPostAndEmploi(Long postGroupId, Long emploiId, PersistenceStatus status);

    @Query("select p from PostParam p where p.postGroupId = ?1 and p.emploiId = ?2 and p.status = ?3")
    PostParam findByPostAndEmploi(Long postGroupId, Long emploiId, PersistenceStatus status);

    @Query("select p from PostParam p where p.postGroupId = ?1 and p.emploiId = ?2 and p.status = 'ACTIVE'")
    PostParam findByPostAndEmploiActive(Long postGroupId, Long emploiId);

    @Query("select p from PostParam p where p.postGroupId = ?1 and p.emploiId = ?2")
    PostParam findByPostAndEmploi(Long postGroupId, Long emploiId);

    @Query("select pp.emploiId from PostParam pp where pp.postGroupId = ?1")
    Set<Long> getEmploiIdsByPost(Long postGroupId);
}