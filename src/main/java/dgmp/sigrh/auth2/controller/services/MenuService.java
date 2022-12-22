package dgmp.sigrh.auth2.controller.services;

import dgmp.sigrh.auth2.controller.repositories.MenuRepo;
import dgmp.sigrh.auth2.controller.repositories.PrvRepo;
import dgmp.sigrh.auth2.model.entities.Menu;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class MenuService implements IMenuService
{
    private final MenuRepo menuRepo;


    //@Bean
    CommandLineRunner initMenus(TypeRepo typeRepo, PrvRepo prvRepo)
    {
        String[] secPrvsCodes = prvRepo.getPrvCodeByPrvType(typeRepo.findByUniqueCode("PRV_SEC").getTypeId());
        String[] adminPrvsCodes = prvRepo.getPrvCodeByPrvType(typeRepo.findByUniqueCode("PRV_ADM").getTypeId());

        String[] strPrvsCodes = prvRepo.getPrvCodeByPrvType(typeRepo.findByUniqueCode("PRV_STR").getTypeId());
        String[] agtPrvsCodes = prvRepo.getPrvCodeByPrvType(typeRepo.findByUniqueCode("PRV_AGT").getTypeId());

        String[] mvtPrvsCodes = prvRepo.getPrvCodeByPrvType(typeRepo.findByUniqueCode("PRV_MVT").getTypeId());

        String[] prmtPrvsCodes = prvRepo.getPrvCodeByPrvType(typeRepo.findByUniqueCode("PRV_PRMT").getTypeId());
        String[] nmntPrvsCodes = prvRepo.getPrvCodeByPrvType(typeRepo.findByUniqueCode("PRV_NMNT").getTypeId());

        return args->
        {

            menuRepo.save(new Menu(null, "REC", new String[]{"INIT-NEW-REC", "SEE-REC-LST"}, PersistenceStatus.ACTIVE));
            menuRepo.save(new Menu(null, "PERS", new String[]{"RGST-PERS", "SEE-PERS-LST"}, PersistenceStatus.ACTIVE));
            //menuRepo.save(new Menu(null, "TYP", new String[]{"SEE-TYP-LST", "CRT-TYP", "SEE-TYP-TRSH"}, PersistenceStatus.ACTIVE));
            //menuRepo.save(new Menu(null, "GRD", new String[]{"SEE-GRD-LST", "CRT-GRD", "SEE-GRD-TRSH"}, PersistenceStatus.ACTIVE));
            //menuRepo.save(new Menu(null, "INSTCE", new String[]{"SEE-INSTCE-LST", "CRT-INSTCE", "SEE-INSTCE-TRSH"}, PersistenceStatus.ACTIVE));
        };
    }

    @Override
    public String[] getPrvCodesByMenuCode(String menuCode) {
        Menu menu =menuRepo.findByMenuCode(menuCode);
        if(menu == null) return new String[0];
        return menu.getPrvsCodes();
    }
}
