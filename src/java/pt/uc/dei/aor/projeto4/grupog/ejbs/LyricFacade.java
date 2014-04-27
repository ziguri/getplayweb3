/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pt.uc.dei.aor.projeto4.grupog.entities.AppUser;
import pt.uc.dei.aor.projeto4.grupog.entities.Lyric;
import pt.uc.dei.aor.projeto4.grupog.entities.Music;

/**
 *
 * @author Zueb LDA
 */
@Stateless
public class LyricFacade extends AbstractFacade<Lyric> {

    @PersistenceContext(unitName = "GetPlayWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LyricFacade() {
        super(Lyric.class);
    }

    /**
     * Return the Lyric in database table
     *
     * @param m
     * @param u
     * @return
     */
    public String getLyric(Music m, AppUser u) {

        Query q = em.createNamedQuery("appuser.findLyric");
        q.setParameter("mus", m).setParameter("us", u);

        try {
            return (String) q.getSingleResult();
        } catch (NoResultException e) {
            return "";
        }

    }

    public Lyric getObjectLyric(Music m, AppUser u) {

        Query q = em.createNamedQuery("appuser.findObjectLyric");
        q.setParameter("mus", m).setParameter("us", u);

        try {
            return (Lyric) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void addLyric(String lyric, Music m, AppUser u) {

        Lyric l = new Lyric();
        l.setFullLyric(lyric);
        l.setMusic(m);
        l.setUser(u);
        this.create(l);
    }

    public void editLyric(Lyric l, String fullLyric) {

        l.setFullLyric(fullLyric);
        this.edit(l);
    }

}
