package lk.ijse.preordersystem.exception;

import lk.ijse.preordersystem.dto.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value={Exception.class})
    public CommonResponse handleServerException(Exception e, WebRequest request){

        e.printStackTrace();
        return new CommonResponse(500, "UNEXPECTED_ERROR");
    }

    @ExceptionHandler(value = {CustomException.class})
    public CommonResponse handleCustomException(CustomException ex, WebRequest request){

        ex.printStackTrace();
     // There are 2 methods
     // return new ResponseEntity<CommonResponse>(new CommonResponse(), HttpStatus.OK).getBody();
        return ResponseEntity.ok(new CommonResponse(ex.getStatus(), ex.getMessage())).getBody();
    }
}
