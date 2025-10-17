package com.payment.Payment.Exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {


    //-------------------------- AUTHENTICATION -----------------------------------
    UNAUTHENTICATED(1001, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZE(1003, "You do not have permission", HttpStatus.FORBIDDEN),
    UNSUPPORTED_USER_TYPE(1002, "Unsupported User Type", HttpStatus.BAD_REQUEST),
    //-------------------------- UNIT PRICE -----------------------------------

    //-------------------------- GENERAL -----------------------------------
    UNCATEGORIES_EXCEPTION(9999, "Uncategories exception", HttpStatus.INTERNAL_SERVER_ERROR),
    RECORD_EXIST(1004, "Record already exists", HttpStatus.CONFLICT),
    NULL_RECORD(1005,"Null Record", HttpStatus.CONFLICT),
    PAYMENT_INVALID_SIGN(1006, "Payment Invalid Sign", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
