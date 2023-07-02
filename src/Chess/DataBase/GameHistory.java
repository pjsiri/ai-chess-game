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
public final class GameHistory extends GameDB {
    
    public GameHistory() {
        super();
        createTable();
    }
    
    public void createTable()
    {
        String createStatement = "CREATE TABLE GAME_HISTORY (NUMBER INT, WHITE VARCHAR(20), BLACK VARCHAR(20), RESULT VARCHAR(2), MOVES INT, DATE DATE)";
        
        try {
            // Check if the table already exists
            DatabaseMetaData metaData = getConn().getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "GAME_HISTORY", null);
            if (!resultSet.next()) {
                Statement statement = getConn().createStatement();
                statement.execute(createStatement);
                statement.close();
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(GameHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void uploadCompletedGame(String playerWhite, String playerBlack, String result, int moves, Date date)
    {
        String insertStatement = "INSERT INTO GAME_HISTORY VALUES (0, '" + playerWhite + "', '" + playerBlack + "', '" + result + "', " + moves + ", '" + date + "')";
        try {
            updateGameHistoryTable();
            Statement statement = getConn().createStatement();
            statement.executeUpdate(insertStatement);
            statement.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(GameHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet getHistoryGameInfo(int slotNum)
    {
        ResultSet resultSet = null;
        String queryStatement = "SELECT WHITE, BLACK, RESULT, MOVES, DATE FROM GAME_HISTORY WHERE NUMBER = " + (slotNum-1);
        
        try {
            Statement statement = getConn().createStatement();
            resultSet = statement.executeQuery(queryStatement);
        }
        catch (SQLException ex) {
            Logger.getLogger(GameHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultSet;
    }

    private void updateGameHistoryTable()
    {
        try {
            Statement statement = getConn().createStatement();
            String updateStatement = "UPDATE GAME_HISTORY SET NUMBER = NUMBER + 1";
            statement.executeUpdate(updateStatement);
            String deleteStatement = "DELETE FROM GAME_HISTORY WHERE NUMBER > 4";
            statement.executeUpdate(deleteStatement);
            statement.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(GameHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
