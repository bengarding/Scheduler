package data;

/**
 * The Report class is used to store information when creating reports for appointments by month and type
 *
 * @author Ben Garding
 */
public class Report {

    private String type;
    private int count;
    private int month;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
