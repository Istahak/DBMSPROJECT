import java.sql.*;
import java.util.Scanner;

public class Medicine {

    private int medicineId;
    private String medicineName;
    private int locationRack;
    public Connection connection;
    public Medicine(Connection connection){
        this.connection=connection;
    }

    public Medicine(int medicineId, String medicineName, int locationRack) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.locationRack = locationRack;
    }

    // Getters and setters...


    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getLocationRack() {
        return locationRack;
    }

    public void setLocationRack(int locationRack) {
        this.locationRack = locationRack;
    }

    public static Medicine createMedicineFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Medicine Details:");

        System.out.print("Medicine ID: ");
        int medicineId = scanner.nextInt();

        scanner.nextLine(); // Consume the newline character

        System.out.print("Medicine Name: ");
        String medicineName = scanner.nextLine();

        System.out.print("Location Rack: ");
        int locationRack = scanner.nextInt();

        return new Medicine(medicineId, medicineName, locationRack);
    }

    public  void addMedicine( ) {
        Medicine medicine=createMedicineFromUserInput();
        try {
            String insertQuery = "INSERT INTO medicines " +
                    "(medicine_id, medicine_name, location_rack) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, medicine.getMedicineId());
            preparedStatement.setString(2, medicine.getMedicineName());
            preparedStatement.setInt(3, medicine.getLocationRack());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Medicine added successfully!");
            } else {
                System.out.println("Failed to add medicine.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding medicine: " + e.getMessage());
        }
    }

    public  void showAllMedicines() {
        try {
            String selectQuery = "SELECT * FROM medicines";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Print header
            System.out.printf("%-15s%-50s%-15s%n", "Medicine ID", "Medicine Name", "Location Rack");

            // Print medicine details
            while (resultSet.next()) {
                int medicineId = resultSet.getInt("medicine_id");
                String medicineName = resultSet.getString("medicine_name");
                int locationRack = resultSet.getInt("location_rack");

                System.out.printf("%-15d%-50s%-15d%n", medicineId, medicineName, locationRack);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving medicine list: " + e.getMessage());
        }
    }


}
