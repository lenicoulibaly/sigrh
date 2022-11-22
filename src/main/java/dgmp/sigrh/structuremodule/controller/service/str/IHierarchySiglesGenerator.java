package dgmp.sigrh.structuremodule.controller.service.str;

import java.time.LocalDateTime;

public interface IHierarchySiglesGenerator
{
    String getHierarchySigles(long strId);

    String getHistoHierarchySigles(long strId, LocalDateTime dateTime);
}
