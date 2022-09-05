package dgmp.sigrh.agentmodule.model.projections;

import dgmp.sigrh.agentmodule.model.enums.Civility;

import java.time.LocalDate;

public interface AgentInfoLight
{
    Long getIdAgent();
    Civility getCivilite();
    LocalDate getDateNaissance();
    String getMatricule();
    String getNom();
    String getNomPhoto();
    String getPrenom();
    PostInfo getPost();

    interface PostInfo
    {
        StructureInfoLight getStructure();
        interface StructureInfoLight
        {
            String getStrName();
            String getStrSigle();
        }
    }
}
