package dgmp.sigrh.archivemodule.model.dtos;

import dgmp.sigrh.archivemodule.model.constants.ArchivageConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidFileExtension.ValidFileExtensionValidator.class, ValidFileExtension.ValidFileExtensionValidatorOnCreate.class, ValidFileExtension.ValidFileExtensionValidatorOnUpdate.class})
@Documented
public @interface ValidFileExtension
{
    String message() default "Type de fichier non pris en charge";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidFileExtensionValidator implements ConstraintValidator<ValidFileExtension, MultipartFile>
    {
        @Override
        public boolean isValid(MultipartFile file, ConstraintValidatorContext context)
        {
            if(file == null) return true;
            if(file.getOriginalFilename().equals("")) return true;
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            List<String> authorizedExtensions = ArchivageConstants.PHOTO_AUTHORIZED_TYPE.stream().map(String::toLowerCase).collect(Collectors.toList());
            return authorizedExtensions.contains(extension.toLowerCase()) ;
        }
    }

    @Component @RequiredArgsConstructor
    class ValidFileExtensionValidatorOnCreate implements ConstraintValidator<ValidFileExtension, CreateArchiveDTO>
    {
        @Override
        public boolean isValid(CreateArchiveDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getArchiveTypeCode() == null) return true;
            String extension = FilenameUtils.getExtension(dto.getFile().getOriginalFilename());
            List<String> authorizedExtensions =
                    dto.getArchiveTypeCode().equals("PRF_PHT")?
                            ArchivageConstants.PHOTO_AUTHORIZED_TYPE : ArchivageConstants.DOCUMENT_AUTHORIZED_TYPE;

            return authorizedExtensions.contains(extension) ;
        }
    }

    @Component @RequiredArgsConstructor
    class ValidFileExtensionValidatorOnUpdate implements ConstraintValidator<ValidFileExtension, UpdateArchiveDTO>
    {
        @Override
        public boolean isValid(UpdateArchiveDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null) return true;
            if(dto.getArchiveTypeCode() == null) return true;
            String extension = FilenameUtils.getExtension(dto.getFile().getOriginalFilename());
            List<String> authorizedExtensions =
                    dto.getArchiveTypeCode().equals("PRF_PHT") ?
                            ArchivageConstants.PHOTO_AUTHORIZED_TYPE : ArchivageConstants.DOCUMENT_AUTHORIZED_TYPE;

            return authorizedExtensions.contains(extension) ;
        }
    }
}
