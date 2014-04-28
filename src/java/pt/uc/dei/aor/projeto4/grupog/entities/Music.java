/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@Entity
@Table(name = "MUSIC")
@NamedQueries({
    @NamedQuery(name = "Music.findAll", query = "SELECT m FROM Music m"),
    @NamedQuery(name = "Music.findMusicById", query = "SELECT m FROM Music m WHERE m.music_id = :music_id"),
    @NamedQuery(name = "Music.findMusicByTitle", query = "SELECT m FROM Music m WHERE m.title LIKE :word"),
    @NamedQuery(name = "Music.findMusicByArtist", query = "SELECT m FROM Music m WHERE m.artist LIKE :word"),
    @NamedQuery(name = "Music.findMusicByTitleOrArtist", query = "SELECT m FROM Music m WHERE m.artist LIKE :word OR m.title LIKE :word"),
    @NamedQuery(name = "Music.findMusicByPlaylist", query = "SELECT m FROM Music m WHERE m.playlists = :playlists"),
    @NamedQuery(name = "Music.findAllFromUser", query = "SELECT m FROM Music m WHERE m.user = :user"),
    @NamedQuery(name = "Music.findMostPopularMusics", query = "SELECT m FROM Music m WHERE SIZE(m.playlists)>0 ORDER BY SIZE(m.playlists) DESC "),
    @NamedQuery(name = "Music.findRestLyric", query = "SELECT m.restLyric FROM Music m WHERE m.music_id=:mus"),
    @NamedQuery(name = "Music.findRestLyric", query = "SELECT m.soapLyric FROM Music m WHERE m.music_id=:mus"),})

public class Music implements Serializable, Comparable<Music> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer music_id;

    @NotNull
    @Size(max = 40, message = "Max size 40char")
    @Column(nullable = false, length = 40, name = "TITLE")
    private String title;

    @NotNull
    @Size(max = 40, message = "Max size 40char")
    @Column(nullable = false, length = 40, name = "ARTIST")
    private String artist;

    @NotNull
    @Size(max = 40, message = "Max size 40char")
    @Column(nullable = false, length = 40, name = "ALBUM")
    private String album;

    @NotNull
    @Column(nullable = false, name = "MUSIC_YEAR")
    private Integer music_year;

    @NotNull
    @Column(nullable = false, name = "MUSIC_PATH")
    private String music_path;

    @ManyToOne()
    private AppUser user;

    @ManyToMany(mappedBy = "musics")
    private List<Playlist> playlists;

    @Column(nullable = false, name = "SOAPLYRIC")
    private boolean soapLyric;

    @Column(nullable = false, name = "RESTLYRIC")
    private boolean restLyric;

    public Music() {
    }

    public Integer getId() {
        return music_id;
    }

    public void setId(Integer id) {
        this.music_id = id;
    }

    public Integer getMusic_id() {
        return music_id;
    }

    public void setMusic_id(Integer music_id) {
        this.music_id = music_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Integer getMusic_year() {
        return music_year;
    }

    public void setMusic_year(Integer music_year) {
        this.music_year = music_year;
    }

    public String getMusic_path() {
        return music_path;
    }

    public void setMusic_path(String music_path) {
        this.music_path = music_path;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public boolean isSoapLyric() {
        return soapLyric;
    }

    public void setSoapLyric(boolean soapLyric) {
        this.soapLyric = soapLyric;
    }

    public boolean isRestLyric() {
        return restLyric;
    }

    public void setRestLyric(boolean restLyric) {
        this.restLyric = restLyric;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (music_id != null ? music_id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the music_id fields are not set
        if (!(object instanceof Music)) {
            return false;
        }
        Music other = (Music) object;
        if ((this.music_id == null && other.music_id != null) || (this.music_id != null && !this.music_id.equals(other.music_id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistence.Music[ id=" + music_id + " ]";
    }

    @Override
    public int compareTo(Music o) {
        return o.getPlaylists().size() - this.playlists.size();
    }

}
