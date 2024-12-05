package org.iesvdm.jsp_servlet_jdbc.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAOImpl;
import org.iesvdm.jsp_servlet_jdbc.model.Socio;

import java.io.IOException;
import java.util.List;

@WebServlet(name="EditarSociosServlet", value="/EditarSociosServlet")
public class EditarSociosServlet extends HttpServlet {
    private SocioDAOImpl socioDao = new SocioDAOImpl();

    //MÉTODO PARA RUTAS GET /GrabarSociosServlet
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/formularioSocioB.jsp");
        dispatcher.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        RequestDispatcher dispatcher=null;
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

        if(codigo!=null) {
            List<Socio> listado = this.socioDao.getAll();
            request.setAttribute("listado", listado);

            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp_servlet_editar.jsp");
            dispatcher.forward(request, response);
        }else{
            //Mostramos el error
            System.out.println("Parámetro no válido");
        }


    }
}
