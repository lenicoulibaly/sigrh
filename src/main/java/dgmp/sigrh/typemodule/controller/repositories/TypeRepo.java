package dgmp.sigrh.typemodule.controller.repositories;

import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.typemodule.model.dtos.ReadTypeDTO;
import dgmp.sigrh.typemodule.model.entities.Type;
import dgmp.sigrh.typemodule.model.enums.TypeGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface TypeRepo extends JpaRepository<Type, Long>
{
    @Query("select t from Type t where (upper(t.name) like upper(concat('%', ?1, '%')) or t.uniqueCode like %?1% or t.typeGroup = ?2) and t.status = ?3")
    Page<Type> searchPageOfTypes(String key, TypeGroup typeGroup, PersistenceStatus status, Pageable pageable);

    @Query("select t from Type t where upper(t.name) like upper(concat('%', ?1, '%')) and upper(t.uniqueCode) like upper(concat('%', ?1, '%')) and t.typeGroup in ?2 and t.status = ?3")
    Page<Type> searchPageOfTypes(String key, Collection<TypeGroup> typeGroups, PersistenceStatus status, Pageable pageable);

    @Query("select t from Type t where (upper(t.name) like upper(concat('%', ?1, '%')) or t.uniqueCode like %?1%) and t.status = ?2")
    Page<Type> searchPageOfTypes(String key, PersistenceStatus status, Pageable pageable);

    @Query("select t from Type t where t.status = ?1")
    Page<Type> searchPageOfTypes(PersistenceStatus status, Pageable pageable);

    @Query("select new dgmp.sigrh.typemodule.model.dtos.ReadTypeDTO(t) from Type t where upper(t.typeGroup) = upper(?1)")
    List<ReadTypeDTO> findByTypeGroup(String typeGroup);

    @Query("select new dgmp.sigrh.typemodule.model.dtos.ReadTypeDTO(t) from Type t order by t.typeGroup, t.uniqueCode, t.typeId")
    List<ReadTypeDTO> findAllTypes();

    @Query("select new dgmp.sigrh.typemodule.model.dtos.ReadTypeDTO(s.child) from TypeParam s where s.parent.typeId = ?1")
    List<ReadTypeDTO> findSousTypeOf(Long parentId);

    @Query("select new dgmp.sigrh.typemodule.model.dtos.ReadTypeDTO(s.child) from TypeParam s where upper(s.parent.uniqueCode) = upper(?1)")
    List<ReadTypeDTO> findSousTypeOf(String parentUniqueCode);

    @Query("select tp.child from TypeParam  tp where tp.parent.typeId = ?1 ")
    List<Type> findByParent(Long parentId);



    @Query("select tp.child.typeId from TypeParam  tp where tp.parent.typeId = ?1 ")
    List<Long> findChildrenIds(Long parentId);

    @Query("select (count (stp)>0) from TypeParam stp where stp.child.typeId=?2 and stp.parent.typeId=?1")
    boolean isSousTypeOf(long parentId, long childId);


    @Query("select (count(t) > 0) from Type t where upper(t.uniqueCode) = upper(?1)")
    boolean existsByUniqueCode(String uniqueCode);

    @Query("select (count(t) > 0) from Type t where t.typeId <> :typeId and upper(t.uniqueCode) = upper(:uniqueCode)")
    boolean existsByUniqueCode(@Param("typeId") Long typeId, @Param("uniqueCode") String uniqueCode);

    @Query("select (count(stp) = 0) from TypeParam stp where stp.child.typeId = ?1 or stp.parent.typeId = ?1")
    boolean isDeletable(long typeId);

    @Query("select (count(stp) = 0) from TypeParam stp where stp.child.typeId = ?1 or stp.parent.typeId = ?1")
    boolean isDeletable(String uniqueCode);

    @Modifying
    @Query("delete from Type t where t.uniqueCode = ?1")
    long deleteByUniqueCode(String uniqueCode);

    @Modifying
    @Query("update Type t set t.typeGroup = :typeGroup, t.uniqueCode = :uniqueCode, t.name = :name where t.typeId =:typeId")
    long updateType(@Param("typeId") long typeId, @Param("typeGroup") String typeGroup, @Param("uniqueCode") String uniqueCode, @Param("name") String name);

}