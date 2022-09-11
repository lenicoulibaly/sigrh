package dgmp.sigrh.structuremodule.model.dtos.str;

import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CompatibleTypeAndParentStr.CompatibleTypeAndParentStrValidatorOnCreate.class, CompatibleTypeAndParentStr.CompatibleTypeAndParentStrValidator.class})
@Documented
public @interface CompatibleTypeAndParentStr
{
    String message() default "Imcompatibilité de type : Impossible de loger une structure dans une tutelle de type inférieur";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
    @Component @RequiredArgsConstructor
    class CompatibleTypeAndParentStrValidator implements ConstraintValidator<CompatibleTypeAndParentStr, ChangeAncrageDTO>
    {
        private final StrRepo strRepo;
        @Override
        public boolean isValid(ChangeAncrageDTO dto, ConstraintValidatorContext context)
        {
            return strRepo.parentHasCompatibleSousType(dto.getNewParentId(), dto.getNewTypeId());
        }
    }

    @Component @RequiredArgsConstructor
    public class CompatibleTypeAndParentStrValidatorOnCreate implements ConstraintValidator <CompatibleTypeAndParentStr, CreateStrDTO>
    {
        private final StrRepo strRepo;
        @Override
        public boolean isValid(CreateStrDTO dto, ConstraintValidatorContext context)
        {
            return strRepo.parentHasCompatibleSousType(dto.getParentId(), dto.getTypeId());
        }
    }
}
