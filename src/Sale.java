import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Sale implements DatabaseOperations{

    private long VIN;
    private long CNIC;
    private int Employee_ID;
    private double Amount_Paid;
    private String Date_Time;


    public Sale(long VIN, long CNIC, int Employee_ID, double Amount_Paid, String Date_Time){
    	this.VIN = VIN;
    	this.CNIC = CNIC;
    	this.Employee_ID = Employee_ID;
    	this.Amount_Paid = Amount_Paid;
    	this.Date_Time = Date_Time;
    }

//-----------------Getters----------------------

    public long getVin(){ return this.VIN; }
    public long getCNIC(){ return this.CNIC; }
    public int getEmployee_ID(){ return this.Employee_ID; }
    public double getAmount_Paid(){ return this.Amount_Paid; }
    public String getDate_Time(){ return this.Date_Time; }

//-----------------Setters----------------------

	public void setVin(long VIN){ this.VIN = VIN; }
    public void setCNIC(long CNIC){ this.CNIC = CNIC; }
    public void setEmployee_ID(int Employee_ID){ this.Employee_ID = Employee_ID; }
    public void setAmount_Paid(double Amount_Paid){ this.Amount_Paid = Amount_Paid; }
    public void setDate_Time(String Date_Time){ this.Date_Time = Date_Time; }



//-----------------Methods----------------------



	public void insert(){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "INSERT INTO Sales VALUES (?, ?, ?, ?, ?);";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setLong(1, this.VIN);
			stmt.setLong(2, this.CNIC);
			stmt.setInt(3, this.Employee_ID);
			stmt.setDouble(4, this.Amount_Paid);
			stmt.setString(5, this.Date_Time);

			stmt.executeUpdate();
			System.out.println("Sale inserted successfully!");

			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			}
	}

	public void display(){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "SELECT * FROM Sales";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				System.out.println("VIN: " + rs.getLong("VIN"));
				System.out.println("CNIC: " + rs.getLong("CNIC"));
				System.out.println("Employee_ID: " + rs.getInt("Employee_ID"));
				System.out.println("Amount_Paid: " + rs.getDouble("Amount_Paid"));
				System.out.println("Date_Time: " + rs.getString("Date_Time"));
				System.out.println("----------------------------");
			}
			con.close();
		} catch (Exception e){
			System.out.println(e.getMessage());
			}
	}

	public void updateSale(long VIN, long newCNIC, int newEmployee_ID, double newAmount_Paid, String newDate_Time){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "UPDATE Sales SET CNIC = ?, Employee_ID = ?, Amount_Paid = ?, Date_Time = ? WHERE VIN = ?";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setLong(1, newCNIC);
			stmt.setInt(2, newEmployee_ID);
			stmt.setDouble(3, newAmount_Paid);
			stmt.setString(4, newDate_Time);
			stmt.setLong(5, VIN);

			int rows = stmt.executeUpdate();

			if(rows > 0){
				System.out.println("Sale updated successfully!");
			}
			else{
				System.out.println("No Sale found with this VIN!");
			}
			
			con.close();
		} catch (Exception e){
			System.out.println(e.getMessage());
			}
	}

	public void deleteSale(long VIN){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "DELETE FROM Sales WHERE VIN = ?;";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setLong(1,VIN);

			int rows = stmt.executeUpdate();

			if(rows > 0){
				System.out.println("Sale deleted successfully!");
			}
			else{
				System.out.println("No Sale found with this VIN!");
			}

			con.close();
		} catch (Exception e){
			System.out.println(e.getMessage());
			}
	}

	public void searchSale(long VIN){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "SELECT * FROM Sales WHERE VIN = ?";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setLong(1, VIN);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				System.out.println("VIN: " + rs.getLong("VIN"));
				System.out.println("CNIC: " + rs.getLong("CNIC"));
				System.out.println("Employee ID: " + rs.getInt("Employee_ID"));
				System.out.println("Amount Paid: " + rs.getDouble("Amount_Paid"));
				System.out.println("Date Time: " + rs.getString("Date_Time"));
				System.out.println("----------------------------");
			}
			else{
				System.out.println("No Sale found with this VIN");
			}

			con.close();
		} catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}