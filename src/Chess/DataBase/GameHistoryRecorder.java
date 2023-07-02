/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.DataBase;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rh200
 */
public final class GameHistoryRecorder extends GameDB {
    
    public GameHistoryRecorder() {
        super();
        createTable();
    }
    
    public void createTable() {
        
        String createStatement = "CREATE TABLE GAME_HISTORY_RECORDER (NUMBER INT, MOVE_NUM INT, PIECE_TYPE VARCHAR(2), position INT)";
        
        try {
            // Check if the table already exists
            DatabaseMetaData metaData = getConn().getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "GAME_HISTORY_RECORDER", null);
            if (!resultSet.next()) {
                Statement statement = getConn().createStatement();
                statement.execute(createStatement);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GameHistoryRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void uploadCompletedGame()
    {
        String insertStatement = "INSERT INTO GAME_HISTORY_RECORDER (NUMBER, MOVE_NUM, PIECE_TYPE, position) " +
                                 "SELECT NUMBER, MOVE_NUM, PIECE_TYPE, position " +
                                 "FROM GAME_SAVER_RECORDER " +
                                 "WHERE NUMBER = 0";
        try {
            updateGameHRecorderTable();
            Statement statement = getConn().createStatement();
            statement.executeUpdate(insertStatement);
        }
        catch (SQLException ex) {
            Logger.getLogger(GameHistoryRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet getHistoryGameBoard(int slotNum, int moveNum)
    {
        ResultSet resultset = null;
        String queryStatement = "SELECT PIECE_TYPE, position FROM GAME_HISTORY_RECORDER WHERE NUMBER = " + (slotNum-1) + " AND MOVE_NUM = " + moveNum;
        
        try {
            Statement statement = getConn().createStatement();
            resultset = statement.executeQuery(queryStatement);
        }
        catch (SQLException ex) {
            Logger.getLogger(GameHistoryRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultset;
    }
    
    private void updateGameHRecorderTable()
    {
        try {
            Statement statement = getConn().createStatement();
            String updateStatement = "UPDATE GAME_HISTORY_RECORDER SET NUMBER = NUMBER + 1";
            statement.executeUpdate(updateStatement);
            String deleteStatement = "DELETE FROM GAME_HISTORY_RECORDER WHERE NUMBER > 4";
            statement.executeUpdate(deleteStatement);
            statement.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(GameHistoryRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
