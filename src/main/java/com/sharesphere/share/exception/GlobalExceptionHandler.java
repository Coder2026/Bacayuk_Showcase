package com.sharesphere.share.exception;

import com.sharesphere.share.annotation.StandardApiErrors;
import com.sharesphere.share.config.WebRedirectProperties;
import com.sharesphere.share.dto.Response;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@ControllerAdvice
@StandardApiErrors
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler{


    private final WebRedirectProperties redirectProperties;

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Response<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new Response<>(ex.getMessage(),null));
  }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response<String>> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new Response<>(ex.getMessage(), null));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Response<String>> handleInsufficientBalance(InsufficientBalanceException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY) // 422
                .body(new Response<>(ex.getMessage(), null));
    }

    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<Response<String>> handleBankAccountNotFound(BankAccountNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response<>(ex.getMessage(), null));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<String>> handleException(Exception ex) {
        log.error("❌ Unhandled exception: ", ex); // log full stacktrace
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response<>(ex.getMessage(), null));
    }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<Response<String>> handleIllegalStateException(IllegalStateException ex) {
    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new Response<>(ex.getMessage(),null));
  }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Response<String>> handleConflictException(ConflictException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new Response<>(ex.getMessage(), null));
    }


  @ExceptionHandler(JwtException.class)
  public ResponseEntity<Response<String>> handleJwtException(JwtException ex) {
    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new Response<>(ex.getMessage(),null));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Response<String>> handleConstraintViolationException(ConstraintViolationException ex) {
    String errorMessage = "Validation failed: " + ex.getConstraintViolations().size() + " error(s)";

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new Response<>(errorMessage, null));
  }

  @ExceptionHandler(UserLocationNotSetException.class)
  public ResponseEntity<Response<String>> handleUserLocationNotSet(UserLocationNotSetException ex) {
    return ResponseEntity
            .status(HttpStatus.PRECONDITION_REQUIRED)
            .body(new Response<>(ex.getMessage(), null));
  }

    @ExceptionHandler(WebRedirectException.class)
    public ResponseEntity<Void> handleWebRedirectException(WebRedirectException ex) {
        String encodedMessage = UriUtils.encode(ex.getMessageParam(), StandardCharsets.UTF_8);
        String url = redirectProperties.buildUrl(ex.getPath(), encodedMessage);

        return ResponseEntity.status(HttpStatus.FOUND) // 302 Redirect
                .location(URI.create(url))
                .build();
    }
}
