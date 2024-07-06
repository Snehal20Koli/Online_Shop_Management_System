package OnlineShop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Products {
    private int pid, qty;
    private String name, type;
    private float price;

    public void productsPage() throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nWELCOME TO PRODUCTS MANAGEMENT PAGE\n");
        int ch;
        do {
            System.out.println("*****************************************************\n");
            System.out.println("1 - ADD PRODUCTS");
            System.out.println("2 - REMOVE PRODUCTS");
            System.out.println("3 - ALTER PRODUCT INFO");
            System.out.println("4 - VIEW ALL PRODUCTS");
            System.out.println("5 - SEARCH A PARTICULAR PRODUCT");
            System.out.println("6 - EXIT PAGE");
            System.out.println("*****************************************************\n");
            System.out.print("Enter choice : ");
            ch = Integer.parseInt(br.readLine());
            switch (ch) {
                case 1:
                    addProducts();
                    break;
                case 2:
                    removeProducts();
                    break;
                case 3:
                    alterProduct();
                    break;
                case 4:
                    viewProducts();
                    break;
                case 5:
                    searchProduct();
                    break;
                case 6:
                    System.out.println("Thank you");
                    break;
                default:
                    System.out.println("Wrong choice ");
                    break;
            }
        } while (ch != 6);
    }

    private void alterProduct() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {Connection con = getConnection();{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM products");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                System.out.println("NO PRODUCTS AVAILABLE");
            } else {
                do {
                    System.out.print("Enter product ID to update info (0 to exit): ");
                    int pid = Integer.parseInt(br.readLine());
                    if (pid == 0) {
                        break;
                    }

                    PreparedStatement ps1 = con.prepareStatement("SELECT * FROM products WHERE productID = ?");
                    ps1.setInt(1, pid);
                    ResultSet rs1 = ps1.executeQuery();

                    if (!rs1.next()) {
                        System.out.println("PRODUCT NOT FOUND !");
                    } else {
                        String name = rs1.getString("Name");
                        String type = rs1.getString("Type");
                        int qty = rs1.getInt("Quantity");
                        float price = rs1.getFloat("Price");

                        do {
                            System.out.println("FETCHED PRODUCT INFO :\n");
                            System.out.printf("Product ID   = %-5d\n", pid);
                            System.out.printf("Product Name = %-20s\n", name);
                            System.out.printf("Product Type = %-20s\n", type);
                            System.out.printf("Quantity     = %-5d\n", qty);
                            System.out.printf("Price        = %-10f\n", price);
                            System.out.println("\n1 - UPDATE PRODUCT NAME");
                            System.out.println("2 - UPDATE PRODUCT TYPE");
                            System.out.println("3 - UPDATE PRODUCT QUANTITY");
                            System.out.println("4 - UPDATE PRICE");
                            System.out.print("\nEnter choice : ");
                            int ch1 = Integer.parseInt(br.readLine());

                            switch (ch1) {
                                case 1:
                                    System.out.print("ENTER NEW NAME : ");
                                    name = br.readLine();
                                    break;
                                case 2:
                                    System.out.print("ENTER NEW TYPE : ");
                                    type = br.readLine();
                                    break;
                                case 3:
                                    System.out.print("ENTER NEW QUANTITY : ");
                                    qty = Integer.parseInt(br.readLine());
                                    break;
                                case 4:
                                    System.out.print("ENTER NEW PRICE : ");
                                    price = Float.parseFloat(br.readLine());
                                    break;
                                default:
                                    System.out.println("Invalid choice");
                                    break;
                            }

                            System.out.print("DO YOU WANT TO CONTINUE (Y for yes, N for no): ");
                            String chd2 = br.readLine();
                            if (!chd2.equalsIgnoreCase("Y")) {
                                break;
                            }
                        } while (true);

                        PreparedStatement ps2 = con.prepareStatement("UPDATE products SET Name = ?, Type = ?, Quantity = ?, Price = ? WHERE productID = ?");
                        ps2.setString(1, name);
                        ps2.setString(2, type);
                        ps2.setInt(3, qty);
                        ps2.setFloat(4, price);
                        ps2.setInt(5, pid);
                        int updated = ps2.executeUpdate();
                        if (updated > 0) {
                            System.out.println("PRODUCT INFO UPDATED SUCCESSFULLY !");
                        }
                    }

                    System.out.print("DO YOU WANT TO CONTINUE (Y for yes, N for no): ");
                    String chd1 = br.readLine();
                    if (!chd1.equalsIgnoreCase("Y")) {
                        break;
                    }
                } while (true);
            }}
        } catch (SQLException e) {
            System.out.println("Error altering product: " + e.getMessage());
        }
    }


    private void searchProduct() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM products WHERE productID = ?");
            int pid;

            do {
                System.out.print("Enter product ID to search (0 to exit): ");
                pid = Integer.parseInt(br.readLine());
                if (pid == 0) {
                    break;
                }
                
                ps.setInt(1, pid);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    System.out.printf("Product ID   =  %-5d\n", rs.getInt("productID"));
                    System.out.printf("Product Name =  %-20s\n", rs.getString("Name"));
                    System.out.printf("Product Type =  %-20s\n", rs.getString("Type"));
                    System.out.printf("Quantity     =  %-5d\n", rs.getInt("Quantity"));
                    System.out.printf("Price        =  %-10f\n", rs.getFloat("Price"));
                } else {
                    System.out.println("PRODUCT NOT FOUND !");
                }

                System.out.print("Do you want to continue (Y for yes, N for no): ");
                String ch = br.readLine();
                if (!ch.equalsIgnoreCase("Y")) {
                    break;
                }
            } while (true);

        } catch (SQLException e) {
            System.out.println("Error searching product: " + e.getMessage());
        }
    }


    private void removeProducts() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM products WHERE productID = ?");
            do {
                System.out.print("Enter product ID which you want to delete : ");
                pid = Integer.parseInt(br.readLine());
                ps.setInt(1, pid);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    System.out.println("PRODUCT NOT FOUND !");
                } else {
                    System.out.println("PRODUCT DELETED SUCCESSFULLY !");
                }
                System.out.print("Do you want to continue (Y for YES, N for NO): ");
                String ch = br.readLine();
                if (!ch.equalsIgnoreCase("Y")) {
                    break;
                }
            } while (true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addProducts() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO products(productID, Name, Type, Quantity, Price) VALUES (?, ?, ?, ?, ?)");
            do {
                pid = setPid();
                System.out.println("Product ID = " + pid);
                System.out.print("Enter Name : ");
                name = br.readLine();
                System.out.print("Enter Type : ");
                type = br.readLine();
                System.out.print("Enter Quantity : ");
                qty = Integer.parseInt(br.readLine());
                System.out.print("Enter price : ");
                price = Float.parseFloat(br.readLine());
                ps.setInt(1, pid);
                ps.setString(2, name);
                ps.setString(3, type);
                ps.setInt(4, qty);
                ps.setFloat(5, price);
                int inserted = ps.executeUpdate();
                if (inserted > 0) {
                    System.out.println("PRODUCT ADDED SUCCESSFULLY !");
                }
                System.out.print("Do you want to continue (Y for yes, N for no): ");
                String ch = br.readLine();
                if (!ch.equalsIgnoreCase("Y")) {
                    break;
                }
            } while (true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewProducts() {
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM products");
            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                if (count == 0) {
                    System.out.println("The products are : \n");
                    System.out.println("****************************************************************************************************************************\n");
                    System.out.printf("%-15s \t %-20s \t %-20s \t %-15s \t %-15s\n", "Product ID", "Name", "Type", "Quantity", "Price");
                    System.out.println("****************************************************************************************************************************\n");
                }
                pid = rs.getInt("productID");
                name = rs.getString("Name");
                type = rs.getString("Type");
                qty = rs.getInt("Quantity");
                price = rs.getFloat("Price");
                System.out.printf("%-15d \t %-20s \t %-20s \t %-15d \t %-15f\n", pid, name, type, qty, price);
                count++;
            }
            if (count == 0) {
                System.out.println("NO PRODUCTS AVAILABLE !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/onlineshop?autoReconnect=true&useSSL=false", "root", DatabaseConnection.root);
    }

    private int setPid() {
        int y = 999;
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT productID FROM products");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                y = rs.getInt("productID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return y + 1;
    }
}
