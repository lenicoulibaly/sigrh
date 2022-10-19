package dgmp.sigrh.structuremodule.controller.service.str;

import dgmp.sigrh.structuremodule.model.dtos.str.*;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import dgmp.sigrh.typemodule.model.entities.Type;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IStrService
{
    ReadStrDTO createStr(CreateStrDTO dto);
    ReadStrDTO updateStr(UpdateStrDTO dto);
    ReadStrDTO deleteStr(Long strId);
    ReadStrDTO restoreStr(Long strId);
    ReadStrDTO changeAncrage(ChangeAnchorDTO dto);

    String generateStrCode(Structure str);

    List<Type> getStrTypes();
    Structure loadChildrenTree(Long strId);

    List<StrTreeView> loadStrTreeView(Long strId);

    List<StrTreeView> loadStrTreeView(Long strId, String critere);

    List<Structure> getParents(Long strId);
    String getHierarchySigles(long strId);
    boolean strHasAnyChildMatching(long strId, String key);
    boolean childBelongToParent(Long childId, Long parentId);
    boolean parentHasChild(Long parentId, Long childId);


    Page<ReadStrDTO> searchStr(String key, int pageNum, int pageSize);
    Page<ReadStrDTO> searchStrByType(String key, Long typeId, int pageNum, int pageSize);
    Page<ReadStrDTO> searchStrByParent(String key, Long parentId, int pageNum, int pageSize);


    /*long countVacantPosts(Long StrId);
    long countNoneVacantPosts(Long StrId);*/
}
