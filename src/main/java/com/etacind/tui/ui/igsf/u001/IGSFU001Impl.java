package com.etacind.tui.ui.igsf.u001;

import com.etacind.tui.command.igsf.t001.IGSFT001;
import com.etacind.tui.dto.igsf.c001.UsersTableRecord;
import com.etacind.tui.dto.igsf.c001.ProductsTableRecord;
import com.etacind.tui.dto.igsf.c001.StatusTableRecord;
import com.etacind.tui.dto.igsf.c001.ActionsTableRecord;
import com.etacind.tui.dto.igsf.c001.HistoryTableRecord;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
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
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            MultiWindowTextGUI gui = new MultiWindowTextGUI(
                    screen, 
                    new DefaultWindowManager(), 
                    new EmptySpace(TextColor.ANSI.BLUE)
            );

            BasicWindow window = new BasicWindow("Sistema ETAC - Menu Principal");
            window.setHints(List.of(Window.Hint.CENTERED));

            Panel panel = new Panel(new GridLayout(1));
            panel.addComponent(new Label("Seleccione una operacion de la lista:"));
            panel.addComponent(new Separator(Direction.HORIZONTAL));

            panel.addComponent(new Button("Listar Usuarios", () -> showUsersList(gui)));
            panel.addComponent(new Button("Crear Usuario", () -> showCreateUserDialog(gui)));
            panel.addComponent(new Button("Listar Productos", () -> showProductsList(gui)));
            panel.addComponent(new Button("Crear Producto", () -> showCreateProductDialog(gui)));
            panel.addComponent(new Button("Ver Historial", () -> showHistoryList(gui)));
            panel.addComponent(new Button("Ver Estados", () -> showStatusesList(gui)));
            panel.addComponent(new Button("Ver Acciones", () -> showActionsList(gui)));
            panel.addComponent(new Separator(Direction.HORIZONTAL));
            panel.addComponent(new Button("Salir", window::close));

            window.setComponent(panel);
            gui.addWindowAndWait(window);

            screen.stopScreen();
        } catch (Exception e) {
            System.err.println("Error al iniciar la interfaz Lanterna: " + e.getMessage());
        }
    }

    private void showUsersList(WindowBasedTextGUI gui) {
        BasicWindow listWindow = new BasicWindow("Lista de Usuarios");
        listWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));

        SupabaseApiResponse<UsersTableRecord> response = igsfT001.getUsers();
        if (response == null || response.data() == null || response.data().isEmpty()) {
            mainPanel.addComponent(new Label("No se encontraron usuarios."));
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
        BasicWindow dialogWindow = new BasicWindow("Crear Nuevo Usuario");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("Identificador:"));
        TextBox txtIdentifier = new TextBox();
        formPanel.addComponent(txtIdentifier);

        formPanel.addComponent(new Label("Nombre:"));
        TextBox txtName = new TextBox();
        formPanel.addComponent(txtName);

        formPanel.addComponent(new Label("ID Estado:"));
        TextBox txtStatusId = new TextBox("1");
        formPanel.addComponent(txtStatusId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Guardar", () -> {
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
                MessageDialog.showMessageDialog(gui, "Exito", "Usuario creado exitosamente");
                dialogWindow.close();
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "Fallo al crear usuario en Supabase");
            }
        }));
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void showProductsList(WindowBasedTextGUI gui) {
        BasicWindow listWindow = new BasicWindow("Lista de Productos");
        listWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));

        SupabaseApiResponse<ProductsTableRecord> response = igsfT001.getProducts();
        if (response == null || response.data() == null || response.data().isEmpty()) {
            mainPanel.addComponent(new Label("No se encontraron productos."));
        } else {
            Table<String> table = new Table<>("ID", "Nombre", "Descripcion", "Cantidad", "ID Estado", "Fecha Creacion");
            for (ProductsTableRecord p : response.data()) {
                table.getTableModel().addRow(
                        p.productId() != null ? p.productId() : "",
                        p.productName() != null ? p.productName() : "",
                        p.productDescription() != null ? p.productDescription() : "",
                        p.productQuantity() != null ? p.productQuantity() : "",
                        p.statusId() != null ? p.statusId() : "",
                        p.createdAt() != null ? p.createdAt() : ""
                );
            }
            mainPanel.addComponent(table);
        }

        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Volver", listWindow::close));

        listWindow.setComponent(mainPanel);
        gui.addWindow(listWindow);
    }

    private void showCreateProductDialog(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Crear Nuevo Producto");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("Nombre del producto:"));
        TextBox txtName = new TextBox();
        formPanel.addComponent(txtName);

        formPanel.addComponent(new Label("Descripcion:"));
        TextBox txtDescription = new TextBox();
        formPanel.addComponent(txtDescription);

        formPanel.addComponent(new Label("Cantidad:"));
        TextBox txtQuantity = new TextBox("1");
        formPanel.addComponent(txtQuantity);

        formPanel.addComponent(new Label("ID Estado:"));
        TextBox txtStatusId = new TextBox("1");
        formPanel.addComponent(txtStatusId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        buttonPanel.addComponent(new Button("Guardar", () -> {
            String name = txtName.getText();
            String description = txtDescription.getText();
            String quantity = txtQuantity.getText();
            String statusId = txtStatusId.getText();

            if (name.isEmpty() || description.isEmpty() || quantity.isEmpty() || statusId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "Todos los campos son obligatorios");
                return;
            }

            ProductsTableRecord record = new ProductsTableRecord(null, name, description, quantity, statusId, null);
            SupabaseApiResponse<ProductsTableRecord> response = igsfT001.createProduct(record);
            if (response != null && response.data() != null && !response.data().isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Exito", "Producto creado exitosamente");
                dialogWindow.close();
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "Fallo al crear producto en Supabase");
            }
        }));
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void showHistoryList(WindowBasedTextGUI gui) {
        BasicWindow listWindow = new BasicWindow("Historial de Acciones");
        listWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));

        SupabaseApiResponse<HistoryTableRecord> response = igsfT001.getHistory();
        if (response == null || response.data() == null || response.data().isEmpty()) {
            mainPanel.addComponent(new Label("No se encontraron registros en el historial."));
        } else {
            Table<String> table = new Table<>("ID Historial", "ID Usuario", "ID Accion", "ID Producto", "Fecha Ejecucion");
            for (HistoryTableRecord h : response.data()) {
                table.getTableModel().addRow(
                        h.historyId() != null ? h.historyId() : "",
                        h.userId() != null ? h.userId() : "",
                        h.actionId() != null ? h.actionId() : "",
                        h.productId() != null ? h.productId() : "",
                        h.executionDate() != null ? h.executionDate() : ""
                );
            }
            mainPanel.addComponent(table);
        }

        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Volver", listWindow::close));

        listWindow.setComponent(mainPanel);
        gui.addWindow(listWindow);
    }

    private void showStatusesList(WindowBasedTextGUI gui) {
        BasicWindow listWindow = new BasicWindow("Lista de Estados");
        listWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));

        SupabaseApiResponse<StatusTableRecord> response = igsfT001.getStatuses();
        if (response == null || response.data() == null || response.data().isEmpty()) {
            mainPanel.addComponent(new Label("No se encontraron estados."));
        } else {
            Table<String> table = new Table<>("ID Estado", "Valor", "Descripcion", "Fecha Creacion");
            for (StatusTableRecord s : response.data()) {
                table.getTableModel().addRow(
                        s.statusId() != null ? s.statusId() : "",
                        s.statusValue() != null ? s.statusValue() : "",
                        s.statusDescription() != null ? s.statusDescription() : "",
                        s.createdAt() != null ? s.createdAt() : ""
                );
            }
            mainPanel.addComponent(table);
        }

        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Volver", listWindow::close));

        listWindow.setComponent(mainPanel);
        gui.addWindow(listWindow);
    }

    private void showActionsList(WindowBasedTextGUI gui) {
        BasicWindow listWindow = new BasicWindow("Lista de Acciones");
        listWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));

        SupabaseApiResponse<ActionsTableRecord> response = igsfT001.getActions();
        if (response == null || response.data() == null || response.data().isEmpty()) {
            mainPanel.addComponent(new Label("No se encontraron acciones."));
        } else {
            Table<String> table = new Table<>("ID Accion", "Nombre", "Descripcion", "Fecha Creacion");
            for (ActionsTableRecord a : response.data()) {
                table.getTableModel().addRow(
                        a.actionId() != null ? a.actionId() : "",
                        a.actionName() != null ? a.actionName() : "",
                        a.actionDescription() != null ? a.actionDescription() : "",
                        a.createdAt() != null ? a.createdAt() : ""
                );
            }
            mainPanel.addComponent(table);
        }

        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Volver", listWindow::close));

        listWindow.setComponent(mainPanel);
        gui.addWindow(listWindow);
    }
}
