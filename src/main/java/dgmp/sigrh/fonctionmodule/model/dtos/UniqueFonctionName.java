package dgmp.sigrh.fonctionmodule.model.dtos;

import dgmp.sigrh.fonctionmodule.controller.repositories.FonctionDAO;
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
    public class UniqueFonctionNameValidator implements ConstraintValidator<UniqueFonctionName, String>
    {
        private final FonctionDAO fonctionDAO;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !fonctionDAO.existsByNomFonction(value);
        }
    }

    @Component @RequiredArgsConstructor
    public class UniqueFonctionNameValidatorOnUpdate implements ConstraintValidator<UniqueFonctionName, UpdateFonctionDTO>
    {
        private final FonctionDAO fonctionDAO;
        @Override
        public boolean isValid(UpdateFonctionDTO dto, ConstraintValidatorContext context) {
            return !fonctionDAO.existsByNomFonction(dto.getIdFonction(), dto.getNomFonction());
        }
    }
}
