package com.etacind.tui.ui.runner;

import com.etacind.tui.ui.igsf.u001.IGSFU001;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TuiRunner implements CommandLineRunner {

    private final IGSFU001 igsfU001;

    public TuiRunner(IGSFU001 igsfU001) {
        this.igsfU001 = igsfU001;
    }

    @Override
    public void run(String... args) throws Exception {
        igsfU001.executeUserMenu();
    }
}
