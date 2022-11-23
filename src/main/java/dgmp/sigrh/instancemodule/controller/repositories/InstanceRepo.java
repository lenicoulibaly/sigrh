package dgmp.sigrh.instancemodule.controller.repositories;

import dgmp.sigrh.instancemodule.model.entities.Instance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InstanceRepo extends JpaRepository<Instance, Long>
{
    @Query("select (count(i.instanceId)>0) from Instance i where i.rh.strId = ?1")
    boolean alreadyExistsByRhId(Long rhId);

    @Query("select (count(i.instanceId)>0) from Instance i where i.instanceId = ?1 and i.rh.strId <> ?2")
    boolean alreadyExistsByRhId(Long instanceId, Long rhId);

    @Query("select (count(i.instanceId)>0) from Instance i where i.head.strId = ?1")
    boolean alreadyExistsByHeadId(Long headId);
    @Query("select (count(i.instanceId)>0) from Instance i where i.instanceId = ?1 and i.head.strId <> ?2")
    boolean alreadyExistsByHeadId(Long instanceId, Long headId);

    @Query("select new dgmp.sigrh.instancemodule.model.dtos.ReadInstanceDTO(i.instanceId, i.name, i.head.strId, i.head.strName, i.head.strSigle, function('get_hierarchy_sigles', i.head.strId), i.rh.strId, i.rh.strName, i.rh.strSigle, function('get_hierarchy_sigles', i.rh.strId) ) from Instance i where (locate(concat(function('getstrcode', ?1), '/') , i.head.strCode)=1 or i.head.strId = ?1) and (locate(?2, trim(upper(function('strip_accents', i.name))))>0 or locate(?2, trim(upper(function('strip_accents', i.head.strName))))>0 or locate(?2, trim(upper(function('strip_accents', i.head.strSigle))))>0 or locate(?2, trim(upper(function('strip_accents', i.rh.strName))))>0 or locate(?2, trim(upper(function('strip_accents', i.rh.strSigle))))>0) ")
    Page<Instance> searchInstances(Long parentId, String key, Pageable pageable);



}
