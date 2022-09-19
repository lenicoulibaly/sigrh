package dgmp.sigrh.structuremodule.controller.repositories.post;

import dgmp.sigrh.structuremodule.model.entities.post.Post;
import dgmp.sigrh.structuremodule.model.entities.post.PostGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PostRepo extends JpaRepository<Post, Long>
{
    @Query("select (count(p) > 0) from Post p where p.postGroup.postGroupId = ?1")
    boolean existsByPostGroup(Long postGroupId);

    @Query("select count(p) from Post p where p.postGroup.postGroupId = ?1 and p.status = 'ACTIVE'")
    long countByPostGroupActive(Long postGroupId);
    @Query("select count(p) from Post p where p.postGroup.postGroupId = ?1 and p.vacant = true and p.status = 'ACTIVE'")
    long countVacantPostsByPostGroup(Long postGroupId);
    @Query("select count(p) from Post p where p.postGroup.postGroupId = ?1 and p.vacant = false and p.status = 'ACTIVE'")
    long countNoneVacantPostsByPostGroup(Long postGroupId);

    @Query("select count(p) from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%')) and p.status = 'ACTIVE')")
    long countPosts(String key);

    @Query("select count(p) from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%')) or p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2)) and p.status = 'ACTIVE'")
    long countPostsWithEmplois(String key, Set<Long> emplpoiIds);

    @Query("select count(p) from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%'))) and p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2) and p.status = 'ACTIVE'")
    long countPostsInEmplois(String key, Set<Long> emplpoiIds);

    @Query("select count(p) from Post p where p.postGroup.structure.strId = ?1 and p.status ='ACTIVE'")
    long countActiveByStr(long strId);

    @Query("select count(p) from Post p where p.postGroup.structure.strId = ?1 and p.vacant = true and p.status ='ACTIVE'")
    long countVacantByStr(long strId);

    @Query("select count(p) from Post p where p.postGroup.structure.strId = ?1 and p.vacant = false and p.status ='ACTIVE'")
    long countNoneVacantByStr(long strId);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%'))) and p.status = 'ACTIVE'")
    Page<PostGroup> searchPosts(String key, Pageable pageable);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%'))) and p.status = 'ACTIVE'")
    List<PostGroup> searchPosts(String key);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%')) or p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2)) and p.status = 'ACTIVE'")
    Page<PostGroup> searchPostsWithEmplois(String key, Set<Long> emplpoiIds, Pageable pageable);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%'))) and p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2) and p.status = 'ACTIVE'")
    Page<PostGroup> searchPostsInEmplois(String key, Set<Long> emplpoiIds, Pageable pageable);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%')) or p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2)) and p.status = 'ACTIVE'")
    List<PostGroup> searchPostsWithEmplois(String key, Set<Long> emplpoiIds);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%'))) and p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2) and p.status = 'ACTIVE'")
    List<PostGroup> searchPostsInEmplois(String key, Set<Long> emplpoiIds);


    @Query("select p from PostGroup p left join Post p1 left join Structure s on p.postGroupId = p1.postGroup.postGroupId and p.structure.strId = s.strId where " +
            "(upper(p.intitule) like upper(concat('%', ?1, '%')) or " +
            "upper(p.postDescription) like upper(concat('%', ?1, '%')) or " +
            "upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or " +
            "upper(p.structure.strName) like upper(concat('%', ?1, '%')) or " +
            "upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%'))) " +
            "and p1.vacant =true and p1.status = 'ACTIVE' and p.status = 'ACTIVE'")
    Page<PostGroup> searchVacantPosts(String key, Pageable pageable);

    @Query("select p from PostGroup p left join Post p1 left join Structure s on p.postGroupId = p1.postGroup.postGroupId and p.structure.strId = s.strId where " +
            "(upper(p.intitule) like upper(concat('%', ?1, '%')) or " +
            "upper(p.postDescription) like upper(concat('%', ?1, '%')) or " +
            "upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or " +
            "upper(p.structure.strName) like upper(concat('%', ?1, '%')) or " +
            "upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%'))) " +
            "and p1.vacant =true and p1.status = 'ACTIVE' and p.status = 'ACTIVE'")
    List<PostGroup> searchVacantPosts(String key);

    @Query("select p from PostGroup p left join Post p1 left join Structure s on p.postGroupId = p1.postGroup.postGroupId and p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%')) or p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2))" +
            "and p1.vacant =true and p1.status = 'ACTIVE' and p.status = 'ACTIVE'")
    Page<PostGroup> searchVacantPostsWithEmplois(String key, Set<Long> emplpoiIds, Pageable pageable);

    @Query("select p from PostGroup p left join Post p1 left join Structure s on p.postGroupId = p1.postGroup.postGroupId and p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%'))) and p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2) " +
            "and p1.vacant =true and p1.status = 'ACTIVE' and p.status = 'ACTIVE'")
    Page<PostGroup> searchVacantPostsInEmplois(String key, Set<Long> emplpoiIds, Pageable pageable);

    @Query("select p from PostGroup p left join Post p1 left join Structure s on p.postGroupId = p1.postGroup.postGroupId and p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%')) or p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2))" +
            "and p1.vacant =true and p1.status = 'ACTIVE' and p.status = 'ACTIVE'")
    List<PostGroup> searchVacantPostsWithEmplois(String key, Set<Long> emplpoiIds);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', ?1, '%')) or upper(p.postDescription) like upper(concat('%', ?1, '%')) or upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or upper(p.structure.strName) like upper(concat('%', ?1, '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%'))) and p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2) " +
            "and p.status = 'ACTIVE'")
    List<PostGroup> searchVacantPostsInEmplois(String key, Set<Long> emplpoiId);
}