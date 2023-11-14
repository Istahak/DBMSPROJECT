import java.sql.*;

public class Mysqldbdriver {

   public  Connection connection = null;

    public Mysqldbdriver() {

        String url = "jdbc:mysql://localhost:3306/project";
        String user = "admin";
        String password = "Ssc250799&";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try {
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connected to the database!");

            } catch (SQLException e) {
                System.err.println("Error connecting to the database: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) {
        new Mysqldbdriver();


    }
}
