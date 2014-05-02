/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.ejbs;

import WebServiceSoap.LyricWikiPortType_Stub;
import WebServiceSoap.LyricWiki_Impl;
import WebServiceSoap.LyricsResult;
import chartLyricsWebService.Apiv1;
import chartLyricsWebService.GetLyricResult;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.WebServiceRef;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pt.uc.dei.aor.projeto4.grupog.entities.AppUser;
import pt.uc.dei.aor.projeto4.grupog.entities.Lyric;
import pt.uc.dei.aor.projeto4.grupog.entities.Music;
import pt.uc.dei.aor.projeto4.grupog.managebeans.GeneralController;

/**
 *
 * @author Zueb LDA
 */
@Stateless
public class LyricFacade extends AbstractFacade<Lyric> {

    @PersistenceContext(unitName = "GetPlayWebPU3")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/api.chartlyrics.com/apiv1.asmx.wsdl")
    private Apiv1 service;

    private String artist;
    private String title;

    public LyricFacade() {
        super(Lyric.class);
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Return the Lyric in database table
     *
     * @param m
     * @param u
     * @return
     */
    public String getLyric(Music m, AppUser u) {

        Query q = em.createNamedQuery("lyric.findLyric");
        q.setParameter("mus", m).setParameter("us", u);

        try {
            return (String) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * Return the Object Lyric from database
     *
     * @param m
     * @param u
     * @return
     */
    public Lyric getObjectLyric(Music m, AppUser u) {

        Query q = em.createNamedQuery("lyric.findObjectLyric");
        q.setParameter("mus", m).setParameter("us", u);

        try {
            return (Lyric) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Return all the Lyric´s with that music reference
     *
     * @param m
     * @return
     */
    public List<Lyric> getAllLyricsOfMusic(Music m) {

        Query q = em.createNamedQuery("lyric.findLyricByMusic");
        q.setParameter("mus", m);
        return (List<Lyric>) q.getResultList();
    }

    /**
     * Remove all Lyric entries with music reference
     *
     * @param m
     */
    public void deleteLyricByMusic(Music m) {

        Query q = em.createNamedQuery("lyric.deleteLyricByMusic");
        q.setParameter("mus", m);
    }

    /**
     * Add lyric to database when
     *
     * @param lyric
     * @param m
     * @param u
     */
    public void addLyric(String lyric, Music m, AppUser u) {

        Lyric l = new Lyric();
        m.setRestLyric(true);
        m.setSoapLyric(true);

        l.setFullLyric(lyric);
        l.setMusic(m);
        l.setUser(u);
        this.create(l);
    }

    public void editLyric(Lyric l, String fullLyric) {

        l.setFullLyric(fullLyric);
        this.edit(l);
    }

    private static LyricWikiPortType_Stub createProxy() {
        return (LyricWikiPortType_Stub) (new LyricWiki_Impl().getLyricWikiPort());
    }

    /**
     * Return the result using SOAP web service.
     *
     * @param artist
     * @param m
     * @param title
     * @return
     */
    public String soapResult(String artist, String title) {

        try {
            LyricWikiPortType_Stub lw = createProxy();
            LyricsResult lr = lw.getSong(artist, title);
            return lr.getLyrics();
        } catch (Exception ex) {
            Logger.getLogger(GeneralController.class.getName()).log(Level.SEVERE, null, ex);
            return "Lyric not found in http://lyrics.wikia.com/";
        }

    }

    public String restResult(String artist, String title) {

        WebTarget target = ClientBuilder.newClient().target("http://lyrics.wikia.com/api.php");
        Invocation invocation = target.queryParam("func", "getSong").queryParam("fmt", "text")
                .queryParam("artist", artist).queryParam("song", title).request(MediaType.TEXT_PLAIN).buildGet();
        Response response = invocation.invoke();
        return response.readEntity(String.class);

    }

    private GetLyricResult searchLyricDirect(String artist, String title) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        chartLyricsWebService.Apiv1Soap port = service.getApiv1Soap();
        this.artist = artist;
        this.title = title;
        return port.searchLyricDirect(artist, title);
    }

    public String chartSoapResult(String artist, String title) {

        return searchLyricDirect(artist, title).getLyric();
    }

    /**
     * Find and return lyric using the rest webservice in chartlyrics
     *
     * @param m
     * @return
     * @throws XPathExpressionException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public String chartRestLyric(Music m) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {

//        try {
        String xml = ClientBuilder.newClient().target("http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect")
                .queryParam("artist", m.getArtist())
                .queryParam("song", m.getTitle())
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);

        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(source);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        String status = xpath.evaluate("/GetLyricResult/Lyric", document);

        return status;
//        } catch (Exception e) {
//
//            return "Please wait 10 sec´s";
//        }
    }

    public boolean chartRestExist(Music m) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {

        String s = chartRestLyric(m);
        return !s.isEmpty();
    }

    public boolean soapExist(String artist, String title) {
        //String s = soapResult(artist, title); Método para verificar no wikia 
        String s = chartSoapResult(artist, title);
        return !s.isEmpty();
    }

    public boolean soapExistDb(String artist, String title) {
        String s = soapResult(artist, title);
        //String s = chartSoapResult(artist, title);
        return !s.isEmpty();
    }

    public boolean restExist(String artist, String title) {
        String s = restResult(artist, title);
        return !s.equals("Not found");
    }

}
