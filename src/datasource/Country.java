package datasource;

public class Country {
    private int id;
    private String name;

    public static final String COUNTRY_NAME = "Country";
    public static final String COUNTRY_ID = "Country_ID";

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
