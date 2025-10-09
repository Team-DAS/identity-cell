package com.udeajobs.identity.account_service.exception;

import com.udeajobs.identity.account_service.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para el servicio de cuentas.
 *
 * Esta clase centraliza el manejo de todas las excepciones que pueden ocurrir
 * en la aplicación, proporcionando respuestas consistentes y bien estructuradas
 * para diferentes tipos de errores.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
@ControllerAdvice
public class GlobarExceptionHadler {

  /**
   * Maneja excepciones de tipo IllegalArgumentException.
   *
   * Se activa cuando ocurren errores de lógica de negocio como usuario no encontrado,
   * código de verificación inválido, token expirado, etc.
   *
   * @param e la excepción IllegalArgumentException capturada
   * @return ResponseEntity con ErrorResponse y código HTTP 400 (Bad Request)
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), "Bad Request");
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

    /**
     * Maneja excepciones de validación de argumentos de métodos.
     *
     * Se activa cuando los datos de entrada no cumplen con las validaciones
     * definidas en los DTOs (campos requeridos, formato de email, etc.).
     *
     * @param ex la excepción MethodArgumentNotValidException capturada
     * @return ResponseEntity con ErrorResponse conteniendo todos los errores de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errors,
                "Bad Request"

        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
