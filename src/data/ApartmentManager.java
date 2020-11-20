package data;

public class ApartmentManager extends Customer {

    public static final String TABLE = "apartment_managers";
    public static final String UNITS = "Units";
    public static final String BUILDINGS = "Buildings";

    private int units;
    private int buildings;

    public ApartmentManager(int id, String name, String address, String postalCode, String phone, int divisionId, String country, String type, int units, int buildings) {
        super(id, name, address, postalCode, phone, divisionId, country, type);
        this.units = units;
        this.buildings = buildings;
    }

    public ApartmentManager() {
        super();
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public int getBuildings() {
        return buildings;
    }

    public void setBuildings(int buildings) {
        this.buildings = buildings;
    }
}
