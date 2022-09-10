package dgmp.sigrh.structuremodule.controller.service;

import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.dtos.post.UpdatePostDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.CreateStrDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.ReadStrDTO;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service @RequiredArgsConstructor
public class StrService implements IStrService
{
    private StrRepo strRepo;
    @Override
    public ReadStrDTO createStr(CreateStrDTO dto) {
        return null;
    }

    @Override
    public ReadStrDTO createStr(UpdatePostDTO dto) {
        return null;
    }

    @Override
    public ReadStrDTO deleteStr(Long strId) {
        return null;
    }

    @Override
    public Structure loadChildrenTree(Long strId)
    {
        Structure str = strRepo.findById(strId).orElse(null);
        if(str == null) return  null;
        str.setStrChildren(strRepo.findByStrParent_StrId(strId));
        str.getStrChildren().forEach(s->loadChildrenTree(s.getStrId()));
        return str;
    }

    @Override
    public Structure loadParentsTree(Long strId)
    {
        Structure str = strRepo.findById(strId).orElse(null);
        if(str == null) return  null;
        //str
        return null;
    }

    @Override
    public List<Structure> getParents(Long strId)
    {
        Structure str = strRepo.findById(strId).orElse(null);
        if(str == null) return  null;
        return Stream.concat(Stream.of(str), str.getStrParent() == null ? new ArrayList<Structure>().stream() :  getParents(str.getStrParent().getStrId()).stream()).collect(Collectors.toList());
    }

    @Override
    public Page<ReadStrDTO> searchStr(String key, String typeStr, int pageNum, int pageSize) {
        return null;
    }
}
