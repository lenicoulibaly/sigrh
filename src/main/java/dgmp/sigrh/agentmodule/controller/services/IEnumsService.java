package dgmp.sigrh.agentmodule.controller.services;

import dgmp.sigrh.agentmodule.model.enums.*;

import java.util.List;

public interface IEnumsService
{
    List<Civility> getAllCivilites();
    List<EtatRecrutement> getAllEtatRecrutements();
    List<SituationMatrimoniale> getAllSitMats();
    List<Position> getAllPositions();
    List<SituationPresence> getAllSitPresence();
    List<TypeAgent> getAllTypeAgents();
    List<TypePiece> getAllTypePieces();
}
