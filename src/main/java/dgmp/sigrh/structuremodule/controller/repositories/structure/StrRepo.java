package dgmp.sigrh.structuremodule.controller.repositories.structure;

import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.model.dtos.str.ChangeAnchorDTO;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.Set;

public interface StrRepo extends JpaRepository<Structure, Long>
{
    @Query("select s from Structure s where s.strParent.strId = ?1 and s.status = 'ACTIVE'")
    List<Structure> findByStrParent(Long strId);

    @Query("select s.strParent from Structure  s where s.strId = ?1")
    Structure getStrParent(long strId);


    List<Structure> findByStrLevel(long strLevel);

    @Query("select s from Structure  s where s.strLevel = ?1 and s.status = ?2")
    List<Structure> findByStrLevel(long strLevel, PersistenceStatus status);

    @Query("select s from Structure s where upper(s.strCode) like upper(concat(?1, '/%')) and s.status = 'ACTIVE' order by s.strCode ASC")
    Page<Structure> findAllChildren(String strCode, Pageable pageable);

    //@Query("select s from Structure s where upper(s.strCode) like upper(concat(?1, '/%')) and s.status = 'ACTIVE' order by s.strCode ASC")
    //List<Structure> findAllChildren(String strCode);

    @Query("select s from Structure s where (locate(concat(function('getStrCode', ?1), '/'), s.strCode) = 1 or s.strId = function('getStrCode', ?1)) and s.status = 'ACTIVE' order by s.strCode ASC")
    List<Structure> findAllChildren(long strId);

    @Query("select s from Structure s where (locate(concat(function('getStrCode', ?1), '/'), s.strCode) = 1 or s.strId = function('getStrCode', ?1)) and s.status = 'ACTIVE' order by s.strCode ASC")
    Page<Structure> findAllChildren(long strId, Pageable pageable);

    @Query("select count(s) from Structure s, Structure s2 where s2.strId = ?1 and (locate(concat(s2.strCode, '/'), s.strCode) = 1 ) and s.status = 'ACTIVE'")
    long countAllChildren(long strId);

    @Query("select count(s) from Structure s where (locate(concat(function('getStrCode', ?1), '/'), s.strCode) = 1 ) and s.status = ?2")
    long countAllChildren(long strId, PersistenceStatus status);

    @Query("select s from Structure s where locate(concat(function('getStrCode', ?1), '/') , s.strCode) = 1 and s.strLevel = ?2 and s.status = 'ACTIVE' order by s.strCode ASC")
    List<Structure> findChildrenByLevel(long strId, long strLevel);

    @Query("select s from Structure s where locate(concat(?1, '/') , s.strCode) = 1 and s.strLevel = ?2 and s.status = 'ACTIVE' order by s.strCode ASC")
    List<Structure> findChildrenByLevel(String strCode, long strLevel);

    //@Query("select s from Structure s, Structure s2 where s2.strId = ?1 and (locate(concat(s.strCode, '/') , s2.strCode) = 1 or s.strId = s2.strId)  and s.status = 'ACTIVE' order by s.strCode ASC")
    @Query("select s from Structure s where (locate(concat(s.strCode, '/') , function('getStrCode', ?1)) = 1 or s.strId = ?1)  and s.status = 'ACTIVE' order by s.strCode ASC")
    List<Structure> findAllParents(long strId);

    @Query("select s.strCode from Structure s where s.strId = ?1 and s.status ='ACTIVE'")
    String getStrCodeIfActive(long strId);

    @Query("select s.strCode from Structure s where s.strId = ?1 ")
    String getStrCode(long strId);

    @Query("select s.strName from Structure s where s.strId = ?1 ")
    String getStrName(long strId);

    @Procedure("getStrCode")
    String getStrCodeFromDbFunction(long strId);

    @Query("select (count(s)>0) from Structure s where s.strId = ?1 and s.status = 'ACTIVE'")
    boolean strIsActive(long strId);

    @Query("select s from Structure s where (coalesce(upper(s.strName), '') like upper(concat('%', ?1, '%')) or coalesce(upper(s.strSigle),'') like upper(concat('%', ?1, '%')) or coalesce(upper(s.strTel), '') like upper(concat('%', ?1, '%')) or coalesce(upper(s.strAddress), '') like upper(concat('%', ?1, '%')) or coalesce(upper(s.situationGeo), '') like upper(concat('%', ?1, '%')) or upper(s.strType.name) like upper(concat('%', ?1, '%'))) and s.status = ?2 order by s.strName")
    Page<Structure> searchStr(String key, PersistenceStatus status, Pageable pageable);

