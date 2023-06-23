/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChessGame.Piece;

import ChessGame.Board.PiecesOnBoard;

/**
 *
 * @author rh200
 */
public class Rook extends Piece {
    
    private boolean[][] availableMoves = new boolean[8][8];
    private boolean[][] targetArea = new boolean[8][8];
    private PiecesOnBoard pieces;
    
    public Rook(PieceColour colour,int col, int row)
    {
        super(colour, col, row);
    }
    
    public Rook(PieceColour colour, int col, int row, int LMN, boolean HNM, boolean HMO)
    {
        super(colour, col, row, LMN, HNM, HMO);
    }
    
    //return white rook or black rook symbol
    @Override
    public String getSymbol()
    {
        if (getColour() == PieceColour.WHITE) 
        {
            return "wR";
        } 
        else
        {
            return "bR";
        }
    }
    
    //return rook's available moves (horizontally and vertically)
    //move can be unavailable due to the board boundary, the same colour pieces, or under pin.
    @Override
    public boolean[][] getAvailableMoves()
    {
        pieces = new PiecesOnBoard();
        int col = super.getColumn();
        int row = super.getRow();
        
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                availableMoves[i][j] = false;
            }
        }
        
        setAvailableMoves(col+1, row);
        setAvailableMoves(col-1, row);
        setAvailableMoves(col, row+1);
        setAvailableMoves(col, row-1);
        
        //if rook is under pin, then return the available moves within the pin path
        if(super.isUnderPin())
        {
            boolean[][] newAvailableMoves = new boolean[8][8];
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    newAvailableMoves[i][j] = false;
                }
            }
            
            for(col = 0; col < 8; col++)
            {
                for(row = 0; row < 8; row++)
                {
                    if(super.getPinPath()[col][row] && availableMoves[col][row])
                    {
                        newAvailableMoves[col][row] = true;
                    }
                }
            }
            
            return newAvailableMoves;
        }
        
        return availableMoves;
    }
    
    //return rook's targeting squares (horizontally and vertically)
    //if rook pin the opponemt king, send the pin path to the piece that is under the pin and set its isUnderPin to true.
    //if rook check the opponent king, send the check path to the PiecesOnBoard class for movement restriction.
    @Override
    public boolean[][] getTargetArea()
    {
        pieces = new PiecesOnBoard();
        int col = super.getColumn();
        int row = super.getRow();
        
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                targetArea[i][j] = false;
            }
        }
        
        setTargetArea(col+1, row);
        setTargetArea(col-1, row);
        setTargetArea(col, row+1);
        setTargetArea(col, row-1);
        
        return targetArea;
    }
    
    //return a further column away from the rook
    private int setColUpOrDown(int col)
    {
        if((col - super.getColumn()) > 0)
        {
            col++;
        }
        else if((col - super.getColumn()) < 0)
        {
            col--;
        }
        
        return col;
    }
    
    //return a further row away from the rook
    private int setRowUpOrDown(int row)
    {
        if((row - super.getRow()) > 0)
        {
            row++;
        }
        else if((row - super.getRow()) < 0)
        {
            row--;
        }
        return row;
    }
        
    //set rook's available squares
    private void setAvailableMoves(int col, int row)
    {
        while(col <= 7 && col >= 0 && row <= 7 && row >= 0)
        {
            if(pieces.getPiece(col, row) == null)
            {
                if (pieces.getCheckPath()[col][row]) {
                    availableMoves[col][row] = true;
                }
                col = setColUpOrDown(col);
                row = setRowUpOrDown(row);
                continue;
            }
            else if(pieces.getPiece(col, row).getColour() != super.getColour() && pieces.getCheckPath()[col][row])
            {
                availableMoves[col][row] = true;
            }
            break;
        }
    }
    
    //set rook's targeting squares as targeted
    private void setTargetArea(int col, int row)
    {
        while(col <= 7 && col >= 0 && row <= 7 && row >= 0)
        {
            if(pieces.getPiece(col, row) != null)
            {
                targetArea[col][row] = true;
                
                //if rook check the opponent king, send the check path to the PiecesOnBoard class for movement restriction.
                if(pieces.getPiece(col, row).getColour() != super.getColour() 
                && pieces.getPiece(col, row).getSymbol().contains("K"))
                {
                    pieces.setInCheck(pieces.getPiece(col, row).getColour(), getPath(col, row));
                    col = setColUpOrDown(col);
                    row = setRowUpOrDown(row);
                    continue;
                }
                
                //if rook pin the opponemt king, send the pin path to the piece that is under pin and set its isUnderPin to true.
                else if(pieces.getPiece(col, row).getColour() != super.getColour())
                {
                    int pinnedCol = col;
                    int pinnedRow = row;
                    col = setColUpOrDown(col);
                    row = setRowUpOrDown(row);
                    
                    while(col <= 7 && col >= 0 && row <= 7 && row >= 0)
                    {
                        if(pieces.getPiece(col, row) == null)
                        {
                            col = setColUpOrDown(col);
                            row = setRowUpOrDown(row);
                            continue;
                        }
                        else if(pieces.getPiece(col, row).getColour() != super.getColour() 
                                && pieces.getPiece(col, row).getSymbol().contains("K"))
                        {
                            pieces.getPiece(pinnedCol, pinnedRow).setUnderPin(true);
                            pieces.getPiece(pinnedCol, pinnedRow).setPinPath(getPath(col, row));
                            break;
                        }
                        else
                        {
                            break;
                        }
                    }
                }
                break;
            }
            targetArea[col][row] = true;
            col = setColUpOrDown(col);
            row = setRowUpOrDown(row);
        }
    }
    
    private boolean[][] getPath(int col, int row)
    {
        boolean[][] path = new boolean[8][8];
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                path[i][j] = false;
            }
        }
        int pathCol = super.getColumn();
        int pathRow = super.getRow();
        while(!(pathCol == col && pathRow == row))
        {
            path[pathCol][pathRow] = true;
            if(pathCol < col)
            {
                pathCol++;
            }
            else if(pathCol > col)
            {
                pathCol--;
            }
            if(pathRow < row)
            {
                pathRow++;
            }
            else if(pathRow > row)
            {
                pathRow--;
            }
        }
        
        return path;
    }
}
