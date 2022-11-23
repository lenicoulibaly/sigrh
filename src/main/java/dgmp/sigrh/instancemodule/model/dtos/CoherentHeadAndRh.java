package dgmp.sigrh.instancemodule.model.dtos;

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
@Constraint(validatedBy = {CoherentHeadAndRh.CoherentHeadAndRhValidatorOnCreate.class})
@Documented
public @interface CoherentHeadAndRh
{
    String message() default "headRh::La structure en charge des ressources humaines\ndoit être sous tutelle de la structure chapeau";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
    @Component @RequiredArgsConstructor
    class CoherentHeadAndRhValidatorOnCreate implements ConstraintValidator<CoherentHeadAndRh, CreateInstanceDTO>
    {
        private final StrRepo strRepo;
        @Override
        public boolean isValid(CreateInstanceDTO dto, ConstraintValidatorContext context)
        {
            Long headId = dto.getHeadId(); Long rhId = dto.getRhId();
            if(headId == null || rhId == null) return true;
            return strRepo.childBelongToParent(rhId, headId) || headId.equals(rhId);
        }
    }

    @Component @RequiredArgsConstructor
    class NoOtherInstanceWithSameRHValidatorOnCreate implements ConstraintValidator <CoherentHeadAndRh, UpdateInstanceDTO>
    {
        private final StrRepo strRepo;
        @Override
        public boolean isValid(UpdateInstanceDTO dto, ConstraintValidatorContext context)
        {
            Long headId = dto.getHeadId(); Long rhId = dto.getRhId();
            if(headId == null || rhId == null) return true;
            return strRepo.childBelongToParent(rhId, headId) || headId.equals(rhId);
        }
    }
}
