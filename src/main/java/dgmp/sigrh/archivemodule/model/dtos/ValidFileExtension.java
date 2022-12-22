package dgmp.sigrh.archivemodule.model.dtos;

import dgmp.sigrh.archivemodule.model.constants.ArchivageConstants;
import dgmp.sigrh.archivemodule.model.enums.ArchiveTypes;
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

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidFileExtension.ValidFileExtensionValidator.class, ValidFileExtension.ValidFileExtensionValidatorOnDTO.class})
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
            List<String> authorizedExtensions =  ArchivageConstants.DOCUMENT_AUTHORIZED_TYPE.stream().map(String::toLowerCase).collect(Collectors.toList());
            return authorizedExtensions.contains(extension.toLowerCase()) ;
        }
    }

    @Component @RequiredArgsConstructor
    class ValidFileExtensionValidatorOnDTO implements ConstraintValidator<ValidFileExtension, ArchiveReqDTO>
    {
        @Override
        public boolean isValid(ArchiveReqDTO dto, ConstraintValidatorContext context)
        {
            MultipartFile file = dto.getFile();
            if(ArchiveTypes.getByUniqueCode(dto.getArchiveTypeCode()) == null) return false;
            List<String> authorizedExtensions = ArchiveTypes.getByUniqueCode(dto.getArchiveTypeCode()).getAuthorizedFiles();
            if(file == null) return true;
            if(file.getOriginalFilename().equals("")) return true;
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            authorizedExtensions =  authorizedExtensions.stream().map(String::toLowerCase).collect(Collectors.toList());
            return authorizedExtensions.contains(extension.toLowerCase()) ;
        }
    }
}
