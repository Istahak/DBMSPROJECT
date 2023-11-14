import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class SalaryPayments {

    private int paymentId;
    private int employeeId;
    private Date paymentMonth;
    private Date paymentDate;
    private double amount;
    private Connection connection;

    public SalaryPayments(Connection connection) {
        this.connection = connection;
    }


    // Getters and setters...


    public SalaryPayments(int paymentId, int employeeId, Date paymentMonth, Date paymentDate, double amount) {
        this.paymentId = paymentId;
        this.employeeId = employeeId;
        this.paymentMonth = paymentMonth;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(Date paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    // Methods...

    public void addSalaryPaymentFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Salary Payment Details:");

        System.out.print("Employee ID: ");
        int employeeId = scanner.nextInt();

        System.out.print("Payment Month (YYYY-MM): ");
        String paymentMonthInput = scanner.next();
        Date paymentMonth = parseDate(paymentMonthInput);

        System.out.print("Payment Date (YYYY-MM-DD): ");
        String paymentDateInput = scanner.next();
        Date paymentDate = parseDate(paymentDateInput);

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();

        SalaryPayments salaryPayment = new SalaryPayments(0, employeeId, paymentMonth, paymentDate, amount);
        addSalaryPayment(salaryPayment);
    }

    private void addSalaryPayment(SalaryPayments salaryPayment) {
        try {
            // Get the maximum payment ID and increment it by one
            int newPaymentId = getMaxPaymentId() + 1;

            String insertQuery = "INSERT INTO salary_payments " +
                    "(payment_id, employee_id, payment_month, payment_date, amount) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, newPaymentId);
            insertStatement.setInt(2, salaryPayment.getEmployeeId());
            insertStatement.setDate(3, new java.sql.Date(salaryPayment.getPaymentMonth().getTime()));
            insertStatement.setDate(4, new java.sql.Date(salaryPayment.getPaymentDate().getTime()));
            insertStatement.setDouble(5, salaryPayment.getAmount());

            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Salary Payment added successfully with ID: " + newPaymentId);
                salaryPayment.setPaymentId(newPaymentId);
            } else {
                System.out.println("Failed to add Salary Payment.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding Salary Payment: " + e.getMessage());
        }
    }

    private int getMaxPaymentId() {
        int maxPaymentId = -1;

        try {
            String selectQuery = "SELECT MAX(payment_id) AS max_id FROM salary_payments";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            if (resultSet.next()) {
                maxPaymentId = resultSet.getInt("max_id");
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving max payment ID: " + e.getMessage());
        }

        return maxPaymentId;
    }

    private Date parseDate(String dateInput) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            return dateFormat.parse(dateInput);
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }

    public void checkPaymentStatus(int employeeId, Date paymentMonth) {
        try {
            String selectQuery = "SELECT * FROM salary_payments " +
                    "WHERE employee_id = ? AND payment_month = ?";

            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, employeeId);
            selectStatement.setDate(2, new java.sql.Date(paymentMonth.getTime()));

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Payment Status: Paid");
            } else {
                System.out.println("Payment Status: Unpaid");
            }

        } catch (SQLException e) {
            System.err.println("Error checking payment status: " + e.getMessage());
        }
    }


    public void showAllSalaryPayments() {
        try {
            String selectQuery = "SELECT * FROM salary_payments";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Print header
            System.out.printf("%-15s%-15s%-20s%-20s%-15s%n",
                    "Payment ID", "Employee ID", "Payment Month", "Payment Date", "Amount");

            // Print salary payments
            while (resultSet.next()) {
                int paymentId = resultSet.getInt("payment_id");
                int employeeId = resultSet.getInt("employee_id");
                Date paymentMonth = resultSet.getDate("payment_month");
                Date paymentDate = resultSet.getDate("payment_date");
                double amount = resultSet.getDouble("amount");

                System.out.printf("%-15d%-15d%-20s%-20s%-15.2f%n",
                        paymentId, employeeId, formatDate(paymentMonth), formatDate(paymentDate), amount);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving salary payments list: " + e.getMessage());
        }
    }

    // Helper method to format Date to String
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
