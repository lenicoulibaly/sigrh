package dgmp.sigrh.structuremodule.model.dtos.post;

import dgmp.sigrh.structuremodule.controller.repositories.post.PostGroupRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingPostGroupId.ExistingPostCodedValidator.class})
@Documented
public @interface ExistingPostGroupId
{
    String message() default "Le code du post est invalide";
    Class<?> [] group() default {};
    Class<? extends Payload> [] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingPostCodedValidator implements ConstraintValidator<ExistingPostGroupId, Long>
    {
        private final PostGroupRepo pgRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            return pgRepo.existsById(value);
        }
    }
}
