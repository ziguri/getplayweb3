/*AppUserFacade
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.ejbs;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import pt.uc.dei.aor.projeto4.grupog.classes.EncryptMD5;
import pt.uc.dei.aor.projeto4.grupog.entities.AppUser;
import pt.uc.dei.aor.projeto4.grupog.exceptions.DuplicateEmailException;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@Stateless
public class AppUserFacade extends AbstractFacade<AppUser> {

    @PersistenceContext(unitName = "GetPlayWebPU3")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AppUserFacade() {
        super(AppUser.class);
    }

    /**
     * Receives User as parameter in order to verify if there is another user
     * with the same e-mail. If not, creates a new user, else
     *
     * @param u
     * @throws DuplicateEmailException
     */
    public void addUser(AppUser u) throws DuplicateEmailException {
        String pass = EncryptMD5.cryptWithMD5(u.getPassword());
        u.setPassword(pass);
        this.existUser(u.getEmail());
        this.create(u);

    }

    /**
     * Receives logged user in order to edit.
     *
     * @param u
     */
    public void editUserLogado(AppUser u) {

        String pass = EncryptMD5.cryptWithMD5(u.getPassword());
        u.setPassword(pass);
        this.edit(u);
    }

    /**
     * Receives one email as parameter, in order to check if the email is
     * already registered. If yes, returns null, else, returns user.
     *
     * @param email
     * @return
     */
    public AppUser existUser(String email) {

        try {
            AppUser u = (AppUser) em.createNamedQuery("appuser.findByEmail").setParameter("email", email).getSingleResult();
            return u;
        } catch (Exception e) {

            return null;
        }
    }

    /**
     * Receives one email as parameter, in order to check if the email is
     * already registered. If yes, returns null, else, returns user.
     *
     * @param email
     * @return
     */
    public AppUser existUser2(String email) throws DuplicateEmailException {
        try {
            AppUser u = (AppUser) em.createNamedQuery("appuser.findByEmail").setParameter("email", email).getSingleResult();
            throw new DuplicateEmailException();
        } catch (NoResultException ex) {
            Logger.getLogger(AppUserFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     * Receives email and password in order to validate the login for the
     * existent user.
     *
     * @param email
     * @param password
     * @return
     */
    public AppUser validaPassword(String email, String password) {

        AppUser u = existUser(email);

        if (u != null && password.equals(u.getPassword())) {

            return u;
        } else {

            return null;
        }
    }

    /**
     * Check if the receive user is valid or null, if null redirect page
     * automatically to index, if not null the user keep going with navigation.
     *
     * @param user
     */
    public void isLogged(AppUser user) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();

        if (user == null) {
            nav.performNavigation("index?faces-redirect=true");
        }

    }

}
