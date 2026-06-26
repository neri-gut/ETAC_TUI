package com.etacind.tui.config.supabase;

public enum ConstantSupabase {

    REST_V1("/rest/v1"),
    API_KEY("apikey"),
    CONTENT_TYPE("Content-Type"),
    PREFER("Prefer"),
    APLICATION_JSON("application/json"),
    RETURN_REPRESENTATION("return=representation");

    private String value;

    ConstantSupabase(final String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
