package gui;

import com.github.lgooddatepicker.components.DatePicker;
import gui.base.enums.GenerosLibros;
import gui.base.enums.TiposEditoriales;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Vista extends JFrame{
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private final static String TITULO_FRAME="Aplicacion libreria";

    //libros
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
    JTable librosTabla;

    //autores
    JPanel JPanelAutor;
    JTextField txtNombre;
    JTextField txtApellidos;
    JTextField txtPais;
    DatePicker fechaNacimiento;
    JButton btnAutoresAnadir;
    JButton btnAutoresModificar;
    JButton btnAutoresEliminar;
    JTable autoresTabla;

    //editoriales
    JPanel JPanelEditorial;
    JTextField txtNombreEditorial;
    JTextField txtEmail;
    JTextField txtTelefono;
    JComboBox comboTipoEditorial;
    JTextField txtWeb;
    JButton btnEditorialesAnadir;
    JButton btnEditorialesModificar;
    JButton btnEditorialesEliminar;
    JTable editorialesTabla;

    //busqueda
    private JLabel etiquetaEstado;
    private JButton btnPedidoLimpiar;

    //default table model
    DefaultTableModel dtmEditoriales;
    DefaultTableModel dtmAutores;
    DefaultTableModel dtmLibros;

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
        for (TiposEditoriales constant: TiposEditoriales.values()) {
            comboTipoEditorial.addItem(constant.getValor());
        }
        //lo coloco en una posicion que no tenga valor
        comboTipoEditorial.setSelectedIndex(-1);
        for (GenerosLibros constant: GenerosLibros.values()) {
            comboAdorno.addItem(constant.getValor());
        }
        comboAdorno.setSelectedIndex(-1);
    }
    private void setTableModels() {
        //librosTabla, autoresTabla, editorialesTabla
        this.dtmLibros=new DefaultTableModel();
        this.librosTabla.setModel(dtmLibros);

        this.dtmAutores=new DefaultTableModel();
        this.autoresTabla.setModel(dtmAutores);

        this.dtmEditoriales=new DefaultTableModel();
        this.editorialesTabla.setModel(dtmEditoriales);
    }
}
