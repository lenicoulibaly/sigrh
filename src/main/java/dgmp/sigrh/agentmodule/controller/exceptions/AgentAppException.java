package dgmp.sigrh.agentmodule.controller.exceptions;

public class AgentAppException extends RuntimeException
{
    private String message;
    public AgentAppException(String message)
    {
        super(message);
        this.message = message;
    }
}
