package org.iesvdm.jsp_servlet_jdbc.servlet;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAO;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAOImpl;
import org.iesvdm.jsp_servlet_jdbc.model.Socio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@WebServlet(name="BorrarSociosServlet", value="/BorrarSociosServlet")
public class BorrarSociosServlet extends HttpServlet {
    private SocioDAOImpl socioDAO = new SocioDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = null;
        String codigoStr = request.getParameter("codigo");
        Integer codigo = null;

        try {
            codigo = Integer.parseInt(codigoStr); // Convertir el código a Integer
            if (codigo != null) {
                // Borrar socio
                socioDAO.delete(codigo); // Llamar al DAO para borrar el socio
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Obtener todos los socios después de borrar
        List<Socio> listado = this.socioDAO.getAll();

        // Asegurarse de que la lista no sea null
        if (listado == null) {
            listado = new ArrayList<>(); // Si la lista es null, inicialízala como una lista vacía
        }

        // Establecer el atributo de solicitud para la JSP
        request.setAttribute("listado", listado);

        // Redirigir a la página de listado de socios
        dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/listadoSociosB.jsp");
        dispatcher.forward(request, response);
    }
}
