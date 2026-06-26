package com.etacind.tui.dto.igsf.c001.request;

import java.util.Map;

/**
 * Contrato de Petición Genérica a Supabase.
 * 
 * @param <T> El tipo del cuerpo (DTO de Request específico de la tabla, ej. UsersTableRecord)
 */
public record SupabaseApiRequest<T>(
        String tableName,
        T body,
        Map<String, String> queryParams
) {
}