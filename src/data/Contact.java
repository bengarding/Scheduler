package data;

public class Contact {

    private int id;
    private String name;
    private String email;

    public static final String CONTACT_NAME = "Contact_Name";
    public static final String CONTACT_ID = "Contact_ID";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}