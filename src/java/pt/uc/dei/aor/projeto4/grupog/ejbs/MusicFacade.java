/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.ejbs;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pt.uc.dei.aor.projeto4.grupog.entities.AppUser;
import pt.uc.dei.aor.projeto4.grupog.entities.Music;
import pt.uc.dei.aor.projeto4.grupog.entities.Playlist;
import pt.uc.dei.aor.projeto4.grupog.exceptions.SearchNullException;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@Stateless
public class MusicFacade extends AbstractFacade<Music> {

    @Inject
    private PlaylistFacade playlistEjb;
    @PersistenceContext(unitName = "GetPlayWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MusicFacade() {
        super(Music.class);
    }

    /**
     * Receives Music, User and path in order to create and persist new music.
     *
     * @param m
     * @param u
     * @param path
     */
    public void addMusic(Music m, AppUser u, String path) {
        try {
            m.setMusic_path(path);
            m.setUser(u);
            this.create(m);

        } catch (Exception e) {
            Logger.getLogger(MusicFacade.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Find and returns all musics in the application.
     *
     * @return
     */
    public List<Music> showAllMusics() {
        try {
            List<Music> m = (List<Music>) em.createNamedQuery("Music.findAll").getResultList();
            return m;
        } catch (NullPointerException | IllegalStateException ex) {
            Logger.getLogger(MusicFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Receive Playlist as a parameter, find and return List<Music> with the all
     * musics of the playlist.
     *
     * @param p
     * @return
     */
    public List<Music> showMusicsPlaylist(Playlist p) {

        try {
            List<Music> m = (List<Music>) em.createNamedQuery("Music.findMusicByPlaylist").setParameter("playlists", p).getResultList();
            return m;
        } catch (NullPointerException | IllegalStateException ex) {
            Logger.getLogger(MusicFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Receives one music in order to remove. First remove the music from the
     * existent playlist, then remove the music from data base and finally
     * removes the file from host.
     *
     * @param music
     */
    public void removeMusic(Music music) {
        try {

            for (int i = 0; i < music.getPlaylists().size(); i++) {

                music.getPlaylists().get(i).setSize(music.getPlaylists().get(i).getSize() - 1);
                playlistEjb.edit(music.getPlaylists().get(i));
            }

            remove(music);

            File file = new File(music.getMusic_path());
            file.delete();

        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(MusicFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Receives word and column as a parameter in order to set the named querys
     * that will order tables. Return the recultant List<Music>
     *
     * @param column
     * @param word
     * @return
     * @throws SearchNullException
     */
    public List<Music> searchByColumn(String column, String word) throws SearchNullException {//Mostra as músicas resultantes de uma pesquisa.

        List<Music> m = null;
        try {

            if (column.equals("Title")) {
                m = (List<Music>) em.createNamedQuery("Music.findMusicByTitle").setParameter("word", "%" + word + "%").getResultList();
            }

            if (column.equals("Artist")) {
                m = (List<Music>) em.createNamedQuery("Music.findMusicByArtist").setParameter("word", "%" + word + "%").getResultList();
            }

            if (column.equals("ArTi")) {
                m = (List<Music>) em.createNamedQuery("Music.findMusicByTitleOrArtist").setParameter("word", "%" + word + "%").getResultList();
            }

            if (m.isEmpty()) {
                throw new SearchNullException();
            }
            return m;

        } catch (NullPointerException | IllegalStateException ex) {
            Logger.getLogger(MusicFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     * Receives one user and return List<Music> with the user musics.
     *
     * @param u
     * @return
     */
    public List<Music> showUserMusics(AppUser u) {

        try {
            List<Music> mus = (List<Music>) em.createNamedQuery("Music.findAllFromUser").setParameter("user", u).getResultList();
            return mus;
        } catch (NullPointerException | IllegalStateException ex) {
            Logger.getLogger(MusicFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * most popular musics
     */
    public List<Music> showMostPopularMusics() {

        try {
            List<Music> m = (List<Music>) em.createNamedQuery("Music.findMostPopularMusics").getResultList();
            return m;
        } catch (NullPointerException | IllegalStateException ex) {
            Logger.getLogger(MusicFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * most popular musics
     * @return 
     */
    public List<Music> showTopTenPopularMusics() {

        try {
            List<Music> m = (List<Music>) em.createNamedQuery("Music.findMostPopularMusics").setMaxResults(10).getResultList();
            return m;
        } catch (NullPointerException | IllegalStateException ex) {
            Logger.getLogger(MusicFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
