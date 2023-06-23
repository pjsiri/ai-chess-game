/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChessGame.DataBase;

import ChessGame.DataBase.GameDB;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rh200
 */
public final class GameSaverRecorder extends GameDB {
    
    public GameSaverRecorder() {
        super();
        createTable();
    }
    
    public void createTable()
    {
        String createStatement = "CREATE TABLE GAME_SAVER_RECORDER (NUMBER INT, MOVE_NUM INT, PIECE_TYPE VARCHAR(2), col INT, row INT, LMN INT, HNM INT, HMO INT)";
        
        try {
            // Check if the table already exists
            DatabaseMetaData metaData = getConn().getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "GAME_SAVER_RECORDER", null);
            if (!resultSet.next()) {
                Statement statement = getConn().createStatement();
                statement.execute(createStatement);
                statement.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(GameSaverRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //record current game
    public void recordCurrentGame(int moveNum, String pieceType, int col, int row, int LMN, int HNM, int HMO)
    {
        try {
            Statement statement = getConn().createStatement();
            String insertStatement = "INSERT INTO GAME_SAVER_RECORDER VALUES (0, "+moveNum+", '"+pieceType+"', "+col+", "+row+", "+LMN+", "+HNM+", "+HMO+")";
            statement.executeUpdate(insertStatement);
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(GameSaverRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //load saved game
    public void loadSavedGame(int slotNum)
    {
        deleteGame(0);
        String insertStatement = "INSERT INTO GAME_SAVER_RECORDER (NUMBER, MOVE_NUM, PIECE_TYPE, col, row, LMN, HNM, HMO) " +
                                 "SELECT 0, MOVE_NUM, PIECE_TYPE, col, row, LMN, HNM, HMO " +
                                 "FROM GAME_SAVER_RECORDER " +
                                 "WHERE NUMBER = ?";
        
        try {
            PreparedStatement preparedStatement = getConn().prepareStatement(insertStatement);
            preparedStatement.setInt(1, slotNum);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(GameSaverRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //save current game
    public void saveCurrentGame(int slotNum)
    {
        deleteGame(slotNum);
        String insertStatement = "INSERT INTO GAME_SAVER_RECORDER (NUMBER, MOVE_NUM, PIECE_TYPE, col, row, LMN, HNM, HMO) " +
                                 "SELECT ?, MOVE_NUM, PIECE_TYPE, col, row, LMN, HNM, HMO " +
                                 "FROM GAME_SAVER_RECORDER " +
                                 "WHERE NUMBER = 0";
        
        try {
            PreparedStatement preparedStatement = getConn().prepareStatement(insertStatement);
            preparedStatement.setInt(1, slotNum);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(GameSaverRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //delete game slotNum
    public void deleteGame(int slotNum)
    {
        String deleteStatement = "DELETE FROM GAME_SAVER_RECORDER WHERE NUMBER = " + slotNum;

        try {
            Statement statement = getConn().createStatement();
            statement.executeUpdate(deleteStatement);
            statement.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(GameSaverRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet getCurrentGameBoard()
    {
       ResultSet resultset = null;
       String queryStatement = "SELECT MOVE_NUM, PIECE_TYPE, col, row, LMN, HNM, HMO FROM GAME_SAVER_RECORDER WHERE NUMBER = 0 AND MOVE_NUM = (SELECT MAX(MOVE_NUM) FROM GAME_SAVER_RECORDER WHERE NUMBER = 0)";
        
        try {
            Statement statement = getConn().createStatement();
            resultset = statement.executeQuery(queryStatement);
        }
        catch (SQLException ex) {
            Logger.getLogger(GameSaverRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultset;
    }
}
