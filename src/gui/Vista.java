package gui;

import com.github.lgooddatepicker.components.DatePicker;
import gui.base.enums.TipoCeremonia;
import gui.base.enums.TipoAdorno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Vista extends JFrame{
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private final static String TITULO_FRAME="Aplicación floristería";

    //pedidos
    JPanel JPanelPedido;
    JTextField txtNumero;
    JComboBox comboContacto;
    JComboBox comboCeremonia;
    JComboBox comboAdorno;
    DatePicker fecha;
    JTextField txtComentario;
    JTextField txtPrecio;
    JButton btnPedidoEliminar;
    JButton btnPedidoModificar;
    JButton btnPedidoAnadir;
    JTable pedidoTabla;
    JButton btnPedidoLimpiar;

    //contactos
    JPanel JPanelContacto;
    JTextField txtNombre;
    JTextField txtApellidos;
    JTextField txtPais;
    DatePicker fechaNacimiento;
    JButton btnContactoAnadir;
    JButton btnContactoModificar;
    JButton btnContactoEliminar;
    JTable contactoTabla;
    JButton btnContactoLimpiar;

    //ceremonias
    JPanel JPanelCeremonia;
    JTextField txtNombreEditorial;
    JTextField txtOtroCeremonia;
    JTextField txtTelefono;
    JComboBox comboTipoEditorial;
    JTextField txtDireccion;
    JButton btnCeremoniaAnadir;
    JButton btnCeremoniaModificar;
    JButton btnCeremoniaEliminar;
    JTable ceremoniaTabla;
    JButton btnCeremoniaLimpiar;
    JComboBox comboTipoCeremonia;
    DatePicker fechaCeremonia;
    JRadioButton radioButtonTienda;
    JRadioButton radioButtonEnvio;
    JLabel lblTipoCeremonia;
    JLabel lblOtroCeremonia;

    //adornos
    JPanel JPanelAdorno;
    JComboBox comboTipoAdorno;
    JTextField txtOtroAdorno;
    JTextField txtTipoFlores;
    JLabel lblTipoAdorno;
    JLabel lblOtroAdorno;
    JTextField txtMensaje;
    JLabel lblOpciones;
    JTextField txtOpciones;
    JButton btnAdornoAnadir;
    JButton btnAdornoModificar;
    JButton btnAdornoLimpiar;
    JButton btnAdornoEliminar;
    JTable adornoTabla;

    //busqueda
    private JLabel etiquetaEstado;


    //default table model
    DefaultTableModel dtmEditoriales;
    DefaultTableModel dtmAutores;
    DefaultTableModel dtmLibros;
    DefaultTableModel dtmAdornos;

    //menubar
    JMenuItem itemOpciones;
    JMenuItem itemDesconectar;
    JMenuItem itemSalir;

    //cuadro dialogo
    OptionDialog optionDialog;
    JDialog adminPasswordDialog;
    JButton btnValidate;
    JPasswordField adminPassword;

    public Vista() {
        super(TITULO_FRAME);
        initFrame();
    }

    public void initFrame() {
        this.setContentPane(panel1);
        //al clickar en cerrar no hace nada
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        //doy dimension
        this.setSize(new Dimension(this.getWidth()+100,this.getHeight()));
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        //creo cuadro dialogo
        optionDialog=new OptionDialog(this);
        //llamo menu
        setMenu();
        //llamo cuadro dialogo admin
        setAdminDialog();
        //cargo enumerados
        setEnumComboBox();
        //cargo table models
        setTableModels();
    }
    private void setMenu() {
        JMenuBar mbBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        itemOpciones = new JMenuItem("Opciones");
        itemOpciones.setActionCommand("Opciones");
        itemDesconectar = new JMenuItem("Desconectar");
        itemDesconectar.setActionCommand("Desconectar");
        itemSalir=new JMenuItem("Salir");
        itemSalir.setActionCommand("Salir");
        menu.add(itemOpciones);
        menu.add(itemDesconectar);
        menu.add(itemSalir);
        mbBar.add(menu);
        mbBar.add(Box.createHorizontalGlue());
        this.setJMenuBar(mbBar);
    }
    private void setAdminDialog() {
        btnValidate = new JButton("Validar");
        btnValidate.setActionCommand("abrirOpciones");
        adminPassword = new JPasswordField();
        //dimension al cuadro de texto
        adminPassword.setPreferredSize(new Dimension(100,26));
        Object[] options=new Object[] {adminPassword,btnValidate};
        JOptionPane jop = new JOptionPane("Introduce la contraseña",JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_OPTION,null,options);
        adminPasswordDialog = new JDialog(this,"Opciones",true);
        adminPasswordDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        adminPasswordDialog.setContentPane(jop);
        adminPasswordDialog.pack();
        adminPasswordDialog.setLocationRelativeTo(this);
    }
    private void setEnumComboBox() {
        //recorrer los enumerados y los cargo en el comboBox correspondiente
        //.values cogemos valores del enumerado
        //.getValor los añadimos al combo
        for (TipoAdorno constant: TipoAdorno.values()) {
            comboTipoAdorno.addItem(constant.getValor());
        }
        //lo coloco en una posicion que no tenga valor
        comboTipoAdorno.setSelectedIndex(-1);
        for (TipoCeremonia constant: TipoCeremonia.values()) {
            comboTipoCeremonia.addItem(constant.getValor());
        }
        comboTipoCeremonia.setSelectedIndex(-1);
    }
    private void setTableModels() {
        //librosTabla, autoresTabla, editorialesTabla
        this.dtmLibros=new DefaultTableModel();
        this.pedidoTabla.setModel(dtmLibros);

        this.dtmAutores=new DefaultTableModel();
        this.contactoTabla.setModel(dtmAutores);

        this.dtmEditoriales=new DefaultTableModel();
        this.ceremoniaTabla.setModel(dtmEditoriales);

        this.dtmAdornos=new DefaultTableModel();
        this.adornoTabla.setModel(dtmAdornos);
    }
}
