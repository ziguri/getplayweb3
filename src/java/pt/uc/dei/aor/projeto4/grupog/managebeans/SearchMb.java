/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.managebeans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.aor.projeto4.grupog.ejbs.MusicFacade;
import pt.uc.dei.aor.projeto4.grupog.entities.Music;
import pt.uc.dei.aor.projeto4.grupog.exceptions.SearchNullException;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@Named("searchMb")
@RequestScoped
public class SearchMb implements Serializable {

    @Inject
    private MusicFacade musics_ejb;
    private String word;
    private String option;
    private DataModel<Music> musics;
    private FacesMessage message;

    private DataModel<Music> model;

    private String errorMessage;

    /**
     * Creates a new instance of SearchMb
     */
    public SearchMb() {
    }

    @PostConstruct
    public void init() {
        this.musics = null;
        this.message = new FacesMessage();
    }

    /**
     * Receives words and search criteria and invoke Music EJB to fulfill the
     * complete search returnig DataModel<Music>
     *
     * @return DataModel<Music>
     */
    public String search() {

        try {
            List<Music> results = musics_ejb.searchByColumn(option, word);
            this.model = (DataModel<Music>) new ListDataModel(results);
            Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
            flash.put("model", model);
            flash.put("word", word);

        } catch (SearchNullException ex) {
            Logger.getLogger(SearchMb.class.getName()).log(Level.SEVERE, null, ex);
            errorMessage = ex.getMessage();
            return null;
        }

        return "search";
    }

    //Getter and Setter
    /**
     * Get DataModel<Music>
     *
     * @return
     */
    public DataModel<Music> getModel() {
        return model;
    }

    /**
     * Set DataModel<Music>
     *
     * @param model
     */
    public void setModel(DataModel<Music> model) {
        this.model = model;
    }

    /**
     * Get MusicFacade
     *
     * @return
     */
    public MusicFacade getMusics_ejb() {
        return musics_ejb;
    }

    /**
     * Set MusicFacade
     *
     * @param musics_ejb
     */
    public void setMusics_ejb(MusicFacade musics_ejb) {
        this.musics_ejb = musics_ejb;
    }

    /**
     * Get word
     *
     * @return
     */
    public String getWord() {
        return word;
    }

    /**
     * Set word
     *
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Get DataModel<Music>
     *
     * @return
     */
    public DataModel<Music> getMusics() {
        return musics;
    }

    /**
     * Set DataModel<Music>
     *
     * @param musics
     */
    public void setMusics(DataModel<Music> musics) {
        this.musics = musics;
    }

    /**
     * Get Option
     *
     * @return
     */
    public String getOption() {
        return option;
    }

    /**
     * Set Option
     *
     * @param opcion
     */
    public void setOption(String opcion) {
        this.option = opcion;
    }

    /**
     * Get message
     *
     * @return
     */
    public FacesMessage getMessage() {
        return message;
    }

    /**
     * Set message
     *
     * @param message
     */
    public void setMessage(FacesMessage message) {
        this.message = message;
    }

    /**
     * Get error message
     *
     * @return
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set error message
     *
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
