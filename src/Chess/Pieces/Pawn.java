/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.Pieces;

import Chess.Board.Board;
import java.util.Collections;
import java.util.HashSet;

/**
 *
 * @author rh200
 */
public class Pawn extends Piece {
    
    private Board board;
    private HashSet<Integer> availableMoves;
    private HashSet<Integer> targetingSquares;
    private final int N = 8;
    private final int S = -8;
    private final int E = 1;
    private final int W = -1;
    
    public Pawn(PieceColour colour, int position, boolean isMoved, Board board)
    {
        super(colour, 100, position, isMoved);
        this.board = board;
        availableMoves = new HashSet<>();
        targetingSquares = new HashSet<>();
    }
    
    public Pawn(Piece piece, Board board)
    {
        super(piece.getColour(), 100, piece.getPosition(), piece.isMoved());
        this.board = board;
        availableMoves = new HashSet<>();
        targetingSquares = new HashSet<>();
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
    
    @Override
    public HashSet<Integer> getAvailableMoves()
    {
        this.availableMoves.clear();
        int position = this.getPosition();
        
        setAvailableMoves(position, 0);
        setAvailableMoves(position, E);
        setAvailableMoves(position, W);
        
        if (board.isChecked()) {
            this.availableMoves.retainAll(board.getCheckPath());
        }
        if (this.isPinned()) {
            this.availableMoves.retainAll(this.getPinPath());
        }
        
        return this.availableMoves;
    }
    
    //need to add en passant if <---
    private void setAvailableMoves(int position, int horizontal)
    {
        if ((position % 8 == 0 && horizontal == W) || ((position - 7) % 8 == 0 && horizontal == E)) {
            return;
        }
        
        int vertical;
        if (this.getColour() == PieceColour.WHITE) {
            vertical = N;
        } else {
            vertical = S;
        }
        
        int destination = position + vertical + horizontal;
        if (destination >= 0 && destination <= 63)
        {
            if (horizontal == 0 && board.getBoard()[destination] == null) {
                this.availableMoves.add(destination);
                if (!this.isMoved() && position == this.getPosition()) {
                    setAvailableMoves(destination, horizontal);
                }
            }
            else if (horizontal != 0 && ((board.getBoard()[destination] != null && board.getBoard()[destination].getColour() != this.getColour()) 
                    || board.isEnPassant(this, destination))) {
                this.availableMoves.add(destination);
            }
        }
    }
    
    @Override
    public HashSet<Integer> getTargetSquares()
    {
        this.targetingSquares.clear();
        int position = this.getPosition();
        
        setTargetSquares(position, E);
        setTargetSquares(position, W);
        
        return this.targetingSquares;
    }
    
    private void setTargetSquares(int position, int horizontal)
    {
        if ((position % 8 == 0 && horizontal == W) || ((position - 7) % 8 == 0 && horizontal == E)) {
            return;
        }
        
        int vertical;
        if (this.getColour() == PieceColour.WHITE) {
            vertical = N;
        } else {
            vertical = S;
        }
        
        int destination = position + vertical + horizontal;
        if (destination >= 0 && destination <= 63)
        {
            this.targetingSquares.add(destination);
            //check opponent king
            if (board.getBoard()[destination] != null && board.getBoard()[destination].getSymbol().contains("K") 
                    && board.getBoard()[destination].getColour() != this.getColour()) {
                board.setCheckPath(new HashSet<>(Collections.singleton(position)));
            }
        }
    }
}
