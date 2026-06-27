package com.etacind.tui.command.igsf.t001;

import com.etacind.tui.command.igsf.t001.constants.UsersComandConstant;
import com.etacind.tui.service.igsf.r002.IGSFR002;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class IGSFT001 {

    private final IGSFR002 igsfR002;

    public IGSFT001(IGSFR002 igsfR002){
        this.igsfR002 = igsfR002;
    }

    @ShellMethod(key = UsersComandConstant.USER_SEARCH, value = UsersComandConstant.USER_SEARCH_VALUE)
    public String searchUsers(
            @ShellOption(value = UsersComandConstant.ID_OPTION, defaultValue = ShellOption.NULL, help = UsersComandConstant.ID_OPTION_VALUE) String userId,
            @ShellOption(value = UsersComandConstant.IDENTIFIER_OPTION, defaultValue = ShellOption.NULL, help = UsersComandConstant.IDENTIFIER_OPTION_VALUE) String userIdentifier,
            @ShellOption(value = UsersComandConstant.NAME_OPTION, defaultValue = ShellOption.NULL, help = UsersComandConstant.NAME_OPTION_VALUE) String userName,
            @ShellOption(value = UsersComandConstant.STATUS_OPTION, defaultValue = ShellOption.NULL, help = UsersComandConstant.STATUS_OPTION_VALUE)
    ){

    }

}
