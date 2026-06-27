package com.etacind.tui.ui.igsf.u001;

import com.etacind.tui.command.igsf.t001.IGSFT001;
import com.etacind.tui.command.igsf.t002.IGSFT002;
import com.etacind.tui.command.igsf.t003.IGSFT003;
import com.etacind.tui.dto.igsf.c001.UsersTableRecord;
import com.etacind.tui.dto.igsf.c001.ProductsTableRecord;
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
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IGSFU001Impl implements IGSFU001 {

    private final IGSFT001 igsfT001;
    private final IGSFT002 igsfT002;
    private final IGSFT003 igsfT003;

    public IGSFU001Impl(IGSFT001 igsfT001, IGSFT002 igsfT002, IGSFT003 igsfT003) {
        this.igsfT001 = igsfT001;
        this.igsfT002 = igsfT002;
        this.igsfT003 = igsfT003;
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
                SwingTerminalFrame terminal = new SwingTerminalFrame("Sistema ETAC - Menu Principal");
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

        BasicWindow window = new BasicWindow("Sistema ETAC - Menu Principal");
        window.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        
        Label title = new Label("  SISTEMA DE GESTION (ETAC)  ");
        mainPanel.addComponent(title);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        mainPanel.addComponent(new Button("Gestion de Usuarios", () -> showUsersSubmenu(gui)));
        mainPanel.addComponent(new Button("Gestion de Productos", () -> showProductsSubmenu(gui)));
        mainPanel.addComponent(new Button("Consulta de Historial", () -> showHistorySubmenu(gui)));
        
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Salir", window::close));

        window.setComponent(mainPanel);
        gui.addWindowAndWait(window);

        screen.stopScreen();
    }

    // ==========================================
    // SUBMENÚ: GESTIÓN DE USUARIOS
    // ==========================================
    private void showUsersSubmenu(WindowBasedTextGUI gui) {
        BasicWindow submenuWindow = new BasicWindow("Gestion de Usuarios");
        submenuWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        mainPanel.addComponent(new Label("Operaciones Disponibles:"));
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        mainPanel.addComponent(new Button("Listar Usuarios", () -> showUsersList(gui)));
        mainPanel.addComponent(new Button("Crear Usuario", () -> showCreateUserDialog(gui)));
        mainPanel.addComponent(new Button("Actualizar Usuario", () -> showUpdateUserDialog(gui)));
        mainPanel.addComponent(new Button("Eliminar Usuario", () -> showDeleteUserDialog(gui)));
        
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Volver al Menu Principal", submenuWindow::close));

        submenuWindow.setComponent(mainPanel);
        gui.addWindow(submenuWindow);
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
        BasicWindow dialogWindow = new BasicWindow("Crear Usuario");
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

    private void showUpdateUserDialog(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Actualizar Usuario");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("ID de Usuario (userid):"));
        TextBox txtUserId = new TextBox();
        formPanel.addComponent(txtUserId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnSearch = new Button("Buscar", () -> {
            String userId = txtUserId.getText().trim();
            if (userId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "El ID de usuario es obligatorio");
                return;
            }

            SupabaseApiResponse<UsersTableRecord> response = igsfT001.getUserById(userId);
            if (response != null && response.data() != null && !response.data().isEmpty()) {
                UsersTableRecord current = response.data().get(0);
                dialogWindow.close();
                showEditUserForm(gui, userId, current);
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "No se pudo encontrar un usuario con ese ID");
            }
        });

        buttonPanel.addComponent(btnSearch);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void showEditUserForm(WindowBasedTextGUI gui, String userId, UsersTableRecord current) {
        BasicWindow editWindow = new BasicWindow("Modificar Usuario");
        editWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("Identificador:"));
        TextBox txtIdentifier = new TextBox(current.userIdentifier() != null ? current.userIdentifier() : "");
        formPanel.addComponent(txtIdentifier);

        formPanel.addComponent(new Label("Nombre Completo:"));
        TextBox txtName = new TextBox(current.userName() != null ? current.userName() : "");
        formPanel.addComponent(txtName);

        formPanel.addComponent(new Label("ID Estado:"));
        TextBox txtStatusId = new TextBox(current.statusId() != null ? current.statusId() : "1");
        formPanel.addComponent(txtStatusId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnSave = new Button("Guardar", () -> {
            String identifier = txtIdentifier.getText().trim();
            String name = txtName.getText().trim();
            String statusId = txtStatusId.getText().trim();

            if (identifier.isEmpty() || name.isEmpty() || statusId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "Todos los campos son obligatorios");
                return;
            }

            UsersTableRecord updated = new UsersTableRecord(null, identifier, name, statusId, null);
            SupabaseApiResponse<UsersTableRecord> response = igsfT001.updateUser(userId, updated);
            if (response != null && response.data() != null && !response.data().isEmpty()) {
                UsersTableRecord result = response.data().get(0);
                
                BasicWindow successWindow = new BasicWindow("Actualizacion Exitosa");
                successWindow.setHints(List.of(Window.Hint.CENTERED));
                
                Panel successPanel = new Panel(new GridLayout(1));
                successPanel.addComponent(new Label("Usuario actualizado exitosamente:"));
                successPanel.addComponent(new Separator(Direction.HORIZONTAL));
                
                Table<String> successTable = new Table<>("ID", "Identificador", "Nombre", "ID Estado", "Fecha Creacion");
                successTable.getTableModel().addRow(
                        result.userId() != null ? result.userId() : "",
                        result.userIdentifier() != null ? result.userIdentifier() : "",
                        result.userName() != null ? result.userName() : "",
                        result.statusId() != null ? result.statusId() : "",
                        result.createdAt() != null ? result.createdAt() : ""
                );
                successPanel.addComponent(successTable);
                successPanel.addComponent(new Separator(Direction.HORIZONTAL));
                successPanel.addComponent(new Button("Aceptar", successWindow::close));
                
                successWindow.setComponent(successPanel);
                editWindow.close();
                gui.addWindow(successWindow);
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "No se pudo actualizar el usuario en Supabase");
            }
        });

        buttonPanel.addComponent(btnSave);
        buttonPanel.addComponent(new Button("Cancelar", editWindow::close));

        mainPanel.addComponent(buttonPanel);
        editWindow.setComponent(mainPanel);
        gui.addWindow(editWindow);
    }

    private void showDeleteUserDialog(WindowBasedTextGUI gui) {
        BasicWindow submenuWindow = new BasicWindow("Eliminar Usuario - Opciones");
        submenuWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        mainPanel.addComponent(new Label("Seleccione el metodo de eliminacion:"));
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        mainPanel.addComponent(new Button("Eliminar por ID (userid)", () -> {
            submenuWindow.close();
            showDeleteByIdForm(gui);
        }));

        mainPanel.addComponent(new Button("Eliminar por Identificador (useridentifier)", () -> {
            submenuWindow.close();
            showDeleteByIdentifierForm(gui);
        }));

        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Volver", submenuWindow::close));

        submenuWindow.setComponent(mainPanel);
        gui.addWindow(submenuWindow);
    }

    private void showDeleteByIdForm(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Eliminar por ID");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("ID de Usuario (userid):"));
        TextBox txtUserId = new TextBox();
        formPanel.addComponent(txtUserId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnDelete = new Button("Eliminar", () -> {
            String userId = txtUserId.getText().trim();
            if (userId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "El ID de usuario es obligatorio");
                return;
            }

            SupabaseApiResponse<UsersTableRecord> response = igsfT001.deleteUserById(userId);
            handleDeleteResponse(gui, dialogWindow, response, "No se pudo encontrar o eliminar el usuario con ese ID");
        });
        
        buttonPanel.addComponent(btnDelete);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void showDeleteByIdentifierForm(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Eliminar por Identificador");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("Identificador (useridentifier):"));
        TextBox txtIdentifier = new TextBox();
        formPanel.addComponent(txtIdentifier);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnDelete = new Button("Eliminar", () -> {
            String identifier = txtIdentifier.getText().trim();
            if (identifier.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "El identificador es obligatorio");
                return;
            }

            SupabaseApiResponse<UsersTableRecord> response = igsfT001.deleteUserByIdentifier(identifier);
            handleDeleteResponse(gui, dialogWindow, response, "No se pudo encontrar o eliminar el usuario con ese identificador");
        });
        
        buttonPanel.addComponent(btnDelete);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void handleDeleteResponse(WindowBasedTextGUI gui, BasicWindow parentWindow, SupabaseApiResponse<UsersTableRecord> response, String errorMessage) {
        if (response != null && response.data() != null && !response.data().isEmpty()) {
            UsersTableRecord deleted = response.data().get(0);
            
            BasicWindow successWindow = new BasicWindow("Eliminacion Exitosa");
            successWindow.setHints(List.of(Window.Hint.CENTERED));
            
            Panel successPanel = new Panel(new GridLayout(1));
            successPanel.addComponent(new Label("Usuario eliminado de la base de datos:"));
            successPanel.addComponent(new Separator(Direction.HORIZONTAL));
            
            Table<String> successTable = new Table<>("ID", "Identificador", "Nombre", "ID Estado", "Fecha Creacion");
            successTable.getTableModel().addRow(
                    deleted.userId() != null ? deleted.userId() : "",
                    deleted.userIdentifier() != null ? deleted.userIdentifier() : "",
                    deleted.userName() != null ? deleted.userName() : "",
                    deleted.statusId() != null ? deleted.statusId() : "",
                    deleted.createdAt() != null ? deleted.createdAt() : ""
            );
            successPanel.addComponent(successTable);
            successPanel.addComponent(new Separator(Direction.HORIZONTAL));
            successPanel.addComponent(new Button("Aceptar", successWindow::close));
            
            successWindow.setComponent(successPanel);
            parentWindow.close();
            gui.addWindow(successWindow);
        } else {
            MessageDialog.showMessageDialog(gui, "Error", errorMessage);
        }
    }

    // ==========================================
    // SUBMENÚ: GESTIÓN DE PRODUCTOS
    // ==========================================
    private void showProductsSubmenu(WindowBasedTextGUI gui) {
        BasicWindow submenuWindow = new BasicWindow("Gestion de Productos");
        submenuWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        mainPanel.addComponent(new Label("Operaciones Disponibles:"));
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        mainPanel.addComponent(new Button("Listar Productos", () -> showProductsList(gui)));
        mainPanel.addComponent(new Button("Crear Producto", () -> showCreateProductDialog(gui)));
        mainPanel.addComponent(new Button("Actualizar Producto", () -> showUpdateProductDialog(gui)));
        mainPanel.addComponent(new Button("Eliminar Producto", () -> showDeleteProductDialog(gui)));
        mainPanel.addComponent(new Button("Buscar Producto", () -> showSearchProductDialog(gui)));
        
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Volver al Menu Principal", submenuWindow::close));

        submenuWindow.setComponent(mainPanel);
        gui.addWindow(submenuWindow);
    }

    private void showProductsList(WindowBasedTextGUI gui) {
        BasicWindow listWindow = new BasicWindow("Productos Registrados");
        listWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));

        SupabaseApiResponse<ProductsTableRecord> response = igsfT002.getProducts();
        if (response == null || response.data() == null || response.data().isEmpty()) {
            mainPanel.addComponent(new Label("No se encontraron registros de productos."));
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
        BasicWindow dialogWindow = new BasicWindow("Crear Producto");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("Nombre Producto:"));
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
        
        Button btnSave = new Button("Guardar", () -> {
            String name = txtName.getText().trim();
            String description = txtDescription.getText().trim();
            String quantity = txtQuantity.getText().trim();
            String statusId = txtStatusId.getText().trim();

            if (name.isEmpty() || description.isEmpty() || quantity.isEmpty() || statusId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "Todos los campos son obligatorios");
                return;
            }

            ProductsTableRecord record = new ProductsTableRecord(null, name, description, quantity, statusId, null);
            SupabaseApiResponse<ProductsTableRecord> response = igsfT002.createProduct(record);
            if (response != null && response.data() != null && !response.data().isEmpty()) {
                ProductsTableRecord created = response.data().get(0);
                
                BasicWindow successWindow = new BasicWindow("Confirmacion");
                successWindow.setHints(List.of(Window.Hint.CENTERED));
                
                Panel successPanel = new Panel(new GridLayout(1));
                successPanel.addComponent(new Label("Producto creado exitosamente:"));
                successPanel.addComponent(new Separator(Direction.HORIZONTAL));
                
                Table<String> successTable = new Table<>("ID", "Nombre", "Descripcion", "Cantidad", "ID Estado", "Fecha Creacion");
                successTable.getTableModel().addRow(
                        created.productId() != null ? created.productId() : "",
                        created.productName() != null ? created.productName() : "",
                        created.productDescription() != null ? created.productDescription() : "",
                        created.productQuantity() != null ? created.productQuantity() : "",
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
                MessageDialog.showMessageDialog(gui, "Error", "Fallo al crear producto en Supabase");
            }
        });
        
        buttonPanel.addComponent(btnSave);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void showUpdateProductDialog(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Actualizar Producto");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("ID de Producto a modificar (productid):"));
        TextBox txtProductId = new TextBox();
        formPanel.addComponent(txtProductId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnSearch = new Button("Buscar", () -> {
            String productId = txtProductId.getText().trim();
            if (productId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "El ID de producto es obligatorio");
                return;
            }

            SupabaseApiResponse<ProductsTableRecord> response = igsfT002.getProductById(productId);
            if (response != null && response.data() != null && !response.data().isEmpty()) {
                ProductsTableRecord current = response.data().get(0);
                dialogWindow.close();
                showEditProductForm(gui, productId, current);
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "No se pudo encontrar un producto con ese ID");
            }
        });

        buttonPanel.addComponent(btnSearch);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void showEditProductForm(WindowBasedTextGUI gui, String productId, ProductsTableRecord current) {
        BasicWindow editWindow = new BasicWindow("Modificar Producto");
        editWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("Nombre Producto:"));
        TextBox txtName = new TextBox(current.productName() != null ? current.productName() : "");
        formPanel.addComponent(txtName);

        formPanel.addComponent(new Label("Descripcion:"));
        TextBox txtDescription = new TextBox(current.productDescription() != null ? current.productDescription() : "");
        formPanel.addComponent(txtDescription);

        formPanel.addComponent(new Label("Cantidad:"));
        TextBox txtQuantity = new TextBox(current.productQuantity() != null ? current.productQuantity() : "1");
        formPanel.addComponent(txtQuantity);

        formPanel.addComponent(new Label("ID Estado:"));
        TextBox txtStatusId = new TextBox(current.statusId() != null ? current.statusId() : "1");
        formPanel.addComponent(txtStatusId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnSave = new Button("Guardar", () -> {
            String name = txtName.getText().trim();
            String description = txtDescription.getText().trim();
            String quantity = txtQuantity.getText().trim();
            String statusId = txtStatusId.getText().trim();

            if (name.isEmpty() || description.isEmpty() || quantity.isEmpty() || statusId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "Todos los campos son obligatorios");
                return;
            }

            ProductsTableRecord updated = new ProductsTableRecord(productId, name, description, quantity, statusId, null);
            SupabaseApiResponse<ProductsTableRecord> response = igsfT002.updateProduct(updated);
            if (response != null && response.data() != null && !response.data().isEmpty()) {
                ProductsTableRecord result = response.data().get(0);
                
                BasicWindow successWindow = new BasicWindow("Actualizacion Exitosa");
                successWindow.setHints(List.of(Window.Hint.CENTERED));
                
                Panel successPanel = new Panel(new GridLayout(1));
                successPanel.addComponent(new Label("Producto actualizado exitosamente:"));
                successPanel.addComponent(new Separator(Direction.HORIZONTAL));
                
                Table<String> successTable = new Table<>("ID", "Nombre", "Descripcion", "Cantidad", "ID Estado", "Fecha Creacion");
                successTable.getTableModel().addRow(
                        result.productId() != null ? result.productId() : "",
                        result.productName() != null ? result.productName() : "",
                        result.productDescription() != null ? result.productDescription() : "",
                        result.productQuantity() != null ? result.productQuantity() : "",
                        result.statusId() != null ? result.statusId() : "",
                        result.createdAt() != null ? result.createdAt() : ""
                );
                successPanel.addComponent(successTable);
                successPanel.addComponent(new Separator(Direction.HORIZONTAL));
                successPanel.addComponent(new Button("Aceptar", successWindow::close));
                
                successWindow.setComponent(successPanel);
                editWindow.close();
                gui.addWindow(successWindow);
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "No se pudo actualizar el producto en Supabase");
            }
        });

        buttonPanel.addComponent(btnSave);
        buttonPanel.addComponent(new Button("Cancelar", editWindow::close));

        mainPanel.addComponent(buttonPanel);
        editWindow.setComponent(mainPanel);
        gui.addWindow(editWindow);
    }

    private void showDeleteProductDialog(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Eliminar Producto");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("ID de Producto a eliminar (productid):"));
        TextBox txtProductId = new TextBox();
        formPanel.addComponent(txtProductId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnDelete = new Button("Eliminar", () -> {
            String productId = txtProductId.getText().trim();
            if (productId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "El ID de producto es obligatorio");
                return;
            }

            SupabaseApiResponse<ProductsTableRecord> response = igsfT002.deleteProduct(productId);
            handleProductDeleteResponse(gui, dialogWindow, response, "No se pudo encontrar o eliminar el producto con ese ID");
        });
        
        buttonPanel.addComponent(btnDelete);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void handleProductDeleteResponse(WindowBasedTextGUI gui, BasicWindow parentWindow, SupabaseApiResponse<ProductsTableRecord> response, String errorMessage) {
        if (response != null && response.data() != null && !response.data().isEmpty()) {
            ProductsTableRecord deleted = response.data().get(0);
            
            BasicWindow successWindow = new BasicWindow("Eliminacion Exitosa");
            successWindow.setHints(List.of(Window.Hint.CENTERED));
            
            Panel successPanel = new Panel(new GridLayout(1));
            successPanel.addComponent(new Label("Producto eliminado de la base de datos:"));
            successPanel.addComponent(new Separator(Direction.HORIZONTAL));
            
            Table<String> successTable = new Table<>("ID", "Nombre", "Descripcion", "Cantidad", "ID Estado", "Fecha Creacion");
            successTable.getTableModel().addRow(
                    deleted.productId() != null ? deleted.productId() : "",
                    deleted.productName() != null ? deleted.productName() : "",
                    deleted.productDescription() != null ? deleted.productDescription() : "",
                    deleted.productQuantity() != null ? deleted.productQuantity() : "",
                    deleted.statusId() != null ? deleted.statusId() : "",
                    deleted.createdAt() != null ? deleted.createdAt() : ""
            );
            successPanel.addComponent(successTable);
            successPanel.addComponent(new Separator(Direction.HORIZONTAL));
            successPanel.addComponent(new Button("Aceptar", successWindow::close));
            
            successWindow.setComponent(successPanel);
            parentWindow.close();
            gui.addWindow(successWindow);
        } else {
            MessageDialog.showMessageDialog(gui, "Error", errorMessage);
        }
    }

    private void showSearchProductDialog(WindowBasedTextGUI gui) {
        BasicWindow submenuWindow = new BasicWindow("Buscar Producto - Opciones");
        submenuWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        mainPanel.addComponent(new Label("Seleccione el metodo de busqueda:"));
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        mainPanel.addComponent(new Button("Buscar por ID", () -> {
            submenuWindow.close();
            showSearchProductByIdForm(gui);
        }));

        mainPanel.addComponent(new Button("Buscar por Nombre", () -> {
            submenuWindow.close();
            showSearchProductByNameForm(gui);
        }));

        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Volver", submenuWindow::close));

        submenuWindow.setComponent(mainPanel);
        gui.addWindow(submenuWindow);
    }

    private void showSearchProductByIdForm(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Buscar por ID");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("ID de Producto:"));
        TextBox txtProductId = new TextBox();
        formPanel.addComponent(txtProductId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnSearch = new Button("Buscar", () -> {
            String productId = txtProductId.getText().trim();
            if (productId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "El ID de producto es obligatorio");
                return;
            }

            SupabaseApiResponse<ProductsTableRecord> response = igsfT002.getProductById(productId);
            displayProductSearchResults(gui, dialogWindow, response, "No se encontro ningun producto con ese ID");
        });
        
        buttonPanel.addComponent(btnSearch);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void showSearchProductByNameForm(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Buscar por Nombre");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("Nombre de Producto:"));
        TextBox txtProductName = new TextBox();
        formPanel.addComponent(txtProductName);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnSearch = new Button("Buscar", () -> {
            String productName = txtProductName.getText().trim();
            if (productName.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "El nombre es obligatorio");
                return;
            }

            SupabaseApiResponse<ProductsTableRecord> response = igsfT002.getProductByName(productName);
            displayProductSearchResults(gui, dialogWindow, response, "No se encontraron productos con ese nombre");
        });
        
        buttonPanel.addComponent(btnSearch);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void displayProductSearchResults(WindowBasedTextGUI gui, BasicWindow parentWindow, SupabaseApiResponse<ProductsTableRecord> response, String errorMessage) {
        if (response != null && response.data() != null && !response.data().isEmpty()) {
            BasicWindow resultsWindow = new BasicWindow("Resultados de Busqueda");
            resultsWindow.setHints(List.of(Window.Hint.CENTERED));
            
            Panel mainPanel = new Panel(new GridLayout(1));
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
            mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
            mainPanel.addComponent(new Button("Aceptar", resultsWindow::close));
            
            resultsWindow.setComponent(mainPanel);
            parentWindow.close();
            gui.addWindow(resultsWindow);
        } else {
            MessageDialog.showMessageDialog(gui, "Error", errorMessage);
        }
    }

    // ==========================================
    // SUBMENÚ: HISTORIAL DE TRANSACCIONES
    // ==========================================
    private void showHistorySubmenu(WindowBasedTextGUI gui) {
        BasicWindow submenuWindow = new BasicWindow("Consulta de Historial");
        submenuWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        mainPanel.addComponent(new Label("Opciones de Consulta de Historial:"));
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        mainPanel.addComponent(new Button("Listar Todo el Historial", () -> showAllHistory(gui)));
        mainPanel.addComponent(new Button("Buscar por ID de Usuario", () -> showSearchHistoryByUserIdForm(gui)));
        mainPanel.addComponent(new Button("Buscar por ID de Accion", () -> showSearchHistoryByActionIdForm(gui)));
        mainPanel.addComponent(new Button("Buscar por ID de Producto", () -> showSearchHistoryByProductIdForm(gui)));
        mainPanel.addComponent(new Button("Buscar por Fecha", () -> showSearchHistoryByDateForm(gui)));
        
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Volver al Menu Principal", submenuWindow::close));

        submenuWindow.setComponent(mainPanel);
        gui.addWindow(submenuWindow);
    }

    private void showAllHistory(WindowBasedTextGUI gui) {
        SupabaseApiResponse<HistoryTableRecord> response = igsfT003.getAllHistory();
        displayHistoryResults(gui, response, "No se encontraron registros de historial en la base de datos.");
    }

    private void showSearchHistoryByUserIdForm(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Buscar Historial por ID de Usuario");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("ID de Usuario (userid):"));
        TextBox txtUserId = new TextBox();
        formPanel.addComponent(txtUserId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnSearch = new Button("Buscar", () -> {
            String userId = txtUserId.getText().trim();
            if (userId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "El ID de usuario es obligatorio");
                return;
            }

            SupabaseApiResponse<HistoryTableRecord> response = igsfT003.getHistoryByUserId(userId);
            dialogWindow.close();
            displayHistoryResults(gui, response, "No se encontro historial para ese ID de usuario");
        });
        
        buttonPanel.addComponent(btnSearch);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void showSearchHistoryByActionIdForm(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Buscar Historial por ID de Accion");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("ID de Accion (actionid):"));
        TextBox txtActionId = new TextBox();
        formPanel.addComponent(txtActionId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnSearch = new Button("Buscar", () -> {
            String actionId = txtActionId.getText().trim();
            if (actionId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "El ID de accion es obligatorio");
                return;
            }

            SupabaseApiResponse<HistoryTableRecord> response = igsfT003.getHistoryByActionId(actionId);
            dialogWindow.close();
            displayHistoryResults(gui, response, "No se encontro historial para ese ID de accion");
        });
        
        buttonPanel.addComponent(btnSearch);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void showSearchHistoryByProductIdForm(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Buscar Historial por ID de Producto");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("ID de Producto (productid):"));
        TextBox txtProductId = new TextBox();
        formPanel.addComponent(txtProductId);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnSearch = new Button("Buscar", () -> {
            String productId = txtProductId.getText().trim();
            if (productId.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "El ID de producto es obligatorio");
                return;
            }

            SupabaseApiResponse<HistoryTableRecord> response = igsfT003.getHistoryByProductId(productId);
            dialogWindow.close();
            displayHistoryResults(gui, response, "No se encontro historial para ese ID de producto");
        });
        
        buttonPanel.addComponent(btnSearch);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void showSearchHistoryByDateForm(WindowBasedTextGUI gui) {
        BasicWindow dialogWindow = new BasicWindow("Buscar Historial por Fecha");
        dialogWindow.setHints(List.of(Window.Hint.CENTERED));

        Panel mainPanel = new Panel(new GridLayout(1));
        Panel formPanel = new Panel(new GridLayout(2));

        formPanel.addComponent(new Label("Fecha (YYYY-MM-DD):"));
        TextBox txtDate = new TextBox();
        formPanel.addComponent(txtDate);

        mainPanel.addComponent(formPanel);
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));

        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        
        Button btnSearch = new Button("Buscar", () -> {
            String date = txtDate.getText().trim();
            if (date.isEmpty()) {
                MessageDialog.showMessageDialog(gui, "Error", "La fecha es obligatoria");
                return;
            }

            SupabaseApiResponse<HistoryTableRecord> response = igsfT003.getHistoryByDate(date);
            dialogWindow.close();
            displayHistoryResults(gui, response, "No se encontro historial para esa fecha");
        });
        
        buttonPanel.addComponent(btnSearch);
        buttonPanel.addComponent(new Button("Cancelar", dialogWindow::close));

        mainPanel.addComponent(buttonPanel);
        dialogWindow.setComponent(mainPanel);
        gui.addWindow(dialogWindow);
    }

    private void displayHistoryResults(WindowBasedTextGUI gui, SupabaseApiResponse<HistoryTableRecord> response, String errorMessage) {
        if (response == null || response.data() == null || response.data().isEmpty()) {
            MessageDialog.showMessageDialog(gui, "Informacion", errorMessage);
            return;
        }

        BasicWindow resultsWindow = new BasicWindow("Historial de Transacciones");
        resultsWindow.setHints(List.of(Window.Hint.CENTERED));
        
        Panel mainPanel = new Panel(new GridLayout(1));
        Table<String> table = new Table<>("ID Historial", "ID Usuario", "ID Accion", "ID Producto", "Fecha Transaccion");
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
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(new Button("Aceptar", resultsWindow::close));
        
        resultsWindow.setComponent(mainPanel);
        gui.addWindow(resultsWindow);
    }
}
