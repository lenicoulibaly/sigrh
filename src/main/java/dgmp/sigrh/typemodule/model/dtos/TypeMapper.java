package dgmp.sigrh.typemodule.model.dtos;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.typemodule.model.entities.Type;
import dgmp.sigrh.typemodule.model.entities.TypeHisto;
import dgmp.sigrh.typemodule.model.entities.TypeParam;
import dgmp.sigrh.typemodule.model.entities.TypeParamHisto;
import dgmp.sigrh.typemodule.model.events.TypeEventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TypeMapper
{
    @Mapping(target = "status", expression = "java(dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE)")
    @Mapping(target = "typeGroup", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.typemodule.model.enums.TypeGroup.class, dto.getTypeGroup()))")
    @Mapping(target = "uniqueCode", expression = "java(dto.getUniqueCode().toUpperCase())")
    @Mapping(target = "name", expression = "java(dto.getName().toUpperCase())")
    Type mapToType(CreateTypeDTO dto);

    Type mapToType(UpdateTypeDTO dto);

    @Mapping(target = "typeGroup", expression = "java(type.getTypeGroup().getGroupName())")
    ReadTypeDTO mapToReadTypeDTO(Type type);
    @Mapping(target = "parent.typeId", source = "dto.parentId")
    @Mapping(target = "child.typeId", source = "dto.childId")
    TypeParam mapToTypeParam(TypeParamDTO dto);

    TypeHisto mapToTypeHisto(Type type, TypeEventType eventType, EventActorIdentifier eai);

    TypeParamHisto mapToTypeParamHisto(TypeParam typeParam, TypeEventType eventType, EventActorIdentifier eai);

    @Mapping(target = "eai.actionId", source = "actionId")
    @Mapping(target = "eai.mainActionName", source = "mainActionName")
    TypeHisto mapToTypeHisto(Type type, TypeEventType eventType, EventActorIdentifier eai, String actionId, String mainActionName);

    @Mapping(target = "eai.actionId", source = "actionId")
    @Mapping(target = "eai.mainActionName", source = "mainActionName")
    TypeParamHisto mapToTypeParamHisto(TypeParam typeParam, TypeEventType eventType, EventActorIdentifier eventActorIdFromSCM, String actionId, String mainActionName);
}