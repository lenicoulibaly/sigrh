package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepo extends JpaRepository<Menu, Long>
{
    @Query("select (count(m)>0) from Menu m where m.menuCode = ?1 and ?2 in (m.prvsCodes) and m.status = 'ACTIVE'")
    boolean menuHasPrivilege(String menuCode, String prvCode);

    @Query("select m.prvsCodes from Menu m where m.menuCode = ?1")
    String[] getPrvsCodesByMenuCode(String menuCode);

    @Query("select m from Menu m where m.menuCode = ?1")
    Menu findByMenuCode(String menuCode);
}