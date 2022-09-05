package dgmp.sigrh;

import dgmp.sigrh.auth.controller.services.spec.IUserService;
import dgmp.sigrh.auth.model.dtos.appuser.CreateActiveUserDTO;
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
    CommandLineRunner start(IUserService userService)
    {
        return args ->
        {
            CreateActiveUserDTO dto = CreateActiveUserDTO.builder()
                    .username("lenigauss")
                    .tel("0505471049")
                    .defaultPassword("1234")
                    .password("1234")
                    .rePassword("1234")
                    .email("lenigauss@gmail.com").build();
            userService.createActiveUser(dto);
        };
    }

    @Bean
    public LayoutDialect thymeleafDialect() { return new LayoutDialect(); }
}
