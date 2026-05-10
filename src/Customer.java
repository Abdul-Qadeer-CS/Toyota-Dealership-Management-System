import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Customer implements DatabaseOperations{

    private long CNIC;
    private String Name;
    private String Phone_Number;

    public Customer(long CNIC, String Name, String Phone_Number){
        this.CNIC = CNIC;
        this.Name = Name;
        this.Phone_Number = Phone_Number;
    }


//-----------------Getters----------------------

    public long getCNIC(){ return this.CNIC; }
    public String getName(){ return this.Name; }
    public String getPhone_Number(){ return this.Phone_Number; }

//-----------------Setters----------------------

    public void setCNIC(long CNIC){ this.CNIC = CNIC; }
    public void setName(String Name){ this.Name = Name; }
    public void setPhone_Number(String Phone_Number){ this.Phone_Number = Phone_Number;}



//-----------------Methods----------------------

    public void insert(){

        try {

            Connection con = DatabaseConnection.getConnection();
            String query = "INSERT INTO Customers VALUES (?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            
            stmt.setLong(1, this.CNIC);
            stmt.setString(2, this.Name);
            stmt.setString(3, this.Phone_Number);
            
            stmt.executeUpdate();
            System.out.println("Customer inserted successfully!");
            
            con.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
            }

    }

    public void display(){
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT * FROM Customers";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                System.out.println("CNIC: " + rs.getLong("CNIC"));
                System.out.println("Name: " +rs.getString("Name"));
                System.out.println("Phone Number: " + rs.getString("Phone_Number"));
                System.out.println("----------------------------");
            }

            con.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
            }
    }

    public void updateCustomers(long CNIC, String newName, String newPhone_Number){
        try{
            Connection con = DatabaseConnection.getConnection();
            String query = "UPDATE Customers  SET Name = ?, Phone_Number = ? WHERE CNIC = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            
            stmt.setString(1, newName);
            stmt.setString(2, newPhone_Number);
            stmt.setLong(3, CNIC);
            
            int rows = stmt.executeUpdate();

            if(rows > 0){
                System.out.println("Customer updated successfully!");
            }
            else{
                System.out.println("No Customer found with this CNIC!");
            }
            con.close();
        } catch(Exception e){
            System.out.println(e.getMessage());
            }
    }

    public void deleteCustomer(long CNIC){
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "DELETE FROM Customers WHERE CNIC = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            
            stmt.setLong(1,CNIC);

            int rows = stmt.executeUpdate();
            if(rows > 0){
                System.out.println("Customer deleted successfully!");
            }
            else{
                System.out.println("No Customer found with this CNIC!");
            }
            con.close();
        } catch(Exception e){
            System.out.println(e.getMessage());
            }
    }

    public void searchCustomer(long CNIC){
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT * FROM Customers WHERE CNIC = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            
            stmt.setLong(1, CNIC);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                System.out.println("CNIC: " + rs.getLong("CNIC"));
                System.out.println("Name: " + rs.getString("Name"));
                System.out.println("Phone Number: " + rs.getString("Phone_Number"));
            }
            else{
                System.out.println("No Customer found with this CNIC!");
            }
            con.close();
        } catch(Exception e){
            System.out.println(e.getMessage());
            }
    }
}