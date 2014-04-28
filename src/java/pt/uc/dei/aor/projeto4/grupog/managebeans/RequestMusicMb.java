/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.managebeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import pt.uc.dei.aor.projeto4.grupog.ejbs.LyricFacade;
import pt.uc.dei.aor.projeto4.grupog.ejbs.MusicFacade;
import pt.uc.dei.aor.projeto4.grupog.entities.Music;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@Named("requestMusicMb")
@RequestScoped
public class RequestMusicMb implements Serializable {

    @Inject
    private MusicFacade music_ejb;
    private DataModel<Music> items = null;
    private Music music;
    private String pathToSave;
    @Inject
    private LoggedUserMb user;
    private int selectedItemIndex;
    private DataModel<Music> musics;
    private String musicPath;
    private Part file1;
    private String message;
    private GregorianCalendar gc;
    private String url;
    private String urlTop;
    private List<Music> musics2;
    @Inject
    private LyricFacade lyricFacade;

    /**
     * Creates a new instance of MusicMb
     */
    public RequestMusicMb() {

    }

    @PostConstruct
    public void init() {
        this.music = new Music();
        musics2 = music_ejb.showMostPopularMusics();
        this.musics = null;
        this.message = null;
        gc = new GregorianCalendar();
        FacesContext ctxt = FacesContext.getCurrentInstance();
        ExternalContext ext = ctxt.getExternalContext();
        this.url = "http://" + ext.getRequestServerName() + ":" + ext.getRequestServerPort()
                + ext.getApplicationContextPath() + "/topmusic?topten=all&" + randomint();

        this.urlTop = "http://" + ext.getRequestServerName() + ":" + ext.getRequestServerPort()
                + ext.getApplicationContextPath() + "/topmusic?topten=topten";
    }

    public int randomint() {
        Random r = new Random();
        return r.nextInt();
    }

    /**
     * addMusic() create one directory in the server (if don´t exist), than copy
     * the byte[] to the new location path and invoke the method addMusic from
     * Music EJB, in order to add the music reference to the DB.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String addMusic() throws FileNotFoundException, IOException {

        try {

            File file = new File("C:\\APPGetPlayWeb\\");
            file.mkdir();
            musicPath = "C:\\APPGetPlayWeb\\" + file1.getSubmittedFileName();
            InputStream inputStream = file1.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(musicPath);

            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while (true) {
                bytesRead = inputStream.read(buffer);
                if (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                } else {
                    break;
                }
            }
            outputStream.close();
            inputStream.close();

            boolean rest = lyricFacade.restExist(music.getArtist(), music.getTitle());
            boolean soap = lyricFacade.soapExist(music.getArtist(), music.getTitle());

            music_ejb.addMusic(music, user.getUser(), musicPath, rest, soap);

            message = "Music " + music.getTitle() + " created with success.";

        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
            message = "File not found. Please try again";

        } catch (IOException ex2) {
            System.err.println(ex2.getMessage());
        }
        return "createMusic";
    }

    /**
     * Invoke Music EJB method that return one List<Music>
     *
     * @return List<Music>
     */
    public List<Music> viewAllMusic() {
        return music_ejb.showAllMusics();

    }

    /**
     * Invoke Music EJB method in order to fill one Music DataModel
     *
     * @return DataModel<Music>
     */
    public DataModel<Music> getMusicList() {

        List<Music> list = music_ejb.findAll();
        DataModel model = (DataModel<Music>) new ListDataModel(list);
        musics = model;

        return musics;
    }

    /**
     * Invoke Music EJB method in order to fill Music DataModel with logged user
     * musics.
     *
     * @return DataModel<Music>
     */
    public DataModel<Music> getMyMusicList() {

        DataModel model = (DataModel<Music>) new ListDataModel(music_ejb.showUserMusics(user.getUser()));
        return model;
    }

    /**
     * Invoke the Music EJB method with item count.
     *
     * @return
     */
    public int countAllItens() {
        return music_ejb.count();
    }

    /**
     * Create a new Instance of object.
     *
     * @return Music
     */
    public Music getMusicSelected() {
        if (music == null) {
            music = new Music();
            selectedItemIndex = -1;
        }
        return music;
    }

//Getter and Setter
    /**
     * Get Music EJB
     *
     * @return
     */
    public MusicFacade getMusic_ejb() {
        return music_ejb;
    }

    /**
     * Set Get Music EJB
     *
     * @param music_ejb
     */
    public void setMusic_ejb(MusicFacade music_ejb) {
        this.music_ejb = music_ejb;
    }

    /**
     * Get Music
     *
     * @return
     */
    public Music getMusic() {
        if (music == null) {
            music = new Music();
            music.setUser(user.getUser());
        }
        return music;
    }

    /**
     * Set Music
     *
     * @param music
     */
    public void setMusic(Music music) {
        this.music = music;
    }

    /**
     * Get DataModel
     *
     * @return
     */
    public DataModel getItems() {
        return items;
    }

    /**
     * Set DataModel
     *
     * @param items
     */
    public void setItems(DataModel items) {
        this.items = items;
    }

    /**
     * Get Path to save
     *
     * @return
     */
    public String getPathToSave() {
        return pathToSave;
    }

    /**
     * Set Path to save
     *
     * @param pathToSave
     */
    public void setPathToSave(String pathToSave) {
        this.pathToSave = pathToSave;
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
     * Get selected item index
     *
     * @return
     */
    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    /**
     * Set selected item index
     *
     * @param selectedItemIndex
     */
    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    /**
     * Get File1
     *
     * @return
     */
    public Part getFile1() {
        return file1;
    }

    /**
     * Set File1
     *
     * @param file1
     */
    public void setFile1(Part file1) {
        this.file1 = file1;
    }

    /**
     * Get DataModel<Music> musics
     *
     * @return
     */
    public DataModel<Music> getMusics() {
        return musics;
    }

    /**
     * Set DataModel<Music> musics
     *
     * @param musics
     */
    public void setMusics(DataModel<Music> musics) {
        this.musics = musics;
    }

    /**
     * Get Music Path
     *
     * @return
     */
    public String getMusicPath() {
        return musicPath;
    }

    /**
     * Set Music Path
     *
     * @param musicPath
     */
    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    /**
     * Get Message
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set Message
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Music> getMusics2() {
        musics2 = music_ejb.showMostPopularMusics();
        return musics2;
    }

    public void setMusics2(List<Music> musics2) {
        this.musics2 = musics2;
    }

    public String getUrlTop() {
        return urlTop;
    }

    public void setUrlTop(String urlTop) {
        this.urlTop = urlTop;
    }

    public LyricFacade getLyricFacade() {
        return lyricFacade;
    }

    public void setLyricFacade(LyricFacade lyricFacade) {
        this.lyricFacade = lyricFacade;
    }

}
