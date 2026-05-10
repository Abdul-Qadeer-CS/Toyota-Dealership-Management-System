import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class Maintenance implements DatabaseOperations{

    private int Service_ID;
    private long VIN;
    private long CNIC;
    private int Employee_ID;
    private String Service_Type;
    private double Amount;
    private String Date_Time;

	public Maintenance(int Service_ID, long VIN, long CNIC, int Employee_ID, String Service_Type, double Amount, String Date_Time){
		this.Service_ID = Service_ID;
		this.VIN = VIN;
		this.CNIC = CNIC;
		this.Employee_ID = Employee_ID;
		this.Service_Type = Service_Type;
		this.Amount = Amount;
		this.Date_Time = Date_Time;
	}

//-----------------Getters----------------------

	public int getService_ID(){ return this.Service_ID; }
    public long getVIN(){ return this.VIN; }
    public long getCNIC(){ return this.CNIC; }
    public int getEmployee_ID(){ return this.Employee_ID; }
    public String getService_Type(){ return this.Service_Type; }
    public double getAmount(){ return this.Amount; }
    public String getDate_Time(){ return this.Date_Time; }

//-----------------Setters----------------------

	public void setService_ID(int Service_ID){ this.Service_ID = Service_ID; }
    public void setVIN(long VIN){ this.VIN = VIN; }
    public void setCNIC(long CNIC){ this.CNIC = CNIC; }
    public void setEmployee_ID(int Employee_ID){ this.Employee_ID = Employee_ID; }
    public void setService_Type(String Service_Type){ this.Service_Type = Service_Type; }
    public void setAmount(double Amount){ this.Amount = Amount; }
    public void setDate_Time(String Date_Time){ this.Date_Time = Date_Time; }



//-----------------Methods----------------------


	public void insert(){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "INSERT INTO Maintenance (Service_ID, VIN, CNIC, Employee_ID, Service_Type, Amount, Date_Time) VALUES (?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setInt(1, this.Service_ID);
			stmt.setLong(2, this.VIN);
			stmt.setLong(3, this.CNIC);
			stmt.setInt(4, this.Employee_ID);
			stmt.setString(5, this.Service_Type);
			stmt.setDouble(6, this.Amount);
			stmt.setString(7, this.Date_Time);

			stmt.executeUpdate();
			System.out.println("Maintenance inserted successfully!");

			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			}
	}

	public void display(){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "SELECT * FROM Maintenance;";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				System.out.println("Service_ID: " + rs.getInt("Service_ID"));
				System.out.println("VIN: " + rs.getLong("VIN"));
				System.out.println("CNIC: " + rs.getLong("CNIC"));
				System.out.println("Employee_ID: " + rs.getInt("Employee_ID"));
				System.out.println("Service_Type: " + rs.getString("Service_Type"));
				System.out.println("Amount: " + rs.getDouble("Amount"));
				System.out.println("Date Time: " + rs.getString("Date_Time"));
				System.out.println("----------------------------");
			}
	
			con.close();
		} catch (Exception e){
			System.out.println(e.getMessage());
			}
	}

	public void updateMaintenance(int Service_ID, long newVIN, long newCNIC, int newEmployee_ID, String newService_Type, double newAmount, String newDate_Time){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "UPDATE Maintenance SET VIN = ?, CNIC = ?, Employee_ID = ?, Service_Type = ?, Amount = ?, Date_Time = ? WHERE Service_ID = ?";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setLong(1, newVIN);
			stmt.setLong(2, newCNIC);
			stmt.setInt(3, newEmployee_ID);
			stmt.setString(4, newService_Type);
			stmt.setDouble(5, newAmount);
			stmt.setString(6, newDate_Time);
			stmt.setInt(7, Service_ID);

			int rows = stmt.executeUpdate();

			if(rows > 0){
				System.out.println("Maintenance updated successfully!");
			}
			else{
				System.out.println("No Maintenance found with this Service_ID!");
			}
			
			con.close();
		} catch (Exception e){
			System.out.println(e.getMessage());
			}
	}

	public void deleteMaintenance(int Service_ID){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "DELETE FROM Maintenance WHERE Service_ID = ?;";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setInt(1,Service_ID);

			int rows = stmt.executeUpdate();

			if(rows > 0){
				System.out.println("Maintenance deleted successfully!");
			}
			else{
				System.out.println("No Maintenance found with this Service_ID!");
			}

			con.close();
		} catch (Exception e){
			System.out.println(e.getMessage());
			}
	}

	public void searchMaintenance(int Service_ID){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "SELECT * FROM Maintenance WHERE Service_ID = ?";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setInt(1, Service_ID);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				System.out.println("Service_ID: " + rs.getInt("Service_ID"));
				System.out.println("VIN: " + rs.getLong("VIN"));
				System.out.println("CNIC: " + rs.getLong("CNIC"));
				System.out.println("Employee_ID: " + rs.getInt("Employee_ID"));
				System.out.println("Service_Type: " + rs.getString("Service_Type"));
				System.out.println("Amount: " + rs.getDouble("Amount"));
				System.out.println("Date Time: " + rs.getString("Date_Time"));
			}
			else{
				System.out.println("No Maintenance found with this Service_ID");
			}
			con.close();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
			
	}
}
