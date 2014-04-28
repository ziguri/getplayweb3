/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.ejbs;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pt.uc.dei.aor.projeto4.grupog.entities.AppUser;
import pt.uc.dei.aor.projeto4.grupog.entities.Music;
import pt.uc.dei.aor.projeto4.grupog.entities.Playlist;
import pt.uc.dei.aor.projeto4.grupog.exceptions.MusicsAlreadyExistInPlaylistException;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@Stateless
public class PlaylistFacade extends AbstractFacade<Playlist> {

    @PersistenceContext(unitName = "GetPlayWebPU3")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlaylistFacade() {
        super(Playlist.class);
    }

    /**
     * Receives User and Playlist as parameter in order to add new Playlist to
     * the system.
     *
     * @param p
     * @param u
     */
    public void addPlaylist(Playlist p, AppUser u) {
        try {
            p.setUser(u);
            this.create(p);

        } catch (Exception e) {
            Logger.getLogger(PlaylistFacade.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Receives Playlist and User as parameter in order to edit existent
     * playlist.
     *
     * @param p
     * @param u
     */
    public void editPlaylist(Playlist p, AppUser u) {
        try {
            p.setUser(u);
            this.edit(p);
        } catch (Exception e) {
            Logger.getLogger(PlaylistFacade.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    /**
     * Receives Playlist and Music as parameter, check if the music exist in
     * playlist and return boolean with the result.
     *
     * @param p
     * @param m
     * @return
     */
    public boolean musicExistInPlaylist(Playlist p, Music m) {
        boolean igual = false;

        for (int i = 0; i < p.getMusics().size() && !igual; i++) {
            if (p.getMusics().get(i).equals(m)) {
                igual = true;
            }
        }
        return igual;
    }

    /**
     * Receive Playlist and Music as parameter in order to add new music to the
     * system.
     *
     * @param playlist
     * @param music
     * @throws MusicsAlreadyExistInPlaylistException
     */
    public void addMusicToPlaylist(Playlist playlist, Music music) throws MusicsAlreadyExistInPlaylistException {

        boolean equal = musicExistInPlaylist(playlist, music);
        try {
            if (equal) {
                throw new MusicsAlreadyExistInPlaylistException();
            } else {
                playlist.getMusics().add(music);
                edit(playlist);
                music.getPlaylists().add(playlist);
                em.merge(music);
            }

        } catch (NullPointerException | IllegalArgumentException | IllegalStateException ex) {
            Logger.getLogger(PlaylistFacade.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Receives Playlist and music as parameter in order to remove that same
     * music from the playlist.
     *
     * @param playlist
     * @param music
     */
    public void removeMusicFromPlaylist(Playlist playlist, Music music) {
        try {
            playlist.getMusics().remove(music);
            edit(playlist);
            music.getPlaylists().remove(playlist);
            em.merge(music);
        } catch (NullPointerException | UnsupportedOperationException ex) {
            Logger.getLogger(PlaylistFacade.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Receives User as paramente and return List<Playlist> with all user
     * playlists.
     *
     * @param u
     * @return
     */
    public List<Playlist> showMyPlaylist(AppUser u) {

        try {

            List<Playlist> pl = (List<Playlist>) em.createNamedQuery("Playlist.findPlaylistByUser").setParameter("user", u).getResultList();
            return pl;

        } catch (NullPointerException | IllegalStateException ex) {
            Logger.getLogger(PlaylistFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Receives Playlist and return List<Music> with all the music from the
     * playlist.
     *
     * @param p
     * @return
     */
    public List<Music> showMusicPlaylist(Playlist p) {

        try {

            List<Music> m = (List<Music>) em.createNamedQuery("Music.findMusicByPlaylist").setParameter("playlist", p).getResultList();
            return m;
        } catch (NullPointerException | IllegalStateException ex) {
            Logger.getLogger(PlaylistFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Receives column, order and user and return sorted List<Playlist> with the
     * result.
     *
     * @param column
     * @param order
     * @param u
     * @return
     */
    public List<Playlist> orderPlaylist(String column, String order, AppUser u) {
        try {
            String namequery = "Playlist.OrderBy" + column + order;
            List<Playlist> pl = (List<Playlist>) em.createNamedQuery(namequery).setParameter("user", u).getResultList();
            return pl;
        } catch (NullPointerException | IllegalStateException ex) {
            Logger.getLogger(PlaylistFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void deletePlaylist(Playlist p) {
        try {
            for (Music m : p.getMusics()) {
                m.getPlaylists().remove(p);
                em.merge(m);
            }

            remove(p);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRRROOOOO");
        }
    }

}
