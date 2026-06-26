package com.etacind.tui.exception.igsf;

public class SupabaseApiException extends RuntimeException {
    private final int statusCode;

   /**
   * Constructor principal.
   * @param message Detalle del error para la superclase.
   * @param statusCode Código de estado HTTP devuelto por Supabase.
   */
    public SupabaseApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

   /**
   * Recupera el código de estado HTTP del error.
   * @return int código HTTP.
   */
    public int getStatusCode() {
    return this.statusCode;
    }
}
