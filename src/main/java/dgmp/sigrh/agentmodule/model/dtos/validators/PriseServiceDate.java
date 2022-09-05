package dgmp.sigrh.agentmodule.model.dtos.validators;

import dgmp.sigrh.agentmodule.model.dtos.CreateAgentDTO;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {PriseServiceDate.PriseServiceDateValidator.class})
public @interface PriseServiceDate
{
    String message() default "La date de prise de prise de service DGMP ne peut être antérieur à la date de première prise de service";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};

    class PriseServiceDateValidator implements ConstraintValidator<PriseServiceDate, CreateAgentDTO.PriseService>
    {
        @Override
        public boolean isValid(CreateAgentDTO.PriseService priseService, ConstraintValidatorContext context) {
            if(priseService == null) return true;
            if(priseService.getDatePriseServiceDGMP() == null || priseService.getDatePriseService1() == null) return true;
            return priseService.getDatePriseService1().isBefore(priseService.getDatePriseServiceDGMP()) || priseService.getDatePriseService1().isEqual(priseService.getDatePriseServiceDGMP()) ;
        }
    }
}
