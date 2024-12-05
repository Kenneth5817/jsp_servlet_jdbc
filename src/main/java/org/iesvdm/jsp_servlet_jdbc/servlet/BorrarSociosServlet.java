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
import java.util.List;

@WebServlet(name="BorrarSociosServlet", value="/BorrarSociosServlet")
public class BorrarSociosServlet extends HttpServlet {
    private SocioDAOImpl socioDAO=new SocioDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = null;
        //Encxontramos en lsitadoSociosB
        String codigoStr = request.getParameter("codigo");

        //Validamos el parametro
        Integer codigo = null;
        //Try y catch para las excepciones
        try {
            //Nos aseguramos de que sea un entero el codigo
            codigo = Integer.parseInt(codigoStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (codigo != null) {
            //SI QUISIERA REDIRECCION DEL LADO DEL NAVEGADOR
            //response.sendRedirect("ListarSociosServlet");

            List<Socio> listado = this.socioDAO.getAll();
            request.setAttribute("listado", listado);

            //pero quiero redireccion interna
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/listadoSociosB.jsp");
            dispatcher.forward(request, response);
        } else {
            System.out.println("Parámetro no válido");
        }
    }

}
