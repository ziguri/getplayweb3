package pt.uc.dei.aor.projeto4.grupog.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@Entity
@Table(name = "PLAYLIST")
@NamedQueries({
    @NamedQuery(name = "Playlist.findAll", query = "SELECT p FROM Playlist p"),
    @NamedQuery(name = "Playlist.findPlaylistById", query = "SELECT p FROM Playlist p WHERE p.id = :playlist_id"),
    @NamedQuery(name = "Playlist.findPlaylistByUser", query = "SELECT p FROM Playlist p WHERE p.user = :user"),
    @NamedQuery(name = "Playlist.OrderByTitleDesc", query = "SELECT p FROM Playlist p WHERE p.user = :user ORDER BY p.name DESC"),
    @NamedQuery(name = "Playlist.OrderByCreationDesc", query = "SELECT p FROM Playlist p WHERE p.user = :user ORDER BY p.creationDate DESC"),
    @NamedQuery(name = "Playlist.OrderBySizeDesc", query = "SELECT p FROM Playlist p WHERE p.user = :user ORDER BY p.size DESC"),
    @NamedQuery(name = "Playlist.OrderByTitleAsc", query = "SELECT p FROM Playlist p WHERE p.user = :user ORDER BY p.name ASC"),
    @NamedQuery(name = "Playlist.OrderByCreationAsc", query = "SELECT p FROM Playlist p WHERE p.user = :user ORDER BY p.creationDate ASC"),
    @NamedQuery(name = "Playlist.OrderBySizeAsc", query = "SELECT p FROM Playlist p WHERE p.user = :user ORDER BY p.size ASC"),})
public class Playlist implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer playlist_id;

    private String playlist_name;

    private Date creation_date;

    private Integer playlist_size;

    private List<Music> musics;

//    @NotNull
    private AppUser user;

    public Playlist() {
    }

    @PrePersist
    public void onCreate() {
        GregorianCalendar gc = new GregorianCalendar();
        this.setCreationDate(gc.getTime());
    }

    @Id
    @Column(name = "PLAYLIST_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return playlist_id;
    }

    public void setId(Integer id) {
        this.playlist_id = id;
    }

    @Column(name = "NAME")
    @NotNull
    @Size(max = 40)
    public String getName() {
        return playlist_name;
    }

    public void setName(String playlist_name) {
        this.playlist_name = playlist_name;
    }

    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE")
    public Date getCreationDate() {
        return creation_date;
    }

    public void setCreationDate(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Playlist(Integer playlist_id, String playlist_name, Date creation_date, Integer playlist_size, List<Music> musics, AppUser user) {
        this.playlist_id = playlist_id;
        this.playlist_name = playlist_name;
        this.creation_date = creation_date;
        this.musics = musics;
        this.user = user;
    }

    @Column(name = "PLAYLIST_SIZE")
    public Integer getSize() {
        if (musics == null) {
            return 0;
        }
        return musics.size();
    }

    public void setSize(Integer s) {
        this.playlist_size = s;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "playlists_has_musics")
    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(ArrayList<Music> musics) {
        this.musics = musics;
    }

    @ManyToOne
    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (playlist_id != null ? playlist_id.hashCode() : 0);
        return hash;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the playlist_id fields are not set
        if (!(object instanceof Playlist)) {
            return false;
        }
        Playlist other = (Playlist) object;
        return (this.playlist_id != null || other.playlist_id == null) && (this.playlist_id == null || this.playlist_id.equals(other.playlist_id));
    }

    @Override
    public String toString() {
        return "persistence.Playlist[ id=" + playlist_id + " ]";
    }

}
