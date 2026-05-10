import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {

                    System.out.println("""
                                    ██     ██ ███████ ██       ██████  ██████  ███    ███ ███████     ████████  ██████                          
                                    ██     ██ ██      ██      ██      ██    ██ ████  ████ ██             ██    ██    ██                         
                                    ██  █  ██ █████   ██      ██      ██    ██ ██ ████ ██ █████          ██    ██    ██                         
                                    ██ ███ ██ ██      ██      ██      ██    ██ ██  ██  ██ ██             ██    ██    ██                         
                                     ███ ███  ███████ ███████  ██████  ██████  ██      ██ ███████        ██     ██████                          
                                                                                                                                                
                                                                                                                                                
            ████████  ██████  ██    ██  ██████  ████████  █████      ██████  ███████  █████  ██      ███████ ██████  ███████ ██   ██ ██ ██████  
               ██    ██    ██  ██  ██  ██    ██    ██    ██   ██     ██   ██ ██      ██   ██ ██      ██      ██   ██ ██      ██   ██ ██ ██   ██ 
               ██    ██    ██   ████   ██    ██    ██    ███████     ██   ██ █████   ███████ ██      █████   ██████  ███████ ███████ ██ ██████  
               ██    ██    ██    ██    ██    ██    ██    ██   ██     ██   ██ ██      ██   ██ ██      ██      ██   ██      ██ ██   ██ ██ ██      
               ██     ██████     ██     ██████     ██    ██   ██     ██████  ███████ ██   ██ ███████ ███████ ██   ██ ███████ ██   ██ ██ ██      
            """);

        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();

        int userOption;
        do {
            System.out.println("Please select your designation from the below options:");
            System.out.println("1. Customer");
            System.out.println("2. Employee");
            System.out.println("3. Exit");
            System.out.print("Select option no: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
                sc.nextLine();
                System.out.print("Select option no: ");
            }

            userOption = sc.nextInt();
            sc.nextLine();

            switch (userOption) {
                case 1:
                    menu.showCustomerMenu(sc);
                    break;
                case 2:
                    int employeeId;
                    boolean validEmployee = false;
                    do {
                        System.out.print("Enter Employee ID: ");
                        while (!sc.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a numeric Employee ID.");
                            sc.nextLine();
                            System.out.print("Enter Employee ID: ");
                        }
                        employeeId = sc.nextInt();
                        sc.nextLine();

                        if (employeeId <= 0) {
                            System.out.println("Employee ID must be greater than 0.");
                            validEmployee = false;
                            continue;
                        }

                        validEmployee = employeeExists(employeeId);
                        if (!validEmployee) {
                            System.out.println("Invalid Employee ID. Access denied.");
                        }
                    } while (!validEmployee);

                    menu.showEmployeeMenu(sc, employeeId);
                    break;
                case 3:
                    System.out.println("Thank you for using our Car Dealership Management System!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        } while (userOption != 3);

        sc.close();
    }

    private static boolean employeeExists(int employeeId) {
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
}