package dgmp.sigrh.grademodule.model.dtos;

import dgmp.sigrh.grademodule.controller.repositories.GradeDAO;
import dgmp.sigrh.grademodule.model.enums.Categorie;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueGrade.UniqueGradeValidator.class, UniqueGrade.UniqueGradeValidatorOnUpdate.class})
public @interface UniqueGrade
{
    String message() default "Ce grade existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueGradeValidator implements ConstraintValidator<UniqueGrade, CreateGradeDTO>
    {
        private final GradeDAO gradeDAO;
        @Override
        public boolean isValid(CreateGradeDTO dto, ConstraintValidatorContext context)
        {
            if(dto==null) return false;
            return !gradeDAO.existsByRankAndCategory(dto.getRang(), EnumUtils.getEnum(Categorie.class, dto.getCategorie()));
        }
    }

    @Component
    @RequiredArgsConstructor
    class UniqueGradeValidatorOnUpdate implements ConstraintValidator<UniqueGrade, UpdateGradeDTO>
    {
        private final GradeDAO gradeDAO;
        @Override
        public boolean isValid(UpdateGradeDTO dto, ConstraintValidatorContext context)
        {
            if(dto==null) return false;
            return !gradeDAO.existsByRankAndCategory(dto.getIdGrade(), dto.getRang(), EnumUtils.getEnum(Categorie.class, dto.getCategorie()));
        }
    }
}
