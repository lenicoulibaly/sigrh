package dgmp.sigrh.agentmodule.controller.services;

import dgmp.sigrh.agentmodule.model.enums.*;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnumsService implements IEnumsService {
    @Override
    public List<Civility> getAllCivilites() {
        return EnumUtils.getEnumList(Civility.class);
    }

    @Override
    public List<EtatRecrutement> getAllEtatRecrutements() {
        return EnumUtils.getEnumList(EtatRecrutement.class);
    }

    @Override
    public List<SituationMatrimoniale> getAllSitMats() {
        return EnumUtils.getEnumList(SituationMatrimoniale.class);
    }

    @Override
    public List<Position> getAllPositions() {
        return EnumUtils.getEnumList(Position.class);
    }

    @Override
    public List<SituationPresence> getAllSitPresence() {
        return EnumUtils.getEnumList(SituationPresence.class);
    }

    @Override
    public List<TypeAgent> getAllTypeAgents() {
        return EnumUtils.getEnumList(TypeAgent.class);
    }

    @Override
    public List<TypePiece> getAllTypePieces() {
        return EnumUtils.getEnumList(TypePiece.class);
    }
}
