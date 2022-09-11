package dgmp.sigrh.structuremodule.controller.service;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.dtos.str.*;
import dgmp.sigrh.structuremodule.model.entities.structure.StrHisto;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import dgmp.sigrh.structuremodule.model.events.StrEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service @RequiredArgsConstructor
public class StrService implements IStrService
{
    private StrRepo strRepo;
    private StrMapper strMapper;
    private final IHistoService<Structure, StrHisto, StrEventType> strHistoService;
    @Override @Transactional
    public ReadStrDTO createStr(CreateStrDTO dto)
    {
        Structure str = strMapper.mapToStructure(dto);
        str = strRepo.save(str);
        strHistoService.storeEntity(str, StrEventType.CREATE_STR);
        return strMapper.mapToReadStrDTO(str);
    }

    @Override @Transactional
    public ReadStrDTO updateStr(UpdateStrDTO dto)
    {
        Structure str = strMapper.mapToStructure(dto);
        str = strRepo.save(str);
        strHistoService.storeEntity(str, StrEventType.UPDATE_STR);
        return strMapper.mapToReadStrDTO(str);
    }

    @Override @Transactional
    public ReadStrDTO deleteStr(Long strId)
    {
        if(strId==null) return null;
        Structure loadedStructure = strRepo.findById(strId).orElse(null);
        if(loadedStructure == null ) return null;
        loadedStructure.setStatus(PersistenceStatus.DELETED);
        return strMapper.mapToReadStrDTO(loadedStructure);
    }

    @Override @Transactional
    public ReadStrDTO changeAncrage(ChangeAncrageDTO dto)
    {
        Structure str = strMapper.mapToStructure(dto);
        str = strRepo.save(str);
        strHistoService.storeEntity(str, StrEventType.CHANGE_STR_ANCHOR);
        return strMapper.mapToReadStrDTO(str);
    }

    @Override
    public Structure loadChildrenTree(Long strId)
    {
        Structure str = strRepo.findById(strId).orElse(null);
        if(str == null) return  null;
        str.setStrChildren(strRepo.findByStrParent(strId));
        str.getStrChildren().forEach(s->loadChildrenTree(s.getStrId()));
        return str;
    }

    @Override
    public List<Structure> getAllChildren(Long strId)
    {
        if(!strRepo.existsById(strId)) return new ArrayList<>();
        Structure str = strRepo.findById(strId).orElse(null);
        if(str == null) return new ArrayList<>();
        return Stream.concat(Stream.of(str), strRepo.findByStrParent(strId).stream().flatMap(s->this.getAllChildren(s.getStrId()).stream())).collect(Collectors.toList());
    }

    @Override
    public long countAllChildren(Long strId)
    {
        if(!strRepo.existsById(strId)) return 0;
        long nbrChildren = strRepo.countDirectChildren(strId, PersistenceStatus.ACTIVE);
        return strRepo.getStrChildrenIds(strId).stream().reduce(nbrChildren, (id0, id1)->countAllChildren(id0) + countAllChildren(id1));
    }

    @Override
    public List<Structure> getParents(Long strId)
    {
        Structure str = strRepo.findById(strId).orElse(null);
        if(str == null) return  null;
        return Stream.concat(Stream.of(str), str.getStrParent() == null ? new ArrayList<Structure>().stream() :  getParents(str.getStrParent().getStrId()).stream())
                .sorted(Comparator.comparingLong(Structure::getStrLevel).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public boolean childBelongToParent(Long childId, Long parentId)
    {
        if(childId == null || parentId == null) return false;
        if(!strRepo.existsById(childId) || !strRepo.existsById(parentId)) return false;
        if(strRepo.isDirectChildOf(childId, parentId) || this.childBelongToParent(strRepo.getStrParentId(childId), parentId)) return true;
        return false;
    }

    @Override
    public boolean parentHasChild(Long parentId, Long childId)
    {
        return this.childBelongToParent(childId, parentId);
    }

    @Override
    public Page<ReadStrDTO> searchStr(String key, int pageNum, int pageSize)
    {
        Page<Structure> strPage = strRepo.searchStr(key, PersistenceStatus.ACTIVE, PageRequest.of(pageNum, pageSize));
        List<ReadStrDTO> strDTOS = strPage.stream().map(strMapper::mapToReadStrDTO).collect(Collectors.toList());
        return new PageImpl<>(strDTOS, PageRequest.of(pageNum, pageSize), strRepo.countByStatus(PersistenceStatus.ACTIVE));
    }

    @Override
    public Page<ReadStrDTO> searchStrByType(String key, Long typeId, int pageNum, int pageSize)
    {
        Page<Structure> strPage = strRepo.searchStrByType(key, typeId, PersistenceStatus.ACTIVE, PageRequest.of(pageNum, pageSize));
        List<ReadStrDTO> strDTOS = strPage.stream().map(strMapper::mapToReadStrDTO).collect(Collectors.toList());
        return new PageImpl<>(strDTOS, PageRequest.of(pageNum, pageSize), strRepo.countByType(typeId, PersistenceStatus.ACTIVE));
    }

    @Override
    public Page<ReadStrDTO> searchStrByParent(String key, Long parentId, int pageNum, int pageSize)
    {
        Page<Structure> strPage = strRepo.searchStr(key, PersistenceStatus.ACTIVE, PageRequest.of(pageNum, pageSize));

        List<ReadStrDTO> strDTOS = strPage.stream()
                .filter(str->this.childBelongToParent(str.getStrId(), parentId))
                .map(strMapper::mapToReadStrDTO).collect(Collectors.toList());

        return new PageImpl<>(strDTOS, PageRequest.of(pageNum, pageSize), countAllChildren(parentId));
    }
}
