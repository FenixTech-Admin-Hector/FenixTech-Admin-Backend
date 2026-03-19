package com.proyecto.fenixtech.exception;

import jakarta.servlet.http.HttpServletRequest;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        ex.printStackTrace();
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Content Too Large");
        response.put("message", "El archivo es demasiado grande. El límite es de 5MB.");
        return ResponseEntity.status(HttpStatus.CONTENT_TOO_LARGE).body(response);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> serializacionError(
            BadCredentialsException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 401);
        response.put("error", "BadCredentialsException: Error en usuario o contraseña");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ DisabledException.class, LockedException.class })
    public ResponseEntity<Map<String, Object>> handleAccountStatusExceptions(Exception ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("error", "Acceso Denegado");
        response.put("message",
                "Esta cuenta ha sido desactivada, bloqueada o eliminada");
        response.put("path", request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 403);
        response.put("error", "Forbidden: No tienes permiso para acceder a este recurso");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> serializacionError(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 400);
        response.put("error", "HttpMessageNotReadableException: Error al serializar los datos del body");

        String detalleError = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();

        response.put("message", "Revisa los valores enviados: " + detalleError);
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> manejarValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 400);
        response.put("error", "MethodArgumentNotValidException: Error de validación en valores de los campos");
        response.put("path", request.getRequestURI());

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        response.put("validaciones", errores);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDuplicate(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 409);
        response.put("error", "DataIntegrityViolationException: Duplicate entry");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleNotFound(
            EmptyResultDataAccessException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 404);
        response.put("error", "EmptyResultDataAccessException: Data Not Found");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 1. Primero: Recurso no encontrado (404 personalizado)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 404);
        response.put("error", "ResourceNotFoundException: Recurso Not Found");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 2. Segundo: URL no encontrada (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            NoHandlerFoundException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 404);
        response.put("error", "NoHandlerFoundException: URL Not Found");
        response.put("message", "Endpoint not found: " + request.getRequestURI());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 3. Tercero: Error específico de recurso estático (convertir a 404)
    @ExceptionHandler(NoResourceFoundException.class) // Spring Boot 3.x
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(
            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 404);
        response.put("error", "NoResourceFoundException: Not Found");
        response.put("message", "Endpoint not found: " + request.getRequestURI());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 1. Error de tipo de parámetro (400)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 400);
        response.put("path", request.getRequestURI());

        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            response.put("error", "Valor inválido para el campo '" + ex.getName() + "'");
            response.put("message",
                    "El valor '" + ex.getValue() + "' no es reconocido dentro de las opciones válidas.");
        } else {
            response.put("error", "Bad Request - Type Mismatch");
            response.put("message", "Invalid parameter value for '" + ex.getName() + "'");
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 2. Parámetros faltantes (400)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParams(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", "Missing required parameter: '" + ex.getParameterName() + "'");
        response.put("path", request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 4. Cuarto: Error general (500) con detección de 404s disfrazados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(
            Exception ex, HttpServletRequest request) {

        String message = ex.getMessage();
        String path = request.getRequestURI();

        // DETECTAR si es un "404 disfrazado de 500"
        if (isActually404Error(message, path)) {
            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", java.time.LocalDateTime.now());
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "Exception: Endpoint not found: " + path);
            response.put("path", path);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // ES UN VERDADERO ERROR 500
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 500);
        response.put("error", "Internal Server Error");
        response.put("message", message);
        response.put("path", path);

        // Opcional: Solo para desarrollo, incluir más detalles
        if (isDevelopment()) {
            response.put("exception", ex.getClass().getName());
            response.put("stackTrace", getFirstLinesOfStackTrace(ex));
        }

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Excepción para que las fechas de inicio no sea menor que la de fin
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIlegalExceptionHandler(
            IllegalArgumentException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Método auxiliar para detectar 404s disfrazados
    private boolean isActually404Error(String message, String path) {
        if (message == null) {
            return false;
        }

        // Patrones que indican que es un 404, no un 500
        String lowerMessage = message.toLowerCase();

        return lowerMessage.contains("no static resource")
                || lowerMessage.contains("no handler found")
                || lowerMessage.contains("no mapping for")
                || lowerMessage.contains("resource not found")
                || lowerMessage.contains("404")
                || (path.startsWith("/api/")
                        && !lowerMessage.contains("internal server error")
                        && !lowerMessage.contains("database")
                        && !lowerMessage.contains("sql")
                        && !lowerMessage.contains("connection"));
    }

    private boolean isDevelopment() {
        // Cambia esto según tu entorno
        return true; // o leer de application.properties
    }

    private List<String> getFirstLinesOfStackTrace(Exception ex) {
        return Arrays.stream(ex.getStackTrace())
                .limit(5)
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
    }
}
