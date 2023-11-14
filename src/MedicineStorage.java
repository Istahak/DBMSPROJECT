import java.sql.*;
import java.util.Scanner;

public class MedicineStorage {

    private int storeId;
    private int medicineId;
    private int purchaseId;
    private int medQty;
    private double medPrice;
    private Date expDate;
    private Connection connection;

    public MedicineStorage(Connection connection) {
        this.connection = connection;
    }

    public MedicineStorage(int storeId, int medicineId, int purchaseId, int medQty,
                           double medPrice, Date expDate) {
        this.storeId = storeId;
        this.medicineId = medicineId;
        this.purchaseId = purchaseId;
        this.medQty = medQty;
        this.medPrice = medPrice;
        this.expDate = expDate;
    }

    // Getters and setters...

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getMedQty() {
        return medQty;
    }

    public void setMedQty(int medQty) {
        this.medQty = medQty;
    }

    public double getMedPrice() {
        return medPrice;
    }

    public void setMedPrice(double medPrice) {
        this.medPrice = medPrice;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    // Methods...

    public void showAllMedicineStorage() {
        try {
            String selectQuery = "SELECT * FROM medicine_storage";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Print header
            System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%n",
                    "Store ID", "Medicine ID", "Purchase ID", "Med Qty", "Med Price", "Exp Date");

            // Print medicine storage details
            while (resultSet.next()) {
                int storeId = resultSet.getInt("STORE_ID");
                int medicineId = resultSet.getInt("medicine_id");
                int purchaseId = resultSet.getInt("purchase_id");
                int medQty = resultSet.getInt("med_qty");
                double medPrice = resultSet.getDouble("med_price");
                Date expDate = resultSet.getDate("exp_date");

                System.out.printf("%-15d%-15d%-15d%-15d%-15.2f%-15s%n",
                        storeId, medicineId, purchaseId, medQty, medPrice, expDate);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving medicine storage list: " + e.getMessage());
        }
    }

    // Other methods...
}
