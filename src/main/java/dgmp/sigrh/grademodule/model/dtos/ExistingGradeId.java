package dgmp.sigrh.grademodule.model.dtos;

import dgmp.sigrh.grademodule.controller.repositories.GradeDAO;
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
@Constraint(validatedBy = {ExistingGradeId.ExistingGradeIdValidator.class})
public @interface ExistingGradeId
{
    String message() default "Ce grade n'existe pas";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingGradeIdValidator implements ConstraintValidator<ExistingGradeId, Long>
    {
        private final GradeDAO gradeDAO;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value==null) return false;
            return gradeDAO.existsById(value);
        }
    }
}
