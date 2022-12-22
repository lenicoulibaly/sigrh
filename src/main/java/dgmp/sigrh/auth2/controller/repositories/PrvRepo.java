package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PrvRepo extends JpaRepository<AppPrivilege, Long>
{
    @Query("select (count(a) > 0) from AppPrivilege a where upper(a.privilegeName) = upper(?1) and a.privilegeId <> ?2")
    boolean alreadyExistsByName(String privilegeName, Long privilegeId);

    @Query("select (count(a) > 0) from AppPrivilege a where upper(a.privilegeName) = upper(?1)")
    boolean alreadyExistsByName(String privilegeName);

    @Query("select (count(a) > 0) from AppPrivilege a where upper(a.privilegeCode) = upper(?1) and a.privilegeId <> ?2")
    boolean alreadyExistsByCode(String privilegeCode, Long privilegeId);

    @Query("select (count(a) > 0) from AppPrivilege a where upper(a.privilegeCode) = upper(?1)")
    boolean alreadyExistsByCode(String privilegeCode);



    @Query("select a from AppPrivilege a left join Type t on a.prvType.typeId = t.typeId where " +
            "locate(coalesce(?1, ''), upper(function('strip_accents', a.privilegeCode))) > 0 or " +
            "locate(coalesce(?1, ''), upper(function('strip_accents', a.privilegeName))) > 0 or " +
            "locate(coalesce(?1, ''), upper(function('strip_accents', t.name))) > 0 ")
    Page<AppPrivilege> searchPrivileges(String searchKey, Pageable pageable);

    @Query("select a from AppPrivilege a where " +
            "(upper(function('strip_accents', a.privilegeCode) ) like concat('%', coalesce(?1, ''), '%') or " +
            "upper(function('strip_accents', a.privilegeName)) like concat('%', coalesce(?1, ''), '%') or " +
            "upper(function('strip_accents', a.prvType.name)) like concat('%', coalesce(?1, ''), '%')) and " +
            "a.prvType.typeId in ?2")
    Page<AppPrivilege> searchPrivileges(String searchKey, List<Long> typeIds, Pageable pageable);

    @Query("select a from AppPrivilege a where upper(function('strip_accents', a.privilegeCode) ) like upper(concat('%', function('strip_accents', coalesce(?1, '')) , '%')) or upper(function('strip_accents', a.privilegeName)) like upper(concat('%', function('strip_accents', coalesce(?1, '')), '%'))")
    Set<AppPrivilege> searchPrivileges(String searchKey);

    @Query("select p.privilegeName from AppPrivilege p")
    Set<String> getPrivilegeNames();

    @Query("select p from AppPrivilege p where p.privilegeCode = ?1")
    AppPrivilege findByPrivilegeCode(String privilegeCode);

    @Query("select p from AppPrivilege p where p.prvType.typeId = ?1")
    List<AppPrivilege> findByPrvType(Long typeId);

    @Query("select p.privilegeCode from AppPrivilege p where p.prvType.typeId = ?1")
    String[] getPrvCodeByPrvType(Long typeId);
}