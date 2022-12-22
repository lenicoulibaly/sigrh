package dgmp.sigrh.archivemodule.model.dtos;

import dgmp.sigrh.agentmodule.model.dtos.ExistingOrNullAgtId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AddCivilArchivesDTO
{
    @ExistingOrNullAgtId
    @NotNull(message = "L'ID de l'agent ne peut être nul")
    private Long agtId;

    @ValidFileExtension
    @ValidFileSize
    private MultipartFile cv;

    @ValidFileExtension
    @ValidFileSize
    private MultipartFile diplome;

    @NotNull(message = "La description du diplôme ne peut être nulle") @NotBlank(message = "La description du diplôme ne peut être nulle")
    private String dplDescription;

    @ValidFileExtension @ValidFileSize
    private MultipartFile cni;
    @UniqueArchiveNum(message="Ce N° de CNI est déjà enregistré", typeCode="CNI")
    private String cniNum;

    @ValidFileExtension @ValidFileSize
    private MultipartFile attIdt;
    @UniqueArchiveNum(message="Ce N° d'attestation est déjà enregistré", typeCode="ATT_IDT")
    private String attIdtNum;

    @ValidFileExtension @ValidFileSize
    private MultipartFile ppt;
    @UniqueArchiveNum(message="Ce N° de passeport est déjà enregistré", typeCode="PPT")
    private String pptNum;

    @ValidFileExtension @ValidFileSize
    private MultipartFile pc;
    @UniqueArchiveNum(message="Ce N° de permis de conduire est déjà enregistré", typeCode="PC")
    private String pcNum;

    @ValidFileExtension @ValidFileSize
    private MultipartFile exn;
    @UniqueArchiveNum(message="Ce N° d'extrait de naissance est déjà enregistré", typeCode="EXN")
    private String exnNum;

    @ValidFileExtension @ValidFileSize
    private MultipartFile crtfNat;
    @UniqueArchiveNum(message="Ce N° de certificat de nationalité est déjà enregistré", typeCode="CRTF_NAT")
    private String crtfNatNum;

    @ValidFileExtension @ValidFileSize
    private MultipartFile actMrg;
    @UniqueArchiveNum(message="Ce N° d'acte de mariage est déjà enregistré", typeCode="ACT_MRG")
    private String actMrgNum;

    @ValidFileExtension @ValidFileSize
    private MultipartFile prfPht;
}
