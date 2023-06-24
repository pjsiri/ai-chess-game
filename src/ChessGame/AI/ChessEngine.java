/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChessGame.AI;

import ChessGame.Board.PiecesOnBoard;
import ChessGame.ChessController;
import ChessGame.Piece.Piece;
import ChessGame.Piece.PieceColour;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 *
 * @author rh200
 */
public class ChessEngine {
    
    private ChessController chessController = new ChessController();
    private int fromCol, fromRow, toCol, toRow;
    private int engineLayer;
    
    public int[] getBotMove(PieceColour botColour, int engineLayer)
    {
        this.engineLayer = engineLayer;
        PiecesOnBoard board = new PiecesOnBoard(chessController.getBoard());
        minimax(board, botColour, engineLayer, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return getBotMove();
    }
    
    //minimax function
    private int minimax(PiecesOnBoard board, PieceColour colour, int layer, int alpha, int beta)
    {
        if (layer <= 0 || gameEnded(board, colour))
        {
            return board.getPieces().getOverallEvaluation();
        }
        else if (colour == PieceColour.WHITE)
        {
            int maxPointValue = Integer.MIN_VALUE;
            
            outerloop: 
            for(Piece i : board.getPieces().getAllPieces(colour))
            {
                for(int col = 0; col < 8; col++)
                {
                    for(int row = 0; row < 8; row++)
                    {
                        if (i.getAvailableMoves()[col][row])
                        {
                            PiecesOnBoard newBoard = new PiecesOnBoard(board);
                            newBoard.movePiece(i.getColumn(), i.getRow(), col, row);
                            int pointValue = minimax(newBoard, colour.getOppColour(), layer-1, alpha, beta);
                            maxPointValue = max(maxPointValue, pointValue);
                            if (this.engineLayer == layer && maxPointValue == pointValue) {
                                setMovement(i.getColumn(), i.getRow(), col, row);
                            }
                            alpha = max(alpha, pointValue);
                            if (beta <= alpha) {
                                break outerloop;
                            }
                        }
                    }
                }
            }
            return maxPointValue;
        }
        else
        {
            int minPointValue = Integer.MAX_VALUE;
            
            outerloop: 
            for(Piece i : board.getPieces().getAllPieces(colour))
            {
                for(int col = 0; col < 8; col++)
                {
                    for(int row = 0; row < 8; row++)
                    {
                        if (i.getAvailableMoves()[col][row])
                        {
                            PiecesOnBoard newBoard = new PiecesOnBoard(board);
                            newBoard.movePiece(i.getColumn(), i.getRow(), col, row);
                            int pointValue = minimax(newBoard, colour.getOppColour(), layer-1, alpha, beta);
                            minPointValue = min(minPointValue, pointValue);
                            if (this.engineLayer == layer && minPointValue == pointValue) {
                                setMovement(i.getColumn(), i.getRow(), col, row);
                            }
                            beta = min(beta, pointValue);
                            if (beta <= alpha) {
                                break outerloop;
                            }
                        }
                    }
                }
            }
            return minPointValue;
        }
    }
    
    private void setMovement(int fromCol, int fromRow, int toCol, int toRow)
    {
        this.fromCol = fromCol;
        this.fromRow = fromRow;
        this.toCol = toCol;
        this.toRow = toRow;
    }
    
    private int[] getBotMove()
    {
        int[] movement = new int[4];
        movement[0] = fromCol;
        movement[1] = fromRow;
        movement[2] = toCol;
        movement[3] = toRow;
        return movement;
    }
    
    //check contidion
    private boolean gameEnded(PiecesOnBoard board, PieceColour colour)
    {
        return (board.isCheckmate(colour) || board.isStalemate(colour));
    }
}
