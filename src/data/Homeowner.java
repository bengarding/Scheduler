package data;

public class Homeowner extends Customer {

    public static final String TABLE = "homeowners";
    public static final String YEAR_BUILT = "Year_Built";
    public static final String WINDOWS = "Windows";
    public static final String DOORS = "Doors";
    public static final String ROOMS = "Rooms";

    private int yearBuilt;
    private int windows;
    private int doors;
    private int rooms;

    public Homeowner(int id, String name, String address, String postalCode, String phone, int divisionId, String country, String type, int yearBuilt, int windows, int doors, int rooms) {
        super(id, name, address, postalCode, phone, divisionId, country, type);
        this.yearBuilt = yearBuilt;
        this.windows = windows;
        this.doors = doors;
        this.rooms = rooms;
    }

    public Homeowner() {
        super();
    }

    public int getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(int yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public int getWindows() {
        return windows;
    }

    public void setWindows(int windows) {
        this.windows = windows;
    }

    public int getDoors() {
        return doors;
    }

    public void setDoors(int doors) {
        this.doors = doors;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }
}
