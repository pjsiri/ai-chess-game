/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.Pieces;

import Chess.Board.Board;
import java.util.HashSet;

/**
 *
 * @author rh200
 */
public class King extends Piece {
    
    private Board board;
    private HashSet<Integer> availableMoves;
    private HashSet<Integer> targetingSquares;
    private final int N = 8;
    private final int S = -8;
    private final int E = 1;
    private final int W = -1;
    
    public King(PieceColour colour, int position, boolean isMoved,Board board)
    {
        super(colour, 1000, position, isMoved);
        this.board = board;
        availableMoves = new HashSet<>();
        targetingSquares = new HashSet<>();
    }
    
    public King(Piece piece, Board board)
    {
        super(piece.getColour(), 1000, piece.getPosition(), piece.isMoved());
        this.board = board;
        availableMoves = new HashSet<>();
        targetingSquares = new HashSet<>();
    }
    
    //return white king or black king symbol
    @Override
    public String getSymbol()
    {
        if (getColour() == PieceColour.WHITE) {
            return "wK";
        } else {
            return "bK";
        }
    }
    
    @Override
    public HashSet<Integer> getAvailableMoves()
    {
        this.availableMoves.clear();
        int position = this.getPosition();
        
        setAvailableMoves(position, N, 0);
        setAvailableMoves(position, S, 0);
        setAvailableMoves(position, 0, E);
        setAvailableMoves(position, 0, W);
        setAvailableMoves(position, N, E);
        setAvailableMoves(position, N, W);
        setAvailableMoves(position, S, E);
        setAvailableMoves(position, S, W);
        
        //castle moves
        setAvailableMoves(position, E, E);
        setAvailableMoves(position, W, W);
        
        return this.availableMoves;
    }
    
    //need to add castle if <---
    private void setAvailableMoves(int position, int vertical, int horizontal)
    {
        if (position % 8 == 0 && horizontal == W) {
            return;
        }
        else if ((position - 7) % 8 == 0 && horizontal == E) {
            return;
        }
        
        int destination = position + vertical + horizontal;
        if (destination >= 0 && destination <= 63 && !board.getPieces().getAllTargetSquares(this.getColour().getOppColour()).contains(destination))
        {
            if (vertical != horizontal && (board.getBoard()[destination] == null || board.getBoard()[destination].getColour() != this.getColour())) {
                this.availableMoves.add(destination);
            }
            else if (vertical == horizontal && board.isCastling(this, destination)) {
                this.availableMoves.add(destination);
            }
        }
    }
    
    @Override
    public HashSet<Integer> getTargetSquares()
    {
        this.targetingSquares.clear();
        int position = this.getPosition();
        
        setTargetSquares(position, N, 0);
        setTargetSquares(position, S, 0);
        setTargetSquares(position, 0, E);
        setTargetSquares(position, 0, W);
        setTargetSquares(position, N, E);
        setTargetSquares(position, N, W);
        setTargetSquares(position, S, E);
        setTargetSquares(position, S, W);
        
        return this.targetingSquares;
    }
    
    private void setTargetSquares(int position, int vertical, int horizontal)
    {
        if (position % 8 == 0 && horizontal == W) {
            return;
        }
        else if ((position - 7) % 8 == 0 && horizontal == E) {
            return;
        }
        
        int destination = position + vertical + horizontal;
        if (destination >= 0 && destination <= 63)
        {
            this.targetingSquares.add(destination);
        }
    }
}
