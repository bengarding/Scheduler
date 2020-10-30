package datasource;

public class User {

    private int id;
    private String userName;
    private String password;

    public static final int INDEX_ID = 1;
    public static final int INDEX_NAME = 2;
    public static final int INDEX_PASSWORD = 3;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
