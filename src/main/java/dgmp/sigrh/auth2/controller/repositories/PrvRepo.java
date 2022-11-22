package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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



    @Query("select a from AppPrivilege a where upper(function('strip_accents', a.privilegeCode) ) like upper(concat('%', function('strip_accents', ?1) , '%')) or upper(function('strip_accents', a.privilegeName)) like upper(concat('%', function('strip_accents', ?1), '%')) or upper(function('strip_accents', a.prvGroup)) like upper(concat('%', function('strip_accents', ?1), '%'))")
    Page<AppPrivilege> searchPrivileges(String searchKey, Pageable pageable);

    @Query("select a from AppPrivilege a where upper(function('strip_accents', a.privilegeCode) ) like upper(concat('%', function('strip_accents', ?1) , '%')) or upper(function('strip_accents', a.privilegeName)) like upper(concat('%', function('strip_accents', ?1), '%'))")
    Set<AppPrivilege> searchPrivileges(String searchKey);

    @Query("select p.privilegeName from AppPrivilege p")
    Set<String> getPrivilegeNames();

    AppPrivilege findByPrivilegeCode(String privilegeCode);
}