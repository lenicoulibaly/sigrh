package dgmp.sigrh.structuremodule.controller.service;

import dgmp.sigrh.structuremodule.model.dtos.post.UpdatePostDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.CreateStrDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.ReadStrDTO;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IStrService
{
    ReadStrDTO createStr(CreateStrDTO dto);
    ReadStrDTO createStr(UpdatePostDTO dto);
    ReadStrDTO deleteStr(Long strId);

    Structure loadChildrenTree(Long strId);
    Structure loadParentsTree(Long strId);
    List<Structure> getParents(Long strId);



    Page<ReadStrDTO> searchStr(String key, String typeStr, int pageNum, int pageSize);
}
