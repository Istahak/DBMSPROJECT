import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String gender;
    private String employeeType;
    private Date joinDate;
    private double salary;
    private long phoneNumber;
    private String email;
    private String address;
    private Connection connection;

    public Employee(Connection connection){
        this.connection=connection;
    }

    public Employee(int employeeId, String firstName, String lastName, Date birthDate, String gender,
                    String employeeType, Date joinDate, double salary, long phoneNumber, String email, String address) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.employeeType = employeeType;
        this.joinDate = joinDate;
        this.salary = salary;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    // Getters and setters...

    public static Employee createEmployeeFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Employee Details:");

        System.out.print("Employee ID: ");
        int employeeId = scanner.nextInt();

        scanner.nextLine(); // Consume the newline character

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Birth Date (yyyy-MM-dd): ");
        Date birthDate = null;
        try {
            birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.print("Gender: ");
        String gender = scanner.nextLine();

        System.out.print("Employee Type: ");
        String employeeType = scanner.nextLine();

        System.out.print("Join Date (yyyy-MM-dd): ");
        Date joinDate = null;
        try {
            joinDate = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.print("Salary: ");
        double salary = scanner.nextDouble();

        System.out.print("Phone Number: ");
        long phoneNumber = scanner.nextLong();

        scanner.nextLine(); // Consume the newline character

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        return new Employee(employeeId, firstName, lastName, birthDate, gender,
                employeeType, joinDate, salary, phoneNumber, email, address);
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private  void addEmployee(Employee employee) {
        try {


            String insertQuery = "INSERT INTO employees " +
                    "(employee_id, first_name, last_name, birth_date, gender, employee_type, join_date, salary, phone_number, email, address) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, employee.getEmployeeId());
            preparedStatement.setString(2, employee.getFirstName());
            preparedStatement.setString(3, employee.getLastName());
            preparedStatement.setDate(4, new java.sql.Date(employee.getBirthDate().getTime()));
            preparedStatement.setString(5, employee.getGender());
            preparedStatement.setString(6, employee.getEmployeeType());
            preparedStatement.setDate(7, new java.sql.Date(employee.getJoinDate().getTime()));
            preparedStatement.setDouble(8, employee.getSalary());
            preparedStatement.setLong(9, employee.getPhoneNumber());
            preparedStatement.setString(10, employee.getEmail());
            preparedStatement.setString(11, employee.getAddress());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee added successfully!");
            } else {
                System.out.println("Failed to add employee.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
        }
    }

    public  void showAllEmployees() {
        try {
            // Connection setup code...

            String selectQuery = "SELECT * FROM employees";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Print header
            System.out.printf("%-15s%-15s%-15s%-15s%-10s%-20s%-15s%-15s%-15s%-30s%-30s%n",
                    "Employee ID", "First Name", "Last Name", "Birth Date", "Gender",
                    "Employee Type", "Join Date", "Salary", "Phone Number", "Email", "Address");

            // Print employee details
            while (resultSet.next()) {
                int employeeId = resultSet.getInt("employee_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                java.sql.Date birthDate = resultSet.getDate("birth_date");
                String gender = resultSet.getString("gender");
                String employeeType = resultSet.getString("employee_type");
                java.sql.Date joinDate = resultSet.getDate("join_date");
                double salary = resultSet.getDouble("salary");
                long phoneNumber = resultSet.getLong("phone_number");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");

                System.out.printf("%-15d%-15s%-15s%-15s%-10s%-20s%-15s%-15.2f%-15d%-30s%-30s%n",
                        employeeId, firstName, lastName, birthDate, gender,
                        employeeType, joinDate, salary, phoneNumber, email, address);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving employee list: " + e.getMessage());
        }
    }

    public  void addEmployeeWithUserInput() {
        Employee employee = Employee.createEmployeeFromUserInput();
        addEmployee(employee);
    }


}
