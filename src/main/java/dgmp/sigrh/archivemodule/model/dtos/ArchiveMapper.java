package dgmp.sigrh.archivemodule.model.dtos;

import dgmp.sigrh.agentmodule.model.dtos.RegisterAgentDTO;
import dgmp.sigrh.archivemodule.model.entities.Archive;
import dgmp.sigrh.archivemodule.model.entities.ArchiveHisto;
import dgmp.sigrh.archivemodule.model.enums.ArchiveEventTypes;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import dgmp.sigrh.typemodule.model.entities.Type;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ArchiveMapper
{
    @Autowired protected TypeRepo typeRepo;

    @Mapping(target = "agtId", source = "agent.agentId")
    @Mapping(target = "archiveTypeCode", source = "archiveType.uniqueCode")
    public abstract ArchiveReqDTO mapToArchiveReqDTO(Archive archive);

    public abstract ArchiveHisto mapToArchiveHisto(Archive archive, ArchiveEventTypes eventType, EventActorIdentifier eai);

    @Mapping(target = "agent", expression = "java(new dgmp.sigrh.agentmodule.model.entities.Agent(dto.getAgtId()))")
    @Mapping(target = "archiveType", expression = "java(new dgmp.sigrh.typemodule.model.entities.Type(typeRepo.findTypeIdByUniqueCode(dto.getArchiveTypeCode())))")
    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    public abstract Archive mapToArchive(ArchiveReqDTO dto);

    public Archive mapToArchive(Archive archive, ArchiveReqDTO dto)
    {
        if(archive == null) return null;
        if(dto == null) return archive;
        archive.setArchiveType(dto.getArchiveTypeCode() == null ? archive.getArchiveType() : new Type(typeRepo.findTypeIdByUniqueCode(dto.getArchiveTypeCode())));
        archive.setArchiveNum(dto.getArchiveNum());
        archive.setDescription(dto.getDescription() == null ? archive.getDescription() : dto.getDescription());
        archive.setProductionDate(dto.getProductionDate());
        archive.setExpirationDate(dto.getExpirationDate());
        return archive;
    }

    @Mapping(target = "description", expression = "java(\"Photo de profil\")")
    @Mapping(target = "archiveTypeCode", expression = "java(\"PRF_PHT\")")
    @Mapping(target = "file", source = "dto.photoFile")
    public abstract ArchiveReqDTO mapToPhotoArchiveDTO(RegisterAgentDTO dto, Long agtId);

    @Mapping(target = "description", expression = "java(\"Curriculum vitae\")")
    @Mapping(target = "archiveTypeCode", expression = "java(\"CV\")")
    @Mapping(target = "file", source = "dto.cv")
    public abstract ArchiveReqDTO mapToCvArchiveDTO(AddCivilArchivesDTO dto);

    @Mapping(target = "description", source = "dto.dplDescription")
    @Mapping(target = "archiveTypeCode", expression = "java(\"DPL\")")
    @Mapping(target = "file", source = "dto.diplome")
    public abstract ArchiveReqDTO mapToDplArchiveDTO(AddCivilArchivesDTO dto);

    @Mapping(target = "description", expression = "java(\"Photo de profil\")")
    @Mapping(target = "archiveTypeCode", expression = "java(\"PRF_PHT\")")
    @Mapping(target = "file", source = "dto.prfPht")
    public abstract ArchiveReqDTO mapToPhotoArchiveDTO(AddCivilArchivesDTO dto);

    @Mapping(target = "description", expression = "java(\"Carte Nationale d'Identité\")")
    @Mapping(target = "archiveTypeCode", expression = "java(\"CNI\")")
    @Mapping(target = "file", source = "dto.cni")
    @Mapping(target = "archiveNum", source = "dto.cniNum")
    public abstract ArchiveReqDTO mapToCniArchiveDTO(AddCivilArchivesDTO dto);

    @Mapping(target = "description", expression = "java(\"Attestation d'Identité\")")
    @Mapping(target = "archiveTypeCode", expression = "java(\"ATT_IDT\")")
    @Mapping(target = "file", source = "dto.attIdt")
    @Mapping(target = "archiveNum", source = "dto.attIdtNum")
    public abstract ArchiveReqDTO mapToAttestationArchiveDTO(AddCivilArchivesDTO dto);

    @Mapping(target = "description", expression = "java(\"Passeport\")")
    @Mapping(target = "archiveTypeCode", expression = "java(\"PPT\")")
    @Mapping(target = "file", source = "dto.ppt")
    @Mapping(target = "archiveNum", source = "dto.pptNum")
    public abstract ArchiveReqDTO mapToPassportArchiveDTO(AddCivilArchivesDTO dto);

    @Mapping(target = "description", expression = "java(\"Permis de conduire\")")
    @Mapping(target = "archiveTypeCode", expression = "java(\"PC\")")
    @Mapping(target = "file", source = "dto.pc")
    @Mapping(target = "archiveNum", source = "dto.pcNum")
    public abstract ArchiveReqDTO mapToPermisArchiveDTO(AddCivilArchivesDTO dto);

    @Mapping(target = "description", expression = "java(\"Extrait de naissance\")")
    @Mapping(target = "archiveTypeCode", expression = "java(\"EXN\")")
    @Mapping(target = "file", source = "dto.exn")
    @Mapping(target = "archiveNum", source = "dto.exnNum")
    public abstract ArchiveReqDTO mapToExtraitArchiveDTO(AddCivilArchivesDTO dto);

    @Mapping(target = "description", expression = "java(\"Certificat de nationalité\")")
    @Mapping(target = "archiveTypeCode", expression = "java(\"CRTF_NAT\")")
    @Mapping(target = "file", source = "dto.crtfNat")
    @Mapping(target = "archiveNum", source = "dto.crtfNatNum")
    public abstract ArchiveReqDTO mapToCertificatNatArchiveDTO(AddCivilArchivesDTO dto);

    @Mapping(target = "description", expression = "java(\"Acte de mariage\")")
    @Mapping(target = "archiveTypeCode", expression = "java(\"ACT_MRG\")")
    @Mapping(target = "file", source = "dto.actMrg")
    @Mapping(target = "archiveNum", source = "dto.actMrgNum")
    public abstract ArchiveReqDTO mapToActMrgArchiveDTO(AddCivilArchivesDTO dto);
}