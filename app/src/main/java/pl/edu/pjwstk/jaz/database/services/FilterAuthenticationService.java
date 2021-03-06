package pl.edu.pjwstk.jaz.database.services;

import org.springframework.stereotype.Component;
import pl.edu.pjwstk.jaz.deprecated.User;
import pl.edu.pjwstk.jaz.deprecated.UserDB;
import pl.edu.pjwstk.jaz.exceptions.UserDoesNotExistException;
import pl.edu.pjwstk.jaz.filter.UserSession;


@Component
public class FilterAuthenticationService {

    User user;
    private final UserDB userDB;
    private final UserSession userSession;

    public FilterAuthenticationService(UserDB userDB, UserSession userSession) {
        this.userDB = userDB;
        this.userSession = userSession;
    }

    public boolean login(String username, String password) throws UserDoesNotExistException {
        user = userDB.getUser(username);
        if(user == null){
            throw new UserDoesNotExistException();
        }
        if(user != null && user.getPassword().equals(password)){
            userSession.logIn();
            return true;
        }
        return false;
    }

}
