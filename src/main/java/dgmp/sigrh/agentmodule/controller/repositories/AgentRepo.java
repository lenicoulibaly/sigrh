package dgmp.sigrh.agentmodule.controller.repositories;

import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.agentmodule.model.enums.Civility;
import dgmp.sigrh.agentmodule.model.enums.EtatRecrutement;
import dgmp.sigrh.agentmodule.model.enums.TypeAgent;
import dgmp.sigrh.agentmodule.model.projections.AgentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgentRepo extends JpaRepository<Agent, Long>
{
    @Query("select (count(a) > 0) from Agent a where upper(a.email) = upper(:email) or upper(a.emailPro) = upper(:email)")
    boolean existsByEmail(@Param("email") String email);

    @Query("select (count(a) > 0) from Agent a where upper(a.email) = upper(:email) and a.agentId <> :idAgent")
    boolean existsByEmail(@Param("email") String email, @Param("idAgent") Long idAgent);

    @Query("select (count(a) > 0) from Agent a where upper(a.emailPro) = upper(:emailPro)")
    boolean existsByEmailPro(@Param("emailPro") String emailPro);

    @Query("select (count(a) > 0) from Agent a where upper(a.emailPro) = upper(:emailPro) and a.agentId <> :idAgent")
    boolean existsByEmailPro(@Param("emailPro") String emailPro, @Param("idAgent") Long idAgent);

    @Query("select concat(a.nom, ' ', a.prenom) from Agent a where a.user.userId = ?1")
    String getFullNameByUserId(Long userId);

    @Query("select a.matricule from Agent a where a.user.userId = ?1")
    String getMatriculeUserId(Long userId);

    @Query("select a.email from Agent a where a.agentId = ?1")
    String getEmailByAgtId(Long agtId);

    @Query("select (count(a) > 0) from Agent a where upper(a.tel) = upper(:tel)")
    boolean existsByTel(@Param("tel") String tel);

    @Query("select (count(a) > 0) from Agent a where upper(a.tel) = upper(:tel) and a.agentId <> :idAgent")
    boolean existsByTel(@Param("tel") String tel, @Param("idAgent") Long idAgent);

    @Query("select (count(a) > 0) from Agent a where upper(a.fixeBureau) = upper(:fixeBureau)")
    boolean existsByFixeBureau(@Param("fixeBureau") String fixeBureau);

    @Query("select (count(a) > 0) from Agent a where upper(a.fixeBureau) = upper(:fixeBureau) and a.agentId <> :idAgent")
    boolean existsByFixeBureau(@Param("fixeBureau") String fixeBureau, @Param("idAgent") Long idAgent);


    @Query("select (count(a) > 0) from Agent a where upper(a.numPiece) = upper(:numPiece) and upper(a.typePiece) = upper(:typePiece)")
    boolean existsByNumPiece(@Param("numPiece") String numPiece, @Param("typePiece") String typePiece);
    @Query("select (count(a) > 0) from Agent a where upper(a.numPiece) = upper(:numPiece)")
    boolean existsByNumPiece(@Param("numPiece") String numPiece);

    @Query("select (count(a) > 0) from Agent a where upper(a.numPiece) = upper(:numPiece) and a.agentId <> :idAgent")
    boolean existsByNumPiece(@Param("numPiece") String numPiece, @Param("idAgent") Long idAgent);

    @Query("select (count(a) > 0) from Agent a where upper(a.numPiece) = upper(:numPiece) and upper(a.typePiece) = upper(:typePiece) and a.agentId <> :idAgent")
    boolean existsByNumPiece(@Param("numPiece") String numPiece, @Param("typePiece") String typePiece, @Param("idAgent") Long idAgent);

    @Query("select (count(a) > 0) from Agent a where upper(a.numBadge) = upper(:numBadge)")
    boolean existsByNumBadge(@Param("numBadge") String numBadge);

    @Query("select (count(a) > 0) from Agent a where upper(a.numBadge) = upper(:numBadge) and a.agentId <> :idAgent")
    boolean existsByNumBadge(@Param("numBadge") String numBadge, @Param("idAgent") Long idAgent);

    @Query("select (count(a) > 0) from Agent a where upper(a.matricule) = upper(:matricule)")
    boolean existsByMatricule(@Param("matricule") String matricule);

    @Query("select (count(a) > 0) from Agent a where upper(a.matricule) = upper(:matricule) and a.agentId <> :idAgent")
    boolean existsByMatricule(@Param("matricule") String matricule, @Param("idAgent") Long idAgent);

    @Query("select a from Agent a order by a.nom, a.prenom")
    List<AgentInfo> getAllAgentsInfoPage(Pageable pageable);

    @Query("select count(a) from Agent a where a.emploi.idEmploi = ?1")
    long countByEmploi(Long idEmploi);

    @Query("select count(a) from Agent a where a.fonction.idFonction = ?1")
    long countByFonction(Long idFonction);

    @Query("select count(a) from Agent a where a.grade.idGrade = ?1")
    long countByGrade(Long idGrade);

    @Query("select count(a) from Agent a where a.structure.strId = ?1 and a.status = 'ACTIVE'")
    long countActiveByStructure(long agtId);

    @Query("select count(a) from Agent a where a.structure.strId = ?1 and a.etatRecrutement in ?2 and a.status = 'ACTIVE'")
    long countAgentsByStrAndEtat(long strId, List<EtatRecrutement> states);

    @Query("select a from Agent a where a.structure.strId = ?1 and a.etatRecrutement in ?2 and a.status = 'ACTIVE'")
    List<Agent> getAgentsByStrAndEtat(long strId, List<EtatRecrutement> states);

    @Query("select a from Agent  a where locate(concat(function('getStrCode', ?1) , '/') , a.structure.strCode) = 1 and a.etatRecrutement in ?2 and a.status = 'ACTIVE'")
    Page<Agent> findAgentByStrAndEtat(long strId, List<EtatRecrutement> states, Pageable pageable);

    @Query("select a from Agent  a left join Structure s on a.structure.strId = s.strId where (locate(concat(function('getStrCode', ?1) , '/') , a.structure.strCode) = 1 or s.strId = ?1)and a.etatRecrutement in ?2 and a.status = 'ACTIVE' and " +
            "(upper(function('strip_accents', a.nom) ) like concat('%', coalesce(?3, ''), '%' ) or " +
            "upper(function('strip_accents', a.prenom) ) like concat('%', coalesce(?3, ''), '%' ) or " +
            "upper(function('strip_accents', a.email) ) like concat('%', coalesce(?3, ''), '%' ) or " +
            "upper(function('strip_accents', a.emailPro) ) like concat('%', coalesce(?3, ''), '%' ) or " +
            "upper(function('strip_accents', a.matricule) ) like concat('%', coalesce(?3, ''), '%' ) or " +
            "upper(function('strip_accents', a.titre) ) like concat('%', coalesce(?3, ''), '%' ) or " +
            "upper(function('strip_accents', a.tel) ) like concat('%', coalesce(?3, ''), '%' ) )")
    Page<Agent> searchAgentByStrAndEtat(long strId, List<EtatRecrutement> states, String key, Pageable pageable);

    @Query("select a from Agent  a left join Structure s on a.structure.strId = s.strId left join Fonction f on a.fonction.idFonction = f.idFonction where (locate(concat(function('getStrCode', :strId) , '/') , s.strCode) = 1 or s.strId = :strId) and a.etatRecrutement in :states and " +
            "a.civilite in (:civilities) and " +
            "a.typeAgent in (:typeAgents) and " +
            "f.idFonction in (:fonctionsIds) and " +
            "a.grade.idGrade in (:gradesIds) and " +
            "a.emploi.idEmploi in (:emploisIds) and " +
            "a.status = 'ACTIVE' and " +
            "(upper(function('strip_accents', a.nom) ) like concat('%', coalesce(:searchKey, '') , '%' ) or " +
            "upper(function('strip_accents', a.prenom) ) like concat('%', coalesce(:searchKey, ''), '%' ) or " +
            "upper(function('strip_accents', a.email) ) like concat('%', coalesce(:searchKey, ''), '%' ) or " +
            "upper(function('strip_accents', a.emailPro) ) like concat('%', coalesce(:searchKey, ''), '%' ) or " +
            "upper(function('strip_accents', a.matricule) ) like concat('%', coalesce(:searchKey, ''), '%' ) or " +
            "upper(function('strip_accents', a.titre) ) like concat('%', coalesce(:searchKey, ''), '%' ) or " +
            "upper(function('strip_accents', a.tel) ) like concat('%', coalesce(:searchKey, ''), '%' ) )")
    Page<Agent> searchAgentsMultiCriteres(@Param("strId") long strId, @Param("searchKey") String key, @Param("states") List<EtatRecrutement> states,
                                        @Param("civilities") List<Civility> civilities,
                                        @Param("typeAgents") List<TypeAgent> typeAgents,
                                        @Param("fonctionsIds") List<Long> fonctionsIds,
                                        @Param("gradesIds") List<Long> gradesIds,
                                        @Param("emploisIds") List<Long> emploisIds,
                                       Pageable pageable);

    @Query("select a from Agent  a left join Structure s on a.structure.strId = s.strId left join Fonction f on a.fonction.idFonction = f.idFonction where (locate(concat(function('getStrCode', :strId) , '/') , a.structure.strCode) = 1 or s.strId = :strId) and a.status = 'ACTIVE' and " +
            "(upper(function('strip_accents', a.nom) ) like concat('%', coalesce(:searchKey, ''), '%' ) or " +
            "upper(function('strip_accents', a.prenom) ) like concat('%', coalesce(:searchKey, ''), '%' ) or " +
            "upper(function('strip_accents', a.email) ) like concat('%', coalesce(:searchKey, ''), '%' ) or " +
            "upper(function('strip_accents', a.emailPro) ) like concat('%', coalesce(:searchKey, ''), '%' ) or " +
            "upper(function('strip_accents', a.matricule) ) like concat('%', coalesce(:searchKey, ''), '%' ) or " +
            "upper(function('strip_accents', a.titre) ) like concat('%', coalesce(:searchKey, ''), '%' ) or " +
            "upper(function('strip_accents', a.tel) ) like concat('%', coalesce(:searchKey, ''), '%' ) )")
    Page<Agent> searchAgents(@Param("strId")Long visibilityId, @Param("searchKey") String key, Pageable pageable);

    @Query("select a.user.userId from Agent  a where a.agentId = ?1")
    Long getUserId(Long idAgent);

    @Query("select a.agentId from Agent a where a.user.userId = ?1")
    Long getAgtIdByUserId(Long userId);

    @Query("select concat(a.nom, '_', a.prenom) from Agent a where a.agentId = ?1")
    String getNameToSnackFormatByAgtId(Long agtId);

    Agent findByMatricule(String matricule);
    Agent findByEmail(String email);
    Agent findByEmailPro(String emailPro);
    Agent findByTel(String tel);
}