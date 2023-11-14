import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class SalesItems {

    private int saleId;
    private int medicineId;
    private int saleQuantity;
    private double totalPrice;
    private Connection connection;

    public SalesItems(Connection connection) {
        this.connection = connection;
    }

    public SalesItems(int saleId, int medicineId, int saleQuantity, double totalPrice) {
        this.saleId = saleId;
        this.medicineId = medicineId;
        this.saleQuantity = saleQuantity;
        this.totalPrice = totalPrice;
    }
    // Getters and setters...

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public int getSaleQuantity() {
        return saleQuantity;
    }

    public void setSaleQuantity(int saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    // Methods...

    public void addSalesItemsFromUserInput(int saleId) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Sale Items Details:");

        System.out.print("Medicine ID: ");
        int medicineId = scanner.nextInt();

        System.out.print("Sale Quantity: ");
        int saleQuantity = scanner.nextInt();

        SalesItems salesItems = new SalesItems(saleId, medicineId, saleQuantity, 0.0);
        addSalesItems(salesItems);
    }

    private void addSalesItems(SalesItems salesItems) {
        try {
            // Check if the medicine is available in the storage with the required quantity and a valid expiration date
            if (isMedicineAvailable(salesItems.getMedicineId(), salesItems.getSaleQuantity())) {
                // Get the price per medicine from the medicine_storage table
                double pricePerMedicine = getPricePerMedicine(salesItems.getMedicineId());

                // Calculate the total price for the sale item
                double totalPrice = pricePerMedicine * salesItems.getSaleQuantity();

                // Update the total price in the SalesItems object
                salesItems.setTotalPrice(totalPrice);

                // Insert the sale item into the sales_items table
                String insertQuery = "INSERT INTO sales_items " +
                        "(sale_id, medicine_id, sale_quantity, total_price) " +
                        "VALUES (?, ?, ?, ?)";

                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, salesItems.getSaleId());
                insertStatement.setInt(2, salesItems.getMedicineId());
                insertStatement.setInt(3, salesItems.getSaleQuantity());
                insertStatement.setDouble(4, salesItems.getTotalPrice());

                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Sale Item added successfully!");
                } else {
                    System.out.println("Failed to add Sale Item.");
                }
                updateMedicineStorageAfterSale(salesItems.getMedicineId(),salesItems.getSaleQuantity());
            } else {
                System.out.println("Medicine not available in the required quantity or expired.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding Sale Item: " + e.getMessage());
        }
    }

    private boolean isMedicineAvailable(int medicineId, int requiredQuantity) {
        try {
            String selectQuery = "SELECT medicine_id, SUM(MED_QTY) AS total_quantity " +
                    "FROM medicine_storage " +
                    "WHERE medicine_id = ? AND EXP_DATE >= CURRENT_DATE " +
                    "GROUP BY medicine_id " +
                    "HAVING total_quantity >= ?";

            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, medicineId);
            selectStatement.setInt(2, requiredQuantity);

            ResultSet resultSet = selectStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            System.err.println("Error checking medicine availability: " + e.getMessage());
            return false;
        }
    }


    private double getPricePerMedicine(int medicineId) {
        try {
            String selectQuery = "SELECT MED_PRICE FROM medicine_storage " +
                    "WHERE medicine_id = ?";

            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, medicineId);

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("MED_PRICE");
            } else {
                return 0.0;
            }

        } catch (SQLException e) {
            System.err.println("Error getting price per medicine: " + e.getMessage());
            return 0.0;
        }
    }

    private void updateMedicineStorageAfterSale(int medicineId, int soldQuantity) {
        try {
            // Update the MED_QTY in medicine_storage for the given medicine_id
            String updateQuery = "UPDATE medicine_storage " +
                    "SET MED_QTY = MED_QTY - ? " +
                    "WHERE medicine_id = ? AND EXP_DATE >= CURRENT_DATE " +
                    "ORDER BY EXP_DATE ASC " +
                    "LIMIT ?";

            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, soldQuantity);
            updateStatement.setInt(2, medicineId);
            updateStatement.setInt(3, soldQuantity); // Limit to the sold quantity

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Medicine storage updated after sale.");
            } else {
                System.out.println("Failed to update medicine storage after sale.");
            }

        } catch (SQLException e) {
            System.err.println("Error updating medicine storage after sale: " + e.getMessage());
        }
    }


    // Other methods...
}
