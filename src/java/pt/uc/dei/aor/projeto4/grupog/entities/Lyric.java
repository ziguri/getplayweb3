/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Zueb LDA
 */
@Entity
@Table(name = "lyric")
@NamedQueries({
    @NamedQuery(name = "lyric.findLyric", query = "SELECT l.fullLyric FROM Lyric l WHERE l.music=:mus and l.user=:us"),
    @NamedQuery(name = "lyric.findObjectLyric", query = "SELECT l FROM Lyric l WHERE l.music=:mus and l.user=:us"),
    @NamedQuery(name = "lyric.findLyricByMusic", query = "SELECT l FROM Lyric l WHERE l.music=:mus"),
    @NamedQuery(name = "lyric.deleteLyricByMusic", query = "DELETE FROM Lyric l WHERE l.music=:mus"),})
public class Lyric implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private AppUser user;

    @ManyToOne
    private Music music;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length = 100000)
    private String fullLyric;

    public Lyric() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullLyric() {
        return fullLyric;
    }

    public void setFullLyric(String fullLyric) {
        this.fullLyric = fullLyric;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lyric)) {
            return false;
        }
        Lyric other = (Lyric) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pt.uc.dei.aor.projeto4.grupog.entities.Lyric[ id=" + id + " ]";
    }

}
