package com.etacind.tui.command.igsf.t001.constants;

public final class UsersComandConstant {

    public static final String USER_SEARCH = "user search";
    public static final String USER_SEARCH_VALUE = "Buscar usuarios por criterios";

    public static final String USER_LIST = "user list";
    public static final String USER_LIST_VALUE = "Listar todos los usuarios";

    public static final String USER_CREATE = "user create";
    public static final String USER_CREATE_VALUE = "Crear un nuevo usuario";

    // Parámetros / Opciones (Options)
    public static final String ID_OPTION = "--id";
    public static final String ID_OPTION_VALUE = "ID del usuario de la tabla";

    public static final String IDENTIFIER_OPTION = "--identifier";
    public static final String IDENTIFIER_OPTION_VALUE = "Identificador único del usuario";

    public static final String NAME_OPTION = "--name";
    public static final String NAME_OPTION_VALUE = "Nombre del usuario";

    public static final String STATUS_OPTION = "--status";
    public static final String STATUS_OPTION_VALUE = "ID de estado";

    private UsersComandConstant(){
        throw new UnsupportedOperationException("Clase utilitaria de constantes");
    }
}
