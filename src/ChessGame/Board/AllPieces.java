/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChessGame.Board;

import ChessGame.Piece.Bishop;
import ChessGame.Piece.King;
import ChessGame.Piece.Knight;
import ChessGame.Piece.Pawn;
import ChessGame.Piece.Piece;
import ChessGame.Piece.PieceColour;
import ChessGame.Piece.Queen;
import ChessGame.Piece.Rook;
import java.util.ArrayList;

/**
 *
 * @author rh200
 */
public class AllPieces {
    
    private ArrayList<Piece> pieces = new ArrayList<Piece>();
    
    // pieces list constructor
    public AllPieces(PiecesOnBoard board)
    {
        pieces.clear();
	pieces.add(new King(PieceColour.WHITE, 4, 0, board));
        pieces.add(new Queen(PieceColour.WHITE, 3, 0, board));
        pieces.add(new Bishop(PieceColour.WHITE, 2, 0, board));
        pieces.add(new Bishop(PieceColour.WHITE, 5, 0, board));
        pieces.add(new Knight(PieceColour.WHITE, 1, 0, board));
        pieces.add(new Knight(PieceColour.WHITE, 6, 0, board));
        pieces.add(new Rook(PieceColour.WHITE, 0, 0, board));
        pieces.add(new Rook(PieceColour.WHITE, 7, 0, board));
        pieces.add(new Pawn(PieceColour.WHITE, 0, 1, board));
        pieces.add(new Pawn(PieceColour.WHITE, 1, 1, board));
        pieces.add(new Pawn(PieceColour.WHITE, 2, 1, board));
        pieces.add(new Pawn(PieceColour.WHITE, 3, 1, board));
        pieces.add(new Pawn(PieceColour.WHITE, 4, 1, board));
        pieces.add(new Pawn(PieceColour.WHITE, 5, 1, board));
        pieces.add(new Pawn(PieceColour.WHITE, 6, 1, board));
        pieces.add(new Pawn(PieceColour.WHITE, 7, 1, board));

	pieces.add(new King(PieceColour.BLACK, 4, 7, board));
        pieces.add(new Queen(PieceColour.BLACK, 3, 7, board));
        pieces.add(new Bishop(PieceColour.BLACK, 2, 7, board));
        pieces.add(new Bishop(PieceColour.BLACK, 5, 7, board));
        pieces.add(new Knight(PieceColour.BLACK, 1, 7, board));
        pieces.add(new Knight(PieceColour.BLACK, 6, 7, board));
        pieces.add(new Rook(PieceColour.BLACK, 0, 7, board));
        pieces.add(new Rook(PieceColour.BLACK, 7, 7, board));
        pieces.add(new Pawn(PieceColour.BLACK, 0, 6, board));
        pieces.add(new Pawn(PieceColour.BLACK, 1, 6, board));
        pieces.add(new Pawn(PieceColour.BLACK, 2, 6, board));
        pieces.add(new Pawn(PieceColour.BLACK, 3, 6, board));
        pieces.add(new Pawn(PieceColour.BLACK, 4, 6, board));
        pieces.add(new Pawn(PieceColour.BLACK, 5, 6, board));
        pieces.add(new Pawn(PieceColour.BLACK, 6, 6, board));
        pieces.add(new Pawn(PieceColour.BLACK, 7, 6, board));
    }
    
    //return a list of white pieces
    public ArrayList<Piece> getAllPieces()
    {
        return this.pieces;
    }
    
    //return a piece that is located at the selected square from the list
    public Piece getPiece(int col, int row)
    {
        for(Piece i : pieces)
        {
            if(i.getColumn() == col && i.getRow() == row)
            {
                return i;
            }
        }
        return null;
    }
    
    //return the king piece
    public Piece getKing(PieceColour colour)
    {
        for(Piece i : pieces)
        {
            if(i.getSymbol().contains("K") && i.getColour() == colour)
            {
                return i;
            }
        }
        return null;
    }
    
    //add a piece into the list
    public void addPiece(Piece piece)
    {
        pieces.add(piece);
    }
    
    //remove all pieces from the list
    public void clearPieces()
    {
        pieces.clear();
    }
    
    //remove a piece that is located at a square from the list
    public void removePiece(int col, int row)
    {
        for(Piece i : pieces)
        {
            if(i.getColumn() == col && i.getRow() == row)
            {
                pieces.remove(i);
                break;
            }
        }
    }
    
    //replace a piece from the list that is located at a square with another piece
    public void replacePiece(Piece replacement, int col, int row)
    {
        for(Piece i : pieces)
        {
            if(i.getColumn() == col && i.getRow() == row)
            {
                removePiece(col, row);
                pieces.add(replacement);
                break;
            }
        }
    }
    
    //return the target area on the board from all the white pieces
    public boolean[][] getTargetAreas(PieceColour colour)
    {
        boolean[][] allTargetArea = new boolean[8][8];
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                allTargetArea[i][j] = false;
            }
        }
        
        for(Piece i : pieces)
        {
            if (i.getColour() == colour)
            {
                boolean[][] targetArea = i.getTargetArea();
                for(int col = 0; col < 8; col++)
                {
                    for(int row = 0; row < 8; row++)
                    {
                        if(targetArea[col][row])
                        {
                            allTargetArea[col][row] = true;
                        }
                    }
                }
            }
        }
        return allTargetArea;
    }
    
    public void resetUnderPin()
    {
        for(Piece i : pieces)
        {
            i.setUnderPin(false);
        }
    }
    
    public PieceColour getCurrentColourTurn()
    {
        int moveNum = 0;
        PieceColour lastMovedColour = PieceColour.BLACK;
        for(Piece i : pieces)
        {
            if(i.getLastMoveNum() > moveNum)
            {
                moveNum = i.getLastMoveNum();
                lastMovedColour = i.getColour();
            }
        }
        
        return lastMovedColour.getOppColour();
    }
}
