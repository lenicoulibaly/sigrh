package dgmp.sigrh.typemodule.controller.services;

import dgmp.sigrh.typemodule.model.dtos.CreateTypeDTO;
import dgmp.sigrh.typemodule.model.dtos.ReadTypeDTO;
import dgmp.sigrh.typemodule.model.dtos.TypeParamDTO;
import dgmp.sigrh.typemodule.model.dtos.UpdateTypeDTO;
import org.springframework.data.domain.Page;

public interface ITypeService
{
    ReadTypeDTO createType(CreateTypeDTO dto);
    ReadTypeDTO updateType(UpdateTypeDTO dto);
    void deleteType(Long typeId);
    void setSousType(TypeParamDTO dto);
    void removeSousType(TypeParamDTO dto);

    Page<ReadTypeDTO> searchPageOfTypes(String key, int pageNum, int pageSize);
    Page<ReadTypeDTO> searchPageOfDeletedTypes(String key, int pageNum, int pageSize);
}
