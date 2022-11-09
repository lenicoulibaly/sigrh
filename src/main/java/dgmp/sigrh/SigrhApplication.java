package dgmp.sigrh;

import dgmp.sigrh.auth2.controller.services.spec.IUserService;
import dgmp.sigrh.auth2.model.dtos.appuser.CreateActiveUserDTO;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SigrhApplication {

    public static void main(String[] args) {
        SpringApplication.run(SigrhApplication.class, args);
    }

    //@Bean
    CommandLineRunner start(IUserService userService, StrRepo strRepo)
    {
        return args ->
        {
            /*strRepo.findByStrLevel(0).forEach(str->{
                str.setStrCode(str.getStrType().getUniqueCode() + "-" + str.getStrId());
                strRepo.save(str);
            });

            strRepo.findByStrLevel(1).forEach(str->{
                str.setStrCode(str.getStrParent().getStrCode() + "/" +str.getStrType().getUniqueCode() + "-" + str.getStrId());
                strRepo.save(str);
            });

            strRepo.findByStrLevel(2).forEach(str->{
                str.setStrCode(str.getStrParent().getStrCode() + "/" +str.getStrType().getUniqueCode() + "-" + str.getStrId());
                strRepo.save(str);
            });

            strRepo.findByStrLevel(3).forEach(str->{
                str.setStrCode(str.getStrParent().getStrCode() + "/" +str.getStrType().getUniqueCode() + "-" + str.getStrId());
                strRepo.save(str);
            });

            strRepo.findByStrLevel(4).forEach(str->{
                str.setStrCode(str.getStrParent().getStrCode() + "/" +str.getStrType().getUniqueCode() + "-" + str.getStrId());
                strRepo.save(str);
            });*/

            CreateActiveUserDTO dto = CreateActiveUserDTO.builder()
                    .username("lenigauss")
                    .tel("0505471049")
                    .defaultPassword("1234")
                    .password("1234")
                    .rePassword("1234")
                    .email("lenigauss@gmail.com").build();
            //userService.cr(dto);
        };
    }

    @Bean
    public LayoutDialect thymeleafDialect() { return new LayoutDialect(); }
}
