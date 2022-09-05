package dgmp.sigrh.auth.controller.validators.Exceptions;

public class AppException extends RuntimeException
{
    private String message;
    public AppException(String message)
    {
        super(message);
        this.message = message;
    }
}
