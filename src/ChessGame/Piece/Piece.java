/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChessGame.Piece;

import ChessGame.Piece.PieceColour;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author rh200
 */
public abstract class Piece {
    
    private int row;
    private int column;
    private PieceColour colour;
    private final int pointValue;
    private int lastmoveNum;
    private boolean hasNotMoved;
    private boolean hasMovedOnce;
    private boolean isUnderPin;
    private boolean[][] pinPath = new boolean[8][8];
    
    //a piece constructor
    public Piece(PieceColour colour, int col, int row, int points)
    {
        this.colour = colour;
        this.column = col;
        this.row = row;
        this.pointValue = points;
        this.lastmoveNum = 0;
        this.hasNotMoved = true;
        this.hasMovedOnce = false;
    }
    
    //a piece constructor
    public Piece(PieceColour colour, int col, int row, int LMN, boolean HNM, boolean HMO, int points)
    {
        this.colour = colour;
        this.column = col;
        this.row = row;
        this.pointValue = points;
        this.lastmoveNum = LMN;
        this.hasNotMoved = HNM;
        this.hasMovedOnce = HMO;
    }
    
    public Image getImage()
    {
        String path = "chessPiece/" + getSymbol() + ".png";
        ImageIcon icon = new ImageIcon(path);
        
        return icon.getImage();
    }
    
    public int getPointValue()
    {
        return pointValue;
    }
    
    //return piece's column
    public int getColumn()
    {
        return column;
    }
    
    //return piece's row
    public int getRow()
    {
        return row;
    }
    
    //set piece's column and row
    public void setColAndRow(int col, int row)
    {
        this.column = col;
        this.row = row;
    }
    
    //return the colour of the piece
    public PieceColour getColour()
    {
        return colour;
    }
    
    //return the opposite colour of the piece
    public PieceColour getOppColour()
    {
        return colour.getOppColour();
    }
    
    //return the piece's symbol
    public abstract String getSymbol();
    
    //return the last move number of the piece
    public int getLastMoveNum()
    {
        return lastmoveNum;
    }
    
    public void setLastMoveNum(int LMN)
    {
        lastmoveNum = LMN;
    }
    
    //set move has being done
    public void setMove()
    {
        if(hasNotMoved)
        {
            hasMovedOnce = true;
        }
        else
        {
            hasMovedOnce = false;
        }
        hasNotMoved = false;
    }
    
    //return true if the piece has not moved yet
    public boolean hasNotMoved()
    {
        return hasNotMoved;
    }
    
    //return true if the piece only moved once
    public boolean hasMovedOnce()
    {
        return hasMovedOnce;
    }
    
    //return the path of pin area on the board
    public boolean[][] getPinPath()
    {
        return pinPath;
    }
    
    //set the pin path to the piece
    public void setPinPath(boolean[][] pinPath)
    {
        this.pinPath = pinPath;
    }
    
    //set this piece as under pin
    public void setUnderPin(boolean TorF)
    {
        this.isUnderPin = TorF;
    }
    
    //return true is the piece is under pin, else false
    public boolean isUnderPin()
    {
        return isUnderPin;
    }
    
    //return the available moves of the piece
    public boolean[][] getAvailableMoves()
    {
        boolean[][] availableMoves = new boolean[8][8];
        for(boolean[] i : availableMoves)
        {
            for(boolean j : i)
            {
                j = false;
            }
        }
        return availableMoves;
    }
    
    //return the target area of the piece
    public boolean[][] getTargetArea()
    {
        boolean[][] targetArea = new boolean[8][8];
        for(boolean[] i : targetArea)
        {
            for(boolean j : i)
            {
                j = false;
            }
        }
        return targetArea;
    }
}