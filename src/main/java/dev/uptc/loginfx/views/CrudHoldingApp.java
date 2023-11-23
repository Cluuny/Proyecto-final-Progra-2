package dev.uptc.loginfx.views;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Optional;
import javafx.scene.layout.HBox;

public class CrudHoldingApp extends Application {

    private ObservableList<Empresa> empresaList = FXCollections.observableArrayList();
    private ObservableList<Sede> sedeList = FXCollections.observableArrayList();
    private ObservableList<Departamento> departamentoList = FXCollections.observableArrayList();
    private ObservableList<Empleado> empleadoList = FXCollections.observableArrayList();

    private static final String DB_URL = "jdbc:mysql://localhost:3306/javafxdbp2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private Connection dbConnection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadEmpresas();
        loadSedes();
        loadDepartamentos();
        loadEmpleados();

        primaryStage.setTitle("Department and Employee App");

        Label empresaLabel = new Label("Empresas");
        ListView<Empresa> empresaListView = new ListView<>(empresaList);
        empresaListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Empresa item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getId() + ", Nombre: " + item.getNombre());
                }
            }
        });

        Label sedeLabel = new Label("Sedes");
        ListView<Sede> sedeListView = new ListView<>(sedeList);
        sedeListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Sede item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getId() + ", Nombre: " + item.getNombre() + ", Empresa ID: " + item.getEmpresaId());
                }
            }
        });

        Label departamentoLabel = new Label("Departamentos");
        ListView<Departamento> departamentoListView = new ListView<>(departamentoList);
        departamentoListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Departamento item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getId() + ", Nombre: " + item.getNombre() + ", Sede ID: " + item.getSedeId());
                }
            }
        });

        Label empleadoLabel = new Label("Empleados");
        ListView<Empleado> empleadoListView = new ListView<>(empleadoList);
        empleadoListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Empleado item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("ID: " + item.getId() + ", Nombre: " + item.getNombre() + ", Departamento ID: " + item.getDepartamentoId());
                }
            }
        });

//BOTONES:
        Button agregarEmpresaButton = new Button("Agregar Empresa");
        agregarEmpresaButton.setOnAction(e -> agregarEmpresa());

        Button agregarSedeButton = new Button("Agregar Sede");
        agregarSedeButton.setOnAction(e -> agregarSede());

        Button agregarDepartamentoButton = new Button("Agregar Departamento");
        agregarDepartamentoButton.setOnAction(e -> agregarDepartamento());

        Button agregarEmpleadoButton = new Button("Agregar Empleado");
        agregarEmpleadoButton.setOnAction(e -> agregarEmpleado());

///// botones eliminar:
        Button eliminarEmpleadoButton = new Button("Eliminar Empleado");
        eliminarEmpleadoButton.setOnAction(e -> {
            Empleado empleadoSeleccionado = empleadoListView.getSelectionModel().getSelectedItem();
            eliminarEmpleado(empleadoSeleccionado);
        });

        Button eliminarDepartamentoButton = new Button("Eliminar Departamento");
        eliminarDepartamentoButton.setOnAction(e -> {
            Departamento departamentoSeleccionado = departamentoListView.getSelectionModel().getSelectedItem();
            eliminarDepartamento(departamentoSeleccionado);
        });

        Button eliminarSedeButton = new Button("Eliminar Sede");
        eliminarSedeButton.setOnAction(e -> {
            Sede sedeSeleccionada = sedeListView.getSelectionModel().getSelectedItem();
            eliminarSede(sedeSeleccionada);
        });

        Button eliminarEmpresaButton = new Button("Eliminar Empresa");
        eliminarEmpresaButton.setOnAction(e -> {
            Empresa empresaSeleccionada = empresaListView.getSelectionModel().getSelectedItem();
            eliminarEmpresa(empresaSeleccionada);
        });
