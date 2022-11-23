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
@Constraint(validatedBy = {NoOtherInstanceWithSameHead.NoOtherInstanceWithSameHeadValidatorOnCreate.class, NoOtherInstanceWithSameHead.NoOtherInstanceWithSameHeadValidatorOnUpdate.class})
@Documented
public @interface NoOtherInstanceWithSameHead
{
    String message() default "Cette structure est déjà chapeau d'une autre instance";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
    @Component @RequiredArgsConstructor
    class NoOtherInstanceWithSameHeadValidatorOnUpdate implements ConstraintValidator<NoOtherInstanceWithSameHead, UpdateInstanceDTO>
    {
        private final InstanceRepo instanceRepo;
        @Override
        public boolean isValid(UpdateInstanceDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getInstanceId() == null || dto.getHeadId() == null) return true;
            return !instanceRepo.alreadyExistsByHeadId(dto.getInstanceId(), dto.getHeadId());
        }
    }

    @Component @RequiredArgsConstructor
    class NoOtherInstanceWithSameHeadValidatorOnCreate implements ConstraintValidator <NoOtherInstanceWithSameHead, Long>
    {
        private final InstanceRepo instanceRepo;
        @Override
        public boolean isValid(Long headId, ConstraintValidatorContext context)
        {
            if(headId==null) return true;
            return !instanceRepo.alreadyExistsByHeadId(headId);
        }
    }
}
