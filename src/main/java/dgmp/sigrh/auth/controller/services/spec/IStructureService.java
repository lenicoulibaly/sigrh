package dgmp.sigrh.auth.controller.services.spec;

import dgmp.sigrh.structuremodule.model.entities.structure.Structure;

import java.util.stream.Stream;

public interface IStructureService
{
    Structure searchStructure(String searchKey);
    Stream<Structure> getStructureChildrenStream(Long strId);

    Stream<Structure> getStructureParentStream(Long strId);
    boolean isRecursivelyParentOf(Long parentId, Long childId);
    boolean isRecursivelyChildOf(Long childId, Long parentId);
}
