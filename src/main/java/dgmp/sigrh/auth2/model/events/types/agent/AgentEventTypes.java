package dgmp.sigrh.auth2.model.events.types.agent;


import dgmp.sigrh.shared.model.enums.EventType;

public enum AgentEventTypes implements EventType
{
    CREATE_AGENT("CREATE_AGENT"),
    CHANGE_POST("CHANGE_POST"),
    CHANGE_STRUCTURE("CHANGE_STRUCTURE"),//Mouvement
    CHANGE_GRADE("CHANGE_GRADE"), //Promotion
    CHANGE_EMPLOI("CHANGE_EMPLOI"), //Mobilite Professionnelle
    SIMPLE_UPDATE("SIMPLE_UPDATE");

    String name;
    AgentEventTypes(String name)
    {
        this.name = name;
    }

    AgentEventTypes(){  }

    @Override
    public String toString() {
        return name;
    }
}
