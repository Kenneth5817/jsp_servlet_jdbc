package org.iesvdm.jsp_servlet_jdbc.dao;

import org.iesvdm.jsp_servlet_jdbc.model.Socio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SocioDAOImpl extends AbstractDAOImpl implements SocioDAO {
    @Override
    public void create(Socio socio) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rsGenKeys = null;

        try {
            conn = connectDB();


            //1 alternativas comentadas:
            //Ver también, AbstractDAOImpl.executeInsert ...
            //Columna fabricante.codigo es clave primaria auto_increment, por ese motivo se omite de la sentencia SQL INSERT siguiente.
            ps = conn.prepareStatement("INSERT INTO socio (nombre, estatura, edad, localidad) VALUES (?, ? , ?, ?)", Statement.RETURN_GENERATED_KEYS);

            int idx = 1;
            ps.setString(idx++, socio.getNombre());
            ps.setInt(idx++, socio.getEstatura());
            ps.setInt(idx++, socio.getEdad());
            ps.setString(idx++, socio.getLocalidad());

            int rows = ps.executeUpdate();
            if (rows == 0)
                System.out.println("INSERT de socio con 0 filas insertadas.");

            rsGenKeys = ps.getGeneratedKeys();
            if (rsGenKeys.next())
                //Se lee la columna
                socio.setSocioId(rsGenKeys.getInt(1));

        } catch (SQLException | ClassNotFoundException  e) {
            e.printStackTrace();
        } finally {
            closeDb(conn, ps, rs);
        }

    }

    @Override
    public List<Socio> getAll() {

        Connection conn = null;
        Statement s = null;
        ResultSet rs = null;

        List<Socio> listSocio = new ArrayList<>();

        try {
            conn = connectDB();

            // Se utiliza un objeto Statement dado que no hay parámetros en la consulta.
            s = conn.createStatement();

            rs = s.executeQuery("SELECT * FROM socio");
            while (rs.next()) {
                Socio socio = new Socio();

                socio.setSocioId(rs.getInt("socioID"));
                socio.setNombre(rs.getString("nombre"));
                socio.setEstatura(rs.getInt("estatura"));
                socio.setEdad(rs.getInt("edad"));
                socio.setLocalidad(rs.getString("localidad"));
                listSocio.add(socio);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeDb(conn, s, rs);
        }
        return listSocio;

    }

    @Override
    public Optional<Socio> find(int id) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectDB();

            ps = conn.prepareStatement("SELECT * FROM socio WHERE socioID = ?");

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                Socio socio = new Socio();

                socio.setSocioId(rs.getInt("socioID"));
                socio.setNombre(rs.getString("nombre"));
                socio.setEstatura(rs.getInt("estatura"));
                socio.setEdad(rs.getInt("edad"));
                socio.setLocalidad(rs.getString("localidad"));

                return Optional.of(socio);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeDb(conn, ps, rs);
        }

        return Optional.empty();

    }

    @Override
    public void update(Socio socio) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectDB();

            //Comprobamos que los datos no estén nulos
            if (socio == null) {
                System.out.println("Socio es null, no se puede actualizar.");
                return;
            }

            // El id es autoincremental, por lo que solo se actualizan los demás campos
            ps = conn.prepareStatement("UPDATE socio SET nombre = ?, estatura = ?, edad = ?, localidad = ? WHERE socioID = ?");

            int idx = 1;
            ps.setString(idx++, socio.getNombre());
            ps.setDouble(idx++, socio.getEstatura()); // Asegúrate de usar setDouble si estatura es decimal
            ps.setInt(idx++, socio.getEdad());
            ps.setString(idx++, socio.getLocalidad());
            ps.setInt(idx++, socio.getSocioId());

            // Ejecuta la actualización
            int rows = ps.executeUpdate();

            // Verifica si se actualizó algún registro
            if (rows > 0) {
                System.out.println("Socio actualizado correctamente.");
            } else {
                System.out.println("No se encontró el socio o no se actualizó.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeDb(conn, ps, rs);
        }
    }


    @Override
    public void delete(int id) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectDB();

            //Preparamos el exception
            ps = conn.prepareStatement("DELETE FROM socio WHERE socioID = ?");
            int idx = 1;
            ps.setInt(idx, id);

            int rows = ps.executeUpdate();

            if (rows == 0)
                System.out.println("Delete de socio con 0 registros eliminados.");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeDb(conn, ps, rs);
        }

    }

    //------REVISAR EL DAO IMPLEMENTACIÓN------ Para que borre correctamente
    // Método para borrar un socio
    public boolean borrarSocio(int codigo) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean exito = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/baloncesto", "root", "user");

                String sql = "DELETE FROM socio WHERE socioID = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, codigo);

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas > 0) {
                    exito = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) ps.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return exito;
        }
}
