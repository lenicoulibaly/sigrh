package dgmp.sigrh.archivemodule.model.dtos;

import dgmp.sigrh.archivemodule.controller.repositories.ArchiveRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Constraint(validatedBy = {UniqueArchiveNum.UniqueArchiveNumValidator.class, UniqueArchiveNum.UniqueArchiveNumValidatorOnCreate.class})
@Documented
public @interface UniqueArchiveNum
{
    String message() default "Numéro de pièce déjà enregistré";
    Class<?>[] groups() default {};
    Class <? extends Payload>[] payload() default {};
    String typeCode() default "";

    @Component @RequiredArgsConstructor
    class UniqueArchiveNumValidator implements ConstraintValidator<UniqueArchiveNum, String>
    {
        private final ArchiveRepo archiveRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            String typeCode = context.unwrap(UniqueArchiveNum.class).typeCode();
            return !archiveRepo.existsByArchiveNumAndTyCode(value, typeCode);
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueArchiveNumValidatorOnCreate implements ConstraintValidator<UniqueArchiveNum, ArchiveReqDTO>
    {
        private final ArchiveRepo archiveRepo;
        @Override
        public boolean isValid(ArchiveReqDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getArchiveNum() == null) return true;
            return dto.getArchiveId() == null ? !archiveRepo.existsByArchiveNumAndTyCode(dto.getArchiveNum(), dto.getArchiveTypeCode()) : !archiveRepo.existsByArchiveNumAndTyCode(dto.getArchiveId(), dto.getArchiveNum(), dto.getArchiveTypeCode());
        }
    }
}
