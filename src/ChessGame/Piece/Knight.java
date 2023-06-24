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
public class Knight extends Piece {
    
    private boolean[][] availableMoves = new boolean[8][8];
    private boolean[][] targetArea = new boolean[8][8];
    private PiecesOnBoard board;
    
    public Knight(PieceColour colour,int col, int row, PiecesOnBoard board)
    {
        super(colour, col, row, 3);
        this.board = board;
    }
    
    public Knight(PieceColour colour, int col, int row, int LMN, boolean HNM, boolean HMO, PiecesOnBoard board)
    {
        super(colour, col, row, LMN, HNM, HMO, 3);
        this.board = board;
    }
    
    //return white knight or black knight symbol
    @Override
    public String getSymbol()
    {
        if (getColour() == PieceColour.WHITE) 
        {
            return "wN";
        } 
        else
        {
            return "bN";
        }
    }
    
    //return knight's available moves (L-patterned move)
    //move can be unavailable due to the board boundary, the same colour board, or under pin.
    @Override
    public boolean[][] getAvailableMoves()
    {
        int col = super.getColumn();
        int row = super.getRow();
        
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                availableMoves[i][j] = false;
            }
        }
        
        setAvailableMoves(col+2, row+1);
        setAvailableMoves(col+2, row-1);
        setAvailableMoves(col+1, row+2);
        setAvailableMoves(col+1, row-2);
        setAvailableMoves(col-1, row+2);
        setAvailableMoves(col-1, row-2);
        setAvailableMoves(col-2, row+1);
        setAvailableMoves(col-2, row-1);
        
        //if knight is under pin, then there is no available move for knight
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

            return newAvailableMoves;
        }
        
        return availableMoves;
    }
    
    //return knight's targeting squares (L-patterned)
    //if knight check the opponent king, send the check path to the PiecesOnBoard class for movement restriction.
    @Override
    public boolean[][] getTargetArea()
    {
        int col = super.getColumn();
        int row = super.getRow();
        
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                targetArea[i][j] = false;
            }
        }
        
        setTargetArea(col+2, row+1);
        setTargetArea(col+2, row-1);
        setTargetArea(col+1, row+2);
        setTargetArea(col+1, row-2);
        setTargetArea(col-1, row+2);
        setTargetArea(col-1, row-2);
        setTargetArea(col-2, row+1);
        setTargetArea(col-2, row-1);
        
        return targetArea;
    }
    
    //set knight's available squares
    private void setAvailableMoves(int col, int row)
    {
        if(col <= 7 && col >= 0 && row <= 7 && row >= 0 && board.getCheckPath()[col][row])
        {
            if(board.getPiece(col, row) == null)
            {
                availableMoves[col][row] = true;
            }
            else if(board.getPiece(col, row).getColour() != super.getColour())
            {
                availableMoves[col][row] = true;
            }
        }
    }
    
    //set knight's targeting squares as targeted
    private void setTargetArea(int col, int row)
    {
        if(col <= 7 && col >= 0 && row <= 7 && row >= 0)
        {
            targetArea[col][row] = true;
            
            if(board.getPiece(col, row) != null)
            {
                //if knight check the opponent king, send the check path to the PiecesOnBoard class for movement restriction.
                if(board.getPiece(col, row).getColour() != super.getColour() && board.getPiece(col, row).getSymbol().contains("K"))
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
                    board.setInCheck(board.getPiece(col, row).getColour(), checkPath);
                }
            }
        }
    }
}
