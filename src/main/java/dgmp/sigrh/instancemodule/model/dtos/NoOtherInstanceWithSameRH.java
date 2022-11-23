package dgmp.sigrh.instancemodule.model.dtos;

import dgmp.sigrh.instancemodule.controller.repositories.InstanceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NoOtherInstanceWithSameRH.NoOtherInstanceWithSameRHValidatorOnCreate.class, NoOtherInstanceWithSameRH.NoOtherInstanceWithSameRHValidatorOnUpdate.class})
@Documented
public @interface NoOtherInstanceWithSameRH
{
    String message() default "Cette structure est déjà en charge des ressources humaines d'une autre instance";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
    @Component @RequiredArgsConstructor
    class NoOtherInstanceWithSameRHValidatorOnUpdate implements ConstraintValidator<NoOtherInstanceWithSameRH, UpdateInstanceDTO>
    {
        private final InstanceRepo instanceRepo;
        @Override
        public boolean isValid(UpdateInstanceDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getHeadId()==null || dto.getInstanceId() == null) return true;
            return !instanceRepo.alreadyExistsByRhId(dto.getInstanceId(), dto.getRhId());
        }
    }

    @Component @RequiredArgsConstructor
    class NoOtherInstanceWithSameRHValidatorOnCreate implements ConstraintValidator <NoOtherInstanceWithSameRH, Long>
    {
        private final InstanceRepo instanceRepo;
        @Override
        public boolean isValid(Long rhId, ConstraintValidatorContext context)
        {
            if(rhId==null) return true;
            return !instanceRepo.alreadyExistsByRhId(rhId);
        }
    }
}
