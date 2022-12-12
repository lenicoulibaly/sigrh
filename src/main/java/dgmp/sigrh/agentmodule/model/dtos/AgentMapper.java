package dgmp.sigrh.agentmodule.model.dtos;

import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.agentmodule.model.histo.AgentHisto;
import dgmp.sigrh.auth2.model.dtos.appuser.UserMapper;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.agent.AgentEventTypes;
import dgmp.sigrh.emploimodule.model.dtos.EmploiMapper;
import dgmp.sigrh.grademodule.model.dtos.GrageMapper;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.dtos.post.PostMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AgentMapper
{
    @Autowired protected PostMapper postMapper;
    @Autowired protected EmploiMapper emploiMapper;
    @Autowired protected GrageMapper grageMapper;
    @Autowired protected UserMapper userMapper;
    @Autowired protected StrRepo strRepo;

   @Mapping(target="readPostDTO", expression = "java(postMapper.mapToReadPostDTO(agent.getPost()))")
   @Mapping(target="readEmploiDTO", expression = "java(emploiMapper.mapToReadEmploiDTO(agent.getEmploi()))")
   @Mapping(target="readGradeDTO", expression = "java(grageMapper.mapToReadGradeDTO(agent.getGrade()))")
   @Mapping(target="readUserDTO", expression = "java(userMapper.mapToReadUserDTO(agent.getUser()))")

   @Mapping(target="fonction", source = "fonction.nomFonction")
   @Mapping(target="emploi", source = "emploi.nomEmploi")
   @Mapping(target="grade", source = "grade.nomGrade")

   @Mapping(target="civilite", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.Civility.class, agent.getCivilite().name()).toString())")
   @Mapping(target="typePiece", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.TypePiece.class, agent.getTypePiece().name()).toString())")
   @Mapping(target="situationMatrimoniale", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.SituationMatrimoniale.class, agent.getSituationMatrimoniale().name()).toString())")
   //@Mapping(target="situationPresence", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.SituationPresence.class, \"PRESENT\").toString())")

   @Mapping(target="typeAgent", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.TypeAgent.class, agent.getTypeAgent().name()).toString())")
   //@Mapping(target="position", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.Position.class, agent.getPosition().name()).toString())")
   //@Mapping(target="etatRecrutement", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.EtatRecrutement.class, \"EN_SERVICE\").toString())")
   @Mapping(target = "strName", source = "structure.strName")
   @Mapping(target = "strSigle", source = "structure.strSigle")
   @Mapping(target = "hierarchySigles", expression = "java(strRepo.getHierarchySigle(agent.getStructure().getStrId()))")
   public abstract ReadAgentDTO mapToReadAgentDTO(Agent agent);


    public abstract UpdateAgentDTO mapToUpdateAgentDTO(Agent agent);

    public abstract AgentHisto mapToAgentHisto(Agent agent, AgentEventTypes eventType, EventActorIdentifier eai);

    @Mapping(target="civilite", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.Civility.class, dto.getCivilite()))")
    @Mapping(target="typePiece", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.TypePiece.class, dto.getTypePiece()))")
    @Mapping(target="situationMatrimoniale", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.SituationMatrimoniale.class, dto.getSituationMatrimoniale()))")
    //@Mapping(target="situationPresence", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.SituationPresence.class, dto.getSituationPresence()))")

    @Mapping(target="typeAgent", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.TypeAgent.class, dto.getTypeAgent()))")
    //@Mapping(target="position", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.Position.class, dto.getPosition()))")
    //@Mapping(target="etatRecrutement", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.EtatRecrutement.class, dto.getEtatRecrutement()))")
    @Mapping(target = "emploi", expression = "java(dto.getIdEmploi() == null ? null : new dgmp.sigrh.emploimodule.model.entities.Emploi(dto.getIdEmploi()))")
    @Mapping(target = "grade", expression = "java(dto.getIdGrade() == null ? null : new dgmp.sigrh.grademodule.model.entities.Grade(dto.getIdGrade()))")
    @Mapping(target = "structure", expression = "java(dto.getStrId() == null ? null : new dgmp.sigrh.structuremodule.model.entities.structure.Structure(dto.getStrId()))")

    @Mapping(target = "datePriseService1", source = "priseService.datePriseService1")
    @Mapping(target = "datePriseServiceDGMP", source = "priseService.datePriseServiceDGMP")



    @Mapping(target="situationPresence", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.SituationPresence.class, \"PRESENT\"))")
    @Mapping(target="etatRecrutement", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.EtatRecrutement.class, \"EN_SERVICE\"))")
    @Mapping(target="status", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.shared.model.enums.PersistenceStatus.class, \"ACTIVE\"))")
    @Mapping(target = "emailPro", expression = "java(dgmp.sigrh.shared.utilities.StringUtils.blankToNull(dto.getEmailPro()))")
    @Mapping(target = "numBadge", expression = "java(dgmp.sigrh.shared.utilities.StringUtils.blankToNull(dto.getNumBadge()))")
    @Mapping(target = "fixeBureau", expression = "java(dgmp.sigrh.shared.utilities.StringUtils.blankToNull(dto.getFixeBureau()))")
    @Mapping(target = "matricule", expression = "java(dgmp.sigrh.shared.utilities.StringUtils.blankToNull(dto.getMatricule()))")
    public abstract Agent mapToAgent(RegisterAgentDTO dto);

    @Mapping(target="civilite", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.Civility.class, dto.getCivilite()))")
    @Mapping(target="typePiece", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.TypePiece.class, dto.getTypePiece()))")
    @Mapping(target="situationMatrimoniale", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.SituationMatrimoniale.class, dto.getSituationMatrimoniale()))")
    @Mapping(target="situationPresence", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.SituationPresence.class, dto.getSituationPresence()))")

    @Mapping(target="typeAgent", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.TypeAgent.class, dto.getTypeAgent()))")
    //@Mapping(target="position", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.Position.class, dto.getPosition()))")
    @Mapping(target="etatRecrutement", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.EtatRecrutement.class, dto.getEtatRecrutement()))")
    @Mapping(target = "emploi", expression = "java(dto.getIdEmploi() == null ? null : new dgmp.sigrh.emploimodule.model.entities.Emploi(dto.getIdEmploi()))")
    @Mapping(target = "grade", expression = "java(dto.getIdGrade() == null ? null : new dgmp.sigrh.grademodule.model.entities.Grade(dto.getIdGrade()))")

    @Mapping(target = "datePriseService1", source = "priseService.datePriseService1")
    @Mapping(target = "datePriseServiceDGMP", source = "priseService.datePriseServiceDGMP")
    public abstract Agent mapToAgent(CreateNewAgentDTO dto);
}