package dgmp.sigrh.fonctionmodule.controller.repositories;

import dgmp.sigrh.fonctionmodule.model.entities.Fonction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FonctionDAO extends JpaRepository<Fonction, Long> {
    @Query("select (count(f) > 0) from Fonction f where upper(f.nomFonction) = upper(?1)")
    boolean existsByNomFonction(String nomFonction);

    @Query("select (count(f) > 0) from Fonction f where f.idFonction <> ?1 and upper(f.nomFonction) = upper(?2)")
    boolean existsByNomFonction(Long idFonction, String nomFonction);

    @Query("select f from Fonction f where upper(f.nomFonction) like upper(concat('%', ?1, '%')) and f.status='ACTIVE'")
    Page<Fonction> searchPageFonction(String searchKey, PageRequest of);

    @Query("select count(f) from Fonction f where upper(f.nomFonction) like upper(concat('%', ?1, '%'))")
    long countBySearchKey(String nomFonction);

    @Query("select count(f) from Fonction f where upper(f.nomFonction) like upper(concat('%', ?1, '%')) and f.status = 'ACTIVE'")
    long countActiveBySearchKey(String nomFonction);

    @Query("select f from Fonction f where f.status = 'DELETED'")
    List<Fonction> getDeletedFonctions();


    @Query("select f from Fonction f where upper(f.nomFonction) like upper(concat('%', ?1, '%')) and f.status='DELETED'")
    Page<Fonction> searchDeletedPageFonction(String searchKey, PageRequest of);

    @Query("select count(f) from Fonction f where upper(f.nomFonction) like upper(concat('%', ?1, '%')) and f.status = 'DELETED'")
    long countDeletedBySearchKey(String searchKey);
}