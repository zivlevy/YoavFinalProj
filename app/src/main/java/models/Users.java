package models;

public class Users {

    public String name;
    public String password;

    public int id;
    public String gmail;
    public int role;

    public Users(String name, String password, int id, String gmail, int role) {
        this.name = name;
        this.password = password;
        this.id = id;
        this.gmail = gmail;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
