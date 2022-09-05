package dgmp.sigrh.grademodule.model.dtos;

import dgmp.sigrh.grademodule.model.enums.Categorie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingCategoryName.ExistingCategoryNameValidator.class})
public @interface ExistingCategoryName
{
    String message() default "Ce nom de catégorie n'existe pas";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingCategoryNameValidator implements ConstraintValidator<ExistingCategoryName, String>
    {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value==null) return false;
            return Categorie.exists(value);
        }
    }
}
