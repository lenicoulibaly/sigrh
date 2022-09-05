package dgmp.sigrh.emploimodule.model.dtos;

import dgmp.sigrh.emploimodule.controller.repositories.EmploiDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueEmploiName.UniqueEmploiNameValidator.class, UniqueEmploiName.UniqueEmploiNameValidatorOnUpdate.class})
public @interface UniqueEmploiName
{
    String message() default "Cet nom d'emploi existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueEmploiNameValidator implements ConstraintValidator<UniqueEmploiName, String>
    {
        private final EmploiDAO emploiDAO;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value==null) return false;
            return !emploiDAO.existsByNomEmploi(value);
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueEmploiNameValidatorOnUpdate implements ConstraintValidator<UniqueEmploiName, UpdateEmploiDTO>
    {
        private final EmploiDAO emploiDAO;
        @Override
        public boolean isValid(UpdateEmploiDTO dto, ConstraintValidatorContext context)
        {
            return !emploiDAO.existsByNomEmploi(dto.getIdEmploi(), dto.getNomEmploi());
        }
    }
}
