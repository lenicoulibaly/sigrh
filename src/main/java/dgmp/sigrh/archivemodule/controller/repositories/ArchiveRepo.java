package dgmp.sigrh.archivemodule.controller.repositories;

import dgmp.sigrh.archivemodule.model.entities.Archive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArchiveRepo extends JpaRepository<Archive, Long>
{
    @Query("select a.path from Archive a where a.archiveId = ?1")
    String getArchivePath(Long archiveId);

    @Query("select a from Archive a where a.status = 'ACTIVE' and a.agent.agentId = ?1 and (a.archiveType.typeId in ?3 or locate(coalesce(?2, ''), coalesce(upper(function('strip_accents', a.description)), '')) >0  or locate(?2, coalesce(upper(function('strip_accents', a.archiveType.name)), '')) >0 )")
    Page<Archive> searchArchives(Long agtId, String key, List<Long> typeId, Pageable pageable);

    @Query("select (count(a)>0) from Archive a where a.archiveNum = ?1 and a.archiveType.uniqueCode = ?2")
    boolean existsByArchiveNumAndTyCode(String num, String typeCode);

    @Query("select (count(a)>0) from Archive a where a.archiveId <> ?1 and a.archiveNum = ?2 and a.archiveType.uniqueCode = ?3")
    boolean existsByArchiveNumAndTyCode(Long archiveId, String num, String typeCode);

    @Query("select (count(a)>0) from Archive a where a.agent.agentId = ?1 and a.archiveType.uniqueCode = ?2")
    boolean existsByAgtIdAndTyCode(Long agtId, String typeCode);
}