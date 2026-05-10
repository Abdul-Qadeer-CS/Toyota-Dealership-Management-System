import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class Car implements DatabaseOperations{

	private static final String CAR_BROWSE_BASE_QUERY = "SELECT c.*, cf.Power_Windows, cf.Power_Steering, cf.Alloy_Wheels, cf.Imported, cs.Engine_Type, cs.Engine_CC, cs.Body_Type "
	        + "FROM Cars c "
	        + "LEFT JOIN Car_Features cf ON c.VIN = cf.VIN "
	        + "LEFT JOIN Car_Specifications cs ON c.VIN = cs.VIN ";

	private long VIN;
	private String Make;
	private String Model;
	private String Variant;
	private boolean Brand_New;
	private boolean sold;
	private double Price;
	private String Color;
	private int Kilometers_Driven;
	private int Year_of_Manufacture;


	public Car(long VIN, String Make, String Model, String Variant, double Price, String Color, boolean Brand_New, int Kilometers_Driven, int Year_of_Manufacture){
		this.VIN = VIN;
		this.Make = Make;
		this.Model = Model;
		this.Variant = Variant;
		this.Brand_New = Brand_New;
		this.Price = Price;
		this.Color = Color;
		this.sold = false;
		this.Kilometers_Driven = Kilometers_Driven;
		this.Year_of_Manufacture = Year_of_Manufacture;
	}


//-----------------Getters----------------------
	
	public long getVIN(){ return this.VIN; }
	public String getMake(){ return this.Make; }
	public String getModel(){ return this.Model; }
	public String getVariant(){ return this.Variant; }
	public boolean getBrand_New(){ return this.Brand_New; }
	public boolean isSold(){ return this.sold; }
	public double getPrice(){ return this.Price; }
	public String getColor(){ return this.Color; }
	public int getKilometers_Driven(){ return this.Kilometers_Driven; }
	public int getYear_of_Manufacture(){ return this.Year_of_Manufacture; }



//-----------------Setters----------------------

	public void setVIN(long VIN){ this.VIN = VIN; } 
	public void setMake(String Make){ this.Make = Make; }
	public void setModel(String Model){ this.Model = Model; } 
	public void setVariant(String Variant){ this.Variant = Variant; }
	public void setBrand_New(boolean Brand_New){ this.Brand_New = Brand_New; }
	public void setSold(boolean sold){ this.sold = sold; }
	public void setPrice(double Price){	this.Price = Price; }
	public void setColor(String Color){ this.Color = Color;	}
	public void setKilometers_Driven(int Kilometers_Driven){ this.Kilometers_Driven = Kilometers_Driven; }
	public void setYear_of_Manufacture(int Year_of_Manufacture){ this.Year_of_Manufacture = Year_of_Manufacture; }



//-----------------Methods----------------------

	private void printCarWithDetails(ResultSet rs) throws Exception {
	    System.out.println("VIN: " + rs.getLong("VIN"));
	    System.out.println("Make: " + rs.getString("Make"));
	    System.out.println("Model: " + rs.getString("Model"));
	    System.out.println("Variant: " + rs.getString("Variant"));
	    System.out.println("Price: " + rs.getDouble("Price"));
	    System.out.println("Color: " + rs.getString("Color"));
	    System.out.println("Brand New: " + (rs.getBoolean("Brand_New") ? "Yes" : "No"));
	    System.out.println("Kilometers Driven: " + rs.getInt("Kilometers_Driven"));
	    System.out.println("Year of Manufacture: " + rs.getInt("Year_of_Manufacture"));

	    boolean hasFeatureData = rs.getObject("Power_Windows") != null;
	    if (hasFeatureData) {
	        System.out.println("Power Windows: " + (rs.getBoolean("Power_Windows") ? "Yes" : "No"));
	        System.out.println("Power Steering: " + (rs.getBoolean("Power_Steering") ? "Yes" : "No"));
	        System.out.println("Alloy Wheels: " + (rs.getBoolean("Alloy_Wheels") ? "Yes" : "No"));
	        System.out.println("Imported: " + (rs.getBoolean("Imported") ? "Yes" : "No"));
	    } else {
	        System.out.println("Features: Not available");
	    }

	    String engineType = rs.getString("Engine_Type");
	    String engineCC = rs.getString("Engine_CC");
	    String bodyType = rs.getString("Body_Type");

	    if (engineType != null || engineCC != null || bodyType != null) {
	        System.out.println("Engine Type: " + (engineType == null ? "N/A" : engineType));
	        System.out.println("Engine CC: " + (engineCC == null ? "N/A" : engineCC));
	        System.out.println("Body Type: " + (bodyType == null ? "N/A" : bodyType));
	    } else {
	        System.out.println("Specifications: Not available");
	    }

	    System.out.println("----------------------------");
	}

	public void insert(){
	    try {
	        Connection con = DatabaseConnection.getConnection();
	        String query = "INSERT INTO Cars (VIN, Make, Model, Variant, Brand_New, Sold, Price, Color, Kilometers_Driven, Year_of_Manufacture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        PreparedStatement stmt = con.prepareStatement(query);
	        stmt.setLong(1, this.VIN);
	        stmt.setString(2, this.Make);
	        stmt.setString(3, this.Model);
	        stmt.setString(4, this.Variant);
	        stmt.setBoolean(5, this.Brand_New);
	        stmt.setBoolean(6, this.sold);
	        stmt.setDouble(7, this.Price);
	        stmt.setString(8, this.Color);
	        stmt.setInt(9, this.Kilometers_Driven);
	        stmt.setInt(10, this.Year_of_Manufacture);
	        stmt.executeUpdate();
	        System.out.println("Car inserted successfully!");
	        con.close();
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}

	public void display() {
	    try {
	        Connection con = DatabaseConnection.getConnection();
	        String query = CAR_BROWSE_BASE_QUERY;
	        PreparedStatement stmt = con.prepareStatement(query);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            printCarWithDetails(rs);
	        }
	        con.close();
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}

	public void updateCar(long VIN, String newMake, String newModel, String newVariant, boolean newBrand_New, boolean newSold, double newPrice, String newColor, int newKilometers_Driven, int newYear_of_Manufacture) {
	    try {
	        Connection con = DatabaseConnection.getConnection();
	        String query = "UPDATE Cars SET Make = ?, Model = ?, Variant = ?, Brand_New = ?, Sold = ?, Price = ?, Color = ?, Kilometers_Driven = ?, Year_of_Manufacture = ? WHERE VIN = ?";
	        PreparedStatement stmt = con.prepareStatement(query);
	        stmt.setString(1, newMake);
	        stmt.setString(2, newModel);
	        stmt.setString(3, newVariant);
	        stmt.setBoolean(4, newBrand_New);
	        stmt.setBoolean(5, newSold);
	        stmt.setDouble(6, newPrice);
	        stmt.setString(7, newColor);
	        stmt.setInt(8, newKilometers_Driven);
	        stmt.setInt(9, newYear_of_Manufacture);
	        stmt.setLong(10, VIN);

	        int rows = stmt.executeUpdate();

		        if (rows > 0) {
		            System.out.println("Car updated successfully!");
		        } else {
		            System.out.println("No car found with this VIN!");
		        }
	        con.close();
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}

	public void updateVIN(long oldVIN, long newVIN) {
	    try {
	        Connection con = DatabaseConnection.getConnection();
	        String query = "UPDATE Cars SET VIN = ? WHERE VIN = ?";
	        PreparedStatement stmt = con.prepareStatement(query);
	        stmt.setLong(1, newVIN);
	        stmt.setLong(2, oldVIN);

	        int rows = stmt.executeUpdate();

	        if (rows > 0) {
	            System.out.println("VIN updated successfully!");
	        } else {
	            System.out.println("No car found with this VIN!");
	        }
	        con.close();
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}

	public void deleteCar(long VIN) {
	    try {
	        Connection con = DatabaseConnection.getConnection();
	        String query = "DELETE FROM Cars WHERE VIN = ?";
	        PreparedStatement stmt = con.prepareStatement(query);
	        stmt.setLong(1, VIN);
	        int rows = stmt.executeUpdate();
	        if (rows > 0) {
	            System.out.println("Car deleted successfully!");
	        } else {
	            System.out.println("No car found with this VIN!");
	        }
	        con.close();
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}

	public void searchCar(long VIN) {
	    try {
	        Connection con = DatabaseConnection.getConnection();
	        String query = CAR_BROWSE_BASE_QUERY + "WHERE c.VIN = ?";
	        PreparedStatement stmt = con.prepareStatement(query);
	        stmt.setLong(1, VIN);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            printCarWithDetails(rs);
	        } else {
	            System.out.println("No car found with this VIN!");
	        }
	        con.close();
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}
}