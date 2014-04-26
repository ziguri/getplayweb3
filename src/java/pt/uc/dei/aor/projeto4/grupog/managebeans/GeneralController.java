/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.managebeans;

import WebServiceSoap.LyricWikiPortType_Stub;
import WebServiceSoap.LyricWiki_Impl;
import WebServiceSoap.LyricsResult;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.aor.projeto4.grupog.ejbs.LyricFacade;
import pt.uc.dei.aor.projeto4.grupog.ejbs.MusicFacade;
import pt.uc.dei.aor.projeto4.grupog.ejbs.PlaylistFacade;
import pt.uc.dei.aor.projeto4.grupog.entities.Music;
import pt.uc.dei.aor.projeto4.grupog.entities.Playlist;
import pt.uc.dei.aor.projeto4.grupog.exceptions.MusicsAlreadyExistInPlaylistException;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@Named
@RequestScoped
public class GeneralController implements Converter {

    @Inject
    private PlaylistFacade playlistEjb;
    @Inject
    private LoggedUserMb loggedUser;
    @Inject
    private MusicFacade musicEjb;
    @Inject
    private LyricFacade lyricFacade;
    private Playlist playlistSelected;
    private Music musicSelected;
    private Integer musicIdSelected;
    private Integer playlistIdSelected;
    private List<Playlist> itemsPlays;
    private DataModel<Music> musics;
    private String messageErrorMusic;

    public GeneralController() {
    }

    @PostConstruct
    public void init() {
        itemsPlays = null;
    }

    /**
     * Invokes Playlist EJB method to add music to selected playlist
     *
     * @return
     * @throws MusicsAlreadyExistInPlaylistException
     */
    public String saveMusic() throws MusicsAlreadyExistInPlaylistException {
        try {
            playlistEjb.addMusicToPlaylist(playlistSelected, musicSelected);
            return null;
        } catch (MusicsAlreadyExistInPlaylistException e) {
            return "MessageDuplicatedMusic";
        }
    }

    /**
     * Overridable method from abstract class Converter. Transforms received
     * value into selected Object.
     *
     * @param context
     * @param component
     * @param value
     * @return
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        if (!value.matches("\\d+")) {
            throw new ConverterException("The value is not a valid playlist ID: " + value);
        }

        Integer id = Integer.parseInt(value);
        return playlistEjb.find(id);
    }

    /**
     * Overridable method from abstract class Converter. Transforms object into
     * String value.
     *
     * @param context
     * @param component
     * @param value
     * @return
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value == null) {
            return null;
        }

        if (!(value instanceof Playlist)) {
            throw new ConverterException("The value is not a Playlist: " + value);
        }

        String id = ((Playlist) value).getId().toString();
        return (id != null) ? id.toString() : null;
    }

    /**
     * Invokes Playlist EJB method in order to show all logged user playlists
     *
     * @return
     */
    public List<Playlist> myPlaylists() {

        itemsPlays = playlistEjb.showMyPlaylist(loggedUser.getUser());
        return itemsPlays;
    }

    /**
     * Invokes Music EJB method in order to construct DataModel with all musics
     *
     * @return DataModel<Music>
     */
    public DataModel<Music> getMusicList() {
        if (musicEjb != null) {
            DataModel model = (DataModel<Music>) new ListDataModel(musicEjb.findAll());
            musics = model;
            return model;
        }
        return null;
    }

    /**
     * Invokes Music EJB method to remove selected object.
     *
     * @return
     */
    public String destroy() {

        if (musicSelected.getUser().equals(loggedUser.getUser())) {
            musicEjb.removeMusic(musicSelected);
            return "listMyMusics";
        }
        return "listMyMusics";

    }

    /**
     * Invokes Playlist EJB method to remove selected music from selected
     * playlist.
     *
     * @return String
     */
    public String removeMusicPlaylist() {
        playlistEjb.removeMusicFromPlaylist(playlistSelected, musicSelected);
        return "viewPlaylist";
    }

    /**
     * Invokes Music EJB method in order to construct and return
     * DataModel<Music>
     *
     * @return DataModel<Music>
     */
    public DataModel<Music> getPlaylistMusics() {

        DataModel model = (DataModel<Music>) new ListDataModel(musicEjb.
                showMusicsPlaylist(playlistSelected));
        return model;

    }

    /**
     * Saves selected playlist in flash scope to use in add music to playlist
     * view
     *
     * @return String
     */
    public String addMusicToSelectedPlaylist() {

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("play", playlistSelected);

        return "viewPlaylist";

    }

