package dgmp.sigrh.auth.controller.services.impl;

import dgmp.sigrh.auth.controller.services.spec.IStructureService;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor @Slf4j
public class StructureService implements IStructureService
{
    private final StrRepo structureDAO;
    @Override
    public Structure searchStructure(String searchKey)
    {
        return null;
    }

    public boolean isStructureVisibleByFunction(Long functionId, Long strId)
    {
        return getStructureParentStream(strId).anyMatch(str->str.getStrId().equals(strId));
    }


    @Override
    public Stream<Structure> getStructureChildrenStream(Long strId)
    {
        return Stream.concat(Stream.of(structureDAO.findById(strId).orElse(null)), structureDAO.findByStrParent(strId).stream().flatMap(str->getStructureChildrenStream(str.getStrId()))).filter(Objects::nonNull);
    }

    @Override
    public Stream<Structure> getStructureParentStream(Long strId)
    {
        Structure structure = structureDAO.findById(strId).orElse(null);
        if(structure==null) return Stream.empty();
        return Stream.concat(Stream.of(structure), getStructureParentStream(structure.getStrParent().getStrId())).filter(Objects::nonNull);
    }

    @Override
    public boolean isRecursivelyParentOf(Long parentId, Long childId)
    {
        if(parentId == null || childId == null) return false;
        Structure parent = structureDAO.findById(parentId).orElse(null);
        Structure child = structureDAO.findById(childId).orElse(null);
        if(parent == null || child == null) return false;
        if(child.getStrParent().getStrId().equals(parentId)) return true;

        return this.getStructureParentStream(childId).anyMatch(str->str.getStrId().equals(parentId));
    }

    @Override
    public boolean isRecursivelyChildOf(Long childId, Long parentId)
    {
        return isRecursivelyParentOf(parentId, childId);
    }
}
