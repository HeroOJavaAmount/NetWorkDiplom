package netWork.chatServiceUser;

public class AuthificatedUser {

    private boolean isAuth = false;
    private final String login;
    private final String password;

    public AuthificatedUser(boolean isAuth, String login, String password) {
        this.isAuth = isAuth;
        this.login = login;
        this.password = password;
    }
}
