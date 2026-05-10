import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection{

    private static final String URL = "jdbc:mysql://localhost:3306/toyota_dms";
    private static final String USER = "root";
    private static final String PASSWORD = "qadeerkaSQL";

    
    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Connection Failed!");
            System.out.println(e.getMessage());
        }
        return con;
    }
}