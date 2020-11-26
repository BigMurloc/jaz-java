package pl.edu.pjwstk.jaz;

import org.springframework.stereotype.Component;


@Component
public class FilterAuthenthicationService {

    User user;
    private final UserDB userDB;
    private final UserSession userSession;

    public FilterAuthenthicationService(UserDB userDB, UserSession userSession) {
        this.userDB = userDB;
        this.userSession = userSession;
    }

    public boolean login(String username, String password) {
        user = userDB.getUser(username);
        if(user != null && user.getPassword().equals(password)){
            userSession.logIn();
            return true;
        }
        return false;
    }

}
