package data;

public class Contact {

    private int id;
    private String name;

    public static final String TABLE = "contacts";
    public static final String NAME = "Contact_Name";
    public static final String ID = "Contact_ID";

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

}
