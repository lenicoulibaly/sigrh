package dgmp.sigrh.structuremodule.controller.repositories.structure;

import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StrRepo extends JpaRepository<Structure, Long>
{
    List<Structure> findByStrParent_StrId(Long strId);
    @Query("select s from Structure s where upper(s.strName) like upper(concat('%', ?1, '%')) or coalesce(upper(s.strSigle),'')  like upper(concat('%', ?1, '%')) or s.strLevel = :searchKey order by s.strName")
    Page<Structure> searchStructure(String searchKey, Pageable pageable);

}
