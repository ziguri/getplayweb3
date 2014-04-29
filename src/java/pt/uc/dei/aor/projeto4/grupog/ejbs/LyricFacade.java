/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.ejbs;

import WebServiceSoap.LyricWikiPortType_Stub;
import WebServiceSoap.LyricWiki_Impl;
import WebServiceSoap.LyricsResult;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pt.uc.dei.aor.projeto4.grupog.entities.AppUser;
import pt.uc.dei.aor.projeto4.grupog.entities.Lyric;
import pt.uc.dei.aor.projeto4.grupog.entities.Music;
import pt.uc.dei.aor.projeto4.grupog.managebeans.GeneralController;

/**
 *
 * @author Zueb LDA
 */
@Stateless
public class LyricFacade extends AbstractFacade<Lyric> {

    @PersistenceContext(unitName = "GetPlayWebPU3")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LyricFacade() {
        super(Lyric.class);
    }

    /**
     * Return the Lyric in database table
     *
     * @param m
     * @param u
     * @return
     */
    public String getLyric(Music m, AppUser u) {

        Query q = em.createNamedQuery("lyric.findLyric");
        q.setParameter("mus", m).setParameter("us", u);

        try {
            return (String) q.getSingleResult();
        } catch (NoResultException e) {
            return "";
        }

    }

    /**
     * Return the Object Lyric from database
     *
     * @param m
     * @param u
     * @return
     */
    public Lyric getObjectLyric(Music m, AppUser u) {

        Query q = em.createNamedQuery("lyric.findObjectLyric");
        q.setParameter("mus", m).setParameter("us", u);

        try {
            return (Lyric) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Return all the Lyric´s with that music reference
     *
     * @param m
     * @return
     */
    public List<Lyric> getAllLyricsOfMusic(Music m) {

        Query q = em.createNamedQuery("lyric.findLyricByMusic");
        q.setParameter("mus", m);
        return (List<Lyric>) q.getResultList();
    }

    /**
     * Remove all Lyric entries with music reference
     *
     * @param m
     */
    public void deleteLyricByMusic(Music m) {

        Query q = em.createNamedQuery("lyric.deleteLyricByMusic");
        q.setParameter("mus", m);
    }

    public void addLyric(String lyric, Music m, AppUser u) {

        Lyric l = new Lyric();
        l.setFullLyric(lyric);
        l.setMusic(m);
        l.setUser(u);
        this.create(l);
    }

    public void editLyric(Lyric l, String fullLyric) {

        l.setFullLyric(fullLyric);
        this.edit(l);
    }

    private static LyricWikiPortType_Stub createProxy() {
        return (LyricWikiPortType_Stub) (new LyricWiki_Impl().getLyricWikiPort());
    }

    /**
     * Return the result using SOAP web service.
     *
     * @param artist
     * @param m
     * @param title
     * @return
     */
    public String soapResult(String artist, String title) {

        try {
            LyricWikiPortType_Stub lw = createProxy();
            LyricsResult lr = lw.getSong(artist, title);
            return lr.getLyrics();
        } catch (Exception ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
            return "Lyric not found in http://lyrics.wikia.com/";
        }

    }

    public String restResult(String artist, String title) {

        WebTarget target = ClientBuilder.newClient().target("http://lyrics.wikia.com/api.php");
        Invocation invocation = target.queryParam("func", "getSong").queryParam("fmt", "text")
                .queryParam("artist", artist).queryParam("song", title).request(MediaType.TEXT_PLAIN).buildGet();
        Response response = invocation.invoke();
        return response.readEntity(String.class);

    }

    public boolean soapExist(String artist, String title) {
        String s = soapResult(artist, title);

        if (s.equals("Not found")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean restExist(String artist, String title) {
        String s = restResult(artist, title);
        if (s.equals("Not found")) {
            return false;
        } else {
            return true;
        }
    }

}
