package dgmp.sigrh.structuremodule.model.dtos.post;

import dgmp.sigrh.fonctionmodule.controller.repositories.FonctionDAO;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniquePostManagerByStr.UniquePostManagerByStrValidator.class})
@Documented
public @interface UniquePostManagerByStr
{
    String message() default "Impossible d'avoir plusieurs postes de manageur au sein d'une structure";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniquePostManagerByStrValidator implements ConstraintValidator<UniquePostManagerByStr, CreatePostDTO>
    {
        private final PostRepo postRepo;
        private final FonctionDAO fonctionRepo;
        @Override
        public boolean isValid(CreatePostDTO dto, ConstraintValidatorContext context)
        {
            return !(fonctionRepo.functionIsTopManager(dto.getFonctionId()) && (postRepo.strHasPostManager(dto.getStrId()) || dto.getNbrPosts()>1));
        }
    }
}