package dgmp.sigrh.archivemodule.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor
public enum ArchiveTypes
{
    PHOTO("Photo de profile", 1048576000, Arrays.asList("jpeg", "jpg", "png")),
    CERTIFICAT_DE_PRISE_DE_SERVICE("Certificat de prise de service", 3000, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    ACT_DE_PROMOTION("Acte de promotion", 3000, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    ACT_DE_NOMINATION("Acte de  nomination", 5000, Arrays.asList("jpeg", "jpg", "png", "pdf"));
    private String description;
    private long maxSize;
    private List<String> authorizedFiles;
}
