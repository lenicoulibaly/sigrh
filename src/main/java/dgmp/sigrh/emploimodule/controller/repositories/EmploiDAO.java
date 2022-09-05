package dgmp.sigrh.emploimodule.controller.repositories;

import dgmp.sigrh.emploimodule.model.entities.Emploi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EmploiDAO extends JpaRepository<Emploi, Long> {
    boolean existsByNomEmploi(String value);

    @Query("select (count(e) > 0) from Emploi e where e.idEmploi <> ?1 and upper(e.nomEmploi) = upper(?2)")
    boolean existsByNomEmploi(Long idEmploi, String nomEmploi);

    @Query("select e.nomEmploi from Emploi e where e.idEmploi = ?1")
    String getNomEmploi(Long idEmploi);



    @Query("select e from Emploi e where upper(e.nomEmploi) like upper(concat('%', ?1, '%')) and e.status='ACTIVE'")
    Page<Emploi> searchPageEmploi(String nomEmploi, Pageable pageable);

    @Query("select count(e) from Emploi e where upper(e.nomEmploi) like upper(concat('%', ?1, '%'))")
    long countBySearchKey(String nomEmploi);

    @Query("select count(e) from Emploi e where upper(e.nomEmploi) like upper(concat('%', ?1, '%')) and e.status='ACTIVE'")
    long countActiveBySearchKey(String nomEmploi);

    @Query("update Emploi e set e.nomEmploi = ?2 where e.idEmploi = ?1")
    @Modifying
    int updateEmploi(Long idemploi, String nomEmploi);


    @Query("select e from Emploi e where upper(e.nomEmploi) like upper(concat('%', ?1, '%')) and e.status='DELETED'")
    Page<Emploi> searchDeletedPageEmploi(String searchKey, PageRequest of);

    @Query("select count(e) from Emploi e where upper(e.nomEmploi) like upper(concat('%', ?1, '%')) and e.status='DELETED'")
    long countDeletedBySearchKey(String searchKey);
}