package dgmp.sigrh.agentmodule.model.projections;

import dgmp.sigrh.agentmodule.model.enums.*;

import java.time.LocalDate;

public interface AgentInfo {
    Long getIdAgent();

    Civility getCivilite();

    LocalDate getDateNaissance();

    LocalDate getDatePriseService1();

    LocalDate getDatePriseServiceDGMP();

    String getDepartementNaissance();

    String getEmail();

    String getEmailPro();

    String getFixeBureau();

    String getLieuNaissance();

    String getMatricule();

    String getNom();

    String getNomMere();

    String getNomPere();

    String getNomPhoto();

    String getNumBadge();

    String getNumPiece();

    String getPrenom();

    SituationMatrimoniale getSituationMatrimoniale();

    SituationPresence getSituationPresence();

    TypeAgent getStatutAgent();

    String getTel();

    String getTitre();

    TypePiece getTypePiece();

    EmploiInfo getEmploi();

    GradeInfo getGrade();

    PostInfo getPost();

    AppUserInfo getUser();

    interface EmploiInfo {
        String getNomEmploi();
    }

    interface GradeInfo {
        String getNomGrade();
    }

    interface PostInfo {
        StructureInfo getStructure();
    }

    interface AppUserInfo {
        String getTel();

        String getUsername();
    }
}
