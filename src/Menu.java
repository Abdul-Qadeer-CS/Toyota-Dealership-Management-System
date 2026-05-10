import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Menu{
    private static final String CAR_BROWSE_BASE_QUERY = "SELECT c.*, cf.Power_Windows, cf.Power_Steering, cf.Alloy_Wheels, cf.Imported, cs.Engine_Type, cs.Engine_CC, cs.Body_Type "
            + "FROM Cars c "
            + "LEFT JOIN Car_Features cf ON c.VIN = cf.VIN "
            + "LEFT JOIN Car_Specifications cs ON c.VIN = cs.VIN ";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void showMainMenu(Scanner scanner){
        int mainOption;
        do {
            System.out.println("\n========== Toyota Dealership Management System ==========");
            System.out.println("1. Browse Cars (Customer)");
            System.out.println("2. Login (Employee)");
            System.out.println("3. Exit");
            mainOption = readInt(scanner, "Select the option no: ");
            System.out.println();

            switch(mainOption) {
                case 1:
                    showCustomerMenu(scanner);
                    break;
                case 2:
                    int employeeId = readInt(scanner, "Enter Employee ID: ");
                    if (employeeExists(employeeId)) {
                        showEmployeeMenu(scanner, employeeId);
                    } else {
                        System.out.println("Invalid Employee ID. Access denied.");
                    }
                    break;
                case 3:
                    System.out.println("Thank you for using our Car Dealership Management System!");
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while(mainOption != 3);
    }

    public static boolean employeeExists(int employeeId) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT Employee_ID FROM Employees WHERE Employee_ID = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next();
            con.close();
            return exists;
        } catch (Exception e) {
            System.out.println("Login validation error: " + e.getMessage());
            return false;
        }
    }

    public void showCustomerMenu(Scanner scanner){
        int customerOption;
        do{
            System.out.println("Customer Menu:");
            System.out.println("1. Display All Cars");
            System.out.println("2. Display Filtered Cars");
            System.out.println("3. Buy Car");
            System.out.println("4. Exit");

            customerOption = readInt(scanner, "Select the option no: ");
            System.out.println();

            switch(customerOption){
                case 1:
                    displayAllCars();
                    break;
                case 2:
                    displayFilteredCars(scanner);
                    break;
                case 3:
                    buyCar(scanner);
                    break;
                case 4:
                    System.out.println("Exiting Customer Menu...");
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while(customerOption != 4);
    }

    public void displayAllCars(){
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = CAR_BROWSE_BASE_QUERY + "WHERE c.Sold = false";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                printCarWithDetails(rs);
            }

            if (!found) {
                System.out.println("No cars found.");
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println();
        }
    }

    public void displayFilteredCars(Scanner scanner){
        int filterOption;
        do{
            System.out.println("Filter by:");
            System.out.println("1. Make");
            System.out.println("2. Model");
            System.out.println("3. Year");
            System.out.println("4. Price Range");
            System.out.println("5. Kilometers Driven");
            filterOption = readInt(scanner, "Select the filter option no: ");
            System.out.println();

            String filterChoice = null;
            String query = null;
            PreparedStatement stmt = null;

            try{
                Connection con = DatabaseConnection.getConnection();

                switch(filterOption){
                    case 1:
                        String make = readString(scanner, "Enter Make: ");
                        filterChoice = "Make";
                        query = CAR_BROWSE_BASE_QUERY + "WHERE c.Make = ? AND c.Sold = false";
                        stmt = con.prepareStatement(query);
                        stmt.setString(1, make);
                        break;

                    case 2:
                        String model = readString(scanner, "Enter Model: ");
                        filterChoice = "Model";
                        query = CAR_BROWSE_BASE_QUERY + "WHERE c.Model = ? AND c.Sold = false";
                        stmt = con.prepareStatement(query);
                        stmt.setString(1, model);
                        break;

                    case 3:
                        int year = readInt(scanner, "Enter Year of Manufacture: ");
                        filterChoice = "Year_of_Manufacture";
                        query = CAR_BROWSE_BASE_QUERY + "WHERE c.Year_of_Manufacture = ? AND c.Sold = false";
                        stmt = con.prepareStatement(query);
                        stmt.setInt(1, year);
                        break;

                    case 4:
                        double maxPrice = readDouble(scanner, "Enter Maximum Price: ");
                        filterChoice = "Price";
                        query = CAR_BROWSE_BASE_QUERY + "WHERE c.Price <= ? AND c.Sold = false";
                        stmt = con.prepareStatement(query);
                        stmt.setDouble(1, maxPrice);
                        break;

                    case 5:
                        int maxKm = readInt(scanner, "Enter Maximum Kilometers Driven: ");
                        filterChoice = "Kilometers_Driven";
                        query = CAR_BROWSE_BASE_QUERY + "WHERE c.Kilometers_Driven <= ? AND c.Sold = false";
                        stmt = con.prepareStatement(query);
                        stmt.setInt(1, maxKm);
                        break;

                    default:
                        System.out.println("Invalid filter option! Please try again.");
                        con.close();
                        return;
                }

                ResultSet rs = stmt.executeQuery();
                boolean found = false;

                while(rs.next()){
                    found = true;
                    printCarWithDetails(rs);
                }

                if(!found){
                    System.out.println("No car found with this " + filterChoice + "!");
                }

                con.close();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }

        } while(filterOption < 1 || filterOption > 5);

        System.out.println();
    }

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



    public void buyCar(Scanner scanner){
        Connection con = null;
        final int ONLINE_SYSTEM_EMPLOYEE_ID = 0;

        try {
            long vin = readVIN(scanner, "Enter VIN of the car to buy: ");
            long cnic = readCNIC(scanner, "Enter Customer CNIC: ");
            double amountPaid = readNonNegativeDouble(scanner, "Enter Amount to pay: ");

            con = DatabaseConnection.getConnection();
            if (con == null) {
                System.out.println("Could not establish database connection.");
                return;
            }

            con.setAutoCommit(false);

            String carCheckQuery = "SELECT Sold, Price FROM Cars WHERE VIN = ?";
            PreparedStatement carCheckStmt = con.prepareStatement(carCheckQuery);
            carCheckStmt.setLong(1, vin);
            ResultSet carRs = carCheckStmt.executeQuery();

            if (!carRs.next()) {
                System.out.println("No car found with this VIN!");
                con.rollback();
                return;
            }

            if (carRs.getBoolean("Sold")) {
                System.out.println("This car is already sold!");
                con.rollback();
                return;
            }

            if (isSaleAlreadyRecorded(con, vin)) {
                System.out.println("This car has already been sold.");
                con.rollback();
                return;
            }

            double carPrice = carRs.getDouble("Price");
            if (Math.abs(amountPaid - carPrice) > 0.009) {
                System.out.println("Full payment is required. Car price is: " + carPrice);
                con.rollback();
                return;
            }

            ensureCustomerExists(con, scanner, cnic);

            String saleDateTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);

            String insertSaleQuery = "INSERT INTO Sales (VIN, CNIC, Employee_ID, Amount_Paid, Date_Time) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertSaleStmt = con.prepareStatement(insertSaleQuery);
            insertSaleStmt.setLong(1, vin);
            insertSaleStmt.setLong(2, cnic);
            insertSaleStmt.setInt(3, ONLINE_SYSTEM_EMPLOYEE_ID);
            insertSaleStmt.setDouble(4, amountPaid);
            insertSaleStmt.setString(5, saleDateTime);
            insertSaleStmt.executeUpdate();

            String updateCarQuery = "UPDATE Cars SET Sold = true WHERE VIN = ?";
            PreparedStatement updateCarStmt = con.prepareStatement(updateCarQuery);
            updateCarStmt.setLong(1, vin);
            updateCarStmt.executeUpdate();

            con.commit();
            System.out.println("Car purchased successfully! Thank you for your purchase.");
            System.out.println("Your sale has been recorded with Online System (ID: 0).");

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception rollbackException) {
                System.out.println("Rollback failed: " + rollbackException.getMessage());
            }
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception closeException) {
                System.out.println(closeException.getMessage());
            }
            System.out.println();
        }
    }

    public void showEmployeeMenu(Scanner scanner, int employeeId){
        int employeeOption;

        do{
            System.out.println("\n========== Employee Menu (ID: " + employeeId + ") ==========");
            System.out.println("1. Sell Car");
            System.out.println("2. View All Sales");
            System.out.println("3. Manage Cars");
            System.out.println("4. Manage Car Features");
            System.out.println("5. Manage Car Specifications");
            System.out.println("6. Manage Customers");
            System.out.println("7. Manage Employees");
            System.out.println("8. Manage Sales");
            System.out.println("9. Manage Maintenance Operations");
            System.out.println("10. Exit");

            employeeOption = readInt(scanner, "Select the option no: ");
            System.out.println();

            switch(employeeOption){
                case 1:
                    sellCar(scanner, employeeId);
                    break;
                case 2:
                    viewAllSales();
                    break;
                case 3:
                    manageCarsMenu(scanner);
                    break;
                case 4:
                    manageCarFeaturesMenu(scanner);
                    break;
                case 5:
                    manageCarSpecificationsMenu(scanner);
                    break;
                case 6:
                    manageCustomersMenu(scanner);
                    break;
                case 7:
                    manageEmployeesMenu(scanner);
                    break;
                case 8:
                    manageSalesMenu(scanner);
                    break;
                case 9:
                    showMaintenanceMenu(scanner, employeeId);
                    break;
                case 10:
                    System.out.println("Exiting Employee Menu...");
                    if (employeeExists(employeeId)) {
                        break;
                    }
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while(employeeOption != 10);
    }

    public void sellCar(Scanner scanner, int employeeId){
        Connection con = null;

        try {
            if (!employeeExists(employeeId)) {
                System.out.println("Invalid Employee ID. Cannot process sale.");
                return;
            }

            long vin = readVIN(scanner, "Enter VIN of the car to sell: ");
            long cnic = readCNIC(scanner, "Enter Customer CNIC: ");
            double amountPaid = readNonNegativeDouble(scanner, "Enter Amount Paid: ");

            con = DatabaseConnection.getConnection();
            if (con == null) {
                System.out.println("Could not establish database connection.");
                return;
            }

            con.setAutoCommit(false);

            String carCheckQuery = "SELECT Sold FROM Cars WHERE VIN = ?";
            PreparedStatement carCheckStmt = con.prepareStatement(carCheckQuery);
            carCheckStmt.setLong(1, vin);
            ResultSet carRs = carCheckStmt.executeQuery();

            if (!carRs.next()) {
                System.out.println("No car found with this VIN!");
                con.rollback();
                return;
            }

            if (carRs.getBoolean("Sold")) {
                System.out.println("This car is already sold!");
                con.rollback();
                return;
            }

            if (isSaleAlreadyRecorded(con, vin)) {
                System.out.println("This car has already been sold.");
                con.rollback();
                return;
            }

            ensureCustomerExists(con, scanner, cnic);

            String saleDateTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);

            String insertSaleQuery = "INSERT INTO Sales (VIN, CNIC, Employee_ID, Amount_Paid, Date_Time) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertSaleStmt = con.prepareStatement(insertSaleQuery);
            insertSaleStmt.setLong(1, vin);
            insertSaleStmt.setLong(2, cnic);
            insertSaleStmt.setInt(3, employeeId);
            insertSaleStmt.setDouble(4, amountPaid);
            insertSaleStmt.setString(5, saleDateTime);
            insertSaleStmt.executeUpdate();

            String updateCarQuery = "UPDATE Cars SET Sold = true WHERE VIN = ?";
            PreparedStatement updateCarStmt = con.prepareStatement(updateCarQuery);
            updateCarStmt.setLong(1, vin);
            updateCarStmt.executeUpdate();

            con.commit();
            System.out.println("Car sold successfully! Sale recorded with Employee ID: " + employeeId);

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception rollbackException) {
                System.out.println("Rollback failed: " + rollbackException.getMessage());
            }
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception closeException) {
                System.out.println(closeException.getMessage());
            }
            System.out.println();
        }
    }

    private void manageCarsMenu(Scanner scanner) {
        int option;
        do {
            System.out.println("\n===== Manage Cars =====");
            System.out.println("1. Add Car");
            System.out.println("2. Display All Cars");
            System.out.println("3. Update Car Details");
            System.out.println("4. Update Car VIN");
            System.out.println("5. Search Car");
            System.out.println("6. Delete Car");
            System.out.println("7. Back");
            option = readInt(scanner, "Select option: ");
            System.out.println();

            Car car = new Car(0L, "", "", "", 0.0, "", false, 0, 0);
            switch (option) {
                case 1:
                    long vin = readVIN(scanner, "Enter VIN: ");
                    String make = readString(scanner, "Enter Make: ");
                    String model = readString(scanner, "Enter Model: ");
                    String variant = readString(scanner, "Enter Variant: ");
                    boolean brandNew = readBoolean(scanner, "Is Brand New");
                    boolean sold = readBoolean(scanner, "Is Sold");
                    double price = readNonNegativeDouble(scanner, "Enter Price: ");
                    String color = readString(scanner, "Enter Color: ");
                    int km = readNonNegativeInt(scanner, "Enter Kilometers Driven: ");
                    int year = readYear(scanner, "Enter Year of Manufacture: ");
                    car = new Car(vin, make, model, variant, price, color, brandNew, km, year);
                    car.setSold(sold);
                    car.insert();
                    
                    // Add Car Features
                    System.out.println();
                    boolean addFeatures = readBoolean(scanner, "Do you want to add car features");
                    if (addFeatures) {
                        CarFeature feature = new CarFeature(
                            vin,
                            readBoolean(scanner, "Power Windows"),
                            readBoolean(scanner, "Power Steering"),
                            readBoolean(scanner, "Alloy Wheels"),
                            readBoolean(scanner, "Imported")
                        );
                        feature.insert();
                    }
                    
                    // Add Car Specifications
                    System.out.println();
                    boolean addSpecs = readBoolean(scanner, "Do you want to add car specifications");
                    if (addSpecs) {
                        CarSpecification specification = new CarSpecification(
                            vin,
                            readString(scanner, "Enter Engine Type: "),
                            readString(scanner, "Enter Engine CC: "),
                            readString(scanner, "Enter Body Type: ")
                        );
                        specification.insert();
                    }
                    break;
                case 2:
                    car.display();
                    break;
                case 3:
                    long updateVin = readVIN(scanner, "Enter VIN to update: ");
                    String newMake = readString(scanner, "Enter new Make: ");
                    String newModel = readString(scanner, "Enter new Model: ");
                    String newVariant = readString(scanner, "Enter new Variant: ");
                    boolean newBrandNew = readBoolean(scanner, "Is Brand New");
                    boolean newSold = readBoolean(scanner, "Is Sold");
                    double newPrice = readNonNegativeDouble(scanner, "Enter new Price: ");
                    String newColor = readString(scanner, "Enter new Color: ");
                    int newKm = readNonNegativeInt(scanner, "Enter new Kilometers Driven: ");
                    int newYear = readYear(scanner, "Enter new Year of Manufacture: ");

                    car.updateCar(updateVin, newMake, newModel, newVariant, newBrandNew, newSold, newPrice, newColor, newKm, newYear);
                    
                    // Update Car Features
                    System.out.println();
                    boolean updateFeatures = readBoolean(scanner, "Do you want to update car features");
                    if (updateFeatures) {
                        CarFeature feature = new CarFeature(updateVin, false, false, false, false);
                        feature.updateCarFeatures(
                            updateVin,
                            readBoolean(scanner, "Power Windows"),
                            readBoolean(scanner, "Power Steering"),
                            readBoolean(scanner, "Alloy Wheels"),
                            readBoolean(scanner, "Imported")
                        );
                    }
                    
                    // Update Car Specifications
                    System.out.println();
                    boolean updateSpecs = readBoolean(scanner, "Do you want to update car specifications");
                    if (updateSpecs) {
                        CarSpecification specification = new CarSpecification(updateVin, "", "", "");
                        specification.updateCarSpecs(
                            updateVin,
                            readString(scanner, "Enter new Engine Type: "),
                            readString(scanner, "Enter new Engine CC: "),
                            readString(scanner, "Enter new Body Type: ")
                        );
                    }
                    break;
                case 4:
                    long oldVin = readVIN(scanner, "Enter old VIN: ");
                    long newVinNumber = readVIN(scanner, "Enter new VIN: ");
                    car.updateVIN(oldVin, newVinNumber);
                    break;
                case 5:
                    car.searchCar(readVIN(scanner, "Enter VIN to search: "));
                    break;
                case 6:
                    car.deleteCar(readVIN(scanner, "Enter VIN to delete: "));
                    break;
                case 7:
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while (option != 7);
    }

    private void manageCarFeaturesMenu(Scanner scanner) {
        int option;
        do {
            System.out.println("\n===== Manage Car Features =====");
            System.out.println("1. Add Car Feature");
            System.out.println("2. Display All Car Features");
            System.out.println("3. Update Car Feature");
            System.out.println("4. Search Car Feature");
            System.out.println("5. Delete Car Feature");
            System.out.println("6. Back");
            option = readInt(scanner, "Select option: ");
            System.out.println();

            CarFeature feature = new CarFeature(0L, false, false, false, false);
            switch (option) {
                case 1:
                        feature = new CarFeature(
                            readVIN(scanner, "Enter VIN: "),
                            readBoolean(scanner, "Power Windows"),
                            readBoolean(scanner, "Power Steering"),
                            readBoolean(scanner, "Alloy Wheels"),
                            readBoolean(scanner, "Imported")
                    );
                    feature.insert();
                    break;
                case 2:
                    feature.display();
                    break;
                case 3:
                    long vinToUpdate = readVIN(scanner, "Enter VIN to update: ");
                    feature.updateCarFeatures(
                            vinToUpdate,
                            readBoolean(scanner, "Power Windows"),
                            readBoolean(scanner, "Power Steering"),
                            readBoolean(scanner, "Alloy Wheels"),
                            readBoolean(scanner, "Imported")
                    );
                    break;
                case 4:
                    feature.searchCarFeatures(readVIN(scanner, "Enter VIN to search: "));
                    break;
                case 5:
                    feature.deleteCarFeatures(readVIN(scanner, "Enter VIN to delete: "));
                    break;
                case 6:
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while (option != 6);
    }

    private void manageCarSpecificationsMenu(Scanner scanner) {
        int option;
        do {
            System.out.println("\n===== Manage Car Specifications =====");
            System.out.println("1. Add Car Specification");
            System.out.println("2. Display All Car Specifications");
            System.out.println("3. Update Car Specification");
            System.out.println("4. Search Car Specification");
            System.out.println("5. Delete Car Specification");
            System.out.println("6. Back");
            option = readInt(scanner, "Select option: ");
            System.out.println();

            CarSpecification specification = new CarSpecification(0L, "", "", "");
            switch (option) {
                case 1:
                        specification = new CarSpecification(
                            readVIN(scanner, "Enter VIN: "),
                            readString(scanner, "Enter Engine Type: "),
                            readString(scanner, "Enter Engine CC: "),
                            readString(scanner, "Enter Body Type: ")
                    );
                    specification.insert();
                    break;
                case 2:
                    specification.display();
                    break;
                case 3:
                    long vinToUpdate = readVIN(scanner, "Enter VIN to update: ");
                    specification.updateCarSpecs(
                            vinToUpdate,
                            readString(scanner, "Enter new Engine Type: "),
                            readString(scanner, "Enter new Engine CC: "),
                            readString(scanner, "Enter new Body Type: ")
                    );
                    break;
                case 4:
                    specification.searchCarSpecs(readVIN(scanner, "Enter VIN to search: "));
                    break;
                case 5:
                    specification.deleteCarSpecs(readVIN(scanner, "Enter VIN to delete: "));
                    break;
                case 6:
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while (option != 6);
    }

    private void manageCustomersMenu(Scanner scanner) {
        int option;
        do {
            System.out.println("\n===== Manage Customers =====");
            System.out.println("1. Add Customer");
            System.out.println("2. Display All Customers");
            System.out.println("3. Update Customer");
            System.out.println("4. Search Customer");
            System.out.println("5. Delete Customer");
            System.out.println("6. Back");
            option = readInt(scanner, "Select option: ");
            System.out.println();

            Customer customer = new Customer(0L, "", "");
            switch (option) {
                case 1:
                        customer = new Customer(
                            readCNIC(scanner, "Enter CNIC: "),
                            readString(scanner, "Enter Name: "),
                            readPhoneNumber(scanner, "Enter Phone Number: ")
                        );
                    customer.insert();
                    break;
                case 2:
                    customer.display();
                    break;
                case 3:
                    long cnicToUpdate = readCNIC(scanner, "Enter CNIC to update: ");
                    customer.updateCustomers(
                            cnicToUpdate,
                            readString(scanner, "Enter new Name: "),
                            readPhoneNumber(scanner, "Enter new Phone Number: ")
                    );
                    break;
                case 4:
                    customer.searchCustomer(readCNIC(scanner, "Enter CNIC to search: "));
                    break;
                case 5:
                    customer.deleteCustomer(readCNIC(scanner, "Enter CNIC to delete: "));
                    break;
                case 6:
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while (option != 6);
    }

    private void manageEmployeesMenu(Scanner scanner) {
        int option;
        do {
            System.out.println("\n===== Manage Employees =====");
            System.out.println("1. Add Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Search Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. Back");
            option = readInt(scanner, "Select option: ");
            System.out.println();

            Employee employee = new Employee(0, "", "", 0.0, "");
            switch (option) {
                case 1:
                        employee = new Employee(
                            readPositiveInt(scanner, "Enter Employee ID: "),
                            readString(scanner, "Enter Name: "),
                            readString(scanner, "Enter Department: "),
                            readNonNegativeDouble(scanner, "Enter Salary: "),
                            readPhoneNumber(scanner, "Enter Phone Number: ")
                        );
                    employee.insert();
                    break;
                case 2:
                    employee.display();
                    break;
                case 3:
                    int employeeIdToUpdate = readPositiveInt(scanner, "Enter Employee ID to update: ");
                    employee.updateEmployee(
                            employeeIdToUpdate,
                            readString(scanner, "Enter new Name: "),
                            readString(scanner, "Enter new Department: "),
                            readNonNegativeDouble(scanner, "Enter new Salary: "),
                            readPhoneNumber(scanner, "Enter new Phone Number: ")
                    );
                    break;
                case 4:
                    employee.searchEmployee(readPositiveInt(scanner, "Enter Employee ID to search: "));
                    break;
                case 5:
                    employee.deleteEmployee(readPositiveInt(scanner, "Enter Employee ID to delete: "));
                    break;
                case 6:
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while (option != 6);
    }

    private void manageSalesMenu(Scanner scanner) {
        int option;
        do {
            System.out.println("\n===== Manage Sales =====");
            System.out.println("1. Add Sale");
            System.out.println("2. Display All Sales");
            System.out.println("3. Update Sale");
            System.out.println("4. Search Sale");
            System.out.println("5. Delete Sale");
            System.out.println("6. Back");
            option = readInt(scanner, "Select option: ");
            System.out.println();

            Sale sale = new Sale(0L, 0L, 0, 0.0, "");
            switch (option) {
                case 1:
                        sale = new Sale(
                            readVIN(scanner, "Enter VIN: "),
                            readCNIC(scanner, "Enter Customer CNIC: "),
                            readPositiveInt(scanner, "Enter Employee ID: "),
                            readNonNegativeDouble(scanner, "Enter Amount Paid: "),
                            LocalDateTime.now().format(DATE_TIME_FORMATTER)
                        );
                    sale.insert();
                    break;
                case 2:
                    sale.display();
                    break;
                case 3:
                    long saleVinToUpdate = readVIN(scanner, "Enter VIN of sale to update: ");
                    sale.updateSale(
                            saleVinToUpdate,
                            readCNIC(scanner, "Enter new Customer CNIC: "),
                            readPositiveInt(scanner, "Enter new Employee ID: "),
                            readNonNegativeDouble(scanner, "Enter new Amount Paid: "),
                            LocalDateTime.now().format(DATE_TIME_FORMATTER)
                    );
                    break;
                case 4:
                    sale.searchSale(readVIN(scanner, "Enter VIN to search sale: "));
                    break;
                case 5:
                    sale.deleteSale(readVIN(scanner, "Enter VIN to delete sale: "));
                    break;
                case 6:
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while (option != 6);
    }

    private void showMaintenanceMenu(Scanner scanner, int employeeId) {
        int maintenanceOption;

        do {
            System.out.println("\n========== Maintenance Menu ==========");
            System.out.println("1. Add Maintenance Record");
            System.out.println("2. Update Maintenance Record");
            System.out.println("3. Search Maintenance Record");
            System.out.println("4. Delete Maintenance Record");
            System.out.println("5. Display All Maintenance Records");
            System.out.println("6. Back");
            System.out.print("Select the option no: ");
            maintenanceOption = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            switch (maintenanceOption) {
                case 1:
                    addMaintenanceRecord(scanner, employeeId);
                    break;
                case 2:
                    updateMaintenanceRecord(scanner, employeeId);
                    break;
                case 3:
                    searchMaintenanceRecord(scanner);
                    break;
                case 4:
                    deleteMaintenanceRecord(scanner);
                    break;
                case 5:
                    new Maintenance(0, 0L, 0L, employeeId, "", 0.0, "").display();
                    break;
                case 6:
                    System.out.println("Returning to Employee Menu...");
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while (maintenanceOption != 6);
    }

    private void addMaintenanceRecord(Scanner scanner, int employeeId) {
        try {
            int serviceId = readPositiveInt(scanner, "Enter Service ID: ");
            long vin = readVIN(scanner, "Enter VIN: ");
            long cnic = readCNIC(scanner, "Enter Customer CNIC: ");
            String serviceType = readString(scanner, "Enter Service Type: ");
            double amount = readDouble(scanner, "Enter Amount: ");
            String dateTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);

            Maintenance maintenance = new Maintenance(serviceId, vin, cnic, employeeId, serviceType, amount, dateTime);
            maintenance.insert();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateMaintenanceRecord(Scanner scanner, int employeeId) {
        try {
            int serviceId = readPositiveInt(scanner, "Enter Service ID to update: ");
            long vin = readVIN(scanner, "Enter New VIN: ");
            long cnic = readCNIC(scanner, "Enter New Customer CNIC: ");
            String serviceType = readString(scanner, "Enter New Service Type: ");
            double amount = readDouble(scanner, "Enter New Amount: ");
            String dateTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);

            Maintenance maintenance = new Maintenance(0, 0L, 0L, employeeId, "", 0.0, "");
            maintenance.updateMaintenance(serviceId, vin, cnic, employeeId, serviceType, amount, dateTime);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void searchMaintenanceRecord(Scanner scanner) {
        int serviceId = readPositiveInt(scanner, "Enter Service ID to search: ");

        Maintenance maintenance = new Maintenance(0, 0L, 0L, 0, "", 0.0, "");
        maintenance.searchMaintenance(serviceId);
    }

    private void deleteMaintenanceRecord(Scanner scanner) {
        int serviceId = readPositiveInt(scanner, "Enter Service ID to delete: ");

        Maintenance maintenance = new Maintenance(0, 0L, 0L, 0, "", 0.0, "");
        maintenance.deleteMaintenance(serviceId);
    }

    private void ensureCustomerExists(Connection con, Scanner scanner, long cnic) throws Exception {
        String customerCheckQuery = "SELECT CNIC FROM Customers WHERE CNIC = ?";
        PreparedStatement customerCheckStmt = con.prepareStatement(customerCheckQuery);
        customerCheckStmt.setLong(1, cnic);
        ResultSet customerRs = customerCheckStmt.executeQuery();

        if (customerRs.next()) {
            System.out.println("Customer already exists. Reusing the existing customer record.");
            return;
        }

        String customerName = readString(scanner, "Enter Customer Name: ");
        String customerPhone = readPhoneNumber(scanner, "Enter Customer Phone Number: ");

        String insertCustomerQuery = "INSERT INTO Customers (CNIC, Name, Phone_Number) VALUES (?, ?, ?)";
        PreparedStatement insertCustomerStmt = con.prepareStatement(insertCustomerQuery);
        insertCustomerStmt.setLong(1, cnic);
        insertCustomerStmt.setString(2, customerName);
        insertCustomerStmt.setString(3, customerPhone);
        insertCustomerStmt.executeUpdate();
        System.out.println("New customer profile created successfully.");
    }

    private boolean isSaleAlreadyRecorded(Connection con, long vin) throws Exception {
        String saleCheckQuery = "SELECT VIN FROM Sales WHERE VIN = ?";
        PreparedStatement saleCheckStmt = con.prepareStatement(saleCheckQuery);
        saleCheckStmt.setLong(1, vin);
        ResultSet saleRs = saleCheckStmt.executeQuery();
        return saleRs.next();
    }

    private int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            }
            System.out.println("Invalid input for '" + prompt + "'. Expected: an integer number (e.g., 42). Please try again.");
            scanner.nextLine();
        }
    }

    private long readLong(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextLong()) {
                long value = scanner.nextLong();
                scanner.nextLine();
                return value;
            }
            System.out.println("Invalid input for '" + prompt + "'. Expected: a whole number without letters (e.g., 3520212345671). Please try again.");
            scanner.nextLine();
        }
    }

    private double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                String normalizedInput = input.replace(",", "");
                try {
                    double value = Double.parseDouble(normalizedInput);
                    return value;
                } catch (Exception e) {
                    System.out.println("Invalid input for '" + prompt + "'. Expected: a decimal number (e.g., 1234.56 or 18,500,000). Please try again.");
                }
            }
            System.out.println("Invalid input for '" + prompt + "'. Expected: a decimal number (e.g., 1234.56 or 18,500,000). Please try again.");
        }
    }

    private String readString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    private boolean readBoolean(Scanner scanner, String prompt) {
        while (true) {
            String value = readString(scanner, prompt + " (y/n): ").toLowerCase();
            if (value.equals("y") || value.equals("yes")) {
                return true;
            }
            if (value.equals("n") || value.equals("no")) {
                return false;
            }
            System.out.println("Invalid input for '" + prompt + "'. Please enter y (yes) or n (no). Try again.");
        }
    }

    // Validation helpers
    private long readVIN(Scanner scanner, String prompt) {
        while (true) {
            long vin = readLong(scanner, prompt);
            if (vin > 0) return vin;
            System.out.println("VIN must be a positive number. Try again.");
        }
    }

    private long readCNIC(Scanner scanner, String prompt) {
        while (true) {
            long cnic = readLong(scanner, prompt);
            String s = String.valueOf(cnic);
            if (s.length() == 13) return cnic;
            System.out.println("CNIC must be 13 digits (e.g., 3520212345671). Try again.");
        }
    }

    private String readPhoneNumber(Scanner scanner, String prompt) {
        while (true) {
            String phone = readString(scanner, prompt);
            if (phone.matches("\\d{7,15}")) return phone;
            System.out.println("Phone number must be digits only (7-15 digits). Try again.");
        }
    }

    private int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            int v = readInt(scanner, prompt);
            if (v > 0) return v;
            System.out.println("Value must be greater than 0. Try again.");
        }
    }

    private int readNonNegativeInt(Scanner scanner, String prompt) {
        while (true) {
            int v = readInt(scanner, prompt);
            if (v >= 0) return v;
            System.out.println("Value cannot be negative. Try again.");
        }
    }

    private double readNonNegativeDouble(Scanner scanner, String prompt) {
        while (true) {
            double d = readDouble(scanner, prompt);
            if (d >= 0.0) return d;
            System.out.println("Value cannot be negative. Try again.");
        }
    }

    private int readYear(Scanner scanner, String prompt) {
        int current = java.time.LocalDate.now().getYear();
        while (true) {
            int y = readInt(scanner, prompt);
            if (y >= 1900 && y <= current + 1) return y;
            System.out.println("Enter a valid year between 1900 and " + (current + 1) + ". Try again.");
        }
    }

    public void viewAllSales(){
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT * FROM Sales ORDER BY Date_Time DESC";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while(rs.next()){
                found = true;
                System.out.println("VIN: " + rs.getLong("VIN"));
                System.out.println("Customer CNIC: " + rs.getLong("CNIC"));
                System.out.println("Employee ID: " + rs.getInt("Employee_ID"));
                System.out.println("Amount Paid: " + rs.getDouble("Amount_Paid"));
                System.out.println("Date & Time: " + rs.getString("Date_Time"));
                System.out.println("----------------------------");
            }

            if(!found){
                System.out.println("No sales records found.");
            }

            con.close();
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.println();
        }
    }
}