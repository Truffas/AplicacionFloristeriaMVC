package gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import util.Util;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;


public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {

    private Modelo modelo;
    private Vista vista;
    boolean refrescar;
    private boolean modoOscuro = false;

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
        refrescarContactos();
        refrescarCeremonias();
        refrescarAdornos();
        refrescarPedidos();
        refrescar = false;
    }

    private void addActionListeners(ActionListener listener) {
        vista.btnPedidoAnadir.addActionListener(listener);
        vista.btnPedidoAnadir.setActionCommand("anadirPedido");
        vista.btnContactoAnadir.addActionListener(listener);
        vista.btnContactoAnadir.setActionCommand("anadirContacto");
        vista.btnCeremoniaAnadir.addActionListener(listener);
        vista.btnCeremoniaAnadir.setActionCommand("anadirCeremonia");
        vista.btnAdornoAnadir.addActionListener(listener);
        vista.btnAdornoAnadir.setActionCommand("anadirAdorno");
        vista.btnPedidoEliminar.addActionListener(listener);
        vista.btnPedidoEliminar.setActionCommand("eliminarPedido");
        vista.btnContactoEliminar.addActionListener(listener);
        vista.btnContactoEliminar.setActionCommand("eliminarContacto");
        vista.btnCeremoniaEliminar.addActionListener(listener);
        vista.btnCeremoniaEliminar.setActionCommand("eliminarCeremonia");
        vista.btnAdornoEliminar.addActionListener(listener);
        vista.btnAdornoEliminar.setActionCommand("eliminarAdorno");
        vista.btnPedidoModificar.addActionListener(listener);
        vista.btnPedidoModificar.setActionCommand("modificarPedido");
        vista.btnContactoModificar.addActionListener(listener);
        vista.btnContactoModificar.setActionCommand("modificarContacto");
        vista.btnCeremoniaModificar.addActionListener(listener);
        vista.btnCeremoniaModificar.setActionCommand("modificarCeremonia");
        vista.btnAdornoModificar.addActionListener(listener);
        vista.btnAdornoModificar.setActionCommand("modificarAdorno");
        vista.btnPedidoLimpiar.addActionListener(listener);
        vista.btnPedidoLimpiar.setActionCommand("limpiarPedido");
        vista.btnContactoLimpiar.addActionListener(listener);
        vista.btnContactoLimpiar.setActionCommand("limpiarContacto");
        vista.btnCeremoniaLimpiar.addActionListener(listener);
        vista.btnCeremoniaLimpiar.setActionCommand("limpiarCeremonia");
        vista.btnAdornoLimpiar.addActionListener(listener);
        vista.btnAdornoLimpiar.setActionCommand("limpiarAdorno");
        vista.optionDialog.btnOpcionesGuardar.addActionListener(listener);
        vista.optionDialog.btnOpcionesGuardar.setActionCommand("guardarOpciones");
        vista.itemOpciones.addActionListener(listener);
        vista.itemSalir.addActionListener(listener);
        vista.itemDesconectar.addActionListener(listener);
        vista.itemOscuro.addActionListener(listener);
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
                        vista.comboTipoCeremonia.setSelectedItem(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 1)));
                        vista.txtOtroCeremonia.setText(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 2)));
                        vista.fechaEntrega.setDate((Date.valueOf(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 3)))).toLocalDate());
                        if ((String.valueOf(vista.ceremoniaTabla.getValueAt(row, 4))).equals("En tienda")) {
                            vista.radioButtonTienda.setSelected(true);
                        } else if ((String.valueOf(vista.ceremoniaTabla.getValueAt(row, 4))).equals("Por envio")) {
                            vista.radioButtonEnvio.setSelected(true);
                        }
                        vista.txtDireccion.setText(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 5)));
                    } else if (e.getValueIsAdjusting()
                            && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                        if (e.getSource().equals(vista.ceremoniaTabla.getSelectionModel())) {
                            borrarCamposCeremonias();
                        } else if (e.getSource().equals(vista.contactoTabla.getSelectionModel())) {
                            borrarCamposContactos();
                        } else if (e.getSource().equals(vista.pedidoTabla.getSelectionModel())) {
                            borrarCamposPedidos();
                        } else if (e.getSource().equals(vista.adornoTabla.getSelectionModel())) {
                            borrarCamposAdornos();
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
                            borrarCamposCeremonias();
                        } else if (e.getSource().equals(vista.contactoTabla.getSelectionModel())) {
                            borrarCamposContactos();
                        } else if (e.getSource().equals(vista.pedidoTabla.getSelectionModel())) {
                            borrarCamposPedidos();
                        } else if (e.getSource().equals(vista.adornoTabla.getSelectionModel())) {
                            borrarCamposAdornos();
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
                        vista.txtNumero.setText(String.valueOf(vista.pedidoTabla.getValueAt(row, 0)));
                        vista.comboContacto.setSelectedItem(String.valueOf(vista.pedidoTabla.getValueAt(row, 1)));
                        vista.comboCeremonia.setSelectedItem(String.valueOf(vista.pedidoTabla.getValueAt(row, 2)));
                        vista.comboAdorno.setSelectedItem(String.valueOf(vista.pedidoTabla.getValueAt(row, 3)));
                        vista.fecha.setDate((Date.valueOf(String.valueOf(vista.pedidoTabla.getValueAt(row, 4)))).toLocalDate());
                        vista.txtComentario.setText(String.valueOf(vista.pedidoTabla.getValueAt(row, 5)));
                        vista.txtPrecio.setText(String.valueOf(vista.pedidoTabla.getValueAt(row, 6)));
                    } else if (e.getValueIsAdjusting()
                            && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                        if (e.getSource().equals(vista.ceremoniaTabla.getSelectionModel())) {
                            borrarCamposCeremonias();
                        } else if (e.getSource().equals(vista.contactoTabla.getSelectionModel())) {
                            borrarCamposContactos();
                        } else if (e.getSource().equals(vista.pedidoTabla.getSelectionModel())) {
                            borrarCamposPedidos();
                        } else if (e.getSource().equals(vista.adornoTabla.getSelectionModel())) {
                            borrarCamposAdornos();
                        }
                    }
                }
            }
        });

        vista.adornoTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel4 =  vista.adornoTabla.getSelectionModel();
        cellSelectionModel4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel4.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.adornoTabla.getSelectionModel())) {
                        int row = vista.adornoTabla.getSelectedRow();
                        vista.comboTipoAdorno.setSelectedItem(String.valueOf(vista.adornoTabla.getValueAt(row, 1)));
                        vista.txtOtroAdorno.setText(String.valueOf(vista.adornoTabla.getValueAt(row, 2)));
                        vista.txtTipoFlores.setText(String.valueOf(vista.adornoTabla.getValueAt(row, 3)));
                        vista.txtOpciones.setText(String.valueOf(vista.adornoTabla.getValueAt(row, 4)));
                        vista.txtMensaje.setText(String.valueOf(vista.adornoTabla.getValueAt(row, 5)));
                    } else if (e.getValueIsAdjusting()
                            && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                        if (e.getSource().equals(vista.ceremoniaTabla.getSelectionModel())) {
                            borrarCamposCeremonias();
                        } else if (e.getSource().equals(vista.contactoTabla.getSelectionModel())) {
                            borrarCamposContactos();
                        } else if (e.getSource().equals(vista.pedidoTabla.getSelectionModel())) {
                            borrarCamposPedidos();
                        } else if (e.getSource().equals(vista.adornoTabla.getSelectionModel())) {
                            borrarCamposAdornos();
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
                vista.comboTipoCeremonia.setSelectedItem(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 1)));
                vista.txtOtroCeremonia.setText(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 2)));
                vista.fechaEntrega.setDate((Date.valueOf(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 3)))).toLocalDate());
                if ((String.valueOf(vista.ceremoniaTabla.getValueAt(row, 4))).equals("En tienda")) {
                    vista.radioButtonTienda.setSelected(true);
                } else if ((String.valueOf(vista.ceremoniaTabla.getValueAt(row, 4))).equals("Por envio")) {
                    vista.radioButtonEnvio.setSelected(true);
                }
                vista.txtDireccion.setText(String.valueOf(vista.ceremoniaTabla.getValueAt(row, 5)));
            } else if (e.getSource().equals(vista.contactoTabla.getSelectionModel())) {
                int row = vista.contactoTabla.getSelectedRow();
                vista.txtNombre.setText(String.valueOf(vista.contactoTabla.getValueAt(row, 1)));
                vista.txtApellidos.setText(String.valueOf(vista.contactoTabla.getValueAt(row, 2)));
                vista.fechaNacimiento.setDate((Date.valueOf(String.valueOf(vista.contactoTabla.getValueAt(row, 3)))).toLocalDate());
                vista.txtPais.setText(String.valueOf(vista.contactoTabla.getValueAt(row, 4)));
            } else if (e.getSource().equals(vista.adornoTabla.getSelectionModel())) {
                int row = vista.adornoTabla.getSelectedRow();
                vista.comboTipoAdorno.setSelectedItem(String.valueOf(vista.adornoTabla.getValueAt(row, 1)));
                vista.txtOtroAdorno.setText(String.valueOf(vista.adornoTabla.getValueAt(row, 2)));
                vista.txtTipoFlores.setText(String.valueOf(vista.adornoTabla.getValueAt(row, 3)));
                vista.txtOpciones.setText(String.valueOf(vista.adornoTabla.getValueAt(row, 4)));
                vista.txtMensaje.setText(String.valueOf(vista.adornoTabla.getValueAt(row, 5)));
            } else if (e.getSource().equals(vista.pedidoTabla.getSelectionModel())) {
                int row = vista.pedidoTabla.getSelectedRow();
                vista.txtNumero.setText(String.valueOf(vista.pedidoTabla.getValueAt(row, 0)));
                vista.comboContacto.setSelectedItem(String.valueOf(vista.pedidoTabla.getValueAt(row, 1)));
                vista.comboCeremonia.setSelectedItem(String.valueOf(vista.pedidoTabla.getValueAt(row, 2)));
                vista.comboAdorno.setSelectedItem(String.valueOf(vista.pedidoTabla.getValueAt(row, 3)));
                vista.fecha.setDate((Date.valueOf(String.valueOf(vista.pedidoTabla.getValueAt(row, 4)))).toLocalDate());
                vista.txtComentario.setText(String.valueOf(vista.pedidoTabla.getValueAt(row, 5)));
                vista.txtPrecio.setText(String.valueOf(vista.pedidoTabla.getValueAt(row, 6)));
            } else if (e.getValueIsAdjusting()
                    && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                if (e.getSource().equals(vista.ceremoniaTabla.getSelectionModel())) {
                    borrarCamposCeremonias();
                } else if (e.getSource().equals(vista.contactoTabla.getSelectionModel())) {
                    borrarCamposContactos();
                } else if (e.getSource().equals(vista.pedidoTabla.getSelectionModel())) {
                    borrarCamposPedidos();
                } else if (e.getSource().equals(vista.adornoTabla.getSelectionModel())) {
                    borrarCamposAdornos();
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
            case "anadirPedido": {
                try {
                    if (comprobarPedidoVacio()) {
                        Util.showErrorAlert("Campos obligatorios: Contacto - Ceremonia - Adorno - Fecha - Precio");
                        vista.pedidoTabla.clearSelection();
                    } else {
                        modelo.insertarPedido(
                                String.valueOf(vista.comboContacto.getSelectedItem()),
                                String.valueOf(vista.comboCeremonia.getSelectedItem()),
                                String.valueOf(vista.comboAdorno.getSelectedItem()),
                                vista.fecha.getDate(),
                                vista.txtComentario.getText(),
                                Float.parseFloat(vista.txtPrecio.getText()));
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en el campo Precio");
                    vista.pedidoTabla.clearSelection();
                }
                borrarCamposPedidos();
                refrescarPedidos();
            }
            break;
            case "modificarPedido": {
                try {
                    if (comprobarPedidoVacio()) {
                        Util.showErrorAlert("Campos obligatorios: Contacto - Ceremonia - Adorno - Fecha - Precio");
                        vista.pedidoTabla.clearSelection();
                    } else {
                        modelo.modificarPedido(
                                String.valueOf(vista.comboContacto.getSelectedItem()),
                                String.valueOf(vista.comboCeremonia.getSelectedItem()),
                                String.valueOf(vista.comboAdorno.getSelectedItem()),
                                vista.fecha.getDate(),
                                vista.txtComentario.getText(),
                                Float.parseFloat(vista.txtPrecio.getText()),
                                (Integer) vista.pedidoTabla.getValueAt(vista.pedidoTabla.getSelectedRow(), 0));
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en el campo Precio");
                    vista.pedidoTabla.clearSelection();
                }
                borrarCamposPedidos();
                refrescarPedidos();
            }
            break;
            case "limpiarPedido":
                borrarCamposPedidos();
                refrescarPedidos();
                break;
            case "eliminarPedido":
                modelo.eliminarPedido((Integer) vista.pedidoTabla.getValueAt(vista.pedidoTabla.getSelectedRow(), 0));
                borrarCamposPedidos();
                refrescarPedidos();
                break;
            case "anadirContacto": {
                try {
                    if (comprobarContactoVacio()) {
                        Util.showErrorAlert("Campos obligatorios: Nombre - Apellidos - Fecha nacimiento - Pais");
                        vista.contactoTabla.clearSelection();
                    } else if (modelo.contactoNombreYaExiste(vista.txtNombre.getText(),
                            vista.txtApellidos.getText())) {
                        Util.showErrorAlert("Ese nombre ya existe.\nIntroduce un contacto diferente");
                        vista.contactoTabla.clearSelection();
                    } else {
                        modelo.insertarContacto(
                                vista.txtNombre.getText(),
                                vista.txtApellidos.getText(),
                                vista.fechaNacimiento.getDate(),
                                vista.txtPais.getText());
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.contactoTabla.clearSelection();
                }
                refrescarContactos();
                borrarCamposContactos();
            }
            break;
            case "modificarContacto": {
                try {
                    if (comprobarContactoVacio()) {
                        Util.showErrorAlert("Campos obligatorios: Nombre - Apellidos - Fecha nacimiento - Pais");
                        vista.contactoTabla.clearSelection();
                    } else {
                        modelo.modificarContacto(
                                vista.txtNombre.getText(),
                                vista.txtApellidos.getText(),
                                vista.fechaNacimiento.getDate(),
                                vista.txtPais.getText(),
                                (Integer) vista.contactoTabla.getValueAt(vista.contactoTabla.getSelectedRow(), 0));
                        refrescarContactos();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.contactoTabla.clearSelection();
                }
                borrarCamposContactos();
            }
            break;
            case "limpiarContacto":
                borrarCamposContactos();
                refrescarContactos();
                break;
            case "eliminarContacto":
                modelo.eliminarContacto((Integer) vista.contactoTabla.getValueAt(vista.contactoTabla.getSelectedRow(), 0));
                borrarCamposContactos();
                refrescarContactos();
                break;
            case "anadirCeremonia": {
                String lugarEntrega = "";
                if (vista.radioButtonTienda.isSelected()) {
                    lugarEntrega = "En tienda";
                } else if (vista.radioButtonEnvio.isSelected()) {
                    lugarEntrega = "Por envio";
                }
                try {
                    if (comprobarCeremoniaVacia()) {
                        Util.showErrorAlert("Campos obligatorios: Tipo - Fecha entrega");
                        vista.ceremoniaTabla.clearSelection();
                    } else {
                        modelo.insertarCeremonia(
                                (String) vista.comboTipoCeremonia.getSelectedItem(),
                                vista.txtOtroCeremonia.getText(),
                                vista.fechaEntrega.getDate(),
                                lugarEntrega,
                                vista.txtDireccion.getText());
                        refrescarCeremonias();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.ceremoniaTabla.clearSelection();
                }
                borrarCamposCeremonias();
            }
            break;
            case "modificarCeremonia": {
                String lugarEntrega = "";
                if (vista.radioButtonTienda.isSelected()) {
                    lugarEntrega = "En tienda";
                } else if (vista.radioButtonEnvio.isSelected()) {
                    lugarEntrega = "Por envio";
                }
                try {
                    if (comprobarCeremoniaVacia()) {
                        Util.showErrorAlert("RCampos obligatorios: Tipo - Fecha entrega");
                        vista.ceremoniaTabla.clearSelection();
                    } else {
                        modelo.modificarCeremonia(
                                (String) vista.comboTipoCeremonia.getSelectedItem(),
                                vista.txtOtroCeremonia.getText(),
                                vista.fechaEntrega.getDate(),
                                lugarEntrega,
                                vista.txtDireccion.getText(),
                                (Integer) vista.ceremoniaTabla.getValueAt(vista.ceremoniaTabla.getSelectedRow(), 0));
                        refrescarCeremonias();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.ceremoniaTabla.clearSelection();
                }
                borrarCamposCeremonias();
            }
            break;
            case "limpiarCeremonia":
                borrarCamposCeremonias();
                refrescarCeremonias();
                break;
            case "eliminarCeremonia":
                modelo.eliminarCeremonia((Integer) vista.ceremoniaTabla.getValueAt(vista.ceremoniaTabla.getSelectedRow(), 0));
                borrarCamposCeremonias();
                refrescarCeremonias();
                break;

            case "anadirAdorno": {
                try {
                    if (comprobarAdornoVacio()) {
                        Util.showErrorAlert("Campos obligatorios: Tipo - Tipo flores - Opciones");
                        vista.adornoTabla.clearSelection();
                    } else {
                        modelo.insertarAdorno(
                                (String) vista.comboTipoAdorno.getSelectedItem(),
                                vista.txtOtroAdorno.getText(),
                                vista.txtTipoFlores.getText(),
                                vista.txtOpciones.getText(),
                                vista.txtMensaje.getText());
                        refrescarAdornos();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.adornoTabla.clearSelection();
                }
                borrarCamposAdornos();
            }
            break;
            case "modificarAdorno": {
                try {
                    if (comprobarAdornoVacio()) {
                        Util.showErrorAlert("Campos obligatorios: Tipo - Tipo flores - Opciones");
                        vista.adornoTabla.clearSelection();
                    } else {
                        modelo.modificarAdorno(
                                (String) vista.comboTipoAdorno.getSelectedItem(),
                                vista.txtOtroAdorno.getText(),
                                vista.txtTipoFlores.getText(),
                                vista.txtOpciones.getText(),
                                vista.txtMensaje.getText(),
                                (Integer) vista.adornoTabla.getValueAt(vista.adornoTabla.getSelectedRow(), 0));
                        refrescarAdornos();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.adornoTabla.clearSelection();
                }
                borrarCamposAdornos();
            }
            break;
            case "limpiarAdorno":
                borrarCamposAdornos();
                refrescarAdornos();
                break;
            case "eliminarAdorno":
                modelo.eliminarAdorno((Integer) vista.adornoTabla.getValueAt(vista.adornoTabla.getSelectedRow(), 0));
                borrarCamposAdornos();
                refrescarAdornos();
                break;

            case "Modo oscuro":
                try {
                    if (!modoOscuro) {
                        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                    } else {
                        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                    }
                    modoOscuro = !modoOscuro;
                    SwingUtilities.updateComponentTreeUI(vista);
                } catch (Exception ex) {
                    System.out.println("No se ha podido cambiar al tema oscuro.");
                }
                break;
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    private void refrescarCeremonias() {
        try {
            vista.ceremoniaTabla.setModel(construirTableModelCeremonias(modelo.consultarCeremonia()));
            vista.comboCeremonia.removeAllItems();
            for(int i = 0; i < vista.dtmCeremonias.getRowCount(); i++) {
                vista.comboCeremonia.addItem(
                        vista.dtmCeremonias.getValueAt(i, 0) + " - " +
                                vista.dtmCeremonias.getValueAt(i, 1) + " " +
                                vista.dtmCeremonias.getValueAt(i, 2)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private  DefaultTableModel construirTableModelCeremonias(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnLabel(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, columnCount, data);

        vista.dtmCeremonias.setDataVector(data, columnNames);

        return vista.dtmCeremonias;

    }

    private void refrescarAdornos() {
        try {
            vista.adornoTabla.setModel(construirTableModelAdornos(modelo.consultarAdorno()));
            vista.comboAdorno.removeAllItems();
            for(int i = 0; i < vista.dtmAdornos.getRowCount(); i++) {
                vista.comboAdorno.addItem(
                        vista.dtmAdornos.getValueAt(i, 0) + " - " +
                                vista.dtmAdornos.getValueAt(i, 1) + " " +
                                vista.dtmAdornos.getValueAt(i, 2)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private  DefaultTableModel construirTableModelAdornos(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnLabel(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, columnCount, data);

        vista.dtmAdornos.setDataVector(data, columnNames);

        return vista.dtmAdornos;

    }

    private void refrescarContactos() {
        try {
            vista.contactoTabla.setModel(construirTableModeloContactos(modelo.consultarContacto()));
            vista.comboContacto.removeAllItems();
            for(int i = 0; i < vista.dtmContactos.getRowCount(); i++) {
                vista.comboContacto.addItem(vista.dtmContactos.getValueAt(i, 0)+" - "+
                        vista.dtmContactos.getValueAt(i, 2)+", "+vista.dtmContactos.getValueAt(i, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel construirTableModeloContactos(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnLabel(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, columnCount, data);

        vista.dtmContactos.setDataVector(data, columnNames);

        return vista.dtmContactos;

    }

    private void refrescarPedidos() {
        try {
            vista.pedidoTabla.setModel(construirTableModelPedidos(modelo.consultarPedido()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel construirTableModelPedidos(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnLabel(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, columnCount, data);

        vista.dtmPedidos.setDataVector(data, columnNames);

        return vista.dtmPedidos;

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

    private void borrarCamposPedidos() {
        vista.txtNumero.setText("");
        vista.comboContacto.setSelectedIndex(-1);
        vista.comboCeremonia.setSelectedIndex(-1);
        vista.comboAdorno.setSelectedIndex(-1);
        vista.fecha.setText("");
        vista.txtComentario.setText("");
        vista.txtPrecio.setText("");
    }

    private void borrarCamposContactos() {
        vista.txtNombre.setText("");
        vista.txtApellidos.setText("");
        vista.txtPais.setText("");
        vista.fechaNacimiento.setText("");
    }

    private void borrarCamposCeremonias() {
        vista.comboTipoCeremonia.setSelectedIndex(-1);
        vista.txtOtroCeremonia.setText("");
        vista.fechaEntrega.setText("");
        vista.radioButtonEnvio.setSelected(false);
        vista.radioButtonTienda.setSelected(false);
        vista.txtDireccion.setText("");
    }

    private void borrarCamposAdornos() {
        vista.comboTipoAdorno.setSelectedIndex(-1);
        vista.txtOtroAdorno.setText("");
        vista.txtTipoFlores.setText("");
        vista.txtOpciones.setText("");
        vista.txtMensaje.setText("");
    }

    private boolean comprobarPedidoVacio() {
        return vista.txtPrecio.getText().isEmpty() ||
                vista.comboAdorno.getSelectedIndex() == -1 ||
                vista.comboContacto.getSelectedIndex() == -1 ||
                vista.comboCeremonia.getSelectedIndex() == -1 ||
                vista.fecha.getText().isEmpty();
    }

    private boolean comprobarContactoVacio() {
        return vista.txtApellidos.getText().isEmpty() ||
                vista.txtNombre.getText().isEmpty() ||
                vista.txtPais.getText().isEmpty() ||
                vista.fechaNacimiento.getText().isEmpty();
    }

    private boolean comprobarCeremoniaVacia() {
        return vista.comboTipoCeremonia.getSelectedIndex() == -1 ||
                vista.fechaEntrega.getText().isEmpty();
    }

    private boolean comprobarAdornoVacio() {
        return vista.comboTipoAdorno.getSelectedIndex() == -1 ||
                vista.txtTipoFlores.getText().isEmpty() ||
                vista.txtOpciones.getText().isEmpty();
    }

    private void addItemListeners(Controlador controlador) {
        vista.comboTipoAdorno.addItemListener(controlador); //listener para las imagenes de adornos
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == vista.comboTipoAdorno && e.getStateChange() == ItemEvent.SELECTED) {
            String tipo = String.valueOf(vista.comboTipoAdorno.getSelectedItem());
            actualizarImagenAdorno(tipo);
        }
    }

    private void actualizarImagenAdorno(String tipo) {
        String ruta;

        switch (tipo) {
            case "Ramo":
                ruta = "/resources/img/ramo.png";
                break;
            case "Centro":
                ruta = "/resources/img/centro.png";
                break;
            case "Corona":
                ruta = "/resources/img/corona.png";
                break;
            default:
                ruta = "/resources/img/default.png";
                break;
        }

        // Cargar desde resources (funciona dentro del JAR)
        java.net.URL url = getClass().getResource(ruta);

        if (url == null) {
            vista.lblImagenAdorno.setIcon(null);
            return;
        }

        ImageIcon icon = new ImageIcon(url);

        // (Opcional) Escalar a tamaño del label
        Image img = icon.getImage().getScaledInstance(
                vista.lblImagenAdorno.getWidth(),
                vista.lblImagenAdorno.getHeight(),
                Image.SCALE_SMOOTH
        );

        vista.lblImagenAdorno.setIcon(new ImageIcon(img));
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


}