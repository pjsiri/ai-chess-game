/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.DataBase;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rh200
 */
public final class GameSaver extends GameDB {
    
    public GameSaver() {
        super();
        createTable();
    }
    
    public void createTable()
    {
        String createStatement = "CREATE TABLE GAME_SAVER (NUMBER INT, WHITE VARCHAR(20), BLACK VARCHAR(20), DATE DATE)";
        
        try {
            // Check if the table already exists
            DatabaseMetaData metaData = getConn().getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "GAME_SAVER", null);
            if (!resultSet.next()) {
                try (Statement statement = getConn().createStatement()) {
                    statement.execute(createStatement);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GameSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveGame(int slotNum, String playerWhite, String playerBlack, Date date)
    {
        String updateStatement = "UPDATE GAME_SAVER SET WHITE = '" + playerWhite + "', BLACK = '" + playerBlack + "', DATE = '" + date + "' WHERE NUMBER = " + slotNum;
        
        try {
            Statement statement = getConn().createStatement();
            statement.executeUpdate(updateStatement);
            int rowsAffected = statement.getUpdateCount();

            if (rowsAffected == 0)
            {
                // If no rows were affected by the update, insert a new row
                String insertStatement = "INSERT INTO GAME_SAVER VALUES (" + slotNum + ", '" + playerWhite + "', '" + playerBlack + "', '" + date + "')";
                statement.executeUpdate(insertStatement);
                statement.close();
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(GameSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet getSavedGameInfo(int slotNum)
    {
        ResultSet resultSet = null;
        String queryStatement = "SELECT WHITE, BLACK, DATE FROM GAME_SAVER WHERE NUMBER = " + slotNum;
        
        try {
            Statement statement = getConn().createStatement();
            resultSet = statement.executeQuery(queryStatement);
        }
        catch (SQLException ex) {
            Logger.getLogger(GameSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultSet;
    }
}
