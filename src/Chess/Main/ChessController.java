package Chess.Main;

import Chess.AI.Engine;
import Chess.Board.Board;
import Chess.Board.BoardPieces;
import Chess.DataBase.GameHistory;
import Chess.DataBase.GameHistoryRecorder;
import Chess.DataBase.GameSaver;
import Chess.DataBase.GameSaverRecorder;
import Chess.GUI.ChessSoundManager;
import Chess.Pieces.Bishop;
import Chess.Pieces.King;
import Chess.Pieces.Knight;
import Chess.Pieces.Pawn;
import Chess.Pieces.Piece;
import Chess.Pieces.PieceColour;
import Chess.Pieces.Queen;
import Chess.Pieces.Rook;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author rh200
 */
public class ChessController {
    private static Board board;
    private static Engine engine;
    private static Player playerWhite;
    private static Player playerBlack;
    
    private static GameSaver gameSaver;
    private static GameSaverRecorder gameSRecorder;
    private static GameHistory gameHistory;
    private static GameHistoryRecorder gameHRecorder;
    private static int gameMode;
    private static int maxMoves;
    
    public ChessController()
    {
        board = new Board();
        gameSaver = new GameSaver();
        gameSRecorder = new GameSaverRecorder();
        gameHistory = new GameHistory();
        gameHRecorder = new GameHistoryRecorder();
        engine = new Engine(this);
        gameMode = 0;
    }
    
    public int movePiece(int fromSquare, int toSquare)
    {
        int validity = board.movePiece(fromSquare, toSquare);
        //record current board
        recordCurrentBoard();
        return validity;
    }
    
    public void movePiece()
    {
        engine.move(9); // Put odd number
    }
    
    // Uploads manually resigned chess game into the database
    public void resignGame()
    {
        gameHistory.uploadCompletedGame(playerWhite.getName(),playerBlack.getName(), getGameResult(board.getColourTurn(), true), board.getMoveNum(), getCurrentDate());
        gameHRecorder.uploadCompletedGame();
    }
    
    // Uploads manually draw chess game into the database
    public void drawGame()
    {
        gameHistory.uploadCompletedGame(playerWhite.getName(), playerBlack.getName(), "DD", board.getMoveNum(), getCurrentDate());
        gameHRecorder.uploadCompletedGame();
    }
    
    // Resets chess game board and pieces
    public void startNewGame()
    {
        board.setDefault();
        engine = new Engine(this);
        gameSRecorder.deleteGame(0);
        recordCurrentBoard();
    }
    
    /**
     * Saves current game into a slot in the database
     * 
     * game mode = 0 is single player
     * game mode = 1 is multi player
     * 
     * @param slotNum (1, 2, 3, 4, 5)
     */
    public void saveGame(int slotNum)
    {
        gameSaver.saveGame(slotNum, playerWhite.getName(), playerBlack.getName(), getCurrentDate(),gameMode);
        gameSRecorder.saveCurrentGame(slotNum);
    }
    
    /**
     * Gets a saved game info at a certain slot
     * 
     * @param slotNum (1, 2, 3, 4, 5)
     * @return a result set, where col 1: (white player name), col 2: (black player name), col 3: (date)
     */
    public ResultSet getSavedGameInfo(int slotNum)
    {
        return gameSaver.getSavedGameInfo(slotNum);
    }
    
    /**
     * Loads a saved game from a slot into the current game board
     * 
     * @param slotNum (1, 2, 3, 4, 5)
     */
    public void loadSavedGame(int slotNum) 
    {
        gameSRecorder.loadSavedGame(slotNum);
        ResultSet resultset = gameSRecorder.getCurrentGameBoard();

        try {
            board.clearPieces();
            if (resultset.next()) { // Move the cursor to the first row
                board.setMoveNum(resultset.getInt(1));
                do {
                    Piece piece = createPiece(resultset.getString(2), resultset.getInt(3), resultset.getInt(4));
                    board.getPieces().addPiece(piece);
                    board.setEnPassantTarget(resultset.getInt(5));
                } while (resultset.next());
            }
            board.updateBoard();
        } catch (SQLException ex) {
            
        }
    }
    
    /**
     * Gets a history game info at a certain slot
     * 
     * @param slotNum (1, 2, 3, 4, 5)
     * @return a result set, where col 1: (white player name), col 2: (black player name), col 3: (game outcome), col 4: (# of moves), col 5: (date)
     */
    public ResultSet getGameHistoryInfo(int slotNum)
    {
        ResultSet resultSet = gameHistory.getHistoryGameInfo(slotNum);
        try {
            if(resultSet.next())
            {
                setMaxMoves(resultSet.getInt(4));
            }
        } catch (SQLException ex) {
            
        }
        
        return gameHistory.getHistoryGameInfo(slotNum);
    }
    
    /**
     * Loads a game board of a history game and updates the board
     * 
     * @param slotNum (1, 2, 3, 4, 5)
     * @param boardNum (from 0 to max board number), max board number can be obtained from getGameHistoryInfo(slotNum) column 4
     */
    public void loadHistoryGameBoard(int slotNum, int boardNum)
    {
        ResultSet resultSet = gameHRecorder.getHistoryGameBoard(slotNum, boardNum);
        
        try {
            board.clearPieces();
            if (resultSet.next()) // Move the cursor to the first row
            {
                do {
                    Piece piece = createPiece(resultSet.getString(1), resultSet.getInt(2), 0);
                    board.addPiece(piece);
                } while(resultSet.next());
            }
            
            board.updateBoard();
        }
        catch (SQLException ex) {
            
        }
    }
    
