import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CarFeature implements DatabaseOperations{
    
    private long VIN;
    private boolean Power_Windows;
    private boolean Power_Steering;
    private boolean Alloy_Wheels;
    private boolean Imported;

    public CarFeature(long VIN, boolean Power_Windows, boolean Power_Steering, boolean Alloy_Wheels, boolean Imported){
        this.VIN = VIN;
        this.Power_Windows = Power_Windows;
        this.Power_Steering = Power_Steering;
        this.Alloy_Wheels = Alloy_Wheels;
        this.Imported = Imported;
    }


//-----------------Getters----------------------

    public long getVIN(){ return this.VIN; }
    public boolean getPower_Windows() { return this.Power_Windows; }
    public boolean getPower_Steering() { return this.Power_Steering; }
    public boolean getAlloy_Wheels() { return this.Alloy_Wheels; }
    public boolean getImported() { return this.Imported; }


//-----------------Setters----------------------

    public void setVIN(long VIN) { this.VIN = VIN; }
    public void setPower_Windows(boolean Power_Windows) { this.Power_Windows = Power_Windows; }
    public void setPower_Steering(boolean Power_Steering) { this.Power_Steering = Power_Steering; }
    public void setAlloy_Wheels(boolean Alloy_Wheels) { this.Alloy_Wheels = Alloy_Wheels; }
    public void setImported(boolean Imported) { this.Imported = Imported; }



//-----------------Methods----------------------

    public void insert(){
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "INSERT INTO Car_Features VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setLong(1, this.VIN);
            stmt.setBoolean(2, this.Power_Windows);
            stmt.setBoolean(3, this.Power_Steering);
            stmt.setBoolean(4, this.Alloy_Wheels);
            stmt.setBoolean(5, this.Imported);
            stmt.executeUpdate();
            System.out.println("Car Features inserted successfully!");
            con.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
            }
    }

    public void display(){
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT * FROM Car_Features";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("VIN: " + rs.getLong("VIN"));
                System.out.println("Power Windows: " + (rs.getBoolean("Power_Windows") ? "Yes" : "No"));
                System.out.println("Power Steering: " + (rs.getBoolean("Power_Steering") ? "Yes" : "No"));
                System.out.println("Alloy Wheels: " + (rs.getBoolean("Alloy_Wheels") ? "Yes" : "No"));
                System.out.println("Imported: " + (rs.getBoolean("Imported") ? "Yes" : "No"));
                System.out.println("----------------------------");
            }
            con.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
            }
    }

    public void updateCarFeatures(long VIN, boolean newPower_Windows, boolean newPower_Steering, boolean newAlloy_Wheels, boolean newImported){
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "UPDATE Car_Features SET Power_Windows = ?, Power_Steering = ?, Alloy_Wheels = ?, Imported = ? WHERE VIN = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setBoolean(1, newPower_Windows);
            stmt.setBoolean(2, newPower_Steering);
            stmt.setBoolean(3, newAlloy_Wheels);
            stmt.setBoolean(4, newImported);
            stmt.setLong(5, VIN);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Car Features updated successfully!");
            } else {
                System.out.println("No record found with this VIN!");
            }
            con.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
            }
    }

    public void deleteCarFeatures(long VIN){
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "DELETE FROM Car_Features WHERE VIN = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setLong(1, VIN);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Car Features deleted successfully!");
            } else {
                System.out.println("No record found with this VIN!");
            }
            con.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
            }
    }

    public void searchCarFeatures(long VIN){
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT * FROM Car_Features WHERE VIN = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setLong(1, VIN);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("VIN: " + rs.getLong("VIN"));
                System.out.println("Power Windows: " + (rs.getBoolean("Power_Windows") ? "Yes" : "No"));
                System.out.println("Power Steering: " + (rs.getBoolean("Power_Steering") ? "Yes" : "No"));
                System.out.println("Alloy Wheels: " + (rs.getBoolean("Alloy_Wheels") ? "Yes" : "No"));
                System.out.println("Imported: " + (rs.getBoolean("Imported") ? "Yes" : "No"));
            } else {
                System.out.println("No record found with this VIN!");
            }
            con.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
            }
    }
}