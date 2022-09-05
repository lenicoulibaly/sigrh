package dgmp.sigrh.structuremodule.controller.repositories;

import dgmp.sigrh.structuremodule.model.entities.Structure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StructureDAO extends JpaRepository<Structure, Long>
{
    List<Structure> findByStrParent_StrId(Long strId);
    @Query("select s from Structure s where upper(s.strName) like upper(concat('%', ?1, '%')) or coalesce(upper(s.strSigle),'')  like upper(concat('%', ?1, '%')) or s.strLevel = :searchKey order by s.strName")
    Page<Structure> searchStructure(String searchKey, Pageable pageable);

}
