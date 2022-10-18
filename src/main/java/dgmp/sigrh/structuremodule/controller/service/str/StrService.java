package dgmp.sigrh.structuremodule.controller.service.str;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.shared.controller.exception.AppException;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostRepo;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.dtos.str.*;
import dgmp.sigrh.structuremodule.model.entities.structure.StrHisto;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import dgmp.sigrh.structuremodule.model.events.StrEventType;
import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import dgmp.sigrh.typemodule.model.entities.Type;
import dgmp.sigrh.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE;

@Service @RequiredArgsConstructor
public class StrService implements IStrService
{
    private final StrRepo strRepo;
    private final StrMapper strMapper;
    private final TypeRepo typeRepo;
    private final PostRepo postRepo;
    private final IHistoService<Structure, StrHisto, StrEventType> strHistoService;
    @PersistenceContext
    private EntityManager em;

    public String stripAccent(String str)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        //cb.function()
        return null;
    }

    @Override @Transactional
    public ReadStrDTO createStr(CreateStrDTO dto)
    {
        Structure str = strMapper.mapToStructure(dto);
        if(str.getStrParent() != null)
        {
            if(str.getStrParent().getStrId()==null) str.setStrParent(null);
            else str.setStrLevel(strRepo.getStrLevel(str.getStrParent().getStrId()) + 1);
        }
        str = strRepo.save(str);
        str.setStrCode(this.generateStrCode(str));
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
        if(loadedStructure.getStatus() == PersistenceStatus.DELETED) return strMapper.mapToReadStrDTO(loadedStructure);
        loadedStructure.setStatus(PersistenceStatus.DELETED);
        strHistoService.storeEntity(loadedStructure, StrEventType.DELETE_STR);
        return strMapper.mapToReadStrDTO(loadedStructure);
    }

    @Override @Transactional
    public ReadStrDTO restoreStr(Long strId)
    {
        if(strId==null) return null;
        Structure loadedStructure = strRepo.findById(strId).orElse(null);
        if(loadedStructure == null ) return null;
        if(loadedStructure.getStatus() == ACTIVE) return strMapper.mapToReadStrDTO(loadedStructure);
        loadedStructure.setStatus(ACTIVE);
        strHistoService.storeEntity(loadedStructure, StrEventType.RESTORE_STR);
        return strMapper.mapToReadStrDTO(loadedStructure);
    }

    @Override @Transactional
    public ReadStrDTO changeAncrage(ChangeAnchorDTO dto)
    {
        String actionId = UUID.randomUUID().toString();

        Structure str = strMapper.mapToStructure(dto);
        str = strRepo.save(str);
        String oldStrCode = str.getStrCode();
        long childrenMaxLevel = strRepo.getChildrenMaxLevel(oldStrCode);
        str.setStrCode(this.generateStrCode(str));
        strHistoService.storeEntity(str, StrEventType.CHANGE_STR_ANCHOR, actionId, StrEventType.CHANGE_STR_ANCHOR.name());

        for(long level = str.getStrLevel() + 1; level <= childrenMaxLevel; level++)
        {
            strRepo.findChildrenByLevel(dto.getStrId(), level).forEach(s->{
                s.setStrCode(this.generateStrCode(s));
                s = strRepo.save(s);
                strHistoService.storeEntity(s, StrEventType.CHANGE_STR_CODE, actionId, StrEventType.CHANGE_STR_ANCHOR.name());
            });
        }
        return strMapper.mapToReadStrDTO(str);
    }

    @Override
    public String generateStrCode(Structure str)
    {
        if(str == null) throw new AppException("Impossible de généré le code d'une structure non enregistrée");
        if(str.getStrId() == null) throw new AppException("Impossible de généré le code d'une structure non enregistrée");
        String strTypeCode = str.getStrType().getUniqueCode() != null ? str.getStrType().getUniqueCode() : strRepo.getStrTypeUniqueCode(str.getStrId());
        Structure strParent = str.getStrParent() != null ? str.getStrParent() : strRepo.getStrParent(str.getStrId());
        if(strParent == null)
        {
           return strTypeCode + "-" + str.getStrId();
        }
        String parentStrCode = strParent.getStrCode() != null ? strParent.getStrCode() : strRepo.getStrCode(strParent.getStrId());
        return parentStrCode + "/" + strTypeCode + "-" + str.getStrId();
    }

    @Override
    public List<Type> getStrTypes() {
        return typeRepo.findByTypeGroupAndStatus(TypeGroup.STRUCTURE, ACTIVE);
    }

    @Override
    public Structure loadChildrenTree(Long strId)
    {
        Structure str = strRepo.findById(strId).orElse(null);
        if(str == null) return  null;
        str.setStrChildren(strRepo.findByStrParent(strId));
        if(str.getStrChildren() == null) return str;
        str.getStrChildren().forEach(s->loadChildrenTree(s.getStrId()));
        return str;
    }

    @Override
    public List<StrTreeView> loadStrTreeView(Long strId, String key)
    {
        if(strId == null) return new ArrayList<>();
        if(!strRepo.existsById(strId)) return new ArrayList<>();
        if(!this.strHasAnyChildMatching(strId, key) && !strRepo.strMatchesSearchKey(strId, key)) return new ArrayList<>();
        Structure str = strRepo.findById(strId).get();
        //str.setStrChildren(strRepo.findByStrParent(strId));
        StrTreeView strTreeView = new StrTreeView();
        strTreeView.setText(str.toString());
        strTreeView.setHref("/sigrh/structures/str-details?tab=str-tree&strId="+str.getStrId());
        List<StrTreeView> childrenNodes = strRepo.getStrChildrenIds(strId).stream().filter(id->this.strHasAnyChildMatching(id, key)).flatMap(childId->this.loadStrTreeView(childId, key).stream()).collect(Collectors.toList());
        strTreeView.setNodes(childrenNodes);
        return Arrays.asList(strTreeView);
    }

    @Override
    public List<StrTreeView> loadStrTreeView(Long strId)
    {
        if(strId == null) return new ArrayList<>();
        if(!strRepo.existsById(strId)) return new ArrayList<>();
        Structure str = strRepo.findById(strId).get();
        //str.setStrChildren(strRepo.findByStrParent(strId));
        StrTreeView strTreeView = new StrTreeView();
        strTreeView.setText(str.toString());
        strTreeView.setHref("/sigrh/structures/str-details?tab=str-tree&strId="+str.getStrId());
        List<StrTreeView> childrenNodes = strRepo.getStrChildrenIds(strId).stream().flatMap(childId->this.loadStrTreeView(childId).stream()).collect(Collectors.toList());
        strTreeView.setNodes(childrenNodes);
        return Arrays.asList(strTreeView);
    }

    /*
    @Override
    public long countVacantPosts(Long strId)
    {
        return this.getAllChildren(strId).stream().map(str->postRepo.countVacantByStr(str.getStrId())).reduce(0L, (nb1, nb2)->nb1+nb2);
    }

    @Override
    public long countNoneVacantPosts(Long strId) {
        return this.getAllChildren(strId).stream().map(str->postRepo.countNoneVacantByStr(str.getStrId())).reduce(0L, (nb1, nb2)->nb1+nb2);
    }*/

    @Override
    public List<Structure> getParents(Long strId)
    {
        Structure str = strRepo.findById(strId).orElse(null);
        if(str == null) return  new ArrayList<>();
        return strRepo.findAllParents(strId);
    }

    @Override
    public boolean strHasAnyChildMatching(long strId, String key)
    {
        if(strRepo.strMatchesSearchKey(strId, key)) return true;
        return strRepo.strHasAnyChildMatching(strRepo.getStrCode(strId), key);
    }

    @Override
    public boolean childBelongToParent(Long childId, Long parentId)
    {
        if(childId == null || parentId == null) return false;
        if(!strRepo.existsById(childId) || !strRepo.existsById(parentId)) return false;
        return strRepo.getStrCode(childId).startsWith(strRepo.getStrCode(parentId) + "/");
    }

    @Override
    public boolean parentHasChild(Long parentId, Long childId)
    {
        return this.childBelongToParent(childId, parentId);
    }

    @Override
    public Page<ReadStrDTO> searchStr(String key, int pageNum, int pageSize)
    {
        Page<Structure> strPage = strRepo.searchStr(key, ACTIVE, PageRequest.of(pageNum, pageSize));
        List<ReadStrDTO> strDTOS = strPage.stream().map(strMapper::mapToReadStrDTO).collect(Collectors.toList());
        return new PageImpl<>(strDTOS, PageRequest.of(pageNum, pageSize), strRepo.countByStatus(ACTIVE));
    }

    @Override
    public Page<ReadStrDTO> searchStrByType(String key, Long typeId, int pageNum, int pageSize)
    {
        Page<Structure> strPage = strRepo.searchStrByType(key, typeId, ACTIVE, PageRequest.of(pageNum, pageSize));
        List<ReadStrDTO> strDTOS = strPage.stream().map(strMapper::mapToReadStrDTO).collect(Collectors.toList());
        return new PageImpl<>(strDTOS, PageRequest.of(pageNum, pageSize), strRepo.countByType(typeId, ACTIVE));
    }

    @Override
    public Page<ReadStrDTO> searchStrByParent(String key, Long parentId, int pageNum, int pageSize)
    {
        if(parentId!= null )
        {
            if (!strRepo.existsById(parentId))
                return new PageImpl<>(new ArrayList<>(), PageRequest.of(pageNum, pageSize), 0);
        }
        Page<Structure> strPage = parentId == null ?
                strRepo.searchStr(key, ACTIVE, PageRequest.of(pageNum, pageSize)) :
                strRepo.searchStr(parentId, key.trim(), ACTIVE, PageRequest.of(pageNum, pageSize));

        List<ReadStrDTO> strDTOS = strPage.stream().map(strMapper::mapToReadStrDTO).collect(Collectors.toList());
        long nbStr =
                parentId == null && (key==null || "".equals(key)) ? strRepo.countAllStr(ACTIVE) :
                parentId == null ? strRepo.countStr(key, ACTIVE) :
                key==null || "".equals(key) ? strRepo.countAllChildren(parentId, ACTIVE) : strRepo.countStr(parentId, key);

        return new PageImpl<>(strDTOS, PageRequest.of(pageNum, pageSize), nbStr);
    }
}