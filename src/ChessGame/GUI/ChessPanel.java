/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChessGame.GUI;

import ChessGame.ChessController;
import ChessGame.Pieces.Piece;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author pj
 */
public class ChessPanel extends JPanel {
    private static final int BOARD_SIZE = 8;
    private static final int CELL_SIZE = 63;
    private boolean[][] availableMoves;
    private int selectedRow = -1;
    private int selectedCol = -1;

    private boolean flipFlag = false;
    private boolean toggleSwitch = false;
    private ChessFrame chessFrame;
    private ChessController chessController;
    private Piece selectedPiece = null; 
    private boolean gameEnded = false;
    private int wasAtCol = -1;
    private int wasAtRow = -1;
    private int movedToCol = -1;
    private int movedToRow = -1;
    
    public ChessPanel(ChessFrame frame) 
    {
        chessController = new ChessController();
        chessFrame = frame;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(513, 514));
        availableMoves = new boolean[8][8];
        
        addMouseListener();
    }
    
    // Check if the game has not ended or the players have not signed in.
    private void addMouseListener()
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) 
            {
                if (!gameEnded && chessController.getPlayer1()!= null && chessController.getPlayer2() != null) 
                {
                    int col = e.getX() / CELL_SIZE;
                    int row = (BOARD_SIZE - 1) - e.getY() / CELL_SIZE;
                    if (isFlipFlag()) 
                    {
                        col = (BOARD_SIZE - 1) - col;
                        row = (BOARD_SIZE - 1) - row;
                    }
                    if (isLegalMove(col, row)) 
                    {
                        handleClick(col, row);
                    }
                }
            }
        });
    }
    
    // Check if the move is within the board of 8 x 8.
    public boolean isLegalMove(int col, int row) 
    {   
        return col >= 0 && col < BOARD_SIZE && row >= 0 && row < BOARD_SIZE;
    }
    
    // If a piece has been selected, then it will being to call the movePiece method to attempt to move it.
    private void handleClick(int col, int row) 
    {
        if (getSelectedRow() == -1 && getSelectedCol() == -1)  // Check for if piece has not been selected
        {
            selectPiece(col, row);
        } 
        else 
        {
            movePiece(col, row);
        }

        repaint();
    }
    
    // Select piece if the piece isn't empty and if it is the same piece colour as the player playing.
    public void selectPiece(int col, int row) 
    {
        selectedPiece = chessController.getBoard()[col][row];

        if (getSelectedPiece() != null) // Check for Empty piece
        {
            if (getSelectedPiece().getColour() == chessController.getCurrentColourTurn()) // Check if it is the player chess piece colour
            {
                selectedRow = row;
                selectedCol = col;
                wasAtCol = col;
                wasAtRow = row;
                movedToCol = -1;
                movedToRow = -1;
                availableMoves = getSelectedPiece().getAvailableMoves();
            }
        }
    }
    
    // Move piece if it is a legal move and if it a availableMoves. It uses the chessController to move the piece.
    private void movePiece(int col, int row) 
    {
        selectedPiece = chessController.getBoard()[getSelectedCol()][getSelectedRow()];

        if (availableMoves[col][row]) // Check the availableMoves 
        {
            if (chessController.movePiece(getSelectedCol(), getSelectedRow(), col, row)) // Check if it is legal move
            {
                movedToCol = col;
                movedToRow = row;
                updateMoves(getSelectedCol(), getSelectedRow(),col,row);
                flipBoard();
            }
        }
        
        resetSelection();
        if(chessController.gameEnded())
        {
            setGameEnded(true);
            chessFrame.switchTab(4);
            repaint();
            JOptionPane.showMessageDialog(ChessPanel.this, "This Chess Game has ended via Checkmate! Game over.");
        }
        checkForPromotion();
    }
    
    // Update moves to a TextArea in the frame.
    private void updateMoves(int fromCol, int fromRow, int toCol, int toRow)
    {
        String moves = "(" + getSelectedPiece().getSymbol() + ")" + String.format(" %s%d, %s%d%n", (char) ('a' + fromCol), fromRow + 1, (char) ('a' + toCol), toRow + 1);
        if(chessFrame != null)
        {
            chessFrame.updateMovesTextArea(moves);
        }
    }
    
    // Reset all the piece selections for resetting animations such as the check outline and highlights.
    private void resetSelection() 
    {
        selectedPiece = null;
        selectedRow = -1;
        selectedCol = -1;
        availableMoves = new boolean[BOARD_SIZE][BOARD_SIZE];
    }
    
    // Check for class promotion using the controller class
    public boolean checkForPromotion()
    {
        if(chessController.canPromote())
        {
            chessFrame.switchTab(5);
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
        g.setColor(Color.BLACK);
        g.fillRect(CELL_SIZE - frameWidth - 59, CELL_SIZE - frameWidth - 58, frameWidth, boardHeight + 2 * frameWidth); // LEFT
        g.fillRect(CELL_SIZE - frameWidth - 59, CELL_SIZE - frameWidth - 58, boardWidth + 2 * frameWidth, frameWidth); // TOP
        g.fillRect(CELL_SIZE - frameWidth - 59, CELL_SIZE + boardHeight  - 58, boardWidth + 2 * frameWidth, frameWidth);
        g.fillRect(CELL_SIZE + boardWidth - 59, CELL_SIZE - frameWidth - 58, frameWidth, boardHeight + 2 * frameWidth);
        if(chessController.getPlayer1() == null || chessController.getPlayer2() == null) // Check if player has logged in
        {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(CELL_SIZE - 59, CELL_SIZE - 58, BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE);
        }
        
        // If there are available moves, the program will draw them to the GUI
        if(availableMoves != null)
        {
            drawAvailableMoves(g);
        }
    }
    
    private void drawChessBoard(Graphics g) 
    {
        int currentRow = 7;
        int rowChange = -1;
        if(isFlipFlag())
        {
            currentRow = 0;
            rowChange = 1;
        }
        for (int row = 0; row < 8; row++) 
        {
            for (int col = 0; col < 8; col++) 
            {
                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;
                x += 4;
                y += 5;
                
                int currentCol = col;
                
                if(isFlipFlag())
                {
                    currentCol = 7 - col;
                }
                
                Piece piece = chessController.getBoard()[currentCol][currentRow];

                if ((row + col) % 2 == 0) 
                {
                    g.setColor(Color.WHITE);
                } 
                else 
                {
                    g.setColor(Color.LIGHT_GRAY);
                }
                
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                
                drawPiece(g,piece,x,y);
                drawColumnLabels(g,row,col,x,y);
                drawRowLabels(g,col,row,x,y);
                drawYellowOverlay(g,x,y,currentCol,currentRow);
                drawCheckHighlight(g,piece,x,y);
            }
            currentRow += rowChange; 
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
    
    private void drawYellowOverlay(Graphics g, int x, int y,int currentCol, int currentRow) 
    {
        // A yellow overlay on the square where the piece was
        if(wasAtCol == currentCol && wasAtRow == currentRow) 
        {
            g.setColor(new Color(255, 255, 0, 58));
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }
        // A yellow overlay on the square the piece moved to
        if(movedToCol == currentCol && movedToRow == currentRow) 
        {
            g.setColor(new Color(255, 255, 0, 58));
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }
    }
    
    private void drawCheckHighlight(Graphics g, Piece piece, int x, int y) 
    {
        // Draw red outline on the square where the king is in check.
        if (piece != null && piece.getSymbol().contains("K") && chessController.isInCheck(piece.getColour())) 
        {
            g.setColor(Color.RED);
            g.drawRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1);
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
        if (getSelectedRow() != -1 && getSelectedCol() != -1) 
        {
            g.setColor(new Color(0, 0, 0, 50));

            for (int row = 0; row < 8; row++) 
            {
                for (int col = 0; col < 8; col++) 
                {
                    if (availableMoves[col][row]) 
                    {
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
        }
    }
    
    // Reset the whole game and selections
    public void resetGame() 
    {
        wasAtCol = -1;
        wasAtRow = -1;
        movedToCol = -1;
        movedToRow = -1;
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
     * @return the selectedRow
     */
    public int getSelectedRow() {
        return selectedRow;
    }

    /**
     * @return the selectedCol
     */
    public int getSelectedCol() {
        return selectedCol;
    }

    /**
     * @return the selectedPiece
     */
    public Piece getSelectedPiece() {
        return selectedPiece;
    }
}