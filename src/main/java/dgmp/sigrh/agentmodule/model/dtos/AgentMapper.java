package dgmp.sigrh.agentmodule.model.dtos;

import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.auth.model.dtos.appuser.UserMapper;
import dgmp.sigrh.emploimodule.model.dtos.EmploiMapper;
import dgmp.sigrh.grademodule.model.dtos.GrageMapper;
import dgmp.sigrh.structuremodule.model.dtos.PostMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AgentMapper
{
    @Autowired protected PostMapper postMapper;
    @Autowired protected EmploiMapper emploiMapper;
    @Autowired protected GrageMapper grageMapper;
    @Autowired protected UserMapper userMapper;

    @Mappings
    ({
        @Mapping(target="readPostDTO", expression = "java(postMapper.getReadPostDTO(agent.getPost()))"),
        @Mapping(target="readEmploiDTO", expression = "java(emploiMapper.mapToReadEmploiDTO(agent.getEmploi()))"),
        @Mapping(target="readGradeDTO", expression = "java(grageMapper.mapToReadGradeDTO(agent.getGrade()))"),
        @Mapping(target="readUserDTO", expression = "java(userMapper.getReadUserDTO(agent.getUser()))"),

        @Mapping(target="civilite", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.Civility.class, agent.getCivilite().name()).toString())"),
        @Mapping(target="typePiece", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.TypePiece.class, agent.getTypePiece().name()).toString())"),
        @Mapping(target="situationMatrimoniale", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.SituationMatrimoniale.class, agent.getSituationMatrimoniale().name()).toString())"),
        @Mapping(target="situationPresence", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.SituationPresence.class, agent.getSituationPresence().name()).toString())"),

        @Mapping(target="typeAgent", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.TypeAgent.class, agent.getTypeAgent().name()).toString())"),
        @Mapping(target="position", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.Position.class, agent.getPosition().name()).toString())"),
        @Mapping(target="etatRecrutement", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.EtatRecrutement.class, agent.getEtatRecrutement().name()).toString())"),
    })
    public abstract ReadAgentDTO getReadAgentDTO(Agent agent);


    @Mapping(target="civilite", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.Civility.class, dto.getCivilite()))")
    @Mapping(target="typePiece", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.TypePiece.class, dto.getTypePiece()))")
    @Mapping(target="situationMatrimoniale", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.SituationMatrimoniale.class, dto.getSituationMatrimoniale()))")
    @Mapping(target="situationPresence", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.SituationPresence.class, dto.getSituationPresence()))")

    @Mapping(target="typeAgent", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.TypeAgent.class, dto.getTypeAgent()))")
    @Mapping(target="position", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.Position.class, dto.getPosition()))")
    @Mapping(target="etatRecrutement", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(dgmp.sigrh.agentmodule.model.enums.EtatRecrutement.class, dto.getEtatRecrutement()))")
    @Mapping(target = "emploi", expression = "java(dto.getIdEmploi() == null ? null : new dgmp.sigrh.emploimodule.model.entities.Emploi(dto.getIdEmploi()))")
    @Mapping(target = "grade", expression = "java(dto.getIdGrade() == null ? null : new dgmp.sigrh.grademodule.model.entities.Grade(dto.getIdGrade()))")
    @Mapping(target = "structure", expression = "java(dto.getStrId() == null ? null : new dgmp.sigrh.structuremodule.model.entities.Structure(dto.getStrId()))")

    @Mapping(target = "datePriseService1", source = "priseService.datePriseService1")
    @Mapping(target = "datePriseServiceDGMP", source = "priseService.datePriseServiceDGMP")
    public abstract Agent mapToAgent(CreateAgentDTO dto);
}
