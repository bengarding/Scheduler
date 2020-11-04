package data;

public class Division {
    private int id;
    private String name;

    public static final String TABLE = "first_level_divisions";
    public static final String ID = "Division_ID";
    public static final String NAME = "Division";

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
