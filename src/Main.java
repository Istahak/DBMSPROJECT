public class Main {
    public static void main(String[] args) {

        Mysqldbdriver mysql=new Mysqldbdriver();
        // add a new employee;
//        mysql.addEmployeeWithUserInput();
        // show all employees list
//        Employee employee=new Employee(mysql.connection);
//
//        employee.showAllEmployees();
    // customer find or create
//        Customer customer = new Customer(mysql.connection);
//        customer =customer.findOrCreateCustomer(1723129634);
//        System.out.println(customer.getFirstName());
//        customer.showAllCustomers();

//        Medicine medicine=new Medicine(mysql.connection);
//        medicine.showAllMedicines();
//        medicine.addMedicine();


//        MedicineDetails medicineDetails=new MedicineDetails(mysql.connection);
//        medicineDetails.showAllMedicineDetails();
//        medicineDetails.addMedicineDetails();
//        Suppliers suppliers=new Suppliers(mysql.connection);
//        System.out.println(suppliers.findOrCreateSupplier(1723423523).getSupplierId());
//        Purchases purchases =new Purchases(mysql.connection);
//        purchases.addPurchaseFromUserInput();

        MedicineStorage medicineStorage=new MedicineStorage(mysql.connection);
        medicineStorage.showAllMedicineStorage();

    }
}