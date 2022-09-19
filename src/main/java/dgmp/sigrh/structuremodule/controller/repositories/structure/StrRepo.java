package dgmp.sigrh.structuremodule.controller.repositories.structure;

import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface StrRepo extends JpaRepository<Structure, Long>
{
    @Query("select s from Structure s where s.strParent.strId = ?1 and s.status = 'ACTIVE'")
    List<Structure> findByStrParent(Long strId);
    @Query("select s from Structure s where upper(s.strName) like upper(concat('%', ?1, '%')) or coalesce(upper(s.strSigle),'')  like upper(concat('%', ?1, '%')) or s.strLevel = ?1 order by s.strName")
    Page<Structure> searchStructure(String searchKey, Pageable pageable);

    @Query("select s from Structure s where (upper(s.strName) like upper(concat('%', ?1, '%')) or upper(s.strSigle) like upper(concat('%', ?1, '%')) or upper(s.strTel) like upper(concat('%', ?1, '%')) or upper(s.strAddress) like upper(concat('%', ?1, '%')) or upper(s.situationGeo) like upper(concat('%', ?1, '%'))) and s.status = ?2 order by s.strName")
    Page<Structure> searchStr(String key, PersistenceStatus status, Pageable pageable);

    @Query("select s from Structure s where (upper(s.strName) like upper(concat('%', ?1, '%')) or upper(s.strSigle) like upper(concat('%', ?1, '%')) or upper(s.strTel) like upper(concat('%', ?1, '%')) or upper(s.strAddress) like upper(concat('%', ?1, '%')) or upper(s.situationGeo) like upper(concat('%', ?1, '%'))) and s.strType.typeId = ?2 and s.status = ?3 order by s.strName")
    Page<Structure> searchStrByType(String key, Long typeId, PersistenceStatus status, Pageable pageable);

    // Structure dont le type a pour sous type, le type passé en paramètre
    @Query("select s from Structure s where s.strType.typeId in (select tp.parent.typeId from TypeParam tp where tp.child.typeId = ?1 and tp.status = 'ACTIVE') and s.status = 'ACTIVE'")
    List<Structure> findByChildType(Long childTypeId);

    @Query("select ( count(str) > 0) from Structure str where str.strParent.strId = ?1 and str.strId = ?2")
    boolean isDirectParentOf(long parentId, long childId);

    @Query("select ( count(str) > 0) from Structure str where str.strParent.strId = ?2 and str.strId = ?1")
    boolean isDirectChildOf(long childId, long parentId);

    @Query("select str.strParent.strId from Structure  str where str.strId = ?1")
    Long getStrParentId(long strId);

    @Query("select s.strLevel from Structure s where s.strId = ?1")
    long getStrLevel(Long strId);

    @Query("select str.strId from Structure  str where str.strParent.strId = ?1 and str.status = 'ACTIVE'")
    Set<Long> getStrChildrenIds(long strId);

    @Query("select count(s) from Structure s where s.status = ?1")
    long countByStatus(PersistenceStatus status);

    @Query("select count(s) from Structure s where s.strType.typeId = ?1 and s.status = ?2")
    long countByType(Long typeId, PersistenceStatus status);

    @Query("select count(s) from Structure s where s.strParent.strId = ?1 and s.status = ?2")
    long countDirectChildren(Long strId, PersistenceStatus status);

    @Query("select (count(tp)>0) from TypeParam tp where tp.parent.typeId = (select s.strType.typeId from Structure  s where s.strId = ?1) and tp.child.typeId = ?2 and tp.status = 'ACTIVE'")
    boolean parentHasCompatibleSousType(Long strParentId, Long sousTypeId);
}
