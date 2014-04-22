/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.managebeans;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.aor.projeto4.grupog.ejbs.MusicFacade;
import pt.uc.dei.aor.projeto4.grupog.ejbs.PlaylistFacade;
import pt.uc.dei.aor.projeto4.grupog.entities.Music;
import pt.uc.dei.aor.projeto4.grupog.entities.Playlist;
import pt.uc.dei.aor.projeto4.grupog.exceptions.MusicsAlreadyExistInPlaylistException;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@Named("requestPlaylistMb")
@RequestScoped
public class RequestPlaylistMb {

    @Inject
    private PlaylistFacade playlist_ejb;
    @Inject
    private LoggedUserMb user;
    private DataModel<Playlist> play;
    private Playlist playlist;
    private String message;
    @Inject
    private MusicFacade musicFacade;

    /**
     * Creates a new instance of PlaylistMB
     */
    public RequestPlaylistMb() {
    }

    @PostConstruct
    public void init() {
        this.playlist = new Playlist();
        this.message = null;
    }

    /**
     * Invokes the method addPlaylist from Playlist EJB in order to add a new
     * playlist.
     */
    public void addPlaylist() {
        if (!this.playlist.getName().equals("")) {
            playlist_ejb.addPlaylist(playlist, user.getUser());
            this.message = "Playlist " + playlist.getName() + " create with success.";

        } else {
            this.message = "Invalid Name. Please insert a name for your playlist.";

        }
    }

    /**
     * Invokes the method showMyPlaylist from PLaylist EJB in order to return
     * DataModel with logged user playlists.
     *
     * @return
     */
    public DataModel<Playlist> getMyPlaylist() {
        if (playlist_ejb != null) {
            DataModel model = (DataModel<Playlist>) new ListDataModel(playlist_ejb.showMyPlaylist(user.getUser()));
            play = model;
            return model;
        }
        return null;
    }
    
//    public List<Music> topTen(){
//        
//    }

    public void createPlaylistFromServleTopTen() throws MusicsAlreadyExistInPlaylistException{
        addPlaylist();
        List<Music> musicstop = musicFacade.showTopTenPopularMusics();
        for (Music m: musicstop){
            playlist_ejb.addMusicToPlaylist(playlist, m);
        } 
    }
    
    //Getter and Setter

    /**
     * Get Playlist EJB
     *
     * @return
     */
    public PlaylistFacade getPlaylist_ejb() {
        return playlist_ejb;
    }

    /**
     * Set Playlist EJB
     *
     * @param playlist_ejb
     */
    public void setPlaylist_ejb(PlaylistFacade playlist_ejb) {
        this.playlist_ejb = playlist_ejb;
    }

    /**
     * Get Playlist
     *
     * @return
     */
    public Playlist getPlaylist() {
        return playlist;
    }

    /**
     * Set Playlist
     *
     * @param playlist
     */
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    /**
     * Get logged user
     *
     * @return
     */
    public LoggedUserMb getUser() {
        return user;
    }

    /**
     * Set logged user
     *
     * @param user
     */
    public void setUser(LoggedUserMb user) {
        this.user = user;
    }

    /**
     * Get DataModel<Playlist>
     *
     * @return
     */
    public DataModel<Playlist> getPlay() {
        return play;
    }

    /**
     * Set DataModel<Playlist>
     *
     * @param play
     */
    public void setPlay(DataModel<Playlist> play) {
        this.play = play;
    }

    /**
     * Get message
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set message
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
