/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess_Project_2;

/**
 *
 * @author rh200
 */
public class Pawn extends Piece {
    
    private boolean[][] availableMoves = new boolean[8][8];
    private boolean[][] targetArea = new boolean[8][8];
    private PiecesOnBoard pieces;
    
    public Pawn(PieceColour colour,int col, int row)
    {
        super(colour, col, row);
    }
    
    public Pawn(PieceColour colour, int col, int row, int LMN, boolean HNM, boolean HMO)
    {
        super(colour, col, row, LMN, HNM, HMO);
    }
    
    //return white pawn or black pawn symbol
    @Override
    public String getSymbol()
    {
        if (getColour() == PieceColour.WHITE) {
            return "wP";
        } else {
            return "bP";
        }
    }
    
    //return pawn's available moves (one square forward, early two squares advance, or one square diagonally forward for capturing)
    //move can be unavailable due to occupied square by any piece infront, or under pin.
    @Override
    public boolean[][] getAvailableMoves()
    {
        pieces = new PiecesOnBoard();
        resetAvailableMoves();
        setAvailableMove(super.getColumn());
        setAvailableMove(super.getColumn()+1);
        setAvailableMove(super.getColumn()-1);
        
        //if pawn is under pin, then return the available moves within the pin path
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
            
            for(int col = 0; col < 8; col++)
            {
                for(int row = 0; row < 8; row++)
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
    
    private void resetAvailableMoves()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                availableMoves[i][j] = false;
            }
        }
    }
    
    private void setAvailableMove(int col)
    {
        int forwardValue;
        if (super.getColour() == PieceColour.WHITE) {
            forwardValue = 1;
        } else {
            forwardValue = -1;
        }

        int row = super.getRow() + forwardValue;
        //one square forward
        if(row <= 7 && row >= 0 && col <= 7 && col >= 0)
        {
            if((col == super.getColumn() && pieces.getPiece(col, row) == null) 
                    || (col != super.getColumn() && pieces.getPiece(col, row) != null && pieces.getPiece(col, row).getColour() != super.getColour()) 
                    || pieces.isEnPassant(this, col, row))
            {
                if (pieces.getCheckPath()[col][row]) {
                    availableMoves[col][row] = true;
                }

                //two squares advance
                if(super.hasNotMoved() && col == super.getColumn() && pieces.getPiece(super.getColumn(), row+forwardValue) == null && pieces.getCheckPath()[super.getColumn()][row+forwardValue])
                {
                    availableMoves[super.getColumn()][row+forwardValue] = true;
                }
            }
        }
    }
    
    //return pawn's targeting squares (one square diagonally infront)
    //if pawn check the opponent king, send the check path to the PiecesOnBoard class for movement restriction.
    @Override
    public boolean[][] getTargetArea()
    {
        pieces = new PiecesOnBoard();
        resetTargetArea();
        setTargetArea(super.getColumn() + 1);
        setTargetArea(super.getColumn() - 1);
        
        return targetArea;
    }
    
    private void resetTargetArea()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                targetArea[i][j] = false;
            }
        }
    }
    
    //set pawn's targeting squares as targeted
    private void setTargetArea(int col)
    {
        int row = super.getRow();
        if (super.getColour() == PieceColour.WHITE) {
            row++;
        } else {
            row--;
        }
        
        if(col <= 7 && col >= 0 && row <= 7 && row >= 0)
        {
            targetArea[col][row] = true;
            
            //if pawn check the opponent king, send the check path to the PiecesOnBoard class for movement restriction.
            if(pieces.getPiece(col, row) != null && pieces.getPiece(col, row).getSymbol().contains("K") && pieces.getPiece(col, row).getColour() != super.getColour())
            {
                boolean[][] checkPath = new boolean[8][8];
                for(int i = 0; i < 8; i++)
                {
                    for(int j = 0; j < 8; j++)
                    {
                        checkPath[i][j] = false;
                    }
                }
                checkPath[super.getColumn()][super.getRow()] = true;
                pieces.setInCheck(pieces.getPiece(col, row).getColour(), checkPath);
            }
        }
    }
}