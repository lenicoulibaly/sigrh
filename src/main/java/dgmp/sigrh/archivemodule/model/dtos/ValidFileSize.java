package dgmp.sigrh.archivemodule.model.dtos;

import dgmp.sigrh.archivemodule.model.constants.ArchivageConstants;
import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidFileSize.ValidFileSizeValidator.class})
@Documented
public @interface ValidFileSize
{
    String message() default "La taille du fichier doit inférieure à " + (ArchivageConstants.UPLOAD_MAX_SIZE / (1 * 1024 * 1024)) + "Mo"  ;
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidFileSizeValidator implements ConstraintValidator<ValidFileSize, MultipartFile>
    {
        private final TypeRepo typeRepo;

        @Override
        public boolean isValid(MultipartFile file, ConstraintValidatorContext context)
        {
            if(file == null) return true;
            return file.getSize() <= ArchivageConstants.UPLOAD_MAX_SIZE;
        }
    }
}
