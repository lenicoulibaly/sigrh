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

    @Query("select count(p) from PostGroup p left join Structure s on p.structure.strId = s.strId where " +
           "(upper(p.intitule) like upper(concat('%', coalesce(?1, ''), '%')) or " +
           "upper(p.postDescription) like upper(concat('%', coalesce(?1, ''), '%')) or " +
           "upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?1, ''), '%')) or " +
           "upper(p.structure.strName) like upper(concat('%', coalesce(?1, ''), '%')) or " +
           "upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?1, ''), '%'))) and p.status = 'ACTIVE'")
    long countPosts(String key);

    @Query("select count(p) from PostGroup p left join Structure s on p.structure.strId = s.strId where " +
            "(s.strId = ?1 or locate(concat(function('getStrCode', ?1), '/'), s.strCode) = 1) and "+
            "(upper(p.intitule) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.postDescription) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.structure.strName) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?2, ''), '%'))) and p.status = 'ACTIVE'")
    long countPosts(long strId, String key);

    @Query("select count(p) from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.postDescription) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.structure.strName) like upper(concat('%', coalesce(?1, ''), '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?1, ''), '%')) or p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2)) and p.status = 'ACTIVE'")
    long countPostsWithEmplois(String key, Set<Long> emplpoiIds);

    @Query("select count(p) from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.postDescription) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.structure.strName) like upper(concat('%', coalesce(?1, ''), '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?1, ''), '%'))) and p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2) and p.status = 'ACTIVE'")
    long countPostsInEmplois(String key, Set<Long> emplpoiIds);


    @Query("select count(p) from Post p where (locate(concat(function('getStrCode', ?1), '/') , p.postGroup.structure.strCode)= 1 or p.postGroup.structure.strId = ?1) and p.vacant = true and p.status ='ACTIVE'")
    long countVacantByStr(long strId);

    @Query("select count(p) from Post p where (locate(concat(function('getStrCode', ?1), '/'), p.postGroup.structure.strCode) = 1 or p.postGroup.structure.strId = ?1) and p.postGroup.structure.strId = ?1 and p.vacant = false and p.status ='ACTIVE'")
    long countNoneVacantByStr(long strId);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where " +
            "(upper(function('strip_accents', p.intitule) ) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(function('strip_accents', p.postDescription)) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(function('strip_accents', p.fonction.nomFonction)) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(function('strip_accents', p.structure.strName)) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(coalesce(function('strip_accents', p.structure.strSigle), '') ) like upper(concat('%', coalesce(?1, ''), '%'))) and " +
            "p.status = 'ACTIVE'")
    Page<PostGroup> searchPostGroups(String key, Pageable pageable);



    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.postDescription) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.structure.strName) like upper(concat('%', coalesce(?1, ''), '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?1, ''), '%')) or p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2)) and p.status = 'ACTIVE'")
    Page<PostGroup> searchPostsWithEmplois(String key, Set<Long> emplpoiIds, Pageable pageable);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.postDescription) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.structure.strName) like upper(concat('%', coalesce(?1, ''), '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?1, ''), '%'))) and p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2) and p.status = 'ACTIVE'")
    Page<PostGroup> searchPostsInEmplois(String key, Set<Long> emplpoiIds, Pageable pageable);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.postDescription) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.structure.strName) like upper(concat('%', coalesce(?1, ''), '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?1, ''), '%')) or p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2)) and p.status = 'ACTIVE'")
    List<PostGroup> searchPostsWithEmplois(String key, Set<Long> emplpoiIds);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where (upper(p.intitule) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.postDescription) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?1, ''), '%')) or upper(p.structure.strName) like upper(concat('%', coalesce(?1, ''), '%')) or upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?1, ''), '%'))) and p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2) and p.status = 'ACTIVE'")
    List<PostGroup> searchPostsInEmplois(String key, Set<Long> emplpoiIds);


    @Query("select pg from PostGroup pg, Structure s where pg.status = 'ACTIVE' and " +
            "(select count(p) from Post p where p.vacant = true and p.postGroup.postGroupId = pg.postGroupId and p.status = 'ACTIVE')>0")
    Page<PostGroup> findPostGroupHavingVacantPosts(Pageable pageable);

    @Query("select count(pg) from PostGroup pg, Structure s where pg.status = 'ACTIVE' and " +
           "(select count(p) from Post p where p.vacant = true and p.postGroup.postGroupId = pg.postGroupId and p.status = 'ACTIVE')>0")
    long countPostGroupHavingVacantPosts();

    @Query("select count(pg) from PostGroup pg where (?1 = pg.structure.strId or locate(concat(function('getStrCode', ?1), '/'), pg.structure.strCode) = 1) and pg.status = 'ACTIVE' and " +
            "(select count(p) from Post p where p.vacant = true and p.postGroup.postGroupId = pg.postGroupId and p.status = 'ACTIVE')>0")
    long countPostGroupHavingVacantPosts(long strId);

    @Query("select pg from PostGroup pg where (?1 = pg.structure.strId or locate(concat(function('getStrCode', ?1) , '/') , pg.structure.strCode) = 1) and pg.status = 'ACTIVE' and " +
            "(select count(p) from Post p where p.vacant = true and p.postGroup.postGroupId = pg.postGroupId and p.status = 'ACTIVE')>0")
    Page<PostGroup> findPostGroupHavingVacantPosts(long strId, Pageable pageable);

    @Query("select pg from PostGroup pg where (?1 = pg.structure.strId or locate(concat(function('getStrCode', ?1) , '/') , pg.structure.strCode) = 1) and pg.status = 'ACTIVE'")
    Page<PostGroup> findPostGroupByStr(long strId, Pageable pageable);

    @Query("select count(pg) from PostGroup pg where (?1 = pg.structure.strId or locate(concat(function('getStrCode', ?1) , '/') , pg.structure.strCode) = 1) and pg.status = 'ACTIVE'")
    long countPostGroupByStr(long strId);


    @Query("select count(pg) from PostGroup pg left join Structure s on (pg.structure.strId = s.strId) where " +
            "(s.strId = ?1 or locate(concat(function('getStrCode', ?1) , '/'), s.strCode) = 1) and "+
            "(upper(function('strip_accents', pg.intitule)) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(function('strip_accents', pg.postDescription), coalesce(?2, '')) ) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(function('strip_accents', pg.fonction.nomFonction)) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(function('strip_accents', pg.structure.strName)) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(function('strip_accents', pg.structure.strSigle), '')) like upper(concat('%', coalesce(?2, ''), '%'))) and pg.status = 'ACTIVE'")
    long countPostGroups(long strId, String key);

    @Query("select pg from PostGroup pg left join Structure s on pg.structure.strId = s.strId where " +
            "(s.strId = ?1 or locate(concat(function('getStrCode', ?1), '/'), s.strCode) = 1) and "+
            "(upper(function('strip_accents', pg.intitule)) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(function('strip_accents', pg.postDescription), coalesce(?2, '')) ) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(function('strip_accents', pg.fonction.nomFonction)) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(function('strip_accents', pg.structure.strName)) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(function('strip_accents', pg.structure.strSigle), '')) like upper(concat('%', coalesce(?2, ''), '%'))) and pg.status = 'ACTIVE'")
    Page<PostGroup> searchPostGroups(long strId, String key, Pageable pageable);


    @Query("select count(pg) from PostGroup pg left join Post p on (pg.postGroupId = p.postGroup.postGroupId) left join Structure s on (pg.structure.strId = s.strId) where " +
            "(s.strId = ?1 or locate(concat(function('getStrCode', ?1) , '/'), s.strCode) = 1) and "+
            "(upper(function('strip_accents', pg.intitule)) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(function('strip_accents', pg.postDescription), coalesce(?2, '')) ) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(function('strip_accents', pg.fonction.nomFonction)) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(function('strip_accents', pg.structure.strName)) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(function('strip_accents', pg.structure.strSigle), '') ) like upper(concat('%', coalesce(?2, ''), '%'))) and " +
            "(select count(post) from Post post where post.postGroup.postGroupId = pg.postGroupId and post.vacant = true)>0 and " +
            "p.vacant =true and p.status = 'ACTIVE' and pg.status = 'ACTIVE'")
    long countPostGroupHavingVacantPosts(long strId, String key);

    @Query("select pg from PostGroup pg left join Post p left join Structure s on pg.postGroupId = p.postGroup.postGroupId and pg.structure.strId = s.strId where " +
            "(s.strId = ?1 or locate(concat(function('getStrCode', ?1), '/'), s.strCode) = 1) and "+
            "(upper(function('strip_accents', pg.intitule)) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(function('strip_accents', pg.postDescription), coalesce(?2, '')) ) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(function('strip_accents', pg.fonction.nomFonction)) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(function('strip_accents', pg.structure.strName)) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(function('strip_accents', pg.structure.strSigle), '') ) like upper(concat('%', coalesce(?2, ''), '%'))) and " +
            "(select count(post) from Post post where post.postGroup.postGroupId = pg.postGroupId and post.vacant = true)>0 and " +
            "p.vacant =true and p.status = 'ACTIVE' and pg.status = 'ACTIVE'")
    Page<PostGroup> searchPostGroupHavingVacantPosts(long strId, String key, Pageable pageable);


    @Query("select pg from PostGroup pg left join Post p1 left join Structure s on pg.postGroupId = p1.postGroup.postGroupId and pg.structure.strId = s.strId where " +
            "(upper(function('strip_accents', pg.intitule)) like upper(concat('%', ?1, '%')) or " +
            "upper(coalesce(function('strip_accents', pg.postDescription), ?1) ) like upper(concat('%', ?1, '%')) or " +
            "upper(function('strip_accents', pg.fonction.nomFonction)) like upper(concat('%', ?1, '%')) or " +
            "upper(function('strip_accents', pg.structure.strName)) like upper(concat('%', ?1, '%')) or " +
            "upper(coalesce(function('strip_accents', pg.structure.strSigle), '') ) like upper(concat('%', ?1, '%'))) " +
            "and p1.vacant =true and p1.status = 'ACTIVE' and pg.status = 'ACTIVE'")
    Page<PostGroup> searchVacantPosts(String key, Pageable pageable);

    /*@Query("select pg from PostGroup pg left join Post p left join Structure s on pg.postGroupId = p.postGroup.postGroupId and pg.structure.strId = s.strId where " +
            "(upper(pg.intitule) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(pg.postDescription) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(pg.fonction.nomFonction) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(pg.structure.strName) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(coalesce(pg.structure.strSigle, '') ) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "pg.postGroupId in (select .postGroupId from PostParam pp where pp.emploiId in ?2 and pp.status = 'ACTIVE')) and " +
            "p.vacant =true and p.status ='ACTIVE' and pg.status = 'ACTIVE'")
    Page<PostGroup> searchVacantPostsWithEmplois(String key, Set<Long> emplpoiIds, Pageable pageable);*/

    @Query("select p from PostGroup p left join Post p1 left join Structure s on p.postGroupId = p1.postGroup.postGroupId and p.structure.strId = s.strId where " +
            "(s.strId = ?1 or locate(concat(function('getStrCode', ?1), '/'), s.strCode) = 1) and "+
            "(upper(p.intitule) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.postDescription) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?2, ''), '%')) " +
            "or upper(p.structure.strName) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?3))" +
            "and p1.vacant =true and p1.status = 'ACTIVE' and p.status = 'ACTIVE'")
    Page<PostGroup> searchVacantPostsWithEmplois(long strId, String key, Set<Long> emplpoiIds, Pageable pageable);

    @Query("select p from PostGroup p left join Post p1 left join Structure s on p.postGroupId = p1.postGroup.postGroupId and p.structure.strId = s.strId where " +
            "(upper(p.intitule) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(p.postDescription) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(p.structure.strName) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?1, ''), '%'))) and p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2) and " +
            "p1.vacant =true and p1.status = 'ACTIVE' and p.status = 'ACTIVE'")
    Page<PostGroup> searchVacantPostsInEmplois(String key, Set<Long> emplpoiIds, Pageable pageable);

    /*@Query("select p from PostGroup p left join Post p1 left join Structure s on (p.postGroupId = p1.postGroup.postGroupId and p.structure.strId = s.strId) join Structure s2 on where " +
            "(p.structure.strId = ?1 or locate(concat(function('getStrCode', ?1), '/'), s.strCode) = 1) and "+
            "(upper(p.intitule) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.postDescription) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.structure.strName) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?2, ''), '%'))) and p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?3) " +
            "and p1.vacant =true and p1.status = 'ACTIVE' and p.status = 'ACTIVE'")
    Page<PostGroup> searchVacantPostsInEmplois(long strId, String key, Set<Long> emplpoiIds, Pageable pageable);*/
    /*
    @Query("select p from PostGroup p left join Post p1 left join Structure s on p.postGroupId = p1.postGroup.postGroupId and p.structure.strId = s.strId where " +
            "(upper(p.intitule) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(p.postDescription) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(p.structure.strName) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?1, ''), '%')) or " +
            "p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?2))" +
            "and p1.vacant =true and p1.status = 'ACTIVE' and p.status = 'ACTIVE'")
    List<PostGroup> searchVacantPostsWithEmplois(String key, Set<Long> emplpoiIds);

    @Query("select p from PostGroup p left join Post p1 left join Structure s on p.postGroupId = p1.postGroup.postGroupId and p.structure.strId = s.strId where " +
            "(s.strId = ?1 or locate(concat(function('getStrCode', ?1), '/'), s.strCode) = 1) and"+
            "(upper(p.intitule) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.postDescription) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.structure.strName) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?3))" +
            "and p1.vacant =true and p1.status = 'ACTIVE' and p.status = 'ACTIVE'")
    List<PostGroup> searchVacantPostsWithEmplois(long strId, String key, Set<Long> emplpoiIds);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where " +
            "(upper(p.intitule) like upper(concat('%', ?1, '%')) or " +
            "upper(p.postDescription) like upper(concat('%', ?1, '%')) or " +
            "upper(p.fonction.nomFonction) like upper(concat('%', ?1, '%')) or " +
            "upper(p.structure.strName) like upper(concat('%', ?1, '%')) or " +
            "upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', ?1, '%'))) and p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in coalesce(?2, '')) " +
            "and p.status = 'ACTIVE'")
    List<PostGroup> searchVacantPostsInEmplois(String key, Set<Long> emplpoiId);

    @Query("select p from PostGroup p left join Structure s on p.structure.strId = s.strId where " +
            "s.strCode = ?1 or locate(concat(?1 , '/'), s.strCode) = 1 and"+
            "(upper(p.intitule) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.postDescription) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.fonction.nomFonction) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(p.structure.strName) like upper(concat('%', coalesce(?2, ''), '%')) or " +
            "upper(coalesce(p.structure.strSigle, '') ) like upper(concat('%', coalesce(?2, ''), '%'))) and p.postGroupId in (select pp.postGroupId from PostParam pp where pp.emploiId in ?3) " +
            "and p.status = 'ACTIVE'")
    List<PostGroup> searchVacantPostsInEmplois(String strCode, String key, Set<Long> emplpoiId);*/

    @Query("select p from Post p where p.postGroup.postGroupId = ?1 and p.vacant = false and p.status = 'ACTIVE'")
    List<Post> findNoneVacantPostsByPostGroup(long pgId);

    @Query("select (count(p)>0) from Post p where p.postGroup.structure.strId = ?1 and p.postGroup.fonction.fonctionTopManager = true and p.postGroup.status = 'ACTIVE' and p.status = 'ACTIVE'")
    boolean strHasPostManager(Long strId);
}