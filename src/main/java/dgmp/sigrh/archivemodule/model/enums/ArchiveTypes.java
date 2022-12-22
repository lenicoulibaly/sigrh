package dgmp.sigrh.archivemodule.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.EnumUtils;

import java.util.Arrays;
import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor
public enum ArchiveTypes
{
    CV("CV", "Curriculum Vitae", 3*1024*1024, ArchiveNature.PROFESSIONNELLE, false, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    DIPLOME("DPL", "Diplôme", 1*1024*1024, ArchiveNature.PROFESSIONNELLE, true, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    PHOTO("PRF_PHT", "Photo de profile", 1*1024*1024, ArchiveNature.CIVIL, false, Arrays.asList("jpeg", "jpg", "png")),
    CERTIFICAT_DE_PRISE_DE_SERVICE("CRTF_SCE", "Certificat de prise de service", 1*1024*1024, ArchiveNature.PROFESSIONNELLE, true, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    ACT_DE_PROMOTION("ACT_PRMT", "Acte de promotion", 5*1024*1024, ArchiveNature.CARRIERE, true, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    ACT_DE_NOMINATION("ACT_NMT", "Acte de  nomination", 5*1024*1024, ArchiveNature.CARRIERE, true, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    CNI("CNI", "Carte Nationale d'identité", 1*1024*1024, ArchiveNature.CIVIL, false, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    PASSEPORT("PPT", "Passeport", 1*1024*1024, ArchiveNature.CIVIL, false, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    PERMIS_DE_CONDUIRE("PC", "Permis de conduire", 1*1024*1024, ArchiveNature.CIVIL, false, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    ACTE_DE_MARIAGE("ACT_MRG", "Acte de Mariage", 1*1024*1024, ArchiveNature.CIVIL, false, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    EXTRAIT_DE_NAISSANCE("EXN", "Extrait de Naissance", 1*1024*1024, ArchiveNature.CIVIL, false, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    ATTESTATION_D_IDENTITÉ("ATT_IDT", "Attestation d'identité", 1*1024*1024, ArchiveNature.CIVIL, false, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    CERTIFICAT_DE_NATIONALITE("CRTF_NAT", "Certificat de Nationalité", 1*1024*1024, ArchiveNature.CIVIL, false, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    CERTIFICAT_DE_DECES("CRTF_DCD", "Certificat de décès", 1*1024*1024, ArchiveNature.CIVIL, false, Arrays.asList("jpeg", "jpg", "png", "pdf")),
    ;
    private String uniqueCode;
    private String description;
    private long maxSize;
    private ArchiveNature nature;
    private boolean severalPerAgent;
    private List<String> authorizedFiles;

    public static ArchiveTypes getByUniqueCode(String uniqueCode)
    {
        if(uniqueCode == null) return null;
        for (ArchiveTypes e : EnumUtils.getEnumList(ArchiveTypes.class) )
        {
            if(uniqueCode.equals(e.getUniqueCode())) return e;
        }
        return null;
    }

    public static String getSizeErrorMsg(String uniqueCode)
    {
        String typeDesc = ArchiveTypes.getByUniqueCode(uniqueCode).getDescription();
        Long typeMaxSize = ArchiveTypes.getByUniqueCode(uniqueCode).getMaxSize();
        return "Les fichiers de type " + typeDesc + " ne peuvent exceder " + (typeMaxSize / (1*1024*1024)) + "Mo";
    }

    public static String getExtensionErrorMsg(String uniqueCode)
    {
        String typeDesc = ArchiveTypes.getByUniqueCode(uniqueCode).getDescription();
        List<String> extensions = ArchiveTypes.getByUniqueCode(uniqueCode).getAuthorizedFiles();
        return "Seules les extensions " + extensions.stream().reduce("", (e1, e2)->e1 + ", "+ e2) + " sont autorisées pour les fichiers de type " + typeDesc;
    }

    public static List<ArchiveTypes> getAll()
    {
        return EnumUtils.getEnumList(ArchiveTypes.class);
    }
}
