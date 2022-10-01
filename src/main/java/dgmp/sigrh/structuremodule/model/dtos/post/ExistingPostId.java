package dgmp.sigrh.structuremodule.model.dtos.post;

import dgmp.sigrh.structuremodule.controller.repositories.post.PostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingPostId.ExistingPostIddValidator.class})
@Documented
public @interface ExistingPostId
{
    String message() default "L'identifiant du post est invalide";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingPostIddValidator implements ConstraintValidator<ExistingPostId, Long>
    {
        private final PostRepo postRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return postRepo.existsById(value);
        }
    }
}
