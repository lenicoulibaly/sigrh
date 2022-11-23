package dgmp.sigrh.instancemodule.model.dtos;

import dgmp.sigrh.instancemodule.controller.repositories.InstanceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingInstanceId.ExistingInstanceIdValidator.class})
@Documented
public @interface ExistingInstanceId
{
    String message() default "Cette instance n'existe pas";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
    @Component @RequiredArgsConstructor
    class ExistingInstanceIdValidator implements ConstraintValidator<ExistingInstanceId, UpdateInstanceDTO>
    {
        private final InstanceRepo instanceRepo;
        @Override
        public boolean isValid(UpdateInstanceDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getInstanceId() == null) return true;
            return instanceRepo.existsById(dto.getInstanceId());
        }
    }
}
