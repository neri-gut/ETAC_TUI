package com.etacind.tui.exception.igsf;

/**
 * Excepción lanzada cuando una operación no cumple con las reglas de negocio
 * o falla la validación de los datos proporcionados.
 */
public class BusinessValidationException extends RuntimeException {

    /**
     * Construye una nueva excepción con el mensaje de validación especificado.
     *
     * @param message Detalle descriptivo del error de validación.
     */
    public BusinessValidationException(String message) {
        super(message);
    }
}
