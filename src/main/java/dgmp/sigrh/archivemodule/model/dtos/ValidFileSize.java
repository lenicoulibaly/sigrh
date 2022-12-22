package dgmp.sigrh.archivemodule.model.dtos;

import dgmp.sigrh.archivemodule.model.constants.ArchivageConstants;
import dgmp.sigrh.archivemodule.model.enums.ArchiveTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidFileSize.ValidFileSizeValidator.class, ValidFileSize.ValidFileSizeValidatorOnDTO.class})
@Documented
public @interface ValidFileSize
{
    String message() default "Fichier trop volumineux";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    class ValidFileSizeValidator implements ConstraintValidator<ValidFileSize, MultipartFile>
    {
        @Override
        public boolean isValid(MultipartFile file, ConstraintValidatorContext context)
        {
            if(file == null) return true;
            return file.getSize() <=  ArchivageConstants.UPLOAD_MAX_SIZE;
        }
    }

    @Component @RequiredArgsConstructor
    class ValidFileSizeValidatorOnDTO implements ConstraintValidator<ValidFileSize, ArchiveReqDTO>
    {
        @Override
        public boolean isValid(ArchiveReqDTO dto, ConstraintValidatorContext context)
        {
            MultipartFile file = dto.getFile();
            if(ArchiveTypes.getByUniqueCode(dto.getArchiveTypeCode()) == null) return false;
            if(file == null) return true;
            if(file.getOriginalFilename().equals("")) return true;
            return file.getSize() <= ArchiveTypes.getByUniqueCode(dto.getArchiveTypeCode()).getMaxSize();
        }
    }
}
