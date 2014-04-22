/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pt.uc.dei.aor.projeto4.grupog.ejbs.MusicFacade;
import pt.uc.dei.aor.projeto4.grupog.entities.Music;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@WebServlet(name = "TopMusicServlet", urlPatterns = {"/topmusic"})
public class TopMusicServlet extends HttpServlet {

    @Inject
    private MusicFacade musicFacade;

    protected void topTenMusic(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = null;
        List<Music> topTenmusic = musicFacade.showTopTenPopularMusics();

        try {
            /* TODO output your page here. You may use following sample code. */

            out = response.getWriter();
            out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
            out.println("<table>");
            out.println("<th>Top</th>");
            out.println("<th>Title</th>");
            out.println("<th>Artist</th>");
            out.println("<th>Nº of Uses</th>");

            int i = 1;
            for (Music m : topTenmusic) {
                out.println("<tr><td>" + i + "º" + "</td>");
                out.println("<td>" + m.getTitle() + "</td>");
                out.println("<td>" + m.getArtist() + "</td>");
                out.println("<td>" + m.getPlaylists().size() + "</td></tr>");
                i++;
            }
            out.println("</table>");
            out.println("</html>");

        } catch (IOException e) {
            Logger.getLogger(TopMusicServlet.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    protected void allPopularMusic(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = null;
        List<Music> musicAllpop = musicFacade.showMostPopularMusics();
        try {

            out = response.getWriter();
            out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
            out.println("<table>");
            out.println("<th>Nº of Uses</th>");
            out.println("<th>Title</th>");
            out.println("<th>Artist</th>");
            out.println("<th>Album</th>");
            out.println("<th>Music Year</th>");
            out.println("<th>User</th>");
            int a = 1;
            for (Music m : musicAllpop) {
                out.println("<tr><td>" + m.getPlaylists().size() + "</td>");
                out.println("<td>" + m.getTitle() + "</td>");
                out.println("<td>" + m.getArtist() + "</td>");
                out.println("<td>" + m.getAlbum() + "</td>");
                out.println("<td>" + m.getMusic_year() + "</td>");
                out.println("<td>" + m.getUser().getName() + "</td></tr>");
                a++;

            }
            out.println("</table>");
            out.println("</html>");

        } catch (IOException e) {
            Logger.getLogger(TopMusicServlet.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    protected void toptenjson(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml;charset=UTF-8");

        List<Music> topTenmusic = musicFacade.showTopTenPopularMusics();
        response.setContentType("application/json;charset=UTF-8");
        JsonObjectBuilder musicbuilder;
        JsonObjectBuilder toptenlistbuilder = Json.createObjectBuilder();
        JsonObject music;

        int i = 1;
        for (Music m : topTenmusic) {
            musicbuilder = Json.createObjectBuilder();
            musicbuilder.add("top", i);
            musicbuilder.add("title", m.getTitle());
            musicbuilder.add("artist", m.getArtist());
            musicbuilder.add("n_of_uses", m.getPlaylists().size());
            music = musicbuilder.build();
            toptenlistbuilder.add("music", music);
        }

        JsonObject toptenjson = toptenlistbuilder.build();
        try {
            PrintWriter out = response.getWriter();
            out.print(toptenjson);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(TopMusicServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String resp = request.getParameter("topten");

        if (resp != null) {

            if (resp.equals("all")) {

                allPopularMusic(request, response);

            } else if (resp.equals("topten")) {

                topTenMusic(request, response);

            } else if (resp.equals("toptenjson")) {

                toptenjson(request, response);

            }
        }
    }

}
