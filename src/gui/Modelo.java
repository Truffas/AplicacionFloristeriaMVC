package gui;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class Modelo {
    private String ip;
    private String user;
    private String password;
    private String adminPassword;

    public Modelo() {
        getPropValues();
    }

    public String getIp() {
        return ip;
    }
    public String getUser() {
        return user;
    }
    public String getPassword() {
        return password;
    }
    public String getAdminPassword() {
        return adminPassword;
    }

    private Connection conexion;

    void conectar() {

        try {
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://"+ip+":3308/floristeria",user, password);
        } catch (SQLException sqle) {
            try {
                conexion = DriverManager.getConnection(
                        "jdbc:mysql://"+ip+":3308/",user, password);

                PreparedStatement statement = null;

                String code = leerFichero();
                String[] query = code.split("--");
                for (String aQuery : query) {
                    statement = conexion.prepareStatement(aQuery);
                    statement.executeUpdate();
                }
                assert statement != null;
                statement.close();

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String leerFichero() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("basedatosFloristeria_java.sql")) ;
            String linea;
            StringBuilder stringBuilder = new StringBuilder();
            while ((linea = reader.readLine()) != null) {
                stringBuilder.append(linea);
                stringBuilder.append(" ");
            }

            return stringBuilder.toString();
    }

    void desconectar() {
        try {
            conexion.close();
            conexion = null;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    void insertarContacto(String nombre, String apellidos, LocalDate fechaNacimiento, String pais) {
        String sentenciaSql = "INSERT INTO contactos (nombre, apellidos, fechanacimiento, pais) VALUES (?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellidos);
            sentencia.setDate(3, Date.valueOf(fechaNacimiento));
            sentencia.setString(4, pais);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }
    void insertarCeremonia(String tipoCeremonia, String otroCeremonia, LocalDate fechaEntrega, String lugarEntrega, String direccion) {
        String sentenciaSql = "INSERT INTO ceremonias (tipoceremonia, otroceremonia, fechaentrega, lugarentrega, direccion) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, tipoCeremonia);
            sentencia.setString(2, otroCeremonia);
            sentencia.setDate(3, Date.valueOf(fechaEntrega));
            sentencia.setString(4, lugarEntrega);
            sentencia.setString(5, direccion);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void insertarAdorno(String tipoAdorno, String otroAdorno, String tipoFlores, String opciones, String mensaje) {
        String sentenciaSql = "INSERT INTO adornos (tipoadorno, otroadorno, tipoflores, opciones, mensaje) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, tipoAdorno);
            sentencia.setString(2, otroAdorno);
            sentencia.setString(3, tipoFlores);
            sentencia.setString(4, opciones);
            sentencia.setString(5, mensaje);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void insertarPedido(String contacto, String ceremonia, String adorno, LocalDate fecha,
                        String comentario, float precio) {
        String sentenciaSql = "INSERT INTO pedidos (idcontacto, idceremonia, idadorno, fecha, comentario, precio) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        int idcontacto = Integer.valueOf(contacto.split(" ")[0]);
        int idceremonia = Integer.valueOf(ceremonia.split(" ")[0]);
        int idadorno = Integer.valueOf(adorno.split(" ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            //sentencia.setString(0, numero);
            sentencia.setInt(1, idcontacto);
            sentencia.setInt(2, idceremonia);
            sentencia.setInt(3, idadorno);
            sentencia.setDate(4, Date.valueOf(fecha));
            sentencia.setString(5, comentario);
            sentencia.setFloat(6, precio);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }
    void modificarCeremonia(String tipoCeremonia, String otroCeremonia, LocalDate fechaEntrega, String lugarEntrega, String direccion, int idceremonia){

        String sentenciaSql = "UPDATE ceremonias SET tipoceremonia = ?, otroceremonia = ?, fechaentrega = ?, lugarentrega = ?, direccion = ? " +
                "WHERE idceremonia = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, tipoCeremonia);
            sentencia.setString(2, otroCeremonia);
            sentencia.setDate(3, Date.valueOf(fechaEntrega));
            sentencia.setString(4, lugarEntrega);
            sentencia.setString(5, direccion);
            sentencia.setInt(6, idceremonia);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void modificarAdorno(String tipoAdorno, String otroAdorno, String tipoFlores, String opciones, String mensaje, int idadorno){

        String sentenciaSql = "UPDATE adornos SET tipoadorno = ?, otroadorno = ?, tipoflores = ?, opciones = ?, mensaje = ? " +
                "WHERE idadorno = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, tipoAdorno);
            sentencia.setString(2, otroAdorno);
            sentencia.setString(3, tipoFlores);
            sentencia.setString(4, opciones);
            sentencia.setString(5, mensaje);
            sentencia.setInt(6, idadorno);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void modificarContacto(String nombre, String apellidos, LocalDate fechaNacimiento, String pais, int idcontacto){

        String sentenciaSql = "UPDATE contactos SET nombre = ?, apellidos = ?, fechanacimiento = ?, pais = ? " +
                "WHERE idcontacto = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellidos);
            sentencia.setDate(3, Date.valueOf(fechaNacimiento));
            sentencia.setString(4, pais);
            sentencia.setInt(5, idcontacto);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void modificarPedido(String contacto, String ceremonia, String adorno, LocalDate fecha,
                         String comentario, float precio, int idpedido) {

        String sentenciaSql = "UPDATE pedidos SET idcontacto = ?, idceremonia = ?, idadorno = ?, " +
                "fecha = ?, comentario = ?, precio = ? WHERE idpedido = ?";
        PreparedStatement sentencia = null;

        int idcontacto = Integer.valueOf(contacto.split(" ")[0]);
        int idceremonia = Integer.valueOf(ceremonia.split(" ")[0]);
        int idadorno = Integer.valueOf(adorno.split(" ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idcontacto);
            sentencia.setInt(2, idceremonia);
            sentencia.setInt(3, idadorno);
            sentencia.setDate(4, Date.valueOf(fecha));
            sentencia.setString(5, comentario);
            sentencia.setFloat(6, precio);
            sentencia.setInt(7, idpedido);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }
    void eliminarCeremonia(int idceremonia) {
        String sentenciaSql = "DELETE FROM ceremonias WHERE idceremonia = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idceremonia);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void eliminarAdorno(int idadorno) {
        String sentenciaSql = "DELETE FROM adornos WHERE idadorno = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idadorno);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }


    void eliminarContacto(int idcontacto) {
        String sentenciaSql = "DELETE FROM contactos WHERE idcontacto = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idcontacto);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void eliminarPedido(int idpedido) {
        String sentenciaSql = "DELETE FROM pedidos WHERE idpedido = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idpedido);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }
    ResultSet consultarCeremonia() throws SQLException {
        String sentenciaSql = "SELECT idceremonia as 'ID', " +
                "tipoceremonia as 'Tipo ceremonia', " +
                "otroceremonia as 'Otro', " +
                "fechaentrega as 'Fecha entrega', " +
                "lugarentrega as 'Lugar entrega', " +
                "direccion as 'Dirección' " +
                "FROM ceremonias";
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sentenciaSql);
        resultado = sentencia.executeQuery();
        return resultado;
    }

    ResultSet consultarAdorno() throws SQLException {
        String sentenciaSql = "SELECT idadorno as 'ID', " +
                "tipoadorno as 'Tipo adorno', " +
                "otroadorno as 'Otro', " +
                "tipoflores as 'Tipo flores', " +
                "opciones as 'Opciones', " +
                "mensaje as 'Mensaje' " +
                "FROM adornos";
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sentenciaSql);
        resultado = sentencia.executeQuery();
        return resultado;
    }

    ResultSet consultarContacto() throws SQLException {
        String sentenciaSql = "SELECT idcontacto as 'ID', " +
                "nombre as 'Nombre', " +
                "apellidos as 'Apellidos', " +
                "fechanacimiento as 'Fecha de nacimiento'," +
                "pais as 'País de origen' " +
                "FROM contactos";
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sentenciaSql);
        resultado = sentencia.executeQuery();
        return resultado;
    }
    ResultSet consultarPedido() throws SQLException {
        String sentenciaSql = "SELECT p.idpedido as 'ID', " +
                "p.idpedido as 'Numero', " +
                "concat(c.idcontacto, ' - ', c.apellidos, ', ', c.nombre) as 'Contacto', " +
                "concat(e.idceremonia, ' - ', e.tipoceremonia, ' ', e.otroceremonia) as 'Ceremonia', " +
                "concat(a.idadorno, ' - ', a.tipoadorno, ' ', a.otroadorno) as 'Adorno', " +
                "p.fecha as 'Fecha de pedido', " +
                "p.comentario as 'comentario', " +
                "p.precio as 'Precio' " +
                "FROM pedidos as p " +
                "inner join contactos as c " +
                "on c.idcontacto = p.idcontacto " +
                "inner join ceremonias as e " +
                "on e.idceremonia = p.idceremonia " +
                "inner join adornos as a " +
                "on a.idadorno = p.idadorno";
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sentenciaSql);
        resultado = sentencia.executeQuery();
        return resultado;
    }

    private void getPropValues() {
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = new FileInputStream(propFileName);

            prop.load(inputStream);
            ip = prop.getProperty("ip");
            user = prop.getProperty("user");
            password = prop.getProperty("pass");
            adminPassword = prop.getProperty("admin");

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void setPropValues(String ip, String user, String pass, String adminPass) {
        try {
            Properties prop = new Properties();
            prop.setProperty("ip", ip);
            prop.setProperty("user", user);
            prop.setProperty("pass", pass);
            prop.setProperty("admin", adminPass);
            OutputStream out = new FileOutputStream("config.properties");
            prop.store(out, null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.ip = ip;
        this.user = user;
        this.password = pass;
        this.adminPassword = adminPass;
    }

    public boolean contactoNombreYaExiste(String nombre, String apellidos) {
        String completeName = apellidos + ", " + nombre;
        String contactConsult = "SELECT existeContacto(?)";
        PreparedStatement function;
        boolean nameExists = false;
        try {
            function = conexion.prepareStatement(contactConsult);
            function.setString(1, completeName);
            ResultSet rs = function.executeQuery();
            rs.next();

            nameExists = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameExists;
    }
}
