package dgmp.sigrh.archivemodule.model.dtos;

import dgmp.sigrh.archivemodule.controller.repositories.ArchiveRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingArchiveId.ExistingArchiveIdValidator.class})
@Documented
public @interface ExistingArchiveId
{
    String message() default "L'ID de l'archive est invalide";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingArchiveIdValidator implements ConstraintValidator<ExistingArchiveId, Long>
    {
        private final ArchiveRepo archiveRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return archiveRepo.existsById(value) ;
        }
    }
}
