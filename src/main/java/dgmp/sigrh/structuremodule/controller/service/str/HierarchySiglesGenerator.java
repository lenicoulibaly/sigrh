package dgmp.sigrh.structuremodule.controller.service.str;

import dgmp.sigrh.structuremodule.controller.repositories.structure.StrHistoRepo;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class HierarchySiglesGenerator implements IHierarchySiglesGenerator
{
    private final StrRepo strRepo;
    private final StrHistoRepo strHistoRepo;
    @Override
    public String getHierarchySigles(long strId)
    {
        if(!strRepo.existsById(strId)) return "/";
        return strRepo.getHierarchySigles(strId).stream().reduce("", (s1, s2)->s1 + "/" + s2).substring(1);
    }

    @Override
    public String getHistoHierarchySigles(long strId, LocalDateTime dateTime)
    {
        if(!strRepo.existsById(strId)) return "/";
        return strHistoRepo.getStrHistoParentSigles(strId, dateTime).stream().reduce("", (s1, s2)->s1 + "/" + s2).substring(1);
    }
}
