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
public class Knight extends Piece {
    
    private Board board;
    private HashSet<Integer> availableMoves;
    private HashSet<Integer> targetingSquares;
    private final int N = 8;
    private final int S = -8;
    private final int E = 1;
    private final int W = -1;
    
    public Knight(PieceColour colour, int position, boolean isMoved,Board board)
    {
        super(colour, 300, position, isMoved);
        this.board = board;
        availableMoves = new HashSet<>();
        targetingSquares = new HashSet<>();
    }
    
    public Knight(Piece piece, Board board)
    {
        super(piece.getColour(), 300, piece.getPosition(), piece.isMoved());
        this.board = board;
        availableMoves = new HashSet<>();
        targetingSquares = new HashSet<>();
    }
    
    //return white knight or black knight symbol
    @Override
    public String getSymbol()
    {
        if (getColour() == PieceColour.WHITE) {
            return "wN";
        } else {
            return "bN";
        }
    }
    
    @Override
    public HashSet<Integer> getAvailableMoves()
    {
        this.availableMoves.clear();
        int position = this.getPosition();
        
        setAvailableMoves(position, N*2, E);
        setAvailableMoves(position, N, E*2);
        setAvailableMoves(position, S, E*2);
        setAvailableMoves(position, S*2, E);
        setAvailableMoves(position, N*2, W);
        setAvailableMoves(position, N, W*2);
        setAvailableMoves(position, S, W*2);
        setAvailableMoves(position, S*2, W);
        
        if (board.isChecked()) {
            this.availableMoves.retainAll(board.getCheckPath());
        }
        if (this.isPinned()) {
            this.availableMoves.retainAll(this.getPinPath());
        }
        
        return this.availableMoves;
    }
    
    private void setAvailableMoves(int position, int vertical, int horizontal)
    {
        if ((position % 8 == 0 && horizontal < 0) || ((position - 7) % 8 == 0 && horizontal > 0) 
                || ((position - 1) % 8 == 0 && horizontal == W*2) || ((position - 6) % 8 == 0 && horizontal == E*2)) {
            return;
        }
        
        int destination = position + vertical + horizontal;
        if (destination >= 0 && destination <= 63)
        {
            if (board.getBoard()[destination] == null || board.getBoard()[destination].getColour() != this.getColour()) {
                this.availableMoves.add(destination);
            }
        }
    }
    
    @Override
    public HashSet<Integer> getTargetSquares()
    {
        this.targetingSquares.clear();
        int position = this.getPosition();
        
        setTargetSquares(position, N*2, E);
        setTargetSquares(position, N, E*2);
        setTargetSquares(position, S, E*2);
        setTargetSquares(position, S*2, E);
        setTargetSquares(position, N*2, W);
        setTargetSquares(position, N, W*2);
        setTargetSquares(position, S, W*2);
        setTargetSquares(position, S*2, W);
        
        return this.targetingSquares;
    }
    
    private void setTargetSquares(int position, int vertical, int horizontal)
    {
        if ((position % 8 == 0 && horizontal < 0) || ((position - 7) % 8 == 0 && horizontal > 0) 
                || ((position - 1) % 8 == 0 && horizontal == W*2) || ((position - 6) % 8 == 0 && horizontal == E*2)) {
            return;
        }
        
        int destination = position + vertical + horizontal;
        if (destination >= 0 && destination <= 63)
        {
            this.targetingSquares.add(destination);
            //check opponent king
            if (board.getBoard()[destination] != null && board.getBoard()[destination].getSymbol().contains("K") 
                    && board.getBoard()[destination].getColour() != this.getColour()){
                board.setCheckPath(new HashSet<>(Collections.singleton(position)));
            }
        }
    }
}
