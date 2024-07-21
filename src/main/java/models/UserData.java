package models;
public class UserData {

    private String email;
    private String password;
    private String name;

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public UserData withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserData withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserData withName(String name) {
        this.name = name;
        return this;
    }
}
