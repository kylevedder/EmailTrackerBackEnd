/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagedb;

import database.DatabaseWrapper;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.WebServer;
import server.nanohttpd.ServerRunner;

/**
 *
 * @author Kyle
 */
public class Main
{

    public static final String FOLDER_NAME = "imageFiles";
    public static final String IMAGE_NAME = "image.png";
    public static final String FAV_ICON_NAME = "favicon.ico";
    
    public static final File FAV_ICON_FILE_LOCATION = new File(System.getProperty("user.dir") + File.separator + FOLDER_NAME + File.separator + FAV_ICON_NAME);
    public static final File IMAGE_FILE_LOCATION = new File(System.getProperty("user.dir") + File.separator + FOLDER_NAME + File.separator + IMAGE_NAME);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //file path to generate the file in
        
        System.out.println(IMAGE_FILE_LOCATION.getPath());
        //if image creation is succesful
        if (ImageUtils.genImage(IMAGE_FILE_LOCATION))
        {
            //start the web server
            ServerRunner.run(WebServer.class);            
        }
        else
        {
            System.err.println("Failed to generate image, exiting");
            System.exit(-1);
        }
//        String host = "jdbc:derby://localhost:1527/Employees";
//        String uName = "test";
//        String uPass = "test";
//        try
//        {
//            Connection con = DriverManager.getConnection(host, uName, uPass);
//            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            String SQL = "SELECT * FROM Workers";
//            ResultSet rs = stmt.executeQuery(SQL);
//
//            rs.first();
//            System.out.println(rs.getRow());
//            rs.first();
//            int lastAvalRow = 0;
//            while (rs.next())
//            {
////                rs.updateString("First_Name", rs.getString("First_Name").replace("asdf", ""));
////                rs.updateRow();
//                int id_col = rs.getInt("ID");
//                String first_name = rs.getString("First_Name");
//                String last_name = rs.getString("Last_Name");
//                String job = rs.getString("Job_Title");
//                System.out.println(id_col + " " + first_name + " " + last_name + " " + job);
//                lastAvalRow++;
//            }
//            rs.absolute(6);
//            rs.deleteRow();
//            stmt.close();
//            rs.close();
//
////            rs.moveToInsertRow();
////
////            System.out.println(lastAvalRow + 1);
////            rs.updateInt("ID", lastAvalRow + 1);
////            rs.updateString("First_Name", "Foo");
////            rs.updateString("Last_Name", "Bar");
////            rs.updateString("Job_Title", "Tester");
////
////            rs.insertRow();
////
////            while (rs.next())
////            {
////                rs.updateString("First_Name", rs.getString("First_Name").replace("asdf", ""));
////                int id_col = rs.getInt("ID");
////                String first_name = rs.getString("First_Name");
////                String last_name = rs.getString("Last_Name");
////                String job = rs.getString("Job_Title");
////                System.out.println(id_col + " " + first_name + " " + last_name + " " + job);
////            }
////
////            stmt.close();
////            rs.close();
//        }
//        catch (SQLException ex)
//        {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

}
