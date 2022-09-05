package dgmp.sigrh.typemodule.controller.services;

import dgmp.sigrh.typemodule.model.dtos.CreateTypeDTO;
import dgmp.sigrh.typemodule.model.dtos.TypeParamDTO;
import dgmp.sigrh.typemodule.model.dtos.TypeParamsDTO;
import dgmp.sigrh.typemodule.model.dtos.UpdateTypeDTO;
import dgmp.sigrh.typemodule.model.entities.Type;
import dgmp.sigrh.typemodule.model.enums.TypeGroup;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ITypeService
{
    Type createType(CreateTypeDTO dto);
    Type updateType(UpdateTypeDTO dto);
    void deleteType(Long typeId);
    void addSousType(TypeParamDTO dto);
    void removeSousType(TypeParamDTO dto);
    void setSousTypes(TypeParamsDTO dto);
    boolean parentHasDirectSousType(Long parentId, Long childId);
    boolean parentHasDistantSousType(Long parentId, Long childId);

    Type setSousTypesRecursively(Long typeId);
    List<Type> getSousTypesRecursively(Long typeId);
    List<TypeGroup> getTypeGroups();

    Page<Type> searchPageOfTypes(String key, String typeGroup, int pageNum, int pageSize);
    Page<Type> searchPageOfDeletedTypes(String key, String typeGroup, int pageNum, int pageSize);

    void restoreType(Long idType);
}
