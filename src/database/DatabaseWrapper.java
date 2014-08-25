/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import imagedb.Main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kyle
 */
public class DatabaseWrapper
{

    private static DatabaseWrapper dw = null;
    private Connection con = null;

    //Credentials
    private final String UNAME = "root";
    private final String PSWRD = "root";

    //Path pieces to the DB
    private final String PATH_TO_DB = "jdbc:derby://localhost";
    private final String DB_NAME = "ImageDB";
    private final int PORT = 1527;
    private final String HOST = PATH_TO_DB + ":" + PORT + "/" + DB_NAME;

    //Table info
    private final String SCHEMA_NAME = "ROOT";
    private final String TABLE_NAME = "IMAGETABLE";

    //column info
    private final String ID = "ID";
    private final String VIEWS = "VIEWS";

    public static DatabaseWrapper getInstance()
    {
        if (dw == null)
        {
            dw = new DatabaseWrapper();
        }
        return dw;
    }

    private DatabaseWrapper()
    {
        try
        {
            con = DriverManager.getConnection(HOST, UNAME, PSWRD);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseWrapper.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Connection to DB failed, exiting...");
            System.exit(-1);
        }
    }

    /**
     * Increments the viewcount on the item of the specified ID
     *
     * @param id ID of the item whose viewcount is to be incremented.
     * @return success on the update
     */
    public boolean incrementViewcount(int id)
    {
        try
        {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "=" + id;
            ResultSet rs = stmt.executeQuery(SQL);
            //if exists
            if (rs.next())
            {
                rs.first();
                rs.updateInt(VIEWS, (rs.getInt(VIEWS) + 1));
                rs.updateRow();
                return true;
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Adds a new image with a unique id and view count to the database.
     *
     * @return int of the id
     */
    public int addNewImage()
    {
        int id = genID();
        if (id != -1)
        {
            try
            {
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                String SQL = "INSERT INTO " + TABLE_NAME + " (" + ID + ", " + VIEWS + ") VALUES (" + id + ", 0)";
                stmt.execute(SQL);
                stmt.close();
                return id;
            }
            catch (SQLException ex)
            {
                Logger.getLogger(DatabaseWrapper.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        }
        else
        {
            return -1;
        }
    }

    /**
     * Generates an ID for the image by taking the last image id in the table
     * and adding 1 to it
     *
     * @return -1 for error, or the new id for the image.
     */
    private int genID()
    {
        try
        {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT * FROM " + TABLE_NAME;
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.last())
            //return the last id +1
            {
                return rs.getInt(ID) + 1;
            }
            else
            {
                //first item in the table
                return 1;
            }

        }
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseWrapper.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error in SQL, returning -1");
            return -1;
        }
    }

    private void test()
    {
        try
        {

            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT * FROM Workers";
            ResultSet rs = stmt.executeQuery(SQL);

            rs.first();
            System.out.println(rs.getRow());
            rs.first();
            int lastAvalRow = 0;
            while (rs.next())
            {
//                rs.updateString("First_Name", rs.getString("First_Name").replace("asdf", ""));
//                rs.updateRow();
                int id_col = rs.getInt("ID");
                String first_name = rs.getString("First_Name");
                String last_name = rs.getString("Last_Name");
                String job = rs.getString("Job_Title");
                System.out.println(id_col + " " + first_name + " " + last_name + " " + job);
                lastAvalRow++;
            }
            rs.absolute(6);
            rs.deleteRow();
            stmt.close();
            rs.close();

//            rs.moveToInsertRow();
//
//            System.out.println(lastAvalRow + 1);
//            rs.updateInt("ID", lastAvalRow + 1);
//            rs.updateString("First_Name", "Foo");
//            rs.updateString("Last_Name", "Bar");
//            rs.updateString("Job_Title", "Tester");
//
//            rs.insertRow();
//
//            while (rs.next())
//            {
//                rs.updateString("First_Name", rs.getString("First_Name").replace("asdf", ""));
//                int id_col = rs.getInt("ID");
//                String first_name = rs.getString("First_Name");
//                String last_name = rs.getString("Last_Name");
//                String job = rs.getString("Job_Title");
//                System.out.println(id_col + " " + first_name + " " + last_name + " " + job);
//            }
//
//            stmt.close();
//            rs.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
