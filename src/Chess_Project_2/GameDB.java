/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess_Project_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author rh200
 */
public class GameDB {
    
    private static final String USER_NAME = "chess"; //DB username
    private static final String PASSWORD = "chess"; //DB password
    private static final String URL = "jdbc:derby:ChessDB; create=true";  //url of the DB host
    private static Connection conn = null;
    
    public GameDB() {
        dbSetup();
    }
    
    //Establish connection
    private void dbSetup() {
        //Establish a connection to Database
        try {
            if (conn == null) {
                conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                System.out.println(URL + " get CONNECTED...");
                System.out.println(getConn());
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public Connection getConn() {
        return this.conn;
    }
    
    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