    // Shuts down database connection
    public void quit()
    {
        gameSaver.closeConnections();
        gameSRecorder.closeConnections();
        gameHistory.closeConnections();
        gameHRecorder.closeConnections();
    }
    
    // Returns current date
    private java.sql.Date getCurrentDate()
    {
        Date currentDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
        return sqlDate;
    }
    
    // Saves current chess board info into the database
    private void recordCurrentBoard()
    {
        for(Piece i : board.getPieces().getAllPieces(board.getColourTurn()))
        {
            int HNM = 0;
            if(!i.isMoved()) 
            {
                HNM = 1;
            }
            gameSRecorder.recordCurrentGame(board.getMoveNum(), i.getSymbol(), i.getPosition(), HNM, board.getEnPassantTarget());
        }
        for(Piece i : board.getPieces().getAllPieces(board.getColourTurn().getOppColour()))
        {
            int HNM = 0;
            if(!i.isMoved()) {
                HNM = 1;
            }
            gameSRecorder.recordCurrentGame(board.getMoveNum(), i.getSymbol(), i.getPosition(), HNM, board.getEnPassantTarget());
        }
    }
    
    // Returns game outcome if the game has ended. Can resign manually
    private String getGameResult(PieceColour colourTurn, boolean manual)
    {
        String gameResult = "";
        if (board.isCheckmated(colourTurn) || manual)
        {
            if (colourTurn == PieceColour.BLACK) {
                gameResult = "wW";
            } else {
                gameResult = "bW";
            }
        }
        else if (board.isCheckmated(colourTurn))
        {
            gameResult = "DD";
        }
        return gameResult;
    }
    
    private Piece createPiece(String pieceType, int position, int HNM)
    {
        Piece piece;
        PieceColour colour;
        boolean HNMboolean;
        
        if (pieceType.contains("w")) {
            colour = PieceColour.WHITE;
        } else {
            colour = PieceColour.BLACK;
        }
        
        HNMboolean = !(HNM == 1);
        
        if (pieceType.contains("P")) {
            piece = new Pawn(colour, position, HNMboolean, board);
        } else if (pieceType.contains("Q")) {
            piece = new Queen(colour, position, HNMboolean, board);
        } else if (pieceType.contains("B")) {
            piece = new Bishop(colour, position, HNMboolean, board);
        } else if (pieceType.contains("R")) {
            piece = new Rook(colour, position, HNMboolean, board);
        } else if (pieceType.contains("N")) {
            piece = new Knight(colour, position, HNMboolean, board);
        } else {
            piece = new King(colour, position, HNMboolean, board);
        }
        
        return piece;
    }
    
    public void setPlayers(String playerWhiteName, String playerBlackName)
    {
        playerWhite = new Player(PieceColour.WHITE, playerWhiteName);
        playerBlack = new Player(PieceColour.BLACK, playerBlackName);
    }
    
    public void setPlayers(PieceColour colour, String playerName)
    {
        if (colour == PieceColour.WHITE) {
            playerWhite = new Player(PieceColour.WHITE, playerName);
            playerBlack = new Player(PieceColour.BLACK, "AI-Bot");
        } else {
            playerBlack = new Player(PieceColour.BLACK, playerName);
            playerWhite = new Player(PieceColour.WHITE, "AI-Bot");
        }
    }
    
    public int gameEnded()
    {
        if(board.isCheckmated(board.getColourTurn()))
        {
            //upload result to database
            gameHistory.uploadCompletedGame(playerWhite.getName(), playerBlack.getName(), getGameResult(board.getColourTurn(), false), board.getMoveNum(), getCurrentDate());
            gameHRecorder.uploadCompletedGame();
            return 1;
        }
        else if(board.isStalemated(board.getColourTurn()))
        {
            gameHistory.uploadCompletedGame(playerWhite.getName(), playerBlack.getName(), getGameResult(board.getColourTurn(), false), board.getMoveNum(), getCurrentDate());
            gameHRecorder.uploadCompletedGame();
            return 2;
        }
        return 0;
    }
    
    public void promotePawn(String promoteType)
    {
        board.promote(promoteType);
        ChessSoundManager.playPromotionSound();
    }
    
    public Board getBoard()
    {
        return board;
    }
    
    public Piece getSquare(int square)
    {
        return board.getBoard()[square];
    }
    
    public Player getCurrentPlayer()
    {
        if(board.getColourTurn() == PieceColour.WHITE) {
            return playerWhite;
        } else {
            return playerBlack;
        }
    }
    
    public void resetPlayers()
    {
        playerWhite = null;
        playerBlack = null;
    }
    
    public PieceColour getColourTurn()
    {
        return board.getColourTurn();
    }
    
    public boolean canPromote()
    {
        return board.canPromote();
    }
    
    public boolean isInChecked()
    {
        return board.isChecked();
    }
    
    public BoardPieces getPieces()
    {
        return board.getPieces();
    }
    
    /**
     * @return the playerWhte
     */
    public Player getPlayerWhite() {
        return playerWhite;
    }

    /**
     * @return the playerBlack
     */
    public Player getPlayerBlack() {
        return playerBlack;
    }

    /**
     * @return the engine
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * @return the maxMoves
     */
    public int getMaxMoves() {
        return maxMoves;
    }

    /**
     * @param aMaxMoves the maxMoves to set
     */
    public void setMaxMoves(int aMaxMoves) {
        maxMoves = aMaxMoves;
    }

    /**
     * @return the gameMode
     */
    public int getGameMode() {
        return gameMode;
    }

    /**
     * @param aGameMode the gameMode to set
     */
    public void setGameMode(int aGameMode) {
        gameMode = aGameMode;
    }
}