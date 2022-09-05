package dgmp.sigrh.typemodule.controller.services;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.typemodule.controller.exception.AppException;
import dgmp.sigrh.typemodule.controller.repositories.TypeParamRepo;
import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import dgmp.sigrh.typemodule.model.dtos.*;
import dgmp.sigrh.typemodule.model.entities.Type;
import dgmp.sigrh.typemodule.model.entities.TypeHisto;
import dgmp.sigrh.typemodule.model.entities.TypeParam;
import dgmp.sigrh.typemodule.model.entities.TypeParamHisto;
import dgmp.sigrh.typemodule.model.enums.TypeGroup;
import dgmp.sigrh.typemodule.model.events.TypeEventType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service @Transactional
@RequiredArgsConstructor
public class TypeService implements ITypeService
{
    private final TypeRepo typeRepo;
    private final TypeMapper typeMapper;
    private final TypeParamRepo typeParamRepo;
    private final IHistoService<Type, TypeHisto, TypeEventType> typeHistoService;
    private final IHistoService<TypeParam, TypeParamHisto, TypeEventType> typeParamHistoService;
    @Override
    public ReadTypeDTO createType(CreateTypeDTO dto)
    {
        Type type = typeMapper.mapToType(dto);
        type.setUniqueCode(type.getUniqueCode().toUpperCase(Locale.ROOT));
        type = typeRepo.save(type);
        typeHistoService.storeEntity(type, TypeEventType.CREATE_TYPE);
        return typeMapper.mapToReadTypeDTO(type);
    }

    @Override @Transactional
    public ReadTypeDTO updateType(UpdateTypeDTO dto)
    {
        Type loadedType = dto.getTypeId() == null ? null : typeRepo.findById(dto.getTypeId()).orElse(null);
        if(loadedType == null) return null;
        loadedType.setTypeGroup(EnumUtils.getEnum(TypeGroup.class, dto.getTypeGroup()));
        loadedType.setName(dto.getName());
        loadedType.setUniqueCode(dto.getUniqueCode());
        typeHistoService.storeEntity(loadedType, TypeEventType.UPDATE_TYPE);
        return typeMapper.mapToReadTypeDTO(loadedType);
    }

    @Override
    public void deleteType(Long typeId)
    {
        if(!typeRepo.existsById(typeId)) throw new AppException("Impossible de supprimer un type inexistant");
        if(!typeRepo.isDeletable(typeId)) throw new AppException("Impossible de supprimer ce type car il est surtype ou sous type d'un ou de plusieurs autres types");
        typeRepo.deleteById(typeId);
    }

    @Override
    public void setSousType(TypeParamDTO dto)
    {
        if(typeParamRepo.alreadyExistsAndActive(dto.getParentId(), dto.getChildId())) return;
        if(typeParamRepo.alreadyExistsAndNotActive(dto.getParentId(), dto.getChildId()))
        {
            TypeParam typeParam = typeParamRepo.findByParentAndChild(dto.getParentId(), dto.getChildId());
            typeParam.setStatus(PersistenceStatus.ACTIVE);
            typeParamHistoService.storeEntity(typeParam, TypeEventType.ADD_SOUS_TYPE);
            return;
        }
        TypeParam typeParam = typeParamRepo.save(typeMapper.mapToTypeParam(dto));
        typeParamHistoService.storeEntity(typeParam, TypeEventType.ADD_SOUS_TYPE);
    }

    @Override
    public void removeSousType(TypeParamDTO dto)
    {
        if(typeParamRepo.alreadyExistsAndNotActive(dto.getParentId(), dto.getChildId())) return;
        if(typeParamRepo.alreadyExistsAndActive(dto.getParentId(), dto.getChildId()))
        {
            TypeParam typeParam = typeParamRepo.findByParentAndChild(dto.getParentId(), dto.getChildId());
            typeParam.setStatus(PersistenceStatus.DELETED);
            typeParamHistoService.storeEntity(typeParam, TypeEventType.REMOVE_SOUS_TYPE);
            return;
        }
        TypeParam typeParam = typeParamRepo.save(typeMapper.mapToTypeParam(dto));
        typeParamHistoService.storeEntity(typeParam, TypeEventType.REMOVE_SOUS_TYPE);
    }

    @Override
    public Page<ReadTypeDTO> searchPageOfTypes(String key, int pageNum, int pageSize)
    {
        return null;
    }

    @Override
    public Page<ReadTypeDTO> searchPageOfDeletedTypes(String key, int pageNum, int pageSize) {
        return null;
    }
}