//botones modificar update:
        Button modificarEmpleadoButton = new Button("Modificar Empleado");
        modificarEmpleadoButton.setOnAction(e -> {
            Empleado empleadoSeleccionado = empleadoListView.getSelectionModel().getSelectedItem();
            modificarEmpleado(empleadoSeleccionado);
        });

        Button modificarDepartamentoButton = new Button("Modificar Departamento");
        modificarDepartamentoButton.setOnAction(e -> {
            Departamento departamentoSeleccionado = departamentoListView.getSelectionModel().getSelectedItem();
            modificarDepartamento(departamentoSeleccionado);
        });

        Button modificarSedeButton = new Button("Modificar Sede");
        modificarSedeButton.setOnAction(e -> {
            Sede sedeSeleccionada = sedeListView.getSelectionModel().getSelectedItem();
            modificarSede(sedeSeleccionada);
        });

        Button modificarEmpresaButton = new Button("Modificar Empresa");
        modificarEmpresaButton.setOnAction(e -> {
            Empresa empresaSeleccionada = empresaListView.getSelectionModel().getSelectedItem();
            modificarEmpresa(empresaSeleccionada);
        });

        HBox buttonBox_empresa = new HBox(agregarEmpresaButton, modificarEmpresaButton, eliminarEmpresaButton);
        buttonBox_empresa.setSpacing(10);

        HBox buttonBox_sede = new HBox(agregarSedeButton, modificarSedeButton, eliminarSedeButton);
        buttonBox_sede.setSpacing(10);

        HBox buttonBox_departamento = new HBox(agregarDepartamentoButton, modificarDepartamentoButton, eliminarDepartamentoButton);
        buttonBox_departamento.setSpacing(10);

        HBox buttonBox_empleado = new HBox(agregarEmpleadoButton, modificarEmpleadoButton, eliminarEmpleadoButton);
        buttonBox_empleado.setSpacing(10);

        VBox vbox = new VBox(
                empresaLabel,
                empresaListView,
                buttonBox_empresa,
                sedeLabel,
                sedeListView,
                buttonBox_sede,
                departamentoLabel,
                departamentoListView,
                buttonBox_departamento,
                empleadoLabel,
                empleadoListView,
                buttonBox_empleado
        );
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void modificarEmpresa(Empresa empresa) {
        if (empresa == null) {
            showAlert("No se ha seleccionado una empresa", "Por favor, seleccione una empresa para modificar.");
            return;
        }

        // Cuadro de di�logo para ingresar el nuevo nombre de la empresa
        TextInputDialog nombreDialog = new TextInputDialog();
        nombreDialog.setTitle("Modificar Empresa");
        nombreDialog.setHeaderText(null);
        nombreDialog.setContentText("Ingrese el nuevo nombre para la empresa:");

        Optional<String> nuevoNombreResult = nombreDialog.showAndWait();

        // Verificar si se ingres� un nuevo nombre
        if (nuevoNombreResult.isPresent() && !nuevoNombreResult.get().isEmpty()) {
            // Modificar el nombre de la empresa en la base de datos
            modificarEmpresaEnBaseDeDatos(empresa.getId(), nuevoNombreResult.get());

            // Actualizar la lista observable
            loadEmpresas();
            showAlert("Empresa Modificada", "Nombre de la empresa modificado exitosamente.");
        } else {
            showAlert("Entrada Inv�lida", "El nombre de la empresa no puede estar vac�o.");
        }
    }

    private void modificarEmpresaEnBaseDeDatos(int idEmpresa, String nuevoNombre) {
        nuevoNombre = nuevoNombre.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("UPDATE empresa SET nombre_empresa = ? WHERE id_empresa = ?")) {
            statement.setString(1, nuevoNombre);
            statement.setInt(2, idEmpresa);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modificarSede(Sede sede) {
        if (sede == null) {
            showAlert("No se ha seleccionado una sede", "Por favor, seleccione una sede para modificar.");
            return;
        }

        // Cuadro de di�logo para ingresar el nuevo nombre de la sede
        TextInputDialog nombreDialog = new TextInputDialog();
        nombreDialog.setTitle("Modificar Sede");
        nombreDialog.setHeaderText(null);
        nombreDialog.setContentText("Ingrese el nuevo nombre para la sede:");

        Optional<String> nuevoNombreResult = nombreDialog.showAndWait();

        // Verificar si se ingres� un nuevo nombre
        if (nuevoNombreResult.isPresent() && !nuevoNombreResult.get().isEmpty()) {
            // Cuadro de di�logo de selecci�n de empresa
            ObservableList<String> empresaInfoList = FXCollections.observableArrayList();
            for (Empresa empresa : empresaList) {
                String info = String.format("ID: %d, Nombre: %s", empresa.getId(), empresa.getNombre());
                empresaInfoList.add(info);
            }

            ChoiceDialog<String> empresaDialog = new ChoiceDialog<>(empresaInfoList.get(0), empresaInfoList);
            empresaDialog.setTitle("Seleccionar Empresa");
            empresaDialog.setHeaderText(null);
            empresaDialog.setContentText("Seleccione la nueva empresa para la sede:");

            Optional<String> nuevaEmpresaInfoResult = empresaDialog.showAndWait();

            // Verificar si se seleccion� una nueva empresa
            if (nuevaEmpresaInfoResult.isPresent()) {
                // Extraer el ID de la empresa seleccionada
                int startIndex = nuevaEmpresaInfoResult.get().indexOf("ID: ") + 4;
                int endIndex = nuevaEmpresaInfoResult.get().indexOf(", Nombre:");
                int nuevaEmpresaId = Integer.parseInt(nuevaEmpresaInfoResult.get().substring(startIndex, endIndex).trim());

                // Modificar el nombre y la empresa de la sede en la base de datos
                modificarSedeEnBaseDeDatos(sede.getId(), nuevoNombreResult.get(), nuevaEmpresaId);

                // Actualizar la lista observable
                loadSedes();
                showAlert("Sede Modificada", "Nombre y empresa de la sede modificados exitosamente.");
            }
        } else {
            showAlert("Entrada Inv�lida", "El nombre de la sede no puede estar vac�o.");
        }
    }

    private void modificarSedeEnBaseDeDatos(int idSede, String nuevoNombre, int nuevaEmpresaId) {
        nuevoNombre = nuevoNombre.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("UPDATE sede SET nombre_sede = ?, empresa_id = ? WHERE id_sede = ?")) {
            statement.setString(1, nuevoNombre);
            statement.setInt(2, nuevaEmpresaId);
            statement.setInt(3, idSede);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modificarEmpleado(Empleado empleado) {
        if (empleado == null) {
            showAlert("No se ha seleccionado un empleado", "Por favor, seleccione un empleado para modificar.");
            return;
        }

        // Cuadro de di�logo para ingresar el nuevo nombre del empleado
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Modificar Empleado");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese el nuevo nombre para el empleado:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nuevoNombre -> {
            if (!nuevoNombre.trim().isEmpty()) {
                // Modificar el nombre del empleado en la base de datos
                modificarNombreEmpleadoEnBaseDeDatos(empleado.getId(), nuevoNombre);

                // Actualizar la lista observable
                loadEmpleados();
                showAlert("Empleado Modificado", "Nombre del empleado modificado exitosamente.");
            } else {
                showAlert("Entrada Inv�lida", "El nombre del empleado no puede estar vac�o.");
            }
        });
    }

    private void modificarNombreEmpleadoEnBaseDeDatos(int idEmpleado, String nuevoNombre) {
        nuevoNombre = nuevoNombre.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("UPDATE empleado SET nombre_empleado = ? WHERE id_empleado = ?")) {
            statement.setString(1, nuevoNombre);
            statement.setInt(2, idEmpleado);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modificarDepartamento(Departamento departamento) {
        if (departamento == null) {
            showAlert("No se ha seleccionado un departamento", "Por favor, seleccione un departamento para modificar.");
            return;
        }

        // Cuadro de di�logo para ingresar el nuevo nombre del departamento
        TextInputDialog nombreDialog = new TextInputDialog();
        nombreDialog.setTitle("Modificar Departamento");
        nombreDialog.setHeaderText(null);
        nombreDialog.setContentText("Ingrese el nuevo nombre para el departamento:");

        Optional<String> nuevoNombreResult = nombreDialog.showAndWait();

        // Verificar si se ingres� un nuevo nombre
        if (nuevoNombreResult.isPresent() && !nuevoNombreResult.get().isEmpty()) {
            // Cuadro de di�logo de selecci�n de sede
            ObservableList<String> sedeInfoList = FXCollections.observableArrayList();
            for (Sede sede : sedeList) {
                // Obtener informaci�n de la sede y la empresa
                Empresa empresa = getEmpresaBySedeId(sede.getId());
                String info = String.format(
                        "Sede: ID: %d, Nombre: %s, Empresa: ID: %d, Nombre: %s",
                        sede.getId(), sede.getNombre(),
                        empresa.getId(), empresa.getNombre());
                sedeInfoList.add(info);
            }

            ChoiceDialog<String> sedeDialog = new ChoiceDialog<>(sedeInfoList.get(0), sedeInfoList);
            sedeDialog.setTitle("Seleccionar Sede");
            sedeDialog.setHeaderText(null);
            sedeDialog.setContentText("Seleccione la nueva sede para el departamento:");

            Optional<String> nuevaSedeInfoResult = sedeDialog.showAndWait();

            // Verificar si se seleccion� una nueva sede
            if (nuevaSedeInfoResult.isPresent()) {
                // Extraer el ID de la sede seleccionada
                int startIndex = nuevaSedeInfoResult.get().indexOf("Sede: ID: ") + 10;
                int endIndex = nuevaSedeInfoResult.get().indexOf(", Nombre:");
                int nuevaSedeId = Integer.parseInt(nuevaSedeInfoResult.get().substring(startIndex, endIndex).trim());

                // Modificar el nombre y la sede del departamento en la base de datos
                modificarDepartamentoEnBaseDeDatos(departamento.getId(), nuevoNombreResult.get(), nuevaSedeId);

                // Actualizar la lista observable
                loadDepartamentos();
                showAlert("Departamento Modificado", "Nombre y sede del departamento modificados exitosamente.");
            }
        } else {
            showAlert("Entrada Inv�lida", "El nombre del departamento no puede estar vac�o.");
        }
    }

    private void modificarDepartamentoEnBaseDeDatos(int idDepartamento, String nuevoNombre, int nuevaSedeId) {
        nuevoNombre = nuevoNombre.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("UPDATE departamento SET nombre_departamento = ?, sede_id = ? WHERE id_departamento = ?")) {
            statement.setString(1, nuevoNombre);
            statement.setInt(2, nuevaSedeId);
            statement.setInt(3, idDepartamento);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarEmpresa(Empresa empresa) {
        if (empresa == null) {
            showAlert("No se ha seleccionado una empresa", "Por favor, seleccione una empresa para eliminar.");
            return;
        }

        // Verificar si la empresa est� asociada a alguna sede
        if (tieneSedesAsociadas(empresa.getId())) {
            showAlert("Error al Eliminar Empresa", "No se puede eliminar una empresa que est� asociada a una sede.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Empresa");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("�Est� seguro de que desea eliminar la empresa: " + empresa.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Eliminar empresa de la base de datos
            eliminarEmpresaDeBaseDeDatos(empresa.getId());

            // Actualizar la lista observable
            loadEmpresas();
            showAlert("Empresa Eliminada", "Empresa eliminada exitosamente.");
        }
    }

    private boolean tieneSedesAsociadas(int idEmpresa) {
        try (PreparedStatement statement = dbConnection.prepareStatement("SELECT COUNT(*) FROM sede WHERE empresa_id = ?")) {
            statement.setInt(1, idEmpresa);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void eliminarEmpresaDeBaseDeDatos(int idEmpresa) {
        try (PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM empresa WHERE id_empresa = ?")) {
            statement.setInt(1, idEmpresa);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarEmpleado(Empleado empleado) {
        if (empleado == null) {
            showAlert("No se ha seleccionado un empleado", "Por favor, seleccione un empleado para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Empleado");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("�Est� seguro de que desea eliminar al empleado: " + empleado.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Eliminar empleado de la base de datos
            eliminarEmpleadoDeBaseDeDatos(empleado.getId());

            // Actualizar la lista observable
            loadEmpleados();
            showAlert("Empleado Eliminado", "Empleado eliminado exitosamente.");
        }
    }

    private void eliminarEmpleadoDeBaseDeDatos(int idEmpleado) {
        try (PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM empleado WHERE id_empleado = ?")) {
            statement.setInt(1, idEmpleado);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarDepartamento(Departamento departamento) {
        if (departamento == null) {
            showAlert("No se ha seleccionado un departamento", "Por favor, seleccione un departamento para eliminar.");
            return;
        }

        // Verificar si el departamento tiene empleados asociados
        if (tieneEmpleadosAsociados(departamento.getId())) {
            showAlert("Error al Eliminar Departamento", "No se puede eliminar un departamento que tiene empleados asociados.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Departamento");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("�Est� seguro de que desea eliminar el departamento: " + departamento.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Eliminar departamento de la base de datos
            eliminarDepartamentoDeBaseDeDatos(departamento.getId());

            // Actualizar la lista observable
            loadDepartamentos();
            showAlert("Departamento Eliminado", "Departamento eliminado exitosamente.");
        }
    }

    private boolean tieneEmpleadosAsociados(int idDepartamento) {
        try (PreparedStatement statement = dbConnection.prepareStatement("SELECT COUNT(*) FROM empleado WHERE departamento_id = ?")) {
            statement.setInt(1, idDepartamento);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void eliminarDepartamentoDeBaseDeDatos(int idDepartamento) {
        try (PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM departamento WHERE id_departamento = ?")) {
            statement.setInt(1, idDepartamento);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarSede(Sede sede) {
        if (sede == null) {
            showAlert("No se ha seleccionado una sede", "Por favor, seleccione una sede para eliminar.");
            return;
        }

        // Verificar si la sede est� asociada a alg�n departamento
        if (tieneDepartamentosAsociados(sede.getId())) {
            showAlert("Error al Eliminar Sede", "No se puede eliminar una sede que est� asociada a un departamento.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Sede");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("�Est� seguro de que desea eliminar la sede: " + sede.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Eliminar sede de la base de datos
            eliminarSedeDeBaseDeDatos(sede.getId());

            // Actualizar la lista observable
            loadSedes();
            showAlert("Sede Eliminada", "Sede eliminada exitosamente.");
        }
    }

    private boolean tieneDepartamentosAsociados(int idSede) {
        try (PreparedStatement statement = dbConnection.prepareStatement("SELECT COUNT(*) FROM departamento WHERE sede_id = ?")) {
            statement.setInt(1, idSede);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void eliminarSedeDeBaseDeDatos(int idSede) {
        try (PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM sede WHERE id_sede = ?")) {
            statement.setInt(1, idSede);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// M�todo para agregar una nueva empresa
    private void agregarEmpresa() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Empresa");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese el nombre de la nueva empresa:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nombre -> {
            if (!nombre.trim().isEmpty()) {
                // Insertar nueva empresa en la base de datos
                agregarEmpresaToDatabase(nombre);

                // Actualizar la lista observable
                loadEmpresas();
            } else {
                showAlert("Entrada Inv�lida", "El nombre de la empresa no puede estar vac�o.");
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

// M�todo para insertar una nueva empresa en la base de datos
    private void agregarEmpresaToDatabase(String nombre) {
        nombre = nombre.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO empresa (nombre_empresa) VALUES (?)")) {
            statement.setString(1, nombre);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void agregarSede() {
        if (empresaList.isEmpty()) {
            showAlert("Sin empresas", "No hay empresas creadas. Cree al menos una empresa antes de agregar una sede.");
            return;
        }

        // Cuadro de di�logo para ingresar el nombre de la nueva sede
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Sede");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese el nombre de la nueva sede:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nombre -> {
            if (!nombre.trim().isEmpty()) {
                // Cuadro de di�logo de selecci�n de empresa
                ObservableList<String> empresaInfoList = FXCollections.observableArrayList();
                for (Empresa empresa : empresaList) {
                    String info = String.format("ID: %d, Nombre: %s", empresa.getId(), empresa.getNombre());
                    empresaInfoList.add(info);
                }

                ChoiceDialog<String> empresaDialog = new ChoiceDialog<>(empresaInfoList.get(0), empresaInfoList);
                empresaDialog.setTitle("Seleccionar Empresa");
                empresaDialog.setHeaderText(null);
                empresaDialog.setContentText("Seleccione la empresa para la nueva sede:");

                Optional<String> selectedEmpresaInfo = empresaDialog.showAndWait();

                selectedEmpresaInfo.ifPresent(empresaInfo -> {
                    // Extraer el ID de la empresa seleccionada
                    int startIndex = empresaInfo.indexOf("ID: ") + 4;
                    int endIndex = empresaInfo.indexOf(", Nombre:");
                    int empresaId = Integer.parseInt(empresaInfo.substring(startIndex, endIndex).trim());

                    // Insertar nueva sede en la base de datos
                    agregarSedeToDatabase(nombre, empresaId);

                    // Actualizar la lista observable
                    loadSedes();
                });
            } else {
                showAlert("Entrada Inv�lida", "El nombre de la sede no puede estar vac�o.");
            }
        });
    }

    private int getEmpresaIdByNombre(String nombre) {
        for (Empresa empresa : empresaList) {
            if (empresa.getNombre().equals(nombre)) {
                return empresa.getId();
            }
        }
        return -1; // Valor de retorno predeterminado si no se encuentra la empresa
    }

    private void agregarSedeToDatabase(String nombre, int empresaId) {
        nombre = nombre.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO sede (nombre_sede, empresa_id) VALUES (?, ?)")) {
            statement.setString(1, nombre);
            statement.setInt(2, empresaId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
    private void agregarDepartamento() {

        if (sedeList.isEmpty()) {
            showAlert("Sin Sedes", "No hay sedes creadas. Cree al menos una sede antes de agregar un Departamento.");
            return;
        }
        // Cuadro de di�logo para ingresar el nombre del nuevo departamento
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Departamento");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese el nombre del nuevo departamento:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nombre -> {
            if (!nombre.trim().isEmpty()) {
                // Cuadro de di�logo de selecci�n de sede
                ObservableList<String> sedeInfoList = FXCollections.observableArrayList();
                for (Sede sede : sedeList) {
                    // Obtener informaci�n de la sede y la empresa
                    Empresa empresa = getEmpresaBySedeId(sede.getId());
                    String info = String.format(
                            "Sede: ID: %d, Nombre: %s, Empresa: ID: %d, Nombre: %s",
                            sede.getId(), sede.getNombre(),
                            empresa.getId(), empresa.getNombre());
                    sedeInfoList.add(info);
                }

                ChoiceDialog<String> sedeDialog = new ChoiceDialog<>(sedeInfoList.get(0), sedeInfoList);
                sedeDialog.setTitle("Seleccionar Sede");
                sedeDialog.setHeaderText(null);
                sedeDialog.setContentText("Seleccione la sede para el nuevo departamento:");

                Optional<String> selectedSedeInfo = sedeDialog.showAndWait();

                selectedSedeInfo.ifPresent(sedeInfo -> {
                    // Extraer el ID de la sede seleccionada
                    int startIndex = sedeInfo.indexOf("Sede: ID: ") + 10;
                    int endIndex = sedeInfo.indexOf(", Nombre:");
                    int sedeId = Integer.parseInt(sedeInfo.substring(startIndex, endIndex).trim());

                    // Insertar nuevo departamento en la base de datos
                    agregarDepartamentoToDatabase(nombre, sedeId);

                    // Actualizar la lista observable
                    loadDepartamentos();
                });
            } else {
                showAlert("Entrada Inv�lida", "El nombre del departamento no puede estar vac�o.");
            }
        });
    }

    private int getSedeIdByNombre(String nombre) {
        for (Sede sede : sedeList) {
            if (sede.getNombre().equals(nombre)) {
                return sede.getId();
            }
        }
        return -1; // Valor de retorno predeterminado si no se encuentra la sede
    }

    private void agregarDepartamentoToDatabase(String nombre, int sedeId) {
        nombre = nombre.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO departamento (nombre_departamento, sede_id) VALUES (?, ?)")) {
            statement.setString(1, nombre);
            statement.setInt(2, sedeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
    private void agregarEmpleado() {

        // Verificar si existen sedes creadas
        if (departamentoList.isEmpty()) {
            showAlert("Sin Departamentos", "No hay departamentos creados. Cree al menos un departamento antes de agregar un empleado.");
            return;
        }
        // Cuadro de di�logo para ingresar el ID y el nombre del nuevo empleado
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Agregar Empleado");
        idDialog.setHeaderText(null);
        idDialog.setContentText("Ingrese el nuevo ID para el empleado:");

        Optional<String> idResult = idDialog.showAndWait();

        // Verificar si se ingres� un ID
        if (idResult.isPresent() && !idResult.get().isEmpty()) {
            int nuevoIdEmpleado;
            try {
                nuevoIdEmpleado = Integer.parseInt(idResult.get());

                // Verificar si el ID es �nico
                if (isEmpleadoIdUnique(nuevoIdEmpleado)) {
                    TextInputDialog nameDialog = new TextInputDialog();
                    nameDialog.setTitle("Agregar Empleado");
                    nameDialog.setHeaderText(null);
                    nameDialog.setContentText("Ingrese el nombre del nuevo empleado:");

                    Optional<String> nameResult = nameDialog.showAndWait();

                    // Verificar si se ingres� un nombre
                    if (nameResult.isPresent() && !nameResult.get().isEmpty()) {
                        // Cuadro de di�logo de selecci�n de departamento
                        ObservableList<String> departamentoInfoList = FXCollections.observableArrayList();
                        for (Departamento departamento : departamentoList) {
                            // Obtener informaci�n del departamento, sede y empresa
                            Sede sede = getSedeById(departamento.getSedeId());
                            Empresa empresa = getEmpresaBySedeId(sede.getId());
                            String info = String.format(
                                    "Departamento: ID: %d, Nombre: %s, Sede: ID: %d, Nombre: %s, Empresa: ID: %d, Nombre: %s",
                                    departamento.getId(), departamento.getNombre(),
                                    sede.getId(), sede.getNombre(),
                                    empresa.getId(), empresa.getNombre());
                            departamentoInfoList.add(info);
                        }

                        ChoiceDialog<String> departamentoDialog = new ChoiceDialog<>(departamentoInfoList.get(0), departamentoInfoList);
                        departamentoDialog.setTitle("Seleccionar Departamento");
                        departamentoDialog.setHeaderText(null);
                        departamentoDialog.setContentText("Seleccione el departamento para el nuevo empleado:");

                        Optional<String> selectedDepartamentoInfo = departamentoDialog.showAndWait();

                        selectedDepartamentoInfo.ifPresent(departamentoInfo -> {
                            // Extraer el ID del departamento seleccionado
                            int startIndex = departamentoInfo.indexOf("Departamento: ID: ") + 18;
                            int endIndex = departamentoInfo.indexOf(", Nombre:");
                            int departamentoId = Integer.parseInt(departamentoInfo.substring(startIndex, endIndex).trim());

                            // Insertar nuevo empleado en la base de datos
                            agregarEmpleadoToDatabase(nuevoIdEmpleado, nameResult.get(), departamentoId);

                            // Actualizar la lista observable
                            loadEmpleados();
                            showAlert("Empleado Agregado", "Nuevo empleado agregado exitosamente.");
                        });
                    } else {
                        showAlert("Entrada Inv�lida", "El nombre del empleado no puede estar vac�o.");
                    }
                } else {
                    showAlert("ID de Empleado Duplicado", "El ID de empleado debe ser �nico. Por favor, elija un ID diferente.");
                }
            } catch (NumberFormatException e) {
                showAlert("Entrada Inv�lida", "El ID del empleado debe ser un n�mero entero v�lido.");
            }
        } else {
            showAlert("Entrada Inv�lida", "El ID del empleado no puede estar vac�o.");
        }
    }

    /////
    // Obtener la sede por ID
    private Sede getSedeById(int sedeId) {
        for (Sede sede : sedeList) {
            if (sede.getId() == sedeId) {
                return sede;
            }
        }
        return null; // Retorna null si no se encuentra la sede
    }

// Obtener la empresa por ID de la sede
    private Empresa getEmpresaBySedeId(int sedeId) {
        for (Sede sede : sedeList) {
            if (sede.getId() == sedeId) {
                int empresaId = sede.getEmpresaId();
                return getEmpresaById(empresaId);
            }
        }
        return null; // Retorna null si no se encuentra la empresa
    }

// Obtener la empresa por ID
    private Empresa getEmpresaById(int empresaId) {
        for (Empresa empresa : empresaList) {
            if (empresa.getId() == empresaId) {
                return empresa;
            }
        }
        return null; // Retorna null si no se encuentra la empresa
    }

    /////
    private int getDepartamentoIdByNombre(String nombre) {
        for (Departamento departamento : departamentoList) {
            if (departamento.getNombre().equals(nombre)) {
                return departamento.getId();
            }
        }
        return -1; // Valor de retorno predeterminado si no se encuentra el departamento
    }

    private boolean isEmpleadoIdUnique(int empleadoId) {
        for (Empleado empleado : empleadoList) {
            if (empleado.getId() == empleadoId) {
                return false;
            }
        }
        return true;
    }

    private void agregarEmpleadoToDatabase(int nuevoIdEmpleado, String nombre, int departamentoId) {
        nombre = nombre.toUpperCase();

        try (PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO empleado (id_empleado, nombre_empleado, departamento_id) VALUES (?, ?, ?)")) {
            statement.setInt(1, nuevoIdEmpleado);
            statement.setString(2, nombre);
            statement.setInt(3, departamentoId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////    
    private void loadEmpresas() {
        empresaList.clear();

        try (Statement statement = dbConnection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM empresa")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id_empresa");
                String nombre = resultSet.getString("nombre_empresa");

                Empresa empresa = new Empresa(id, nombre);
                empresaList.add(empresa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSedes() {
        sedeList.clear();

        try (Statement statement = dbConnection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM sede")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id_sede");
                String nombre = resultSet.getString("nombre_sede");
                int empresaId = resultSet.getInt("empresa_id");

                Sede sede = new Sede(id, nombre, empresaId);
                sedeList.add(sede);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDepartamentos() {
        departamentoList.clear();

        try (Statement statement = dbConnection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM departamento")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id_departamento");
                String nombre = resultSet.getString("nombre_departamento");
                int sedeId = resultSet.getInt("sede_id");

                Departamento departamento = new Departamento(id, nombre, sedeId);
                departamentoList.add(departamento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadEmpleados() {
        empleadoList.clear();

        try (Statement statement = dbConnection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM empleado")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id_empleado");
                String nombre = resultSet.getString("nombre_empleado");
                int departamentoId = resultSet.getInt("departamento_id");

                Empleado empleado = new Empleado(id, nombre, departamentoId);
                empleadoList.add(empleado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class Empresa {

        private int id;
        private String nombre;

        public Empresa(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }
    }

    public static class Sede {

        private int id;
        private String nombre;
        private int empresaId;

        public Sede(int id, String nombre, int empresaId) {
            this.id = id;
            this.nombre = nombre;
            this.empresaId = empresaId;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public int getEmpresaId() {
            return empresaId;
        }
    }

    public static class Departamento {

        private int id;
        private String nombre;
        private int sedeId;

        public Departamento(int id, String nombre, int sedeId) {
            this.id = id;
            this.nombre = nombre;
            this.sedeId = sedeId;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public int getSedeId() {
            return sedeId;
        }
    }

    public static class Empleado {

        private int id;
        private String nombre;
        private int departamentoId;

        public Empleado(int id, String nombre, int departamentoId) {
            this.id = id;
            this.nombre = nombre;
            this.departamentoId = departamentoId;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public int getDepartamentoId() {
            return departamentoId;
        }
    }
}
