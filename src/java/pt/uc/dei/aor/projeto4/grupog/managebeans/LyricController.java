/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.managebeans;

import WebServiceSoap.LyricWikiPortType_Stub;
import WebServiceSoap.LyricWiki_Impl;
import WebServiceSoap.LyricsResult;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pt.uc.dei.aor.projeto4.grupog.ejbs.LyricFacade;
import pt.uc.dei.aor.projeto4.grupog.ejbs.MusicFacade;
import pt.uc.dei.aor.projeto4.grupog.entities.Lyric;
import pt.uc.dei.aor.projeto4.grupog.entities.Music;

/**
 *
 * @author Zueb LDA
 */
@Named
@ViewScoped
public class LyricController implements Serializable {

    @Inject
    private LyricFacade lyricFacade;
    @Inject
    private LoggedUserMb loggedUser;
    @Inject
    private MusicFacade musicFacade;
    private Music musicSelected;
    private String lyric;
    private Lyric objLyric;

    public LyricFacade getLyricFacade() {
        return lyricFacade;
    }

    public void setLyricFacade(LyricFacade lyricFacade) {
        this.lyricFacade = lyricFacade;
    }

    public LoggedUserMb getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(LoggedUserMb loggedUser) {
        this.loggedUser = loggedUser;
    }

    public Music getMusicSelected() {
        return musicSelected;
    }

    public void setMusicSelected(Music musicSelected) {
        this.musicSelected = musicSelected;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public Lyric getObjLyric() {
        return objLyric;
    }

    public void setObjLyric(Lyric objLyric) {
        this.objLyric = objLyric;
    }

    public String seeLyric(Music m) {
        try {
            this.lyric = lyricFacade.getLyric(m, loggedUser.getUser());
            return lyric;
        } catch (Exception e) {

            return "";
        }

    }

    public String seeEditableLyric(Music m) {

        try {
            this.musicSelected = m;
            this.objLyric = lyricFacade.getObjectLyric(m, loggedUser.getUser());
            this.lyric = objLyric.getFullLyric();
            return lyric;

        } catch (Exception e) {
            lyric = soapResult(m);
            return lyric;
        }
    }

    public void changeMusicSelected(Music m) {

        this.musicSelected = m;
    }

    private static LyricWikiPortType_Stub createProxy() {
        return (LyricWikiPortType_Stub) (new LyricWiki_Impl().getLyricWikiPort());
    }

    /**
     * Return the result using SOAP web service.
     *
     * @param m
     * @return
     */
    public String soapResult(Music m) {

        try {
            LyricWikiPortType_Stub lw = createProxy();
            LyricsResult lr = lw.getSong(m.getArtist(), m.getTitle());
            this.musicSelected = m;
            this.lyric = lr.getLyrics();
            return lyric;
        } catch (Exception ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
            return "Lyric not found in http://lyrics.wikia.com/";
        }

    }

    /**
     * * Return the result using REST web service.
     *
     * @param m
     * @return
     */
    public String restResult(Music m) {

        WebTarget target = ClientBuilder.newClient().target("http://lyrics.wikia.com/api.php");
        Invocation invocation = target.queryParam("func", "getSong").queryParam("fmt", "text")
                .queryParam("artist", m.getArtist()).queryParam("song", m.getTitle()).request(MediaType.TEXT_PLAIN).buildGet();
        Response response = invocation.invoke();

        this.musicSelected = m;
        this.lyric = response.readEntity(String.class);
        return lyric;

    }

    /**
     * Verify if Soap webservice Result is available
     *
     * @param m
     * @return
     */
    public boolean soapExist(Music m) {
        String s = soapResult(m);
        return !s.equals("Not found");
    }

    /**
     * Verify if Rest webservice result is available
     *
     * @param m
     * @return
     */
    public boolean restExist(Music m) {
        String s = restResult(m);
        return !s.equals("Not found");
    }

    /**
     *
     * @return
     */
    public void addLyric() {
        lyricFacade.addLyric(lyric, musicSelected, loggedUser.getUser());
        addMessage("Lyric to " + musicSelected.getTitle() + " - " + musicSelected.getArtist() + " successfully added");
    }

    public String editLyric() {

        if (objLyric != null) {
            lyricFacade.editLyric(objLyric, lyric);
            return "listAllMusics";
        } else {
            lyricFacade.addLyric(lyric, musicSelected, loggedUser.getUser());
            return "listAllMusics";

        }

    }

    /**
     * add a new Success message
     *
     * @param summary message
     */
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success Message", summary);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * add a new error message
     *
     * @param summary message
     */
    public void errorMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Message", summary);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
