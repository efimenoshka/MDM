package sample.tables;

public class Worker {
    private int id;
    private String fullName;
    private String telephone;
    private String email;
    private String position;
    private String password;
    private String login;

    public Worker(int id, String fullName, String telephone, String email, String position, String password, String login) {
        this.id = id;
        this.fullName = fullName;
        this.telephone = telephone;
        this.email = email;
        this.position = position;
        this.password = password;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return fullName;
    }


}
