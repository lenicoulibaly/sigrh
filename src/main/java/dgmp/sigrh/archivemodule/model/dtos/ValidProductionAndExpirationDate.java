package dgmp.sigrh.archivemodule.model.dtos;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidProductionAndExpirationDate.ValidProductionAndExpirationDateValidator.class})
public @interface ValidProductionAndExpirationDate
{
    String message() default "productionAndExpirationDate::La date d'expiration doit être ultérieure à celle de création";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class ValidProductionAndExpirationDateValidator implements ConstraintValidator<ValidProductionAndExpirationDate, ArchiveReqDTO>
    {
        @Override
        public boolean isValid(ArchiveReqDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getProductionDate() == null || dto.getExpirationDate() == null) return true;
            return dto.getProductionDate().isBefore(dto.getExpirationDate());
        }
    }
}
