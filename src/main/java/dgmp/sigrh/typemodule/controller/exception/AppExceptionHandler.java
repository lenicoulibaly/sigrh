package dgmp.sigrh.typemodule.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppExceptionHandler
{
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException err)
    {
        Map<String, String> errorMap = new HashMap<>();
        err.getGlobalErrors().forEach(e->{String[] errTab = e.getDefaultMessage().split(":"); errorMap.put(errTab[0], errTab[1]);});
        err.getBindingResult().getFieldErrors().forEach(e->errorMap.put(e.getField(), e.getDefaultMessage()));
        return errorMap;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAppException(AppException err)
    {
        return err.getMessage();
    }
}
