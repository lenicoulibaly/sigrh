package dgmp.sigrh.auth2.model.dtos.appprivilege;

import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import dgmp.sigrh.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidPrvType.ValidPrvTypeValidator.class})
@Documented
public @interface ValidPrvType
{
    String message() default "Type de privilège invalide";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidPrvTypeValidator implements ConstraintValidator<ValidPrvType, Long>
    {
        private final TypeRepo typeRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return typeRepo.typeGroupHasChild(TypeGroup.PRIVILEGE, value) ;
        }
    }
}
