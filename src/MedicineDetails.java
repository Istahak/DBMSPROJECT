import java.sql.*;
import java.util.Scanner;

public class MedicineDetails {

    private int medDetailsId;
    private int medicineId;
    private String companyName;
    private String diseaseTreated;
    private String genericType;

    private Connection connection;  // Global Connection variable

    public MedicineDetails(int medDetailsId, int medicineId, String companyName, String diseaseTreated, String genericType) {
        this.medDetailsId = medDetailsId;
        this.medicineId = medicineId;
        this.companyName = companyName;
        this.diseaseTreated = diseaseTreated;
        this.genericType = genericType;
    }
    public MedicineDetails(Connection connection){
        this.connection=connection;
    }

    public int getMedDetailsId() {
        return medDetailsId;
    }

    public void setMedDetailsId(int medDetailsId) {
        this.medDetailsId = medDetailsId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDiseaseTreated() {
        return diseaseTreated;
    }

    public void setDiseaseTreated(String diseaseTreated) {
        this.diseaseTreated = diseaseTreated;
    }

    public String getGenericType() {
        return genericType;
    }

    public void setGenericType(String genericType) {
        this.genericType = genericType;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    // Getters and setters...



    private MedicineDetails createMedicineDetailsFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Medicine Details:");

        System.out.print("Medicine Details ID: ");
        int medDetailsId = scanner.nextInt();

        System.out.print("Medicine ID: ");
        int medicineId = scanner.nextInt();

        scanner.nextLine(); // Consume the newline character

        System.out.print("Company Name: ");
        String companyName = scanner.nextLine();

        System.out.print("Disease Treated: ");
        String diseaseTreated = scanner.nextLine();

        System.out.print("Generic Type: ");
        String genericType = scanner.nextLine();

        return new MedicineDetails(medDetailsId, medicineId, companyName, diseaseTreated, genericType);
    }

    public void addMedicineDetails() {
        MedicineDetails medicineDetails=createMedicineDetailsFromUserInput();

        try {
            String insertQuery = "INSERT INTO medicine_details " +
                    "(MED_DETAILS_ID, medicine_id, COMPANY_NAME, DISEASE_TREATED, GENERIC_TYPE) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, medicineDetails.getMedDetailsId());
            preparedStatement.setInt(2, medicineDetails.getMedicineId());
            preparedStatement.setString(3, medicineDetails.getCompanyName());
            preparedStatement.setString(4, medicineDetails.getDiseaseTreated());
            preparedStatement.setString(5, medicineDetails.getGenericType());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Medicine details added successfully!");
            } else {
                System.out.println("Failed to add medicine details.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding medicine details: " + e.getMessage());
        }
    }

    public  void showAllMedicineDetails() {
        try {
            String selectQuery = "SELECT * FROM medicine_details";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Print header
            System.out.printf("%-15s%-15s%-50s%-100s%-50s%n",
                    "Medicine Details ID", "Medicine ID", "Company Name", "Disease Treated", "Generic Type");

            // Print medicine details
            while (resultSet.next()) {
                int medDetailsId = resultSet.getInt("MED_DETAILS_ID");
                int medicineId = resultSet.getInt("medicine_id");
                String companyName = resultSet.getString("COMPANY_NAME");
                String diseaseTreated = resultSet.getString("DISEASE_TREATED");
                String genericType = resultSet.getString("GENERIC_TYPE");

                System.out.printf("%-15d%-15d%-50s%-100s%-50s%n",
                        medDetailsId, medicineId, companyName, diseaseTreated, genericType);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving medicine details list: " + e.getMessage());
        }
    }

    // Main method and other code...
}
