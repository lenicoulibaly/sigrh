package dgmp.sigrh.typemodule.controller.services;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Type createType(CreateTypeDTO dto)
    {
        Type type = typeMapper.mapToType(dto);
        type = typeRepo.save(type);
        typeHistoService.storeEntity(type, TypeEventType.CREATE_TYPE);
        return type;
    }

    @Override @Transactional
    public Type updateType(UpdateTypeDTO dto)
    {
        Type loadedType = dto.getTypeId() == null ? null : typeRepo.findById(dto.getTypeId()).orElse(null);
        if(loadedType == null) return null;
        loadedType.setTypeGroup(EnumUtils.getEnumList(TypeGroup.class).stream().filter(gr->gr.getGroupCode().equalsIgnoreCase(dto.getTypeGroup())).findFirst().orElse(null));
        loadedType.setName(dto.getName().toUpperCase(Locale.ROOT));
        loadedType.setUniqueCode(dto.getUniqueCode().toUpperCase(Locale.ROOT));
        typeHistoService.storeEntity(loadedType, TypeEventType.UPDATE_TYPE);
        return loadedType;
    }

    @Override
    public void deleteType(Long typeId)
    {
        Type loadedType = typeId == null ? null : typeRepo.findById(typeId).orElse(null);
        if(loadedType == null) return;
        loadedType.setStatus(PersistenceStatus.DELETED);
        typeHistoService.storeEntity(loadedType, TypeEventType.DELETE_TYPE);
    }

    @Override
    public void restoreType(Long typeId)
    {
        Type loadedType = typeId == null ? null : typeRepo.findById(typeId).orElse(null);
        if(loadedType == null) return;
        loadedType.setStatus(PersistenceStatus.ACTIVE);
        typeHistoService.storeEntity(loadedType, TypeEventType.RESTORE_TYPE);
    }

    @Override @Transactional
    public void addSousType(TypeParamDTO dto)
    {
        if(this.parentHasDistantSousType(dto.getChildId(), dto.getParentId())) return;
        if(typeParamRepo.alreadyExistsAndActive(dto.getParentId(), dto.getChildId())) return;
        if(typeParamRepo.alreadyExistsAndNotActive(dto.getParentId(), dto.getChildId()))
        {
            TypeParam typeParam = typeParamRepo.findByParentAndChild(dto.getParentId(), dto.getChildId());
            typeParam.setStatus(PersistenceStatus.ACTIVE);
            typeParamHistoService.storeEntity(typeParam, TypeEventType.ADD_SOUS_TYPE);
            return;
        }
        TypeParam typeParam = typeMapper.mapToTypeParam(dto);
        typeParam.setStatus(PersistenceStatus.ACTIVE);
        typeParam = typeParamRepo.save(typeParam);
        typeParamHistoService.storeEntity(typeParam, TypeEventType.ADD_SOUS_TYPE);
    }


    @Override @Transactional
    public void setSousTypes(TypeParamsDTO dto)
    {
        List<Long> alreadyExistingSousTypeIds = typeRepo.findChildrenIds(dto.getParentId());
        Set<Long> newSousTypesToSetIds = Arrays.stream(dto.getChildIds()).filter(id0-> alreadyExistingSousTypeIds.stream().noneMatch(id0::equals)).collect(Collectors.toSet());
        Set<Long> sousTypesToRemoveIds = alreadyExistingSousTypeIds.stream().filter(id0-> Arrays.stream(dto.getChildIds()).noneMatch(id0::equals)).collect(Collectors.toSet());

        newSousTypesToSetIds.stream().map(id->new TypeParamDTO(id, dto.getParentId())).forEach(this::addSousType);

        sousTypesToRemoveIds.stream().map(id->new TypeParamDTO(id, dto.getParentId())).forEach(this::removeSousType);
    }

    @Override @Transactional
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
        TypeParam typeParam = typeMapper.mapToTypeParam(dto);
        typeParam.setStatus(PersistenceStatus.DELETED);
        typeParam = typeParamRepo.save(typeParam);
        typeParamHistoService.storeEntity(typeParam, TypeEventType.REMOVE_SOUS_TYPE);
    }

    @Override
    public boolean parentHasDirectSousType(Long parentId, Long childId)
    {
        return typeParamRepo.parentHasDirectSousType(parentId, childId);
    }

    @Override
    public boolean parentHasDistantSousType(Long parentId, Long childId)
    {
        if(parentHasDirectSousType(parentId, childId)) return true;
        if(!typeRepo.existsById(parentId) || !typeRepo.existsById(childId)) return false;
        return typeRepo.findActiveSousTypes(parentId).stream().anyMatch(st->parentHasDistantSousType(st.getTypeId(), childId));
    }

    @Override
    public List<Type> getPossibleSousTypes(Long parentId)
    {
        return typeRepo.findByTypeGroupAndStatus(typeRepo.findTypeGroupBTypeId(parentId), PersistenceStatus.ACTIVE).stream().filter(t->!this.parentHasDistantSousType(t.getTypeId(), parentId) && !t.getTypeId().equals(parentId)).collect(Collectors.toList());
    }

    @Override
    public Type setSousTypesRecursively(Long typeId)
    {
        Type type = typeRepo.findById(typeId).orElse(null);
        if(type == null) return null;
        List<Type> sousTypes = typeRepo.findActiveSousTypes(typeId);
        type.setChildren(sousTypes);
        sousTypes.forEach(t->setSousTypesRecursively(t.getTypeId()));
        return type;
    }

    @Override
    public List<Type> getSousTypesRecursively(Long typeId)
    {
        Type type = typeRepo.findById(typeId).orElse(null);
        if(type == null) return null;
        return typeRepo.findActiveSousTypes(typeId).stream().flatMap(t-> Stream.concat(Stream.of(t), getSousTypesRecursively(t.getTypeId()).stream())).collect(Collectors.toList());
    }

    @Override
    public List<TypeGroup> getTypeGroups() {
        return EnumUtils.getEnumList(TypeGroup.class);
    }

    @Override
    public Page<Type> searchPageOfTypes(String key, String typeGroup, int pageNum, int pageSize)
    {
        if("".equals(key)) return typeRepo.searchPageOfTypes(PersistenceStatus.ACTIVE, PageRequest.of(pageNum, pageSize));
        Set<TypeGroup> typeGroups =  EnumUtils.getEnumList(TypeGroup.class).stream().filter(
                tg ->StringUtils.containsIgnoreCase(tg.getGroupCode(), key) ||
                StringUtils.containsIgnoreCase(tg.getGroupName(), key) ||
                StringUtils.containsIgnoreCase(tg.name(), key)).collect(Collectors.toSet());
        if(typeGroups.size() == 0) return typeRepo.searchPageOfTypes(key, PersistenceStatus.ACTIVE, PageRequest.of(pageNum, pageSize));
        return typeRepo.searchPageOfTypes(key, typeGroups, PersistenceStatus.ACTIVE, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<Type> searchPageOfDeletedTypes(String key, String typeGroup, int pageNum, int pageSize)
    {
        if("".equals(key)) return typeRepo.searchPageOfTypes(PersistenceStatus.DELETED, PageRequest.of(pageNum, pageSize));
        Set<TypeGroup> typeGroups =  EnumUtils.getEnumList(TypeGroup.class).stream().filter(tg ->StringUtils.containsIgnoreCase(tg.getGroupCode(), key)
                || StringUtils.containsIgnoreCase(tg.getGroupName(), key) || StringUtils.containsIgnoreCase(tg.name(), key)).collect(Collectors.toSet());
        if(typeGroups.size() == 0) return typeRepo.searchPageOfTypes(key, PersistenceStatus.DELETED, PageRequest.of(pageNum, pageSize));
        return typeRepo.searchPageOfTypes(key, typeGroups, PersistenceStatus.DELETED, PageRequest.of(pageNum, pageSize));
    }
}
