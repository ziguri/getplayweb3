/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.projeto4.grupog.ejbs;

import java.io.File;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import pt.uc.dei.aor.projeto4.grupog.entities.AppUser;
import pt.uc.dei.aor.projeto4.grupog.entities.Playlist;

/**
 * @author Elsa Santos
 * @author Pedro Pamplona
 */
@Stateful
public class DeleteUser {

    @Inject
    PlaylistFacade playlists;
    @EJB
    AppUserFacade users;

    /**
     * Method to safely remove user from application. First remove all the
     * reference from the user in other tables, and then removes the user
     * himself.
     *
     * @param user
     */
    public void userRemove(AppUser user) {

        List<Playlist> p = playlists.findAll();

        for (int i = 0; i < p.size(); i++) {
            for (int j = 0; j < p.get(i).getMusics().size(); j++) {
                if (p.get(i).getMusics().get(j).getUser().equals(user)) {
                    File file = new File(p.get(i).getMusics().get(j).getMusic_path());
                    p.get(i).getMusics().remove(p.get(i).getMusics().get(j));
                    file.delete();
                }
            }
        }
        users.remove(user);
    }
}