    @Query("select count(s) from Structure s where (coalesce(upper(s.strName), '') like upper(concat('%', ?1, '%')) or coalesce(upper(s.strSigle),'') like upper(concat('%', ?1, '%')) or coalesce(upper(s.strTel), '') like upper(concat('%', ?1, '%')) or coalesce(upper(s.strAddress), '') like upper(concat('%', ?1, '%')) or coalesce(upper(s.situationGeo), '') like upper(concat('%', ?1, '%')) or upper(s.strType.name) like upper(concat('%', ?1, '%'))) and s.status = ?2")
    long countStr(String key, PersistenceStatus status);

    @Query("select count(s) from Structure s where s.status = ?1")
    long countAllStr(PersistenceStatus status);

    @Query("select s from Structure s where s.strCode like concat(?1, '/%') and (coalesce(upper(s.strName), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strSigle), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strTel), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strAddress), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.situationGeo), '') like upper(concat('%', ?2, '%')) or upper(s.strType.name) like upper(concat('%', ?2, '%'))) and s.status = ?2 order by s.strName")
    Page<Structure> searchStr(String strCode, String key, Pageable pageable);

    @Query("select s from Structure s where (locate(function('getStrCode', ?1), concat(s.strCode, '/')) = 1 or s.strId = ?1) and (coalesce(upper(s.strName), ?2) like upper(concat('%', ?2, '%')) or coalesce(upper(s.strSigle), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strTel), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strAddress), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.situationGeo), '') like upper(concat('%', ?2, '%')) or upper(s.strType.name) like upper(concat('%', ?2, '%'))) and s.status = 'ACTIVE' order by s.strName")
    Page<Structure> searchStr(Long strId, String key, Pageable pageable);

    @Query("select count(s) from Structure s, Structure s2 where locate(concat(s.strCode, '/'), s2.strCode) = 1 and s2.strId = ?1 and (coalesce(upper(s.strName), ?2) like upper(concat('%', ?2, '%')) or coalesce(upper(s.strSigle), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strTel), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strAddress), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.situationGeo), '') like upper(concat('%', ?2, '%')) or upper(s.strType.name) like upper(concat('%', ?2, '%'))) and s.status = 'ACTIVE'")
    long countStr(Long strId, String key);

    @Query("select s from Structure s where (locate(function('getStrCode', ?1), concat(s.strCode, '/')) = 1 or s.strId = ?1) and (coalesce(upper(s.strName), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strSigle), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strTel), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strAddress), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.situationGeo), '') like upper(concat('%', ?2, '%')) or upper(s.strType.name) like upper(concat('%', ?2, '%'))) and s.status = ?3 order by s.strName")
    Page<Structure> searchStr(Long strId, String key, PersistenceStatus status, Pageable pageable);

    @Query("select count(s) from Structure s, Structure s2 where locate(concat(s.strCode, '/'), s2.strCode) = 1 and s2.strId = ?1 and (coalesce(upper(s.strName), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strSigle), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strTel), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.strAddress), '') like upper(concat('%', ?2, '%')) or coalesce(upper(s.situationGeo), '') like upper(concat('%', ?2, '%')) or upper(s.strType.name) like upper(concat('%', ?2, '%'))) and s.status = ?3")
    long countStr(Long strId, String key, PersistenceStatus status);

    List<Structure> findByStatus(PersistenceStatus status);

    //@Query("select count(s) from Structure s where (coalesce(upper(s.strName), '') like upper(concat('%', ?1, '%')) or upper(s.strSigle) like upper(concat('%', ?1, '%')) or upper(s.strTel) like upper(concat('%', ?1, '%')) or upper(s.strAddress) like upper(concat('%', ?1, '%')) or upper(s.situationGeo) like upper(concat('%', ?1, '%')) or upper(s.strType.name) like upper(concat('%', ?1, '%'))) and s.status = ?2 ")
    //Long countByKey(String key, PersistenceStatus status);

    @Query("select s from Structure s where (coalesce(upper(s.strName), '') like upper(concat('%', ?1, '%')) or upper(s.strSigle) like upper(concat('%', ?1, '%')) or upper(s.strTel) like upper(concat('%', ?1, '%')) or upper(s.strAddress) like upper(concat('%', ?1, '%')) or upper(s.situationGeo) like upper(concat('%', ?1, '%'))) and s.strType.typeId = ?2 and s.status = ?3 order by s.strName")
    Page<Structure> searchStrByType(String key, Long typeId, PersistenceStatus status, Pageable pageable);

    // Structure dont le type a pour sous type, le type passé en paramètre
    @Query("select s from Structure s where s.strType.typeId in (select tp.parent.typeId from TypeParam tp where tp.child.typeId = ?1 and tp.status = 'ACTIVE') and (s.strId = ?2 or locate(concat(function('getStrCode', ?2), '/'), s.strCode) = 1)  and s.status = 'ACTIVE'")
    List<Structure> findByChildType(Long childTypeId, long visibility);

