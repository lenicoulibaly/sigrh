package dgmp.sigrh.auth.controller.validators.Exceptions;

public class UserException  extends RuntimeException
{
    private String message;
    public UserException(String message)
    {
        super(message);
        this.message = message;
    }
}
