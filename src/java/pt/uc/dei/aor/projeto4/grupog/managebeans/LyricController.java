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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.aor.projeto4.grupog.ejbs.LyricFacade;
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
            return "";
        }
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

    public String addLyric() {
        lyricFacade.addLyric(lyric, musicSelected, loggedUser.getUser());
        return "listAllMusics";
    }

    public String editLyric() {

        lyricFacade.editLyric(objLyric, lyric);
        return "listAllMusics";

    }

}
