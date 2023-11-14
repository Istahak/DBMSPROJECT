import java.sql.*;
import java.util.Scanner;

public class Customer {

    private int customerId;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private long phoneNumber;
    private String email;
    private Connection connection;
    public Customer(Connection connection){
        this.connection=connection;
    }

    public Customer(int customerId, String firstName, String lastName, int age,
                    String gender, long phoneNumber, String email) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public  Customer findOrCreateCustomer(long phoneNumber) {
        try {
            String selectQuery = "SELECT * FROM customers WHERE phone_number = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setLong(1, phoneNumber);

            ResultSet resultSet = selectStatement.executeQuery();


            if (resultSet.next()) {
                // Customer found, return the customer object
                return mapResultSetToCustomer(resultSet);
            }

            int sz=getMaxCustomerId();
            System.out.println(sz);


            // Customer not found, insert a new customer with user input
                Customer newCustomer = createCustomerFromUserInput(sz,phoneNumber);
                insertCustomer(newCustomer);
                return newCustomer;

        } catch (SQLException e) {
            System.err.println("Error finding/creating customer: " + e.getMessage());
            return null;
        }
    }

    private  Customer createCustomerFromUserInput(int id,long phoneNumber) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Customer not found. Please enter customer details:");

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Age: ");
        int age = scanner.nextInt();

        scanner.nextLine(); // Consume the newline character

        System.out.print("Gender: ");
        String gender = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        return new Customer(id+1, firstName, lastName, age, gender, phoneNumber, email);
    }

    private  void insertCustomer(Customer customer) {
        try {
            String insertQuery = "INSERT INTO customers " +
                    "(customer_id,first_name, last_name, age, gender, phone_number, email) " +
                    "VALUES (?,?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setInt(1,customer.getCustomerId());
            insertStatement.setString(2, customer.getFirstName());
            insertStatement.setString(3, customer.getLastName());
            insertStatement.setInt(4, customer.getAge());
            insertStatement.setString(5, customer.getGender());
            insertStatement.setLong(6, customer.getPhoneNumber());
            insertStatement.setString(7, customer.getEmail());

            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int customerId = generatedKeys.getInt(1);
                    System.out.println("Customer added successfully with ID: " + customerId);
                    customer.setCustomerId(customerId);
                }
            } else {
                System.out.println("Failed to add customer.");
            }

        } catch (SQLException e) {
            System.err.println("Error inserting customer: " + e.getMessage());
        }
    }

    private  Customer mapResultSetToCustomer(ResultSet resultSet) throws SQLException {
        int customerId = resultSet.getInt("customer_id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        int age = resultSet.getInt("age");
        String gender = resultSet.getString("gender");
        long phoneNumber = resultSet.getLong("phone_number");
        String email = resultSet.getString("email");

        return new Customer(customerId, firstName, lastName, age, gender, phoneNumber, email);
    }

    private  int getMaxCustomerId() {
        int maxCustomerId = -1;

        try {
            // Connection setup code...

            String selectQuery = "SELECT MAX(customer_id) AS max_id FROM customers";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            if (resultSet.next()) {
                maxCustomerId = resultSet.getInt("max_id");
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving max customer ID: " + e.getMessage());
        }

        return maxCustomerId;
    }

    public   void showAllCustomers() {
        try {
            // Connection setup code...

            String selectQuery = "SELECT * FROM customers";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Print header
            System.out.printf("%-15s%-15s%-15s%-10s%-20s%-15s%-30s%n",
                    "Customer ID", "First Name", "Last Name", "Age",
                    "Gender", "Phone Number", "Email");

            // Print customer details
            while (resultSet.next()) {
                int customerId = resultSet.getInt("customer_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                long phoneNumber = resultSet.getLong("phone_number");
                String email = resultSet.getString("email");

                System.out.printf("%-15d%-15s%-15s%-10d%-20s%-15d%-30s%n",
                        customerId, firstName, lastName, age, gender, phoneNumber, email);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving customer list: " + e.getMessage());
        }
    }
}
