/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import pt.uc.dei.aor.projeto4.grupog.exceptions.DuplicateEmailException;
import pt.uc.dei.aor.projeto4.grupog.ejbs.AppUserFacade;
import pt.uc.dei.aor.projeto4.grupog.entities.AppUser;
import javax.ejb.embeddable.EJBContainer;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Elsa Santos
 * @author Orlando Neves
 */

public class AppUserFacadeTest {

    static EJBContainer container;
    static AppUserFacade facade;

    public AppUserFacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        facade = (AppUserFacade) container.getContext().lookup("java:global/classes/AppUserFacade");
    }

    @AfterClass
    public static void tearDownClass() {
        container.close();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addUser method, of class AppUserFacade.
     * @throws Exceptions.DuplicateEmailException
     */
    @Test
    public void testAddUser2() throws DuplicateEmailException {
        System.out.println("TesteAddUser");

        AppUser u = new AppUser("testeName", "teste@Email.com", "testePass111");

        facade.create(u);
        AppUser result = facade.existUser(u.getEmail());

        assertEquals(u.getEmail(), result.getEmail());
        assertEquals(u.getName(), result.getName());

        facade.remove(u);
    }

    /**
     * Test of existUser2 method, of class AppUserFacade.
     * @throws Exceptions.DuplicateEmailException
     */
    @Test
    public void testExistUser2() throws DuplicateEmailException {
        System.out.println("testGetUserByEmail");

        String expectedEmail = "teste@teste.com";
        AppUser u = new AppUser("teste", expectedEmail, "testeste");

        facade.create(u);

        AppUser result = facade.existUser(expectedEmail);
        assertEquals(u, result);

        facade.remove(u);
    }

    /**
     * Test of validaPassword method, of class AppUserFacade.
     * @throws java.lang.Exception
     */
    @Test
    public void testValidaPassword() throws Exception {
        System.out.println("validUserPassword");

        String email = "teste@teste.com";
        AppUser u = new AppUser("teste", email, "testeste");

        facade.create(u);
        AppUser result = facade.validaPassword(email, "testeste");
        assertNotNull(result);
        facade.remove(u);
    }

}
