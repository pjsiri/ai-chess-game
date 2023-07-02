/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.GUI;

import Chess.Main.ChessController;
import Chess.Pieces.Piece;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;


/**
 *
 * @author pj
 */
public class ChessPanel extends JPanel {

    private static final int BOARD_SIZE = 8;
    private static final int CELL_SIZE = 63;
    
    private HashSet<Integer> availableMoves;

    private boolean flipFlag = false;
    private boolean toggleSwitch = false;
    private ChessFrame chessFrame;
    private final ChessController chessController;
    private Piece selectedPiece = null; 
    private boolean gameEnded = false;
    private boolean singlePlayer = false;
    private boolean promotionPending = false;
    
    private int selectedPos = -1;
    
    private int wasAtPos = -1;
    private int movedToPos = -1;
    
    public ChessPanel(ChessFrame frame) 
    {
        chessController = new ChessController();
        chessFrame = frame;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(513, 514));
        availableMoves = new HashSet<>();
        addMouseListener();
    }
    
    // Check if the game has not ended or the players have not signed in.
    private void addMouseListener()
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) 
            {
                if (!gameEnded && chessController.getPlayerWhite()!= null && chessController.getPlayerBlack() != null) 
                {
                    int col = e.getX();
                    int row = e.getY();
                    
                    int position = calculatePosition(col,row);
                    
                    if(isLegalMove(position)) 
                    {
                        handleClick(position);
                    }
                }
            }
        });
    }
    
    private int calculatePosition(int x, int y) 
    {
        int row = (BOARD_SIZE - 1) - y / CELL_SIZE;
        int col = x / CELL_SIZE;
        
        if (isFlipFlag()) 
        {
            // Flip the column and row
            col = (BOARD_SIZE - 1) - col;
            row = (BOARD_SIZE - 1) - row;
        }
        
        return row * 8 + col;
    }
    
    // Check if the move is within the board of 0 x 63.
    public boolean isLegalMove(int position) 
    {   
        return position >= 0 && position <= 63;
    }
    
    // If a piece has been selected, then it will being to call the movePiece method to attempt to move it.
    private void handleClick(int position) 
    {
        if (selectedPos == -1)  // Check for if piece has not been selected
        {
            selectPiece(position);
        } 
        else 
        {
            movePiece(position);
        }
        repaint();
    }
    
    // Select piece if the piece isn't empty and if it is the same piece colour as the player playing.
    public boolean selectPiece(int position) 
    {
        selectedPiece = chessController.getSquare(position);
        
        if (selectedPiece != null) // Check for Empty piece
        {
            if (selectedPiece.getColour() == chessController.getColourTurn()) // Check if it is the player chess piece colour
            {
                selectedPos = position;
                wasAtPos = position;
                
                movedToPos = -1;
                availableMoves = getSelectedPiece().getAvailableMoves();
                return true;
            }
        }
        
        return false;
    }
    
    // Move piece if it is a legal move and if it a availableMoves. It uses the chessController to move the piece.
    private void movePiece(int position) 
    {
        selectedPiece = chessController.getSquare(position);
        int moveType = chessController.movePiece(selectedPos, position);
        if(moveType != 0) // Check if it is legal move
        {
            if(selectedPiece != null || moveType == 2)
            {
                ChessSoundManager.playCaptureSound();
            }
            else if(chessController.isInChecked())
            {
                ChessSoundManager.playCheckSound();
            }
            else if(moveType == 1)
            {
                ChessSoundManager.playCastleSound();
            }
            else
            {
                ChessSoundManager.playMoveSound();
            }
            movedToPos = position;

            updateMoves(selectedPos, movedToPos);
            flipBoard();

            resetSelection();
            if(checkForGameStatus())
            {
                return;
            }
            setPromotionPending(checkForPromotion());
        }
        else{
            selectPiece(position);
            return;
        }
        
        if(singlePlayer || chessController.getGameMode() != 0) 
        {
            gameEnded = true;
            SwingWorker<Void, Void> aiMoveWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception {
                    while (promotionPending) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            // Handle the interruption if needed
                        }
                    }
                    chessController.movePiece(); // AI Move

                    return null;
                }

            @Override
            protected void done() 
            {
                try {
                    get(); // Make sure the AI move task is completed

                    wasAtPos = chessController.getEngine().getFromSquare();
                    movedToPos = chessController.getEngine().getToSquare();
                    updateMoves(wasAtPos, movedToPos);
                    checkForGameStatus();
                    flipBoard();
                    repaint();
                    gameEnded = false;
                } catch (InterruptedException | ExecutionException e) {
                    // Handle any exceptions that occurred during the AI move
                }
            }
            };

            aiMoveWorker.execute();
        }
    }
    
    // Check for class promotion using the controller class
    public boolean checkForPromotion()
    {
        if(chessController.canPromote())
        {
            gameEnded = true;
            chessFrame.switchTab(4);
            return true;
        }
        return false;
    }
    
    // Update moves to a TextArea in the frame.
    private void updateMoves(int wasAtPos, int movedToPos)
    {
        int fromCol = wasAtPos % 8;  // Get column from wasAtPos index
        int fromRow = wasAtPos / 8;  // Get row from wasAtPos index
        int toCol = movedToPos % 8;   // Get column from moveToPos index
        int toRow = movedToPos / 8;   // Get row from moveToPos index
        
        String moves = String.format(" %s%d, %s%d%n", (char) ('a' + fromCol), fromRow + 1, (char) ('a' + toCol), toRow + 1);
        if (chessFrame != null) 
        {
            chessFrame.updateMovesTextArea(moves);
        }
    }
    
    // Reset all the piece selections for resetting animations such as the check outline and highlights.
    private void resetSelection() 
    {
        selectedPiece = null;
        selectedPos = -1;
        availableMoves = new HashSet<>();
    }
    
    private boolean checkForGameStatus()
    {
        int gameStatus = chessController.gameEnded();
        if(gameStatus == 1)
        {
            setGameEnded(true);
            chessFrame.switchTab(3);
            repaint();
            ChessSoundManager.playCheckMateSound();
            JOptionPane.showMessageDialog(ChessPanel.this, "This Chess Game has ended via Checkmate! Game over.");
            return true;
        }
        else if(gameStatus == 2)
        {
            setGameEnded(true);
            chessFrame.switchTab(3);
            repaint();
            ChessSoundManager.playCheckMateSound();
            JOptionPane.showMessageDialog(ChessPanel.this, "This Chess Game has ended via Stalement! Game over.");
            return true;
        }
        return false;
    }
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        
        drawChessBoard(g);
        
        int frameWidth = 5; 
        int boardWidth = BOARD_SIZE * CELL_SIZE;
        int boardHeight = BOARD_SIZE * CELL_SIZE;

        // Draw the frame around the chess board
        g.setColor(Color.DARK_GRAY);
        g.fillRect(CELL_SIZE - frameWidth - 59, CELL_SIZE - frameWidth - 58, frameWidth, boardHeight + 2 * frameWidth); // LEFT
        g.fillRect(CELL_SIZE - frameWidth - 59, CELL_SIZE - frameWidth - 58, boardWidth + 2 * frameWidth, frameWidth); // TOP
        g.fillRect(CELL_SIZE - frameWidth - 59, CELL_SIZE + boardHeight  - 58, boardWidth + 2 * frameWidth, frameWidth);
        g.fillRect(CELL_SIZE + boardWidth - 59, CELL_SIZE - frameWidth - 58, frameWidth, boardHeight + 2 * frameWidth);
        
        // If there are available moves, the program will draw them to the GUI
        if(!availableMoves.isEmpty())
        {
            drawAvailableMoves(g);
        }
    }
    
    private void drawChessBoard(Graphics g) 
    {
        int positionCounter = 0;

        for (int i = 0; i < 64; i++) {
            int col = positionCounter % 8;
            int row = positionCounter / 8;

            int x = col * CELL_SIZE + 4;
            int y = row * CELL_SIZE + 5;
            int position = calculatePosition(x, y);

            Piece piece = chessController.getSquare(position);

            if ((row + col) % 2 == 0) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.LIGHT_GRAY);
            }

            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            drawPiece(g, piece, x, y);
            drawColumnLabels(g, row, col, x, y);
            drawRowLabels(g, col, row, x, y);
            drawYellowOverlay(g, x, y, position);
            drawCheckHighlight(g, piece, x, y);

            positionCounter++;
        }
    }
    
    // Draw chess piece using their image
    private void drawPiece(Graphics g, Piece piece, int x, int y) 
    {
        if(piece != null)
        {
            g.drawImage(piece.getImage(), x + 8, y + 7, null);
        }
    }
    
    // Draw row labels of chess board
    private void drawRowLabels(Graphics g, int col, int row, int x, int y) 
    {
        if (col == 0) 
        {   
            g.setColor(Color.BLACK);
            int displayedRow = isFlipFlag() ? row + 1 : BOARD_SIZE - row;
            g.drawString(String.valueOf(displayedRow), x + 3, y + CELL_SIZE / 2 - 16);
        }
    }
    
    // Draw Column labels of chess board
    private void drawColumnLabels(Graphics g, int row, int col, int x, int y) 
    {
        if (row == BOARD_SIZE - 1) {
            if (col % 2 == 0) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.LIGHT_GRAY);
            }
            char displayedColumn = isFlipFlag() ? (char) ('h' - col) : (char) ('a' + col);
            g.drawString(String.valueOf(displayedColumn), x + CELL_SIZE / 2 + 20, y + CELL_SIZE - 6);
        }
    }
    
    private void drawYellowOverlay(Graphics g, int x, int y,int position) 
    {
        // A yellow overlay on the square where the piece was
        if(wasAtPos == position) 
        {
            g.setColor(new Color(255, 255, 0, 58));
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }
        // A yellow overlay on the square the piece moved to
        if(movedToPos == position) 
        {
            g.setColor(new Color(255, 255, 0, 58));
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }
    }
    
    private void drawCheckHighlight(Graphics g, Piece piece, int x, int y) 
    {
        // Draw red outline on the square where the king is in check.
        if (piece != null && piece.getSymbol().contains("K") && chessController.isInChecked()) 
        {
            if(chessController.getColourTurn() == piece.getColour())
            {
                g.setColor(Color.RED);
                g.drawRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
            }
        }
    }
    
    // This is a toggle switch for the GUI to allow for automatic flipping between turns.
    public void flipBoard()
    {
        if(isToggleSwitch())
        {
            setFlipFlag(!isFlipFlag());
        }
    }
    
    // Highlight the availablemoves of a chess piece.
    private void drawAvailableMoves(Graphics g) 
    {
        if (wasAtPos != -1) {
            g.setColor(new Color(0, 0, 0, 50));

            for (int index : availableMoves) {
                int row = index / 8;
                int col = index % 8;
                int x = col * CELL_SIZE;
                int y = (BOARD_SIZE - 1 - row) * CELL_SIZE;

                if (isFlipFlag()) 
                {
                    x = (BOARD_SIZE - 1 - col) * CELL_SIZE;
                    y = row * CELL_SIZE;
                }

                g.fillOval(x + 22, y + 20, 25, 25);
            }
        }
    }
    
    // Reset the whole game and selections
    public void resetGame() 
    {
        wasAtPos = -1;
        movedToPos = -1;
        resetSelection();
        chessController.startNewGame();
        setGameEnded(false);
        repaint();
    }

    /**
     * @return the chessFrame
     */
    public ChessFrame getChessFrame() {
        return chessFrame;
    }

    /**
     * @param chessFrame the chessFrame to set
     */
    public void setChessFrame(ChessFrame chessFrame) 
    {
        this.chessFrame = chessFrame;
    }

    /**
     * @return the flipFlag
     */
    public boolean isFlipFlag() {
        return flipFlag;
    }

    /**
     * @param flipFlag the flipFlag to set
     */
    public void setFlipFlag(boolean flipFlag) {
        this.flipFlag = flipFlag;
    }

    /**
     * @return the toggleSwitch
     */
    public boolean isToggleSwitch() 
    {
        return toggleSwitch;
    }

    /**
     * @param toggleSwitch the toggleSwitch to set
     */
    public void setToggleSwitch(boolean toggleSwitch) 
    {
        this.toggleSwitch = toggleSwitch;
    }

    /**
     * @param gameEnded the gameEnded to set
     */
    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    /**
     * @return the selectedPiece
     */
    public Piece getSelectedPiece() {
        return selectedPiece;
    }
    
    
    /**
     * @param singlePlayer the singlePlayer to set
     */
    public void setSinglePlayer(boolean singlePlayer) {
        this.singlePlayer = singlePlayer;
    }

    /**
     * @param promotionPending the promotionPending to set
     */
    public void setPromotionPending(boolean promotionPending) {
        this.promotionPending = promotionPending;
    }
}