    @Query("select s from Structure s where s.strType.typeId in (select tp.parent.typeId from TypeParam tp where tp.child.typeId = ?1 and tp.status = 'ACTIVE') and s.status = 'ACTIVE'")
    List<Structure> findByChildType(Long childTypeId);

    @Query("select s from Structure s where s.strType.typeId in (select tp.parent.typeId from TypeParam tp where tp.child.typeId = function('getStrTypeId', ?1) and tp.status = 'ACTIVE') and s.status = 'ACTIVE'")
    List<Structure> findPossibleParents(Long strChildId);

    @Query("select s from Structure s where s.strType.typeId in (select tp.parent.typeId from TypeParam tp where tp.child.typeId = function('getStrTypeId', ?1) and tp.status = 'ACTIVE') and (s.strId = ?2 or locate(concat(function('getStrCode', ?2), '/'), s.strCode) = 1)  and s.status = 'ACTIVE'")
    List<Structure> findPossibleParents(Long childTypeId, long visibility);

    @Query("select ( count(str) > 0) from Structure str where str.strParent.strId = ?1 and str.strId = ?2")
    boolean isDirectParentOf(long parentId, long childId);

    @Query("select ( count(str) > 0) from Structure str where str.strParent.strId = ?2 and str.strId = ?1")
    boolean isDirectChildOf(long childId, long parentId);

    @Query("select (locate(concat(s2.strCode, '/') , s1.strCode) = 1) from Structure s1, Structure s2 where s1.strId = ?1 and s2.strId = ?2")
    boolean childBelongToParent(long childId, long parentId);

    @Query("select (locate(concat(s2.strCode, '/') , s1.strCode) = 1) from Structure s1, Structure s2 where s1.strId = ?1 and s2.strId = ?2")
    boolean parentHasChild(long parentId, long childId);

    @Query("select str.strParent.strId from Structure  str where str.strId = ?1")
    Long getStrParentId(long strId);

    @Query("select s.strLevel from Structure s where s.strId = ?1")
    long getStrLevel(Long strId);

    @Query("select str.strId from Structure  str where str.strParent.strId = ?1 and str.status = 'ACTIVE'")
    Set<Long> getStrChildrenIds(long strId);


    @Query("select (count(s)>0) from Structure s where s.strId = ?1 and (upper(s.strName) like upper(concat('%', ?2, '%') ) or upper(s.strSigle) like upper(concat('%', ?2, '%') )) and s.status = 'ACTIVE'")
    boolean strMatchesSearchKey(long strId, String key);

    @Query("select (count(s)>0) from Structure s where s.strCode like concat(?1, '/%') and (upper(s.strName) like upper(concat('%', ?2, '%') ) or upper(s.strSigle) like upper(concat('%', ?2, '%') )) and s.status = 'ACTIVE'")
    boolean strHasAnyChildMatching(String strCode, String key);

    @Query("select count(s) from Structure s where s.status = ?1")
    long countByStatus(PersistenceStatus status);

    @Query("select count(s) from Structure s where s.strType.typeId = ?1 and s.status = ?2")
    long countByType(Long typeId, PersistenceStatus status);

    @Query("select count(s) from Structure s where s.strParent.strId = ?1 and s.status = ?2")
    long countDirectChildren(Long strId, PersistenceStatus status);

    @Query("select (count(tp)>0) from TypeParam tp where tp.parent.typeId = (select s.strType.typeId from Structure  s where s.strId = ?1) and tp.child.typeId = ?2 and tp.status = 'ACTIVE'")
    boolean parentHasCompatibleSousType(Long strParentId, Long sousTypeId);

    @Query("select s.strType.typeId from Structure s where s.strId = ?1")
    long getStrTypeId(long strId);

    @Query("select s.strType.uniqueCode from Structure s where s.strId = ?1")
    String getStrTypeUniqueCode(long strId);

    @Query("select max(s.strLevel) from Structure s where s.strCode like concat(?1, '/%') and s.status = 'ACTIVE'")
    Long getChildrenMaxLevel(String strCode);

    @Query("select new dgmp.sigrh.structuremodule.model.dtos.str.ChangeAnchorDTO(s.strId, s.strType.typeId, s.strParent.strId, s.strName, s.strSigle) from Structure s where s.strId = ?1")
    ChangeAnchorDTO getChangeAnchorDTO(Long strId);

    @Query("select s.strSigle from Structure s where (locate(concat(s.strCode, '/') , function('getStrCode', ?1)) = 1 or s.strId = ?1) and s.status = 'ACTIVE' order by s.strLevel asc ")
    List<String> getHierarchySigles(Long strId);
}
