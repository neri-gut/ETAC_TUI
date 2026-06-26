package com.etacind.tui.dto.igsf.c001.response;

import java.util.List;

/**
 * Contrato de Respuesta Genérica desde Supabase.
 * 
 * @param <R> El tipo del DTO esperado de respuesta (ej. UsersSupabaseResponse)
 */
public record SupabaseApiResponse<R>(
        List<R> data
) {
}