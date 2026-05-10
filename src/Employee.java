import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class Employee implements DatabaseOperations{

	private int Employee_ID;
	private String Name;
	private String Department;
	private double Salary;
	private String Phone_Number;

	public Employee(int Employee_ID, String Name, String Department, double Salary, String Phone_Number){
		this.Employee_ID = Employee_ID;
		this.Name = Name;
		this.Department = Department;
		this.Salary = Salary;
		this.Phone_Number = Phone_Number;
	}


//-----------------Getters----------------------

	public int getEmployee_ID(){ return this.Employee_ID; }
	public String getName(){ return this.Name; }
	public String getDepartment(){ return this.Department; }
	public double getSalary(){ return this.Salary; }
	public String getPhone_Number(){ return this.Phone_Number; }


//-----------------Setters----------------------

	public void setEmployee_ID(int Employee_ID){ this.Employee_ID = Employee_ID; }
	public void setName(String Name){ this.Name = Name; }
	public void setDepartment(String Department){ this.Department = Department; }
	public void setSalary(double Salary){ this.Salary = Salary; }
	public void setPhone_Number(String Phone_Number){ this.Phone_Number = Phone_Number; }



//-----------------Methods----------------------

	public void insert(){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "INSERT INTO Employees VALUES (?, ?, ?, ?, ?);";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setInt(1, this.Employee_ID);
			stmt.setString(2, this.Name);
			stmt.setString(3, this.Department);
			stmt.setDouble(4, this.Salary);
			stmt.setString(5, this.Phone_Number);

			stmt.executeUpdate();
			System.out.println("Employee inserted successfully!");
			
			con.close();
		} catch(Exception e){
			System.out.println(e.getMessage());
			}
	}


	public void display(){
		try {
			Connection con = DatabaseConnection.getConnection();
			String query = "SELECT * FROM Employees";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				System.out.println("Employee_ID: " + rs.getInt("Employee_ID"));
				System.out.println("Name: " + rs.getString("Name"));
				System.out.println("Department: " + rs.getString("Department"));
				System.out.println("Salary: " + rs.getDouble("Salary"));
				System.out.println("Phone Number: " + rs.getString("Phone_Number"));
				System.out.println("----------------------------");
			}

			con.close();
		} catch(Exception e){
			System.out.println(e.getMessage());
			}
	}

	public void updateEmployee(int Employee_ID, String newName, String newDepartment, double newSalary, String newPhone_Number){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "UPDATE Employees SET Name = ?, Department = ?, Salary = ?, Phone_Number = ? WHERE Employee_ID = ?";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setString(1, newName);
			stmt.setString(2, newDepartment);
			stmt.setDouble(3, newSalary);
			stmt.setString(4, newPhone_Number);
			stmt.setInt(5, Employee_ID);

			int rows = stmt.executeUpdate();

			if(rows > 0){
				System.out.println("Employee updated successfully!");
			}
			else{
				System.out.println("No Employee found with this Employee_ID!");
			}

			con.close();
		} catch (Exception e){
			System.out.println(e.getMessage());
			}
	}

	public void deleteEmployee(int Employee_ID){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "DELETE FROM Employees WHERE Employee_ID = ?;";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setInt(1, Employee_ID);

			int rows = stmt.executeUpdate();

			if(rows > 0){
				System.out.println("Employee deleted successfully!");
			}
			else{
				System.out.println("No Employee found with this Employee_ID");
			}

			con.close();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			}
	}

	public void searchEmployee(int Employee_ID){
		try{
			Connection con = DatabaseConnection.getConnection();
			String query = "SELECT * FROM Employees WHERE Employee_ID = ?";
			PreparedStatement stmt = con.prepareStatement(query);

			stmt.setInt(1, Employee_ID);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				System.out.println("Employee_ID: " + rs.getInt("Employee_ID"));
				System.out.println("Name: " + rs.getString("Name"));
				System.out.println("Department: " + rs.getString("Department"));
				System.out.println("Salary: " + rs.getDouble("Salary"));
				System.out.println("Phone Number: " + rs.getString("Phone_Number"));
			}
			else{
				System.out.println("No Employee found with this Employee_ID");
			}

			con.close();
		} catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}