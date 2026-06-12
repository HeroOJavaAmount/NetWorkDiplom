package netWork.chatServiceUser;

public class User {

    AuthificatedUser authificatedUser;

    private final String name;

    public User(String name) {
        this.name = name;
    }

    public User(String name, AuthificatedUser authificatedUser) {
        this.name = name;
        this.authificatedUser = authificatedUser;
    }

    public String getName() {
        return name;
    }


}
