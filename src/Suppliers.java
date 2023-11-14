import java.sql.*;
import java.util.Scanner;

public class Suppliers {

    private int supplierId;
    private String supplierName;
    private String supplierCompanyName;
    private String supplierAddress;
    private long supplierPhoneNumber;
    private String supplierEmail;
    private Connection connection;

    public Suppliers(Connection connection) {
        this.connection = connection;
    }

    public Suppliers(int supplierId, String supplierName, String supplierCompanyName,
                     String supplierAddress, long supplierPhoneNumber, String supplierEmail) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierCompanyName = supplierCompanyName;
        this.supplierAddress = supplierAddress;
        this.supplierPhoneNumber = supplierPhoneNumber;
        this.supplierEmail = supplierEmail;
    }

    // Getters and setters...

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public long getSupplierPhoneNumber() {
        return supplierPhoneNumber;
    }

    public void setSupplierPhoneNumber(long supplierPhoneNumber) {
        this.supplierPhoneNumber = supplierPhoneNumber;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    // Methods...

    public Suppliers findOrCreateSupplier(long supplierPhoneNumber) {
        try {
            String selectQuery = "SELECT * FROM suppliers WHERE supplier_phone_number = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setLong(1, supplierPhoneNumber);

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Supplier found, return the supplier object
                return mapResultSetToSupplier(resultSet);
            }

            int newSupplierId = getMaxSupplierId() + 1;

            // Supplier not found, insert a new supplier with user input
            Suppliers newSupplier = createSupplierFromUserInput(newSupplierId, supplierPhoneNumber);
            insertSupplier(newSupplier);
            return newSupplier;

        } catch (SQLException e) {
            System.err.println("Error finding/creating supplier: " + e.getMessage());
            return null;
        }
    }

    private Suppliers createSupplierFromUserInput(int id, long supplierPhoneNumber) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Supplier not found. Please enter supplier details:");

        System.out.print("Supplier Name: ");
        String supplierName = scanner.nextLine();

        System.out.print("Supplier Company Name: ");
        String supplierCompanyName = scanner.nextLine();

        System.out.print("Supplier Address: ");
        String supplierAddress = scanner.nextLine();

        System.out.print("Supplier Email: ");
        String supplierEmail = scanner.nextLine();

        return new Suppliers(id, supplierName, supplierCompanyName, supplierAddress, supplierPhoneNumber, supplierEmail);
    }

    private void insertSupplier(Suppliers supplier) {
        try {
            String insertQuery = "INSERT INTO suppliers " +
                    "(supplier_id, supplier_name, supplier_company_name, supplier_address, supplier_phone_number, supplier_email) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, supplier.getSupplierId());
            insertStatement.setString(2, supplier.getSupplierName());
            insertStatement.setString(3, supplier.getSupplierCompanyName());
            insertStatement.setString(4, supplier.getSupplierAddress());
            insertStatement.setLong(5, supplier.getSupplierPhoneNumber());
            insertStatement.setString(6, supplier.getSupplierEmail());

            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Supplier added successfully with ID: " + supplier.getSupplierId());
            } else {
                System.out.println("Failed to add supplier.");
            }

        } catch (SQLException e) {
            System.err.println("Error inserting supplier: " + e.getMessage());
        }
    }

    private Suppliers mapResultSetToSupplier(ResultSet resultSet) throws SQLException {
        int supplierId = resultSet.getInt("supplier_id");
        String supplierName = resultSet.getString("supplier_name");
        String supplierCompanyName = resultSet.getString("supplier_company_name");
        String supplierAddress = resultSet.getString("supplier_address");
        long supplierPhoneNumber = resultSet.getLong("supplier_phone_number");
        String supplierEmail = resultSet.getString("supplier_email");

        return new Suppliers(supplierId, supplierName, supplierCompanyName, supplierAddress, supplierPhoneNumber, supplierEmail);
    }

    private int getMaxSupplierId() {
        int maxSupplierId = -1;

        try {
            String selectQuery = "SELECT MAX(supplier_id) AS max_id FROM suppliers";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            if (resultSet.next()) {
                maxSupplierId = resultSet.getInt("max_id");
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving max supplier ID: " + e.getMessage());
        }

        return maxSupplierId;
    }

    public void showAllSuppliers() {
        try {
            String selectQuery = "SELECT * FROM suppliers";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Print header
            System.out.printf("%-15s%-25s%-50s%-30s%-15s%-40s%n",
                    "Supplier ID", "Supplier Name", "Company Name", "Supplier Address", "Phone Number", "Email");

            // Print supplier details
            while (resultSet.next()) {
                int supplierId = resultSet.getInt("supplier_id");
                String supplierName = resultSet.getString("supplier_name");
                String supplierCompanyName = resultSet.getString("supplier_company_name");
                String supplierAddress = resultSet.getString("supplier_address");
                long supplierPhoneNumber = resultSet.getLong("supplier_phone_number");
                String supplierEmail = resultSet.getString("supplier_email");

                System.out.printf("%-15d%-25s%-50s%-30s%-15d%-40s%n",
                        supplierId, supplierName, supplierCompanyName, supplierAddress, supplierPhoneNumber, supplierEmail);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving supplier list: " + e.getMessage());
        }
    }
}
