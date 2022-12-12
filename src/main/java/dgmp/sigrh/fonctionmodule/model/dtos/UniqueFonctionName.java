package dgmp.sigrh.fonctionmodule.model.dtos;

import dgmp.sigrh.fonctionmodule.controller.repositories.FonctionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueFonctionName.UniqueFonctionNameValidator.class, UniqueFonctionName.UniqueFonctionNameValidatorOnUpdate.class})
public @interface UniqueFonctionName
{
    String message() default "Ce nom de fonction existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueFonctionNameValidator implements ConstraintValidator<UniqueFonctionName, String>
    {
        private final FonctionRepo fonctionRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !fonctionRepo.existsByNomFonction(value);
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueFonctionNameValidatorOnUpdate implements ConstraintValidator<UniqueFonctionName, UpdateFonctionDTO>
    {
        private final FonctionRepo fonctionRepo;
        @Override
        public boolean isValid(UpdateFonctionDTO dto, ConstraintValidatorContext context) {
            return !fonctionRepo.existsByNomFonction(dto.getIdFonction(), dto.getNomFonction());
        }
    }
}
