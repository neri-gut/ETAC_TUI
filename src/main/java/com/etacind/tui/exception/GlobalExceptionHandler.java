package com.etacind.tui.exception;

import com.etacind.tui.exception.igsf.BusinessValidationException;
import com.etacind.tui.exception.igsf.SupabaseApiException;
import org.springframework.shell.command.CommandExceptionResolver;
import org.springframework.shell.command.CommandHandlingResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

/**
 * Interceptor global de excepciones para la interfaz de línea de comandos (TUI).
 * Centraliza y transforma los errores técnicos en mensajes legibles y estéticos.
 */
@Component
public class GlobalExceptionHandler implements CommandExceptionResolver {

    private static final int ERROR_EXIT_CODE = 1;

    @Override
    public CommandHandlingResult resolve(Exception exception) {
        return switch (exception) {

            // 1. Error personalizado de la integración con Supabase
            case SupabaseApiException supabaseEx -> handleSupabaseError(supabaseEx);

            // 2. Errores nativos de RestClient
            case RestClientResponseException restEx -> handleRestClientError(restEx);

            // 3. Errores de validación de negocio/DTOs contenedores
            case BusinessValidationException validEx -> handleValidationError(validEx);

            // 4. Errores de conectividad (Timeouts, DNS, sin internet)
            case ResourceAccessException netEx -> handleNetworkError(netEx);

            // 5. Captura genérica
            default -> handleUnexpectedError(exception);
        };
    }

    private CommandHandlingResult handleSupabaseError(SupabaseApiException ex) {
        String message = String.format("""
            
            [Error de Infraestructura] -> Fallo en el servidor de Supabase
            Codigo HTTP: %d
            Detalle: %s
            """, ex.getStatusCode(), ex.getMessage());
        return CommandHandlingResult.of(message, ERROR_EXIT_CODE);
    }

    private CommandHandlingResult handleRestClientError(RestClientResponseException ex) {
        String message = String.format("""
            
            [Error de Comunicacion] -> La API externa devolvio una estructura no esperada.
            Estado: %s
            Respuesta Cruda: %s
            """, ex.getStatusText(), ex.getResponseBodyAsString());
        return CommandHandlingResult.of(message, ERROR_EXIT_CODE);
    }

    private CommandHandlingResult handleValidationError(BusinessValidationException ex) {
        String message = String.format("""
            
            [Datos Invalidos] -> La peticion no cumple con las reglas de negocio.
            Validacion: %s
            """, ex.getMessage());
        return CommandHandlingResult.of(message, ERROR_EXIT_CODE);
    }

    private CommandHandlingResult handleNetworkError(ResourceAccessException ex) {
        String message = String.format("""
            
            [Error de Red] -> No se pudo establecer conexion con el servidor.
            Verifique que su dispositivo cuente con acceso a internet.
            Detalle: %s
            """, ex.getMessage());
        return CommandHandlingResult.of(message, ERROR_EXIT_CODE);
    }

    private CommandHandlingResult handleUnexpectedError(Throwable ex) {
        String message = String.format("""
            
            [Fallo Inesperado] -> Ha ocurrido un error no controlado en el sistema.
            Excepcion: %s
            Mensaje Tecnico: %s
            Sugerencia: Intente ejecutar el comando mediante el asistente guiado.
            """, ex.getClass().getSimpleName(), ex.getMessage());
        return CommandHandlingResult.of(message, ERROR_EXIT_CODE);
    }
}
