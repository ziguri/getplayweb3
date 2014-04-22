/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.managebeans;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.aor.projeto4.grupog.ejbs.MusicFacade;
import pt.uc.dei.aor.projeto4.grupog.ejbs.PlaylistFacade;
import pt.uc.dei.aor.projeto4.grupog.entities.Music;
import pt.uc.dei.aor.projeto4.grupog.entities.Playlist;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@Named("editPlaylistController")
@ViewScoped
public class EditPlaylistController {

    @Inject
    private PlaylistFacade playlistEjb;
    @Inject
    private MusicFacade musicEjb;
    private Music musicSelected;
    private Playlist playlistSelected;
    @Inject
    LoggedUserMb loggedUser;
    private DataModel<Playlist> play;

    /**
     * Creates a new instance of EditPlaylistController
     */
    public EditPlaylistController() {
    }

    @PostConstruct
    public void init() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        setPlaylistSelected((Playlist) flash.get("play"));
        this.play = (DataModel<Playlist>) new ListDataModel(playlistEjb.showMyPlaylist(loggedUser.getUser()));
    }

    /**
     * This method invokes the Playlist EJB removeMusicFromPlaylist method in
     * order to remove the music from the playlist and edit the actual playlist.
     *
     * @return
     */
    public String removeMusicPlaylist() {
        
        playlistEjb.removeMusicFromPlaylist(playlistSelected, musicSelected);
        return null;
    }

    /**
     * Save the selected playlist in flash scoped between views
     *
     * @return String
     */
    public String saveSelectedPlaylist() {

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("play", playlistSelected);

        return "viewPlaylist";

    }

    /**
     * Invoques the music EJB method showMusicsPLaylist in order to construct
     * and return a DataModel<Music>
     *
     * @return DataModel<Music>
     */
    public DataModel<Music> getPlaylistMusics() {

        DataModel model = (DataModel<Music>) new ListDataModel(musicEjb.showMusicsPlaylist(playlistSelected));
        return model;

    }

    /**
     * Invoques the Playlist EJB method editPlaylist(Playlist, User) ir order to
     * edit Playlist sent.
     *
     * @return String
     */
    public String editPlaylist() {
        playlistEjb.editPlaylist(playlistSelected, loggedUser.getUser());
        return "listMyPlaylist";
    }

    /**
     * Invokes Playlist EJB method in order to sort Playlist table.
     *
     * @param column
     * @param order
     * @return null;
     */
    public String sortTable(String column, String order) {

        DataModel model = (DataModel<Playlist>) new ListDataModel(playlistEjb.orderPlaylist(column, order, loggedUser.getUser()));
        play = model;
        return null;

    }

    /**
     * Invokes Playlist EJB method in order to show logged user playlists
     *
     * @return
     */
    public DataModel<Playlist> getMyPlaylist() {
        if (playlistEjb != null) {
            DataModel model = (DataModel<Playlist>) new ListDataModel(playlistEjb.showMyPlaylist(loggedUser.getUser()));
            play = model;
            return model;
        }
        return null;
    }

    /**
     * Invokes Playlist EJB method to remover Object
     *
     * @return
     */
    public String destroy() {
        Playlist playlist = (Playlist) this.play.getRowData();
        playlistEjb.deletePlaylist(playlist);
        getMyPlaylist();
        return null;
    }

    //Getter and Setter
    public PlaylistFacade getPlaylistEjb() {
        return playlistEjb;
    }

    public void setPlaylistEjb(PlaylistFacade playlistEjb) {
        this.playlistEjb = playlistEjb;
    }

    public Music getMusicSelected() {
        return musicSelected;
    }

    public void setMusicSelected(Music musicSelected) {
        this.musicSelected = musicSelected;
    }

    public Playlist getPlaylistSelected() {
        return playlistSelected;
    }

    public void setPlaylistSelected(Playlist playlistSelected) {
        this.playlistSelected = playlistSelected;
    }

    public MusicFacade getMusicEjb() {
        return musicEjb;
    }

    public void setMusicEjb(MusicFacade musicEjb) {
        this.musicEjb = musicEjb;
    }

    public LoggedUserMb getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(LoggedUserMb loggedUser) {
        this.loggedUser = loggedUser;
    }

    public DataModel<Playlist> getPlay() {
        return play;
    }

    public void setPlay(DataModel<Playlist> play) {
        this.play = play;
    }

}
