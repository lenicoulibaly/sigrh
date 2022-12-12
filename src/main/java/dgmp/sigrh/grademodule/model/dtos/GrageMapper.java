package dgmp.sigrh.grademodule.model.dtos;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.grademodule.model.entities.Grade;
import dgmp.sigrh.grademodule.model.entities.GradeHisto;
import dgmp.sigrh.grademodule.model.events.GradeEventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GrageMapper {

    @Mapping(target = "categorie", expression = "java(grade.getCategorie() == null ? null : grade.getCategorie().name())")
    @Mapping(target = "status", expression = "java(grade.getStatus() == null ? null : grade.getStatus().name)")
    ReadGradeDTO mapToReadGradeDTO(Grade grade);

    @Mapping(target = "categorie", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.grademodule.model.enums.Categorie.class, dto.getCategorie()))")
    @Mapping(target = "nomGrade", expression = "java(dto.getCategorie() + dto.getRang())")
    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    Grade mapToGrade(CreateGradeDTO dto);

    GradeHisto mapToGradeHisto(Grade grade, GradeEventType eventType, EventActorIdentifier eai);
    @Mapping(target = "eai.actionId", source = "actionId")
    @Mapping(target = "eai.mainActionName", source = "mainActionName")
    GradeHisto mapToGradeHisto(Grade grade, GradeEventType eventType, EventActorIdentifier eai, String actionId, String mainActionName);
}