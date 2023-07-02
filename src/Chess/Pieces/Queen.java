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
public class Queen extends Piece {
    
    private Board board;
    private HashSet<Integer> availableMoves;
    private HashSet<Integer> targetingSquares;
    private final int N = 8;
    private final int S = -8;
    private final int E = 1;
    private final int W = -1;
    
    public Queen(PieceColour colour, int position, boolean isMoved,Board board)
    {
        super(colour, 900, position, isMoved);
        this.board = board;
        availableMoves = new HashSet<>();
        targetingSquares = new HashSet<>();
    }
    
    public Queen(Piece piece, Board board)
    {
        super(piece.getColour(), 900, piece.getPosition(), piece.isMoved());
        this.board = board;
        availableMoves = new HashSet<>();
        targetingSquares = new HashSet<>();
    }
    
    //return white queen or black queen symbol
    @Override
    public String getSymbol()
    {
        if (getColour() == PieceColour.WHITE) {
            return "wQ";
        } else {
            return "bQ";
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
        if ((position % 8 == 0 && horizontal == W) || ((position - 7) % 8 == 0 && horizontal == E)) {
            return;
        }
        
        int destination = position + vertical + horizontal;
        if (destination >= 0 && destination <= 63)
        {
            if (board.getBoard()[destination] == null) {
                this.availableMoves.add(destination);
                setAvailableMoves(destination, vertical, horizontal);
            }
            else if (board.getBoard()[destination].getColour() != this.getColour()) {
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
        if ((position % 8 == 0 && horizontal == W) || ((position - 7) % 8 == 0 && horizontal == E)) {
            return;
        }
        
        int destination = position + vertical + horizontal;
        if (destination >= 0 && destination <= 63)
        {
            this.targetingSquares.add(destination);
            //empty square
            if (board.getBoard()[destination] == null) {
                setTargetSquares(destination, vertical, horizontal);
            }
            //check opponent king
            else if (board.getBoard()[destination].getSymbol().contains("K") && board.getBoard()[destination].getColour() != this.getColour()) {
                board.setCheckPath(getPath(destination, vertical, horizontal));
                setTargetSquares(destination, vertical, horizontal);
            }
            //pin opponent piece
            else if (board.getBoard()[destination].getColour() != this.getColour() && isPinning(destination, destination, vertical, horizontal)) {
                board.getBoard()[destination].setIsPinned(true);
            }
        }
    }
    
    private boolean isPinning(int pinnedPiece, int destination, int vertical, int horizontal)
    {
        if ((destination % 8 == 0 && horizontal == W) || ((destination - 7) % 8 == 0 && horizontal == E)) {
            return false;
        }
        
        destination += (vertical + horizontal);
        if (destination >= 0 && destination <= 63) {
            if (board.getBoard()[destination] == null) {
                return isPinning(pinnedPiece, destination, vertical, horizontal);
            }
            else if (board.getBoard()[destination].getSymbol().contains("K") && board.getBoard()[destination].getColour() != this.getColour()) {
                board.getBoard()[pinnedPiece].setPinPath(getPath(destination, vertical, horizontal));
                return true;
            }
            else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    private HashSet<Integer> getPath(int destination, int vertical, int horizontal)
    {
        HashSet<Integer> path = new HashSet<>();
        int position = this.getPosition();
        
        while (position != destination) {
            path.add(position);
            position += (vertical + horizontal);
        }
        
        return path;
    }
}
