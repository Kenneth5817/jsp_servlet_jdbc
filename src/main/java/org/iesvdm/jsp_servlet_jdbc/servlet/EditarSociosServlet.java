package org.iesvdm.jsp_servlet_jdbc.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAO;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAOImpl;
import org.iesvdm.jsp_servlet_jdbc.model.Socio;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/EditarSociosServlet")
public class EditarSociosServlet extends HttpServlet {

    private SocioDAO socioDAO = new SocioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("socioId");

        //Si el id no es nulo editamos la pag.
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Optional<Socio> socio = socioDAO.find(id);

                if (socio.isPresent()) {
                    request.setAttribute("codigo", socio.get());
                } else {
                    request.setAttribute("error", "No se encontró el socio con ese ID.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "El ID no es válido. Asegúrese de que el ID es un número.");
            }
        } else {
            //En caso contrario muestra un error
            request.setAttribute("error", "El parámetro 'id' es obligatorio.");
        }

        //LO que no se es por qué al editarlo, me crea un nuevo socio en vez de modificarme el que tenia... Curioso!
        request.getRequestDispatcher("/WEB-INF/jsp/formularioSocioB.jsp").forward(request, response);
    }

}
