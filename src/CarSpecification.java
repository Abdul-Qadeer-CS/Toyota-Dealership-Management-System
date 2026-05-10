import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CarSpecification implements DatabaseOperations{
    private long VIN;
    private String Engine_Type;
    private String Engine_CC;
    private String Body_Type;

    public CarSpecification(long VIN, String Engine_Type, String Engine_CC, String Body_Type) {
        this.VIN = VIN;
        this.Engine_Type = Engine_Type;
        this.Engine_CC = Engine_CC;
        this.Body_Type = Body_Type;
    }


//-----------------Getters----------------------

    public long getVIN() { return this.VIN; }
    public String getEngine_Type() { return this.Engine_Type; }
    public String getEngine_CC() { return this.Engine_CC; }
    public String getBody_Type() { return this.Body_Type; }


//-----------------Setters----------------------

    public void setVIN(long VIN) { this.VIN = VIN; }
    public void setEngine_Type(String Engine_Type) { this.Engine_Type = Engine_Type; }
    public void setEngine_CC(String Engine_CC) { this.Engine_CC = Engine_CC; }
    public void setBody_Type(String Body_Type) { this.Body_Type = Body_Type; }



//-----------------Methods----------------------

    public void insert() {
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "INSERT INTO Car_Specifications VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setLong(1, this.VIN);
            stmt.setString(2, this.Engine_Type);
            stmt.setString(3, this.Engine_CC);
            stmt.setString(4, this.Body_Type);
            stmt.executeUpdate();
            System.out.println("Car Specs inserted successfully!");
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void display() {
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT * FROM Car_Specifications";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("VIN: " + rs.getLong("VIN"));
                System.out.println("Engine Type: " + rs.getString("Engine_Type"));
                System.out.println("Engine CC: " + rs.getString("Engine_CC"));
                System.out.println("Body Type: " + rs.getString("Body_Type"));
                System.out.println("----------------------------");
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateCarSpecs(long VIN, String newEngine_Type, String newEngine_CC, String newBody_Type) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "UPDATE Car_Specifications SET Engine_Type = ?, Engine_CC = ?, Body_Type = ? WHERE VIN = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, newEngine_Type);
            stmt.setString(2, newEngine_CC);
            stmt.setString(3, newBody_Type);
            stmt.setLong(4, VIN);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Car Specs updated successfully!");
            } else {
                System.out.println("No record found with this VIN!");
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCarSpecs(long VIN) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "DELETE FROM Car_Specifications WHERE VIN = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setLong(1, VIN);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Car Specs deleted successfully!");
            } else {
                System.out.println("No record found with this VIN!");
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchCarSpecs(long VIN) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT * FROM Car_Specifications WHERE VIN = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setLong(1, VIN);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("VIN: " + rs.getLong("VIN"));
                System.out.println("Engine Type: " + rs.getString("Engine_Type"));
                System.out.println("Engine CC: " + rs.getString("Engine_CC"));
                System.out.println("Body Type: " + rs.getString("Body_Type"));
            } else {
                System.out.println("No record found with this VIN!");
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}