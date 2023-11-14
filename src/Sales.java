import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Sales {

    private int saleId;
    private int customerId;
    private Date saleDate;
    private Timestamp saleTime;
    private double totalAmount;
    private int employeeId;
    private Connection connection;

    public Sales(Connection connection) {
        this.connection = connection;
    }

    public Sales(int saleId, int customerId, Date saleDate, Timestamp saleTime, double totalAmount, int employeeId) {
        this.saleId = saleId;
        this.customerId = customerId;
        this.saleDate = saleDate;
        this.saleTime = saleTime;
        this.totalAmount = totalAmount;
        this.employeeId = employeeId;
    }
    // Getters and setters...

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Timestamp getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(Timestamp saleTime) {
        this.saleTime = saleTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    // Methods...

    public void addSaleFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Sale Details:");

        System.out.print("Customer ID: ");
        int customerId = scanner.nextInt();

        System.out.print("Sales ID: ");
        int saleid = scanner.nextInt();

        System.out.print("Employee ID: ");
        int employeeId = scanner.nextInt();

        // Additional sale details can be added as per your requirements

        Date saleDate = new Date(System.currentTimeMillis());
        Timestamp saleTime = new Timestamp(System.currentTimeMillis());

        Sales sale = new Sales(saleId, customerId, saleDate, saleTime, getTotalPriceForSale(saleid), employeeId);
        addSale(sale);

    }

    private void addSale(Sales sale) {
        try {
            String insertQuery = "INSERT INTO sales " +
                    "(customer_id, sale_date, sale_time, total_amount, employee_id) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setInt(1, sale.getCustomerId());
            insertStatement.setDate(2, (java.sql.Date) sale.getSaleDate());
            insertStatement.setTimestamp(3, sale.getSaleTime());
            insertStatement.setDouble(4, sale.getTotalAmount());
            insertStatement.setInt(5, sale.getEmployeeId());

            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int saleId = generatedKeys.getInt(1);
                    System.out.println("Sale added successfully with ID: " + saleId);
                    sale.setSaleId(saleId);
                }
            } else {
                System.out.println("Failed to add sale.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding sale: " + e.getMessage());
        }
    }

    public int getNextSaleId() {
        try {
            String selectQuery = "SELECT MAX(sale_id) AS max_sale_id FROM sales";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            if (resultSet.next()) {
                int maxSaleId = resultSet.getInt("max_sale_id");
                return maxSaleId + 1;
            } else {
                return 1; // If no sales exist yet, start from 1
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving max sale ID: " + e.getMessage());
            return -1; // Handle the error case appropriately in your application
        }
    }

    public double getTotalPriceForSale(int saleId) {
        try {
            String selectQuery = "SELECT SUM(total_price) AS total_price FROM sales_items WHERE sale_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, saleId);

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                double totalPrice = resultSet.getDouble("total_price");
                return totalPrice;
            } else {
                return 0.0; // Return 0 if no items are found for the sale
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving total price for sale: " + e.getMessage());
            return -1.0; // Handle the error case appropriately in your application
        }
    }

    // Other methods...
}
