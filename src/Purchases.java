import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Purchases {

    private int purchaseId;
    private int supplierId;
    private int medicineId;
    private int quantity;
    private double totalCost;
    private Date purchaseDate;
    private Connection connection;

    public Purchases(Connection connection) {
        this.connection = connection;
    }

    public Purchases(int purchaseId, int supplierId, int medicineId, int quantity,
                     double totalCost, Date purchaseDate) {
        this.purchaseId = purchaseId;
        this.supplierId = supplierId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.purchaseDate = purchaseDate;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
// Getters and setters...

    // Methods...

    public void addPurchaseFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Purchase Details:");

        System.out.print("Purchase ID: ");
        int purchaseId = scanner.nextInt();

        System.out.print("Supplier ID: ");
        int supplierId = scanner.nextInt();

        System.out.print("Medicine ID: ");
        int medicineId = scanner.nextInt();

        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("Total Cost: ");
        double totalCost = scanner.nextDouble();

        scanner.nextLine(); // Consume the newline character

        System.out.print("Purchase Date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        Date purchaseDate = parseDate(dateStr);

        System.out.print("Expiration Date (YYYY-MM-DD): ");
        String expDateStr = scanner.nextLine();
        Date expDate = parseDate(expDateStr);

        Purchases purchase = new Purchases(purchaseId, supplierId, medicineId, quantity, totalCost, purchaseDate);
        addPurchase(purchase, expDate);
        updateMedicineStorage(purchase, expDate);
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }

    private void addPurchase(Purchases purchase, Date expDate) {
        try {
            String insertQuery = "INSERT INTO purchases " +
                    "(purchase_id, supplier_id, medicine_id, quantity, cost, purchase_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, purchase.getPurchaseId());
            insertStatement.setInt(2, purchase.getSupplierId());
            insertStatement.setInt(3, purchase.getMedicineId());
            insertStatement.setInt(4, purchase.getQuantity());
            insertStatement.setDouble(5, purchase.getTotalCost());
            insertStatement.setDate(6, new java.sql.Date(purchase.getPurchaseDate().getTime()));

            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Purchase added successfully!");
            } else {
                System.out.println("Failed to add purchase.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding purchase: " + e.getMessage());
        }
    }

    private void updateMedicineStorage(Purchases purchase, Date expDate) {
        try {
            // Calculate the price per medicine with a 5% interest
            double pricePerMedicine = purchase.getTotalCost() / purchase.getQuantity();
            double pricePerMedicineWithInterest = pricePerMedicine * 1.05;

            // Get the maximum store ID and increment it by one
            int newStoreId = getMaxStoreId() + 1;

            String insertQuery = "INSERT INTO medicine_storage " +
                    "(store_id, medicine_id, purchase_id, med_qty, med_price, exp_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, newStoreId);
            insertStatement.setInt(2, purchase.getMedicineId());
            insertStatement.setInt(3, purchase.getPurchaseId());
            insertStatement.setInt(4, purchase.getQuantity());
            insertStatement.setDouble(5, pricePerMedicineWithInterest);
            insertStatement.setDate(6, new java.sql.Date(expDate.getTime()));

            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Medicine Storage updated successfully!");
            } else {
                System.out.println("Failed to update Medicine Storage.");
            }

        } catch (SQLException e) {
            System.err.println("Error updating Medicine Storage: " + e.getMessage());
        }
    }

    private int getMaxStoreId() {
        int maxStoreId = -1;

        try {
            String selectQuery = "SELECT MAX(STORE_ID) AS max_store_id FROM medicine_storage";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            if (resultSet.next()) {
                maxStoreId = resultSet.getInt("max_store_id");
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving max store ID: " + e.getMessage());
        }

        return maxStoreId;
    }

    // Other methods...
}
