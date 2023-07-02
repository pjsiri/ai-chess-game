/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.Board;

import Chess.Pieces.Bishop;
import Chess.Pieces.King;
import Chess.Pieces.Knight;
import Chess.Pieces.Pawn;
import Chess.Pieces.Piece;
import Chess.Pieces.PieceColour;
import Chess.Pieces.Queen;
import Chess.Pieces.Rook;
import java.util.HashSet;

/**
 *
 * @author rh200
 */
public class BoardPieces {
    
    private Board board;
    private HashSet<Piece> whitePieces;
    private HashSet<Piece> blackPieces;
    
    public BoardPieces(Board board)
    {
        this.board = board;
        whitePieces = new HashSet<>();
        blackPieces = new HashSet<>();
    }
    
    public int getOverallEvaluation()
    {
        return getColourPoints(PieceColour.WHITE) - getColourPoints(PieceColour.BLACK);
    }
    
    private int getColourPoints(PieceColour colour)
    {
        int totalPoints = 0;
        for(Piece piece : getAllPieces(colour))
        {
            totalPoints += piece.getPoints();
        }
        return totalPoints;
    }
    
    public HashSet<Piece> getAllPieces(PieceColour colour)
    {
        if (colour == PieceColour.WHITE) {
            return whitePieces;
        } else {
            return blackPieces;
        }
    }
    
    public HashSet<Integer[]> getAllMoves(PieceColour colour)
    {
        HashSet<Integer[]> moves = new HashSet<>();
        
        for (Piece piece : getAllPieces(colour)) {
            for (int availableMove : piece.getAvailableMoves()) {
                Integer[] move = new Integer[]{piece.getPosition(), availableMove};
                moves.add(move);
            }
        }
        
        return moves;
    }
    
    public HashSet<Integer[]> getAllCaptureMoves(PieceColour colour)
    {
        HashSet<Integer[]> moves = new HashSet<>();
        
        for (Piece piece : getAllPieces(colour)) {
            for (int availableMove : piece.getAvailableMoves()) {
                if (board.getBoard()[availableMove] != null) {
                    Integer[] move = new Integer[]{piece.getPosition(), availableMove};
                    moves.add(move);
                }
            }
        }
        
        return moves;
    }
    
    public Piece getKing(PieceColour colour)
    {
        for(Piece piece : getAllPieces(colour))
        {
            if (piece.getSymbol().contains("K")) {
                return piece;
            }
        }
        return null;
    }
    
    public void addPiece(Piece piece)
    {
        if (piece.getColour().equals(PieceColour.WHITE)) {
            whitePieces.add(piece);
        } else 
        {
            blackPieces.add(piece);
        }
    }
    
    public void removePiece(int position)
    {
        whitePieces.removeIf(piece -> piece.getPosition() == position);
        blackPieces.removeIf(piece -> piece.getPosition() == position);
    }
    
    public void replacePiece(int position, Piece replacement)
    {
        removePiece(position);
        addPiece(replacement);
    }
    
    public void setDefault()
    {
        whitePieces.clear();
        blackPieces.clear();
        
        whitePieces.add(new Rook(PieceColour.WHITE, 0, false, board));
        whitePieces.add(new Knight(PieceColour.WHITE, 1, false, board));
        whitePieces.add(new Bishop(PieceColour.WHITE, 2, false, board));
        whitePieces.add(new Queen(PieceColour.WHITE, 3, false, board));
        whitePieces.add(new King(PieceColour.WHITE, 4, false, board));
        whitePieces.add(new Bishop(PieceColour.WHITE, 5, false, board));
        whitePieces.add(new Knight(PieceColour.WHITE, 6,false,  board));
        whitePieces.add(new Rook(PieceColour.WHITE, 7, false, board));
        for (int n = 8; n < 16; n++) {
            whitePieces.add(new Pawn(PieceColour.WHITE, n, false, board));
        }
        
        blackPieces.add(new Rook(PieceColour.BLACK, 56, false, board));
        blackPieces.add(new Knight(PieceColour.BLACK, 57, false, board));
        blackPieces.add(new Bishop(PieceColour.BLACK, 58, false, board));
        blackPieces.add(new Queen(PieceColour.BLACK, 59, false, board));
        blackPieces.add(new King(PieceColour.BLACK, 60, false, board));
        blackPieces.add(new Bishop(PieceColour.BLACK, 61, false, board));
        blackPieces.add(new Knight(PieceColour.BLACK, 62, false, board));
        blackPieces.add(new Rook(PieceColour.BLACK, 63, false, board));
        for (int n = 48; n < 56; n++) {
            blackPieces.add(new Pawn(PieceColour.BLACK, n, false, board));
        }
    }
    
    public void clear()
    {
        whitePieces.clear();
        blackPieces.clear();
    }
    
    public void setCopy(BoardPieces pieces)
    {
        whitePieces.clear();
        blackPieces.clear();
        
        for (Piece piece : pieces.getAllPieces(PieceColour.WHITE)) {
            whitePieces.add(getPieceCopy(piece));
        }
        for (Piece piece : pieces.getAllPieces(PieceColour.BLACK)) {
            blackPieces.add(getPieceCopy(piece));
        }
    }
    
    public HashSet<Integer> getAllTargetSquares(PieceColour colour)
    {
        HashSet<Integer> targetingSquares = new HashSet<>();
        
        if (colour == PieceColour.WHITE) {
            for (Piece piece : whitePieces)
            {
                targetingSquares.addAll(piece.getTargetSquares());
            }
        } else {
            for (Piece piece : blackPieces)
            {
                targetingSquares.addAll(piece.getTargetSquares());
            }
        }
        
        return targetingSquares;
    }
    
    private Piece getPieceCopy(Piece piece)
    {
        Piece pieceCopy;
        switch (piece.getSymbol().charAt(1))
        {
            case 'P':
                pieceCopy = new Pawn(piece, board);
                break;
            case 'R':
                pieceCopy = new Rook(piece, board);
                break;
            case 'N':
                pieceCopy = new Knight(piece, board);
                break;
            case 'B':
                pieceCopy = new Bishop(piece, board);
                break;
            case 'K':
                pieceCopy = new King(piece, board);
                break;
            default:
                pieceCopy = new Queen(piece, board);
                break;
        }
        return pieceCopy;
    }
}
