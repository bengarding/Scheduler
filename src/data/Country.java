package data;

/**
 * The Country class creates a country and stores static final variables used for SQL statements
 *
 * @author Ben Garding
 */
public class Country {
    private int id;
    private String name;

    public static final String TABLE = "countries";
    public static final String NAME = "Country";
    public static final String ID = "Country_ID";

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
