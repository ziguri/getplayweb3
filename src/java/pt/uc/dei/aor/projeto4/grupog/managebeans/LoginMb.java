/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.managebeans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.aor.projeto4.grupog.classes.EncryptMD5;
import pt.uc.dei.aor.projeto4.grupog.ejbs.AppUserFacade;
import pt.uc.dei.aor.projeto4.grupog.ejbs.MusicFacade;
import pt.uc.dei.aor.projeto4.grupog.entities.AppUser;
import pt.uc.dei.aor.projeto4.grupog.exceptions.DuplicateEmailException;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@Named
@RequestScoped
public class LoginMb {

    @Inject
    private AppUserFacade user_ejb;
    @Inject
    private MusicFacade musicFacade;
    private AppUser user;
    private String email;
    private String password;
    @Inject
    private LoggedUserMb logado;

    private String errorMessageExperience;
    private String successfullyRegistered;

    /**
     * Creates a new instance of LoginMb
     */
    public LoginMb() {
    }

    @PostConstruct
    public void init() {
        this.errorMessageExperience = null;
        this.successfullyRegistered = null;
    }

    /**
     * Confirm login. Receives email and password, invokes User EJB method to
     * validate the authentication. If the parent method validate the
     * credencials, return User if not, return null.
     *
     * @return
     * @throws pt.uc.dei.aor.projeto4.grupog.exceptions.DuplicateEmailException
     * @throws java.lang.InterruptedException
     */
    public String confirmaLogin() throws DuplicateEmailException, InterruptedException {

        String pass = EncryptMD5.cryptWithMD5(this.password);
        AppUser us = user_ejb.validaPassword(this.email, pass);

        try {

            logado.setUser(us);
            musicFacade.verifySoapLyric();
            musicFacade.verifyRestLyric();
            return "listAllMusics";

        } catch (NullPointerException ex) {
            Logger.getLogger(LoginMb.class.getName()).log(Level.SEVERE, null, ex);
            errorMessageExperience = "Login or Password Invalid. Please try again.";
            return "index";
        }
    }

    /**
     * Invokes User EJB method to remove logged user.
     *
     * @return
     */
    public String deleteUser() {
        user_ejb.remove(logado.getUser());
        return "index.xhtml";
    }

    /**
     * Invokes User EJB method in order to register user.
     */
    public void addUser() {
        try {
            user_ejb.addUser(user);
            successfullyRegistered = "Successfully Registered! Click \"return\" to login";
        } catch (DuplicateEmailException ex) {
            Logger.getLogger(LoginMb.class.getName()).log(Level.SEVERE, null, ex);
            errorMessageExperience = ex.getMessage();
        }
    }

    /**
     * Invokes User EJB method to verify if logged user exist.
     */
    public void verifyUser() {

        user_ejb.isLogged(logado.getUser());
    }

    //Getter an Setter
    /**
     * Get error message experience
     *
     * @return
     */
    public String getErrorMessageExperience() {
        return errorMessageExperience;
    }

    /**
     * Set error message experience
     *
     * @param errorMessageExperience
     */
    public void setErrorMessageExperience(String errorMessageExperience) {
        this.errorMessageExperience = errorMessageExperience;
    }

    /**
     * Get User EJB
     *
     * @return
     */
    public AppUserFacade getUser_ejb() {
        return user_ejb;
    }

    /**
     * Set User EJB
     *
     * @param user_ejb
     */
    public void setUser_ejb(AppUserFacade user_ejb) {
        this.user_ejb = user_ejb;
    }

    /**
     * Get email.
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set email.
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get password
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get logged user
     *
     * @return
     */
    public LoggedUserMb getLogado() {
        return logado;
    }

    /**
     * Set logged user
     *
     * @param logado
     */
    public void setLogado(LoggedUserMb logado) {
        this.logado = logado;
    }

    /**
     * Get User
     *
     * @return
     */
    public AppUser getUser() {
        if (user == null) {
            user = new AppUser();
        }
        return user;
    }

    /**
     * Set User
     *
     * @param user
     */
    public void setUser(AppUser user) {
        this.user = user;
    }

    /**
     * Get Successfully Registered Message
     *
     * @return
     */
    public String getSuccessfullyRegistered() {
        return successfullyRegistered;
    }

    /**
     * Set Successfully Registered Message
     *
     * @param successfullyRegistered
     */
    public void setSuccessfullyRegistered(String successfullyRegistered) {
        this.successfullyRegistered = successfullyRegistered;
    }

}
