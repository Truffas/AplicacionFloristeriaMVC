package gui;

import util.Util;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;


public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {

    private Modelo modelo;
    private Vista vista;
    boolean refrescar;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        modelo.conectar();
        setOptions();
        addActionListeners(this);
        addItemListeners(this);
        addWindowListeners(this);
        refrescarTodo();
        iniciar();
    }

    private void refrescarTodo() {
        refrescarAutores();
        refrescarEditorial();
        refrescarLibros();
        refrescar = false;
    }

    private void addActionListeners(ActionListener listener) {
        vista.btnPedidoAnadir.addActionListener(listener);
        vista.btnPedidoAnadir.setActionCommand("anadirLibro");
        vista.btnContactoAnadir.addActionListener(listener);
        vista.btnContactoAnadir.setActionCommand("anadirAutor");
        vista.btnCeremoniaAnadir.addActionListener(listener);
        vista.btnCeremoniaAnadir.setActionCommand("anadirEditorial");
        vista.btnPedidoEliminar.addActionListener(listener);
        vista.btnPedidoEliminar.setActionCommand("eliminarLibro");
        vista.btnContactoEliminar.addActionListener(listener);
        vista.btnContactoEliminar.setActionCommand("eliminarAutor");
        vista.btnCeremoniaEliminar.addActionListener(listener);
        vista.btnCeremoniaEliminar.setActionCommand("eliminarEditorial");
        vista.btnPedidoModificar.addActionListener(listener);
        vista.btnPedidoModificar.setActionCommand("modificarLibro");
        vista.btnContactoModificar.addActionListener(listener);
        vista.btnContactoModificar.setActionCommand("modificarAutor");
        vista.btnCeremoniaModificar.addActionListener(listener);
        vista.btnCeremoniaModificar.setActionCommand("modificarEditorial");
        vista.optionDialog.btnOpcionesGuardar.addActionListener(listener);
        vista.optionDialog.btnOpcionesGuardar.setActionCommand("guardarOpciones");
        vista.itemOpciones.addActionListener(listener);
        vista.itemSalir.addActionListener(listener);
        vista.itemDesconectar.addActionListener(listener);
        vista.btnValidate.addActionListener(listener);
    }

    private void addWindowListeners(WindowListener listener) {
        vista.addWindowListener(listener);
    }

    void iniciar(){
        vista.ceremoniaTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel =  vista.ceremoniaTabla.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.ceremoniaTabla.getSelectionModel())) {
                        int row = vista.ceremoniaTabla.getSelectedRow();
                        vista.txtNombreEditorial.setText(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 1)));
                        vista.txtOtroCeremonia.setText(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 2)));
                        vista.txtTelefono.setText(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 3)));
                        vista.comboTipoEditorial.setSelectedItem(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 4)));
                        vista.txtDireccion.setText(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 5)));
                    } else if (e.getValueIsAdjusting()
                            && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                        if (e.getSource().equals(vista.ceremoniaTabla.getSelectionModel())) {
                            borrarCamposEditoriales();
                        } else if (e.getSource().equals(vista.contactoTabla.getSelectionModel())) {
                            borrarCamposAutores();
                        } else if (e.getSource().equals(vista.pedidoTabla.getSelectionModel())) {
                            borrarCamposLibros();
                        }
                    }
                }
            }
        });

        vista.contactoTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel2 =  vista.contactoTabla.getSelectionModel();
        cellSelectionModel2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel2.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.contactoTabla.getSelectionModel())) {
                        int row = vista.contactoTabla.getSelectedRow();
                        vista.txtNombre.setText(String.valueOf(vista.contactoTabla.getValueAt(row, 1)));
                        vista.txtApellidos.setText(String.valueOf(vista.contactoTabla.getValueAt(row, 2)));
                        vista.fechaNacimiento.setDate((Date.valueOf(String.valueOf(vista.contactoTabla.getValueAt(row, 3)))).toLocalDate());
                        vista.txtPais.setText(String.valueOf(vista.contactoTabla.getValueAt(row, 4)));
                    } else if (e.getValueIsAdjusting()
                            && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                        if (e.getSource().equals(vista.ceremoniaTabla.getSelectionModel())) {
                            borrarCamposEditoriales();
                        } else if (e.getSource().equals(vista.contactoTabla.getSelectionModel())) {
                            borrarCamposAutores();
                        } else if (e.getSource().equals(vista.pedidoTabla.getSelectionModel())) {
                            borrarCamposLibros();
                        }
                    }
                }
            }
        });

        vista.pedidoTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel3 =  vista.pedidoTabla.getSelectionModel();
        cellSelectionModel3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel3.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.pedidoTabla.getSelectionModel())) {
                        int row = vista.pedidoTabla.getSelectedRow();
                        vista.txtNumero.setText(String.valueOf(vista.pedidoTabla.getValueAt(row, 1)));
                        vista.comboContacto.setSelectedItem(String.valueOf(vista.pedidoTabla.getValueAt(row, 5)));
                        vista.comboCeremonia.setSelectedItem(String.valueOf(vista.pedidoTabla.getValueAt(row, 3)));
                        vista.comboAdorno.setSelectedItem(String.valueOf(vista.pedidoTabla.getValueAt(row, 4)));
                        vista.fecha.setDate((Date.valueOf(String.valueOf(vista.pedidoTabla.getValueAt(row, 7)))).toLocalDate());
                        vista.txtComentario.setText(String.valueOf(vista.pedidoTabla.getValueAt(row, 2)));
                        vista.txtPrecio.setText(String.valueOf(vista.pedidoTabla.getValueAt(row, 6)));
                    } else if (e.getValueIsAdjusting()
                            && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                        if (e.getSource().equals(vista.ceremoniaTabla.getSelectionModel())) {
                            borrarCamposEditoriales();
                        } else if (e.getSource().equals(vista.contactoTabla.getSelectionModel())) {
                            borrarCamposAutores();
                        } else if (e.getSource().equals(vista.pedidoTabla.getSelectionModel())) {
                            borrarCamposLibros();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
            if (e.getSource().equals(vista.ceremoniaTabla.getSelectionModel())) {
                int row = vista.ceremoniaTabla.getSelectedRow();
                vista.txtNombreEditorial.setText(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 1)));
                vista.txtOtroCeremonia.setText(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 2)));
                vista.txtTelefono.setText(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 3)));
                vista.comboTipoEditorial.setSelectedItem(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 4)));
                vista.txtDireccion.setText(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 5)));
            } else if (e.getSource().equals(vista.contactoTabla.getSelectionModel())) {
                int row = vista.contactoTabla.getSelectedRow();
                vista.txtNombre.setText(String.valueOf(vista.contactoTabla.getValueAt(row, 1)));
                vista.txtApellidos.setText(String.valueOf(vista.contactoTabla.getValueAt(row, 2)));
                vista.fechaNacimiento.setDate((Date.valueOf(String.valueOf(vista.contactoTabla.getValueAt(row, 3)))).toLocalDate());
                vista.txtPais.setText(String.valueOf(vista.contactoTabla.getValueAt(row, 4)));
            } else if (e.getSource().equals(vista.pedidoTabla.getSelectionModel())) {
                int row = vista.pedidoTabla.getSelectedRow();
                vista.txtNumero.setText(String.valueOf(vista.pedidoTabla.getValueAt(row, 1)));
                vista.comboContacto.setSelectedItem(String.valueOf(vista.pedidoTabla.getValueAt(row, 5)));
                vista.comboCeremonia.setSelectedItem(String.valueOf(vista.pedidoTabla.getValueAt(row, 3)));
                vista.comboAdorno.setSelectedItem(String.valueOf(vista.pedidoTabla.getValueAt(row, 4)));
                vista.fecha.setDate((Date.valueOf(String.valueOf(vista.pedidoTabla.getValueAt(row, 7)))).toLocalDate());
                vista.txtComentario.setText(String.valueOf(vista.pedidoTabla.getValueAt(row, 2)));
                vista.txtPrecio.setText(String.valueOf(vista.pedidoTabla.getValueAt(row, 6)));
            } else if (e.getValueIsAdjusting()
                    && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                if (e.getSource().equals(vista.ceremoniaTabla.getSelectionModel())) {
                    borrarCamposEditoriales();
                } else if (e.getSource().equals(vista.contactoTabla.getSelectionModel())) {
                    borrarCamposAutores();
                } else if (e.getSource().equals(vista.pedidoTabla.getSelectionModel())) {
                    borrarCamposLibros();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Opciones":
                vista.adminPasswordDialog.setVisible(true);
                break;
            case "Desconectar":
                modelo.desconectar();
                break;
            case "Salir":
                System.exit(0);
                break;
            case "abrirOpciones":
                if(String.valueOf(vista.adminPassword.getPassword()).equals(modelo.getAdminPassword())) {
                    vista.adminPassword.setText("");
                    vista.adminPasswordDialog.dispose();
                    vista.optionDialog.setVisible(true);
                } else {
                    Util.showErrorAlert("La contraseña introducida no es correcta.");
                }
                break;
            case "guardarOpciones":
                modelo.setPropValues(vista.optionDialog.txtIP.getText(), vista.optionDialog.txtUsuario.getText(),
                        String.valueOf(vista.optionDialog.pfPass.getPassword()), String.valueOf(vista.optionDialog.pfAdmin.getPassword()));
                vista.optionDialog.dispose();
                vista.dispose();
                new Controlador(new Modelo(), new Vista());
                break;
            case "anadirLibro": {
                try {
                    if (comprobarLibroVacio()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.pedidoTabla.clearSelection();
                    } else if (modelo.libroIsbnYaExiste(vista.txtComentario.getText())) {
                        Util.showErrorAlert("Ese ISBN ya existe.\nIntroduce un libro diferente");
                        vista.pedidoTabla.clearSelection();
                    } else {
                        modelo.insertarLibro(
                                vista.txtNumero.getText(),
                                vista.txtComentario.getText(),
                                String.valueOf(vista.comboCeremonia.getSelectedItem()),
                                String.valueOf(vista.comboAdorno.getSelectedItem()),
                                String.valueOf(vista.comboContacto.getSelectedItem()),
                                Float.parseFloat(vista.txtPrecio.getText()),
                                vista.fecha.getDate());
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.pedidoTabla.clearSelection();
                }
                borrarCamposLibros();
                refrescarLibros();
            }
            break;
            case "modificarLibro": {
                try {
                    if (comprobarLibroVacio()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.pedidoTabla.clearSelection();
                    } else {
                        modelo.modificarLibro(
                                vista.txtNumero.getText(),
                                vista.txtComentario.getText(),
                                String.valueOf(vista.comboCeremonia.getSelectedItem()),
                                String.valueOf(vista.comboAdorno.getSelectedItem()),
                                String.valueOf(vista.comboContacto.getSelectedItem()),
                                Float.parseFloat(vista.txtPrecio.getText()),
                                vista.fecha.getDate(),
                                (Integer) vista.pedidoTabla.getValueAt(vista.pedidoTabla.getSelectedRow(), 0));
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.pedidoTabla.clearSelection();
                }
                borrarCamposLibros();
                refrescarLibros();
            }
            break;
            case "eliminarLibro":
                modelo.eliminarLibro((Integer) vista.pedidoTabla.getValueAt(vista.pedidoTabla.getSelectedRow(), 0));
                borrarCamposLibros();
                refrescarLibros();
                break;
            case "anadirAutor": {
                try {
                    if (comprobarAutorVacio()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.contactoTabla.clearSelection();
                    } else if (modelo.autorNombreYaExiste(vista.txtNombre.getText(),
                            vista.txtApellidos.getText())) {
                        Util.showErrorAlert("Ese nombre ya existe.\nIntroduce un autor diferente");
                        vista.contactoTabla.clearSelection();
                    } else {
                        modelo.insertarContacto(vista.txtNombre.getText(),
                                vista.txtApellidos.getText(),
                                vista.fechaNacimiento.getDate(),
                                vista.txtPais.getText());
                        refrescarAutores();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.contactoTabla.clearSelection();
                }
                borrarCamposAutores();
            }
            break;
            case "modificarAutor": {
                try {
                    if (comprobarAutorVacio()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.contactoTabla.clearSelection();
                    } else {
                        modelo.modificarAutor(vista.txtNombre.getText(), vista.txtApellidos.getText(),
                                vista.fechaNacimiento.getDate(), vista.txtPais.getText(),
                                (Integer) vista.contactoTabla.getValueAt(vista.contactoTabla.getSelectedRow(), 0));
                        refrescarAutores();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.contactoTabla.clearSelection();
                }
                borrarCamposAutores();
            }
            break;
            case "eliminarAutor":
                modelo.eliminarAutor((Integer) vista.contactoTabla.getValueAt(vista.contactoTabla.getSelectedRow(), 0));
                borrarCamposAutores();
                refrescarAutores();
                break;
            case "anadirEditorial": {
                try {
                    if (comprobarEditorialVacia()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.ceremoniaTabla.clearSelection();
                    } else if (modelo.editorialNombreYaExiste(vista.txtNombreEditorial.getText())) {
                        Util.showErrorAlert("Ese nombre ya existe.\nIntroduce una editorial diferente.");
                        vista.ceremoniaTabla.clearSelection();
                    } else {
                        modelo.insertarEditorial(vista.txtNombreEditorial.getText(), vista.txtOtroCeremonia.getText(),
                                vista.txtTelefono.getText(),
                                (String) vista.comboTipoEditorial.getSelectedItem(),
                                vista.txtDireccion.getText());
                        refrescarEditorial();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.ceremoniaTabla.clearSelection();
                }
                borrarCamposEditoriales();
            }
            break;
            case "modificarEditorial": {
                try {
                    if (comprobarEditorialVacia()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.ceremoniaTabla.clearSelection();
                    } else {
                        modelo.modificarEditorial(vista.txtNombreEditorial.getText(), vista.txtOtroCeremonia.getText(), vista.txtTelefono.getText(),
                                String.valueOf(vista.comboTipoEditorial.getSelectedItem()), vista.txtDireccion.getText(),
                                (Integer) vista.ceremoniaTabla.getValueAt(vista.ceremoniaTabla.getSelectedRow(), 0));
                        refrescarEditorial();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.ceremoniaTabla.clearSelection();
                }
                borrarCamposEditoriales();
            }
            break;
            case "eliminarEditorial":
                modelo.eliminarEditorial((Integer) vista.ceremoniaTabla.getValueAt(vista.ceremoniaTabla.getSelectedRow(), 0));
                borrarCamposEditoriales();
                refrescarEditorial();
                break;
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    private void refrescarEditorial() {
        try {
            vista.ceremoniaTabla.setModel(construirTableModelEditoriales(modelo.consultarEditorial()));
            vista.comboCeremonia.removeAllItems();
            for(int i = 0; i < vista.dtmEditoriales.getRowCount(); i++) {
                vista.comboCeremonia.addItem(vista.dtmEditoriales.getValueAt(i, 0)+" - "+
                        vista.dtmEditoriales.getValueAt(i, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private  DefaultTableModel construirTableModelEditoriales(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, columnCount, data);

        vista.dtmEditoriales.setDataVector(data, columnNames);

        return vista.dtmEditoriales;

    }

    private void refrescarAutores() {
        try {
            vista.contactoTabla.setModel(construirTableModeloAutores(modelo.consultarAutor()));
            vista.comboContacto.removeAllItems();
            for(int i = 0; i < vista.dtmAutores.getRowCount(); i++) {
                vista.comboContacto.addItem(vista.dtmAutores.getValueAt(i, 0)+" - "+
                        vista.dtmAutores.getValueAt(i, 2)+", "+vista.dtmAutores.getValueAt(i, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel construirTableModeloAutores(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, columnCount, data);

        vista.dtmAutores.setDataVector(data, columnNames);

        return vista.dtmAutores;

    }

    private void refrescarLibros() {
        try {
            vista.pedidoTabla.setModel(construirTableModelLibros(modelo.consultarLibros()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel construirTableModelLibros(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, columnCount, data);

        vista.dtmLibros.setDataVector(data, columnNames);

        return vista.dtmLibros;

    }

    private void setDataVector(ResultSet rs, int columnCount, Vector<Vector<Object>> data) throws SQLException {
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
    }

    private void setOptions() {
        vista.optionDialog.txtIP.setText(modelo.getIp());
        vista.optionDialog.txtUsuario.setText(modelo.getUser());
        vista.optionDialog.pfPass.setText(modelo.getPassword());
        vista.optionDialog.pfAdmin.setText(modelo.getAdminPassword());
    }

    private void borrarCamposLibros() {
        vista.comboCeremonia.setSelectedIndex(-1);
        vista.comboContacto.setSelectedIndex(-1);
        vista.txtNumero.setText("");
        vista.txtComentario.setText("");
        vista.comboAdorno.setSelectedIndex(-1);
        vista.txtPrecio.setText("");
        vista.fecha.setText("");
    }

    private void borrarCamposAutores() {
        vista.txtNombre.setText("");
        vista.txtApellidos.setText("");
        vista.txtPais.setText("");
        vista.fechaNacimiento.setText("");
    }

    private void borrarCamposEditoriales() {
        vista.txtNombreEditorial.setText("");
        vista.txtOtroCeremonia.setText("");
        vista.txtTelefono.setText("");
        vista.comboTipoEditorial.setSelectedIndex(-1);
        vista.txtDireccion.setText("");
    }

    private boolean comprobarLibroVacio() {
        return vista.txtNumero.getText().isEmpty() ||
                vista.txtPrecio.getText().isEmpty() ||
                vista.txtComentario.getText().isEmpty() ||
                vista.comboAdorno.getSelectedIndex() == -1 ||
                vista.comboContacto.getSelectedIndex() == -1 ||
                vista.comboCeremonia.getSelectedIndex() == -1 ||
                vista.fecha.getText().isEmpty();
    }

    private boolean comprobarAutorVacio() {
        return vista.txtApellidos.getText().isEmpty() ||
                vista.txtNombre.getText().isEmpty() ||
                vista.txtPais.getText().isEmpty() ||
                vista.fechaNacimiento.getText().isEmpty();
    }

    private boolean comprobarEditorialVacia() {
        return vista.txtNombreEditorial.getText().isEmpty() ||
                vista.txtOtroCeremonia.getText().isEmpty() ||
                vista.txtTelefono.getText().isEmpty() ||
                vista.comboTipoEditorial.getSelectedIndex() == -1 ||
                vista.txtDireccion.getText().isEmpty();
    }

    /*LISTENERS IPLEMENTOS NO UTILIZADOS*/

    private void addItemListeners(Controlador controlador) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}