    /**
     * Saves selected playlist in flash scope to use in selected playlist
     * edition
     *
     * @return String
     */
    public String editSelectedPlaylist() {

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("play", playlistSelected);

        return "editPlaylist";

    }

    /**
     * Saves selected music in flash scope to use in selected music edition
     *
     * @return
     */
    public String editSelectedMusic() {

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("music", musicSelected);

        return "editMusic";

    }

    //Getter and Setter
    /**
     * Get Playlist EJB
     *
     * @return
     */
    public PlaylistFacade getPlaylistEjb() {
        return playlistEjb;
    }

    /**
     * Set Playlist EJB
     *
     * @param playlistEjb
     */
    public void setPlaylistEjb(PlaylistFacade playlistEjb) {
        this.playlistEjb = playlistEjb;
    }

    /**
     * Get logged user
     *
     * @return
     */
    public LoggedUserMb getLoggedUser() {
        return loggedUser;
    }

    /**
     * Set logged user
     *
     * @param loggedUser
     */
    public void setLoggedUser(LoggedUserMb loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * Get Music EJB
     *
     * @return
     */
    public MusicFacade getMusicEjb() {
        return musicEjb;
    }

    /**
     * Set Get Music EJB
     *
     * @param musicEjb
     */
    public void setMusicEjb(MusicFacade musicEjb) {
        this.musicEjb = musicEjb;
    }

    /**
     * Get selected playlist
     *
     * @return
     */
    public Playlist getPlaylistSelected() {
        return playlistSelected;
    }

    /**
     * Set selected playlist
     *
     * @param playlistSelected
     */
    public void setPlaylistSelected(Playlist playlistSelected) {
        this.playlistSelected = playlistSelected;
    }

    /**
     * Get List<Playlist>
     *
     * @return
     */
    public List<Playlist> getItemsPlays() {
        return itemsPlays;
    }

    /**
     * Set List<Playlist>
     *
     * @param itemsPlays
     */
    public void setItemsPlays(List<Playlist> itemsPlays) {
        this.itemsPlays = itemsPlays;
    }

    /**
     * Get DataModel<Music>
     *
     * @return
     */
    public DataModel<Music> getMusics() {
        return musics;
    }

    /**
     * Set DataModel<Music>
     *
     * @param musics
     */
    public void setMusics(DataModel<Music> musics) {
        this.musics = musics;
    }

    /**
     * Get selected music
     *
     * @return
     */
    public Music getMusicSelected() {
        return musicSelected;
    }

    /**
     * Set selected music
     *
     * @param musicSelected
     */
    public void setMusicSelected(Music musicSelected) {
        this.musicSelected = musicSelected;
    }

    /**
     * Get music error message
     *
     * @return
     */
    public String getMessageErrorMusic() {
        return messageErrorMusic;
    }

    /**
     * Set music error message
     *
     * @param messageErrorMusic
     */
    public void setMessageErrorMusic(String messageErrorMusic) {
        this.messageErrorMusic = messageErrorMusic;
    }

    /**
     * Get selected music ID
     *
     * @return
     */
    public Integer getMusicIdSelected() {
        return musicIdSelected;
    }

    /**
     * Set selected music ID
     *
     * @param musicIdSelected
     */
    public void setMusicIdSelected(Integer musicIdSelected) {
        this.musicIdSelected = musicIdSelected;
    }

    /**
     * Get selected Playlist ID
     *
     * @return
     */
    public Integer getPlaylistIdSelected() {
        return playlistIdSelected;
    }

    /**
     * Set selected Playlist ID
     *
     * @param playlistIdSelected
     */
    public void setPlaylistIdSelected(Integer playlistIdSelected) {
        this.playlistIdSelected = playlistIdSelected;
    }

    public String seeLyric(Music m) {

        return lyricFacade.getLyric(m, loggedUser.getUser());
    }

    private static LyricWikiPortType_Stub createProxy() {
        return (LyricWikiPortType_Stub) (new LyricWiki_Impl().getLyricWikiPort());
    }

    /**
     * Return the result using SOAP web service.
     *
     * @return
     */
    public String getResult() {

        try {
            LyricWikiPortType_Stub lw = createProxy();
            LyricsResult lr = lw.getSong(musicSelected.getArtist(), musicSelected.getTitle());
            return lr.getLyrics();
        } catch (Exception ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
            return "Is empty";
        }

    }

}
