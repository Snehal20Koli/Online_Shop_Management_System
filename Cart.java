package OnlineShop;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Cart {
	private ArrayList<Integer> pid=new ArrayList<Integer>();
	private ArrayList<String> pname=new ArrayList<String>();
	private ArrayList<String> ptype=new ArrayList<String>();
	private ArrayList<Integer> qpur=new ArrayList<Integer>();
	private ArrayList<Float> qprice=new ArrayList<Float>();
	public ArrayList<Integer> getpid()throws IOException{

        return pid;
    }

    public ArrayList<String> getpname() {
        return pname;
    }

    public ArrayList<Integer> getpqty() {
        return qpur;
    }

    public ArrayList<Float> getprice() {
        return qprice;
    }

    public void addToCart(int p_id, String p_name, String p_type, int q_pur, float q_price) {
        pid.add(p_id);
        pname.add(p_name);
        ptype.add(p_type);
        qpur.add(q_pur);
        qprice.add(q_price);
    }

    public void viewCart() {
        int x = pid.size();
        if (x != 0) {
            System.out.println("YOUR CART IS : \n");
            System.out.println("***********************************************************************************************************************\n");
            System.out.printf("%-20s \t %-20s \t %-20s \t %-20s \t %-20s\n", "Product_ID", "Product_Name", "Product_Type", "Quantity_Purchased", "Total_Price");
            System.out.println("***********************************************************************************************************************\n");
            for (int i = 0; i < x; i++) {
                System.out.printf("%-20d \t %-20s \t %-20s \t %-20d \t %-20f\n", pid.get(i), pname.get(i), ptype.get(i), qpur.get(i), qprice.get(i));
            }
            System.out.println("***********************************************************************************************************************\n");
        } else {
            System.out.println("CART IS EMPTY !");
        }
    }


	public void removeFromCart(int p_id) throws IOException {
	    try {
	        // Remove item from lists
	        int res = pid.indexOf(p_id);
	        if (res == -1) {
	            System.out.println("YOU HAVE NOT PURCHASED THIS PRODUCT !");
	            return;
	        }
	        pid.remove(res);
	        pname.remove(res);
	        ptype.remove(res);
	        qprice.remove(res);

	        // Update database
	        Class.forName("com.mysql.jdbc.Driver");
	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlineshop?autoReconnect=true&useSSL=false","root",DatabaseConnection.root);
	        
	        PreparedStatement ps1 = con.prepareStatement("select Quantity from products where productID=?");
	        ps1.setInt(1, p_id);
	        ResultSet rs = ps1.executeQuery();
	        
	        int prevq = 0;
	        if (rs.next()) {
	            prevq = rs.getInt(1);
	        }
	        
	        int newq = prevq + qpur.get(res);
	        qpur.remove(res);
	        
	        PreparedStatement ps2 = con.prepareStatement("update products set Quantity=? where productID=?");
	        ps2.setInt(1, newq);
	        ps2.setInt(2, p_id);
	        
	        int x = ps2.executeUpdate();
	        if (x != 0) {
	            System.out.println("CART UPDATED SUCCESSFULLY !");
	        }
	        
	        con.close();
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}

	@SuppressWarnings("unused")
	public void cancelCart() throws IOException {
	    try {
	        Class.forName("com.mysql.jdbc.Driver");
	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlineshop?autoReconnect=true&useSSL=false","root",DatabaseConnection.root);
	        
	        PreparedStatement ps = con.prepareStatement("update products set Quantity=? where productID=?");
	        int x, y;
	        for (int i = 0; i < pid.size(); i++) {
	            PreparedStatement ps1 = con.prepareStatement("select Quantity from products where productID=?");
	            ps1.setInt(1, pid.get(i));
	            ResultSet rs = ps1.executeQuery();
	            
	            int prevq = 0;
	            if (rs.next()) {
	                prevq = rs.getInt(1);
	            }
	            
	            int newq = prevq + qpur.get(i);
	            ps.setInt(1, newq);
	            ps.setInt(2, pid.get(i));
	            
	            y = ps.executeUpdate();
	            if (y == 0) {
	                System.out.println("PRODUCT NOT UPDATED BACK TO PRODUCTS TABLE !");
	            }
	        }
	        
	        con.close();
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}}
