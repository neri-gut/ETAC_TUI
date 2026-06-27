package com.etacind.tui.repository.igsf.r001;

import com.etacind.tui.dto.igsf.c001.request.SupabaseApiRequest;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;

public interface IGSFR001 {
    
    <T, R> SupabaseApiResponse<R> executeInsert(SupabaseApiRequest<T> request, Class<R[]> responseType);
    
    <R> SupabaseApiResponse<R> executeSelect(SupabaseApiRequest<Void> request, Class<R[]> responseType);

    <R> SupabaseApiResponse<R> executeDelete(SupabaseApiRequest<Void> request, Class<R[]> responseType);

    <T, R> SupabaseApiResponse<R> executeUpdate(SupabaseApiRequest<T> request, Class<R[]> responseType);
}