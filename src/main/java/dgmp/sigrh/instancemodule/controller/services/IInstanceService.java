package dgmp.sigrh.instancemodule.controller.services;

import dgmp.sigrh.instancemodule.model.dtos.CreateInstanceDTO;
import dgmp.sigrh.instancemodule.model.dtos.ReadInstanceDTO;

public interface IInstanceService
{
    ReadInstanceDTO createInstance(CreateInstanceDTO dto);
}
