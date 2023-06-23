/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess_Project_2;

/**
 *
 * @author rh200
 */
public class King extends Piece {
    
    private boolean[][] availableMoves = new boolean[8][8];
    private boolean[][] targetArea = new boolean[8][8];
    private PiecesOnBoard pieces;

    public King(PieceColour colour, int col, int row)
    {
        super(colour, col, row);
    }
    
    public King(PieceColour colour, int col, int row, int LMN, boolean HNM, boolean HMO)
    {
        super(colour, col, row, LMN, HNM, HMO);
    }
    
    //return white king or black king symbol
    @Override
    public String getSymbol()
    {
        if (getColour() == PieceColour.WHITE) 
        {
            return "wK";
        } 
        else
        {
            return "bK";
        }
    }
    
    //return king's available moves (the 8 squares around the king)
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
        setAvailableMoves(col+1, row+1);
        setAvailableMoves(col+1, row-1);
        setAvailableMoves(col-1, row+1);
        setAvailableMoves(col-1, row-1);
        
        //castle moves
        setAvailableMoves(col+2, row);
        setAvailableMoves(col-2, row);

        return availableMoves;
    }
    
    //return king's targeting squares (the 8 squares around the king)
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
        setTargetArea(col+1, row+1);
        setTargetArea(col+1, row-1);
        setTargetArea(col-1, row+1);
        setTargetArea(col-1, row-1);
        return targetArea;
    }
    
    //set the square as availbale if within the board boundary and not occupied by same colour piece
    private void setAvailableMoves(int col, int row)
    {
        if(col <= 7 && col >= 0 && row <= 7 && row >= 0)
        {
            if (pieces.isCastling(this, col, row))
            {
                availableMoves[col][row] = true;
            }
            else if (Math.abs(col-this.getColumn()) < 2
                    && (pieces.getPiece(col, row) == null || pieces.getPiece(col, row).getColour() != this.getColour()) 
                    && !pieces.getPieces().getTargetAreas(this.getColour().getOppColour())[col][row])
            {
                availableMoves[col][row] = true;
            }
        }
    }
    
    //set the square as targeted
    private void setTargetArea(int col, int row)
    {
        if(col <= 7 && col >= 0 && row <= 7 && row >= 0)
        {
            targetArea[col][row] = true;
        }
    }
}
