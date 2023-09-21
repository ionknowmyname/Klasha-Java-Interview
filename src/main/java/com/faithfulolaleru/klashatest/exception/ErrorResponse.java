package com.faithfulolaleru.klashatest.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {


    public static final String ERROR_FEIGN = "FEIGN_ERROR_ENCOUNTERED";

    public static final String ERROR_GENERAL = "GENERIC_ERROR_ENCOUNTERED";

    public static final String ERROR_CONVERT = "CONVERSION_ERROR_ENCOUNTERED";





    private String error;
    private String message;
    private HttpStatus httpStatus;


    public ErrorResponse(final GeneralException ex) {
        this.httpStatus = ex.getHttpStatus();
        this.error = ex.getError();
        this.message = ex.getMessage();   //  ex.getLocalizedMessage()
    }
}
