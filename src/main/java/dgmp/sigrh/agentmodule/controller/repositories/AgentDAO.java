package dgmp.sigrh.agentmodule.controller.repositories;

import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.agentmodule.model.projections.AgentInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgentDAO extends JpaRepository<Agent, Long>
{
    @Query("select (count(a) > 0) from Agent a where upper(a.email) = upper(:email) or upper(a.emailPro) = upper(:email)")
    boolean existsByEmail(@Param("email") String email);

    @Query("select (count(a) > 0) from Agent a where upper(a.email) = upper(:email) and a.idAgent <> :idAgent")
    boolean existsByEmail(@Param("email") String email, @Param("idAgent") Long idAgent);

    @Query("select (count(a) > 0) from Agent a where upper(a.emailPro) = upper(:emailPro) or upper(a.email) = upper(:emailPro)")
    boolean existsByEmailPro(@Param("emailPro") String emailPro);

    @Query("select (count(a) > 0) from Agent a where upper(a.emailPro) = upper(:emailPro) and upper(a.idAgent) <> upper(:idAgent)")
    boolean existsByEmailPro(@Param("emailPro") String emailPro, @Param("idAgent") Long idAgent);



    @Query("select (count(a) > 0) from Agent a where upper(a.tel) = upper(:tel)")
    boolean existsByTel(@Param("tel") String tel);

    @Query("select (count(a) > 0) from Agent a where upper(a.tel) = upper(:tel) and a.idAgent <> :idAgent")
    boolean existsByTel(@Param("tel") String tel, @Param("idAgent") Long idAgent);

    @Query("select (count(a) > 0) from Agent a where upper(a.fixeBureau) = upper(:fixeBureau)")
    boolean existsByFixeBureau(@Param("fixeBureau") String fixeBureau);

    @Query("select (count(a) > 0) from Agent a where upper(a.fixeBureau) = upper(:fixeBureau) and a.idAgent <> :idAgent")
    boolean existsByFixeBureau(@Param("fixeBureau") String fixeBureau, @Param("idAgent") Long idAgent);


    @Query("select (count(a) > 0) from Agent a where upper(a.numPiece) = upper(:numPiece) and upper(a.typePiece) = upper(:typePiece)")
    boolean existsByNumPiece(@Param("numPiece") String numPiece, @Param("typePiece") String typePiece);
    @Query("select (count(a) > 0) from Agent a where upper(a.numPiece) = upper(:numPiece)")
    boolean existsByNumPiece(@Param("numPiece") String numPiece);

    @Query("select (count(a) > 0) from Agent a where upper(a.numPiece) = upper(:numPiece) and a.idAgent <> :idAgent")
    boolean existsByNumPiece(@Param("numPiece") String numPiece, @Param("idAgent") Long idAgent);

    @Query("select (count(a) > 0) from Agent a where upper(a.numPiece) = upper(:numPiece) and upper(a.typePiece) = upper(:typePiece) and a.idAgent <> :idAgent")
    boolean existsByNumPiece(@Param("numPiece") String numPiece, @Param("typePiece") String typePiece, @Param("idAgent") Long idAgent);

    @Query("select (count(a) > 0) from Agent a where upper(a.numBadge) = upper(:numBadge)")
    boolean existsByNumBadge(@Param("numBadge") String numBadge);

    @Query("select (count(a) > 0) from Agent a where upper(a.numBadge) = upper(:numBadge) and a.idAgent <> :idAgent")
    boolean existsByNumBadge(@Param("numBadge") String numBadge, @Param("idAgent") Long idAgent);

    @Query("select (count(a) > 0) from Agent a where upper(a.matricule) = upper(:matricule)")
    boolean existsByMatricule(@Param("matricule") String matricule);

    @Query("select (count(a) > 0) from Agent a where upper(a.matricule) = upper(:matricule) and a.idAgent <> :idAgent")
    boolean existsByMatricule(@Param("matricule") String matricule, @Param("idAgent") Long idAgent);

    @Query("select a from Agent a order by a.nom, a.prenom")
    List<AgentInfo> getAllAgentsInfoPage(Pageable pageable);

    @Query("select count(a) from Agent a where a.emploi.idEmploi = ?1")
    long countByEmploi(Long idEmploi);

    @Query("select count(a) from Agent a where a.fonction.idFonction = ?1")
    long countByFonction(Long idFonction);

    @Query("select count(a) from Agent a where a.grade.idGrade = ?1")
    long countByGrade(Long idGrade);









}