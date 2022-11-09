package dgmp.sigrh.auth2.controller.validators.Exceptions;

public class AppException extends RuntimeException
{
    private String message;
    public AppException(String message)
    {
        super(message);
        this.message = message;
    }
}
