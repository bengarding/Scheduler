package datasource;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Datasource {

    private static Connection conn;

    public static boolean open() {
        MysqlDataSource datasource = getMySQLDataSource();

        try {
            conn = datasource.getConnection();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed loading database: " + e.getMessage());
            return false;
        }
    }

    private static MysqlDataSource getMySQLDataSource() {
        Properties props = new Properties();
        FileInputStream fis = null;
        MysqlDataSource mysqlDS = null;
        try {
            fis = new FileInputStream("src/res/db.properties");
            props.load(fis);
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mysqlDS;
    }
}
