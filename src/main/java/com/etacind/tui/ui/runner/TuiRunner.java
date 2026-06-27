package com.etacind.tui.ui.runner;

import com.etacind.tui.ui.igsf.u001.IGSFU001;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TuiRunner implements CommandLineRunner {

    private final IGSFU001 igsfU001;
    private final com.etacind.tui.exception.GlobalExceptionHandler globalExceptionHandler;

    public TuiRunner(IGSFU001 igsfU001, com.etacind.tui.exception.GlobalExceptionHandler globalExceptionHandler) {
        this.igsfU001 = igsfU001;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            igsfU001.executeUserMenu();
        } catch (Exception e) {
            org.springframework.shell.command.CommandHandlingResult result = globalExceptionHandler.resolve(e);
            if (result != null && result.message() != null) {
                System.err.println(result.message());
            } else {
                e.printStackTrace();
            }
            System.exit(1);
        } finally {
            System.exit(0);
        }
    }
}
