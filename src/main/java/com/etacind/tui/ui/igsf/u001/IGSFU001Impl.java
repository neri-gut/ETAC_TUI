package com.etacind.tui.ui.igsf.u001;

import com.etacind.tui.command.igsf.t001.IGSFT001;
import com.etacind.tui.dto.igsf.c001.UsersTableRecord;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IGSFU001Impl implements IGSFU001 {

    private final IGSFT001 igsfT001;

    public IGSFU001Impl(IGSFT001 igsfT001) {
        this.igsfT001 = igsfT001;
    }

    @Override
    public void executeUserMenu() {
        try {
            // Intento 1: Iniciar terminal de consola física (requiere TTY)
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();
            runLanternaGui(screen);
        } catch (Exception e) {
            // Intento 2: Si falta el TTY, instanciamos SwingTerminalFrame directamente (evita stty)
            try {
                System.setProperty("java.awt.headless", "false");
                SwingTerminalFrame terminal = new SwingTerminalFrame("Sistema ETAC - Modulo de Usuarios");
                terminal.setVisible(true);
                Screen screen = new TerminalScreen(terminal);
                screen.startScreen();
                runLanternaGui(screen);
            } catch (Exception ex) {
                System.err.println("Fallo critico al iniciar Lanterna (consola y emulador Swing): " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void runLanternaGui(Screen screen) throws Exception {
        TextColor darkBg = new TextColor.RGB(30, 30, 30);

        MultiWindowTextGUI gui = new MultiWindowTextGUI(
                screen, 
                new DefaultWindowManager(), 
                new EmptySpace(darkBg)
        );

        BasicWindow window = new BasicWindow("Sistema ETAC - Modulo de Usuarios");
        window.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        
        Label title = new Label("  SISTEMA DE GESTION DE USUARIOS  ");
        mainPanel.addComponent(title);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        mainPanel.addComponent(new Button("Listar Usuarios", () -> showUsersList(gui)));
        mainPanel.addComponent(new Button("Crear Usuario", () -> showCreateUserDialog(gui)));
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Salir", window::close));

        window.setComponent(mainPanel);
        gui.addWindowAndWait(window);

        screen.stopScreen();
    }

    private void showUsersList(WindowBasedTextGUI gui) {
        BasicWindow listWindow = new BasicWindow("Usuarios Registrados");
        listWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));

        SupabaseApiResponse<UsersTableRecord> response = igsfT001.getUsers();
        if (response == null || response.data() == null || response.data().isEmpty()) {
            mainPanel.addComponent(new Label("No se encontraron registros de usuarios."));
        } else {
            Table<String> table = new Table<>("ID", "Identificador", "Nombre", "ID Estado", "Fecha Creacion");
            for (UsersTableRecord u : response.data()) {
                table.getTableModel().addRow(
                        u.userId() != null ? u.userId() : "",
                        u.userIdentifier() != null ? u.userIdentifier() : "",
                        u.userName() != null ? u.userName() : "",
                        u.statusId() != null ? u.statusId() : "",
                        u.createdAt() != null ? u.createdAt() : ""
                );
            }
            mainPanel.addComponent(table);
        }

        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Volver", listWindow::close));

        listWindow.setComponent(mainPanel);
        gui.addWindow(listWindow);
    }

    private void showCreateUserDialog(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Crear Nuevo Registro");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("Identificador:"));
        TextBox txtIdentifier = new TextBox();
        formPanel.addComponent(txtIdentifier);

        formPanel.addComponent(new Label("Nombre Completo:"));
        TextBox txtName = new TextBox();
        formPanel.addComponent(txtName);

        formPanel.addComponent(new Label("ID Estado:"));
        TextBox txtStatusId = new TextBox("1");
        formPanel.addComponent(txtStatusId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnSave = new Button("Guardar", () -> {
            String identifier = txtIdentifier.getText();
            String name = txtName.getText();
            String statusId = txtStatusId.getText();

            if (identifier.isEmpty() || name.isEmpty() || statusId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "Todos los campos son obligatorios");
                return;
            }

            UsersTableRecord record = new UsersTableRecord(null, identifier, name, statusId, null);
            SupabaseApiResponse<UsersTableRecord> response = igsfT001.createUser(record);
            if (response != null && response.data() != null && !response.data().isEmpty()) {
                UsersTableRecord created = response.data().get(0);
                
                BasicWindow successWindow = new BasicWindow("Confirmacion");
                successWindow.setHints(List.of(Window.Hint.CENTERED));
                
                Panel successPanel = new Panel(new GridLayout(1));
                successPanel.addComponent(new Label("Usuario creado exitosamente:"));
                successPanel.addComponent(new Separator(Direction.HORIZONTAL));
                
                Table<String> successTable = new Table<>("ID", "Identificador", "Nombre", "ID Estado", "Fecha Creacion");
                successTable.getTableModel().addRow(
                        created.userId() != null ? created.userId() : "",
                        created.userIdentifier() != null ? created.userIdentifier() : "",
                        created.userName() != null ? created.userName() : "",
                        created.statusId() != null ? created.statusId() : "",
                        created.createdAt() != null ? created.createdAt() : ""
                );
                successPanel.addComponent(successTable);
                successPanel.addComponent(new Separator(Direction.HORIZONTAL));
                successPanel.addComponent(new Button("Aceptar", successWindow::close));
                
                successWindow.setComponent(successPanel);
                dialogWindow.close();
                gui.addWindow(successWindow);
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "Fallo al crear usuario en Supabase");
            }
        });
        
        buttonPanel.addComponent(btnSave);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }
}
