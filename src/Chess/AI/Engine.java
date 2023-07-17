/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.AI;

import Chess.Board.Board;
import Chess.GUI.ChessSoundManager;
import Chess.Main.ChessController;
import Chess.Pieces.Piece;
import Chess.Pieces.PieceColour;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author rh200
 */
public class Engine {
    
    private final ChessController controller;
    private final ZobristHashing zobristhashing;
    private final HashMap<Long, Integer> transpositionTable;
    private int engineDepth;
    private int fromSquare;
    private int toSquare;
    
    public Engine(ChessController controller)
    {
        this.controller = controller;
        zobristhashing = new ZobristHashing();
        transpositionTable = new HashMap<>();
        this.engineDepth = engineDepth;
        fromSquare = 0;
        toSquare = 0;
    }
    
    public void move(int engineDepth)
    {
        this.engineDepth = engineDepth;
        boolean isMaximizing;
        if (controller.getColourTurn() == PieceColour.WHITE) {
            isMaximizing = true;
        } else {
            isMaximizing = false;
        }
        
        Board board = new Board(controller.getBoard());
        evaluateMove(board, engineDepth, isMaximizing, Integer.MIN_VALUE, Integer.MAX_VALUE);
        Piece selectedPiece = controller.getSquare(toSquare);
        int moveType = controller.movePiece(fromSquare, toSquare);
        if (controller.getBoard().canPromote()) {
            controller.getBoard().promote("Q");
            ChessSoundManager.playPromotionSound();
        }
        
        if(selectedPiece != null || moveType == 2)
        {
            ChessSoundManager.playCaptureSound();
        }
        else if(controller.isInChecked())
        {
            ChessSoundManager.playCheckSound();
        }
        else if(moveType == 1)
        {
            ChessSoundManager.playCastleSound();
        }
        else
        {
            ChessSoundManager.playMoveSound();
        }

        transpositionTable.clear();
    }
    
    private int evaluateMove(Board board, int depth, boolean isMaximizing, int alpha, int beta)
    {
        if (board.isCheckmated(board.getColourTurn())) {
            if (isMaximizing) {
                return Integer.MIN_VALUE;
            } else {
                return Integer.MAX_VALUE;
            }
        }
        if (board.isStalemated(board.getColourTurn())) {
            return 0;
        }
        if (depth == 0) {
            return evaluateCaptureMove(board, isMaximizing, alpha, beta);
            //return board.getPieces().getOverallEvaluation();
        }
        
        HashSet<Integer[]> moves = getSortedAvailableMoves(board, false);
        
        if (isMaximizing)
        {
            int maxEval = Integer.MIN_VALUE;
            for (Integer[] move : moves)
            {
                Board newBoard = new Board(board);
                newBoard.movePiece(move[0], move[1]);

                if (newBoard.canPromote()) {
                    newBoard.promote("Q");
                }
                
                int eval;
                long key = zobristhashing.generateKey(newBoard);
                if (transpositionTable.containsKey(key)) {
                    eval = transpositionTable.get(key);
                } else {
                    eval = evaluateMove(newBoard, depth-1, false, alpha, beta);
                    transpositionTable.put(key, eval);
                }

                if (eval >= maxEval) {
                    maxEval = eval;
                    setMove(depth, move[0], move[1]);
                }

                alpha = max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        }
        else
        {
            int minEval = Integer.MAX_VALUE;
            for (Integer[] move : moves)
            {
                Board newBoard = new Board(board);
                newBoard.movePiece(move[0], move[1]);
                
                if (newBoard.canPromote()) {
                    newBoard.promote("Q");
                }
                
                int eval;
                long key = zobristhashing.generateKey(newBoard);
                if (transpositionTable.containsKey(key)) {
                    eval = transpositionTable.get(key);
                } else {
                    eval = evaluateMove(newBoard, depth-1, true, alpha, beta);
                    transpositionTable.put(key, eval);
                }

                if (eval <= minEval) {
                    minEval = eval;
                    setMove(depth, move[0], move[1]);
                }

                beta = min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }
    
    private int evaluateCaptureMove(Board board, boolean isMaximizing, int alpha, int beta)
    {
        if (board.isCheckmated(board.getColourTurn())) {
            if (isMaximizing) {
                return Integer.MIN_VALUE;
            } else {
                return Integer.MAX_VALUE;
            }
        }
        if (board.isStalemated(board.getColourTurn())) {
            return 0;
        }
        
        HashSet<Integer[]> moves = getSortedAvailableMoves(board, true);
        if (moves.isEmpty()) {
            return board.getPieces().getOverallEvaluation();
        }
        
        if (isMaximizing)
        {
            int eval = board.getPieces().getOverallEvaluation();
            if (beta <= alpha) {
                return eval;
            }
            alpha = max(alpha, eval);
            int maxEval = eval;
            
            for (Integer[] move : moves)
            {
                Board newBoard = new Board(board);
                newBoard.movePiece(move[0], move[1]);

                if (newBoard.canPromote()) {
                    newBoard.promote("Q");
                }
                
                long key = zobristhashing.generateKey(newBoard);
                if (transpositionTable.containsKey(key)) {
                    eval = transpositionTable.get(key);
                } else {
                    eval = evaluateCaptureMove(newBoard, false, alpha, beta);
                    transpositionTable.put(key, eval);
                }

                maxEval = max(maxEval, eval);
                alpha = max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        }
        else
        {
            int eval = board.getPieces().getOverallEvaluation();
            if (beta <= alpha) {
                return eval;
            }
            beta = min(beta, eval);
            int minEval = eval;
                
            for (Integer[] move : moves)
            {
                Board newBoard = new Board(board);
                newBoard.movePiece(move[0], move[1]);
                
                if (newBoard.canPromote()) {
                    newBoard.promote("Q");
                }
                
                long key = zobristhashing.generateKey(newBoard);
                if (transpositionTable.containsKey(key)) {
                    eval = transpositionTable.get(key);
                } else {
                    eval = evaluateCaptureMove(newBoard, true, alpha, beta);
                    transpositionTable.put(key, eval);
                }

                minEval = min(minEval, eval);
                beta = min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }
    
    private void setMove(int depth, int fromSquare, int toSquare) {
        if (depth == engineDepth) {
            this.fromSquare = fromSquare;
            this.toSquare = toSquare;
        }
    }
    
    private HashSet<Integer[]> getSortedAvailableMoves(Board board, boolean captureOnly)
    {
        HashSet<Integer[]> moves;
        if (captureOnly) {
            moves = board.getPieces().getAllCaptureMoves(board.getColourTurn());
        } else {
            moves = board.getPieces().getAllMoves(board.getColourTurn());
        }
        
        HashSet<Integer[]> sortedMoves = new HashSet<>();
        HashMap<Integer, Integer[]> moveScores = new HashMap<>();
        ArrayList<Integer> scores = new ArrayList<>();
        
        for (Integer[] move : moves) {
            int priorityScore = 0;
            Piece piece = board.getBoard()[move[0]];
            
            if (board.getBoard()[move[1]] != null) {
                priorityScore += (10 * board.getBoard()[move[1]].getPoints()) - piece.getPoints() + 1;
            }
            
            if (board.isPromoting(piece)) {
                priorityScore += 900;
            }
            
            if (board.isCastling(piece, move[1])) {
                priorityScore += 50;
            }
            
            if (board.getPieces().getAllTargetSquares(piece.getColour().getOppColour()).contains(move[1])) {
                priorityScore -= piece.getPoints();
            }
            
            if (board.getPieces().getAllTargetSquares(piece.getColour().getOppColour()).contains(move[0])) {
                priorityScore += piece.getPoints();
            }
            
            Board newBoard = new Board(board);
            newBoard.movePiece(move[0], move[1]);
            
            if (newBoard.isChecked()) {
                priorityScore++;
            }
            
//            while (scores.contains(priorityScore)) {
//                priorityScore--;
//            }
            
            scores.add(priorityScore);
            moveScores.put(priorityScore, move);
        }
        
        Collections.sort(scores, Collections.reverseOrder());
        for (int score : scores) {
            sortedMoves.add(moveScores.get(score));
        }
        return sortedMoves;
    }

    /**
     * @return the fromSquare
     */
    public int getFromSquare() {
        return fromSquare;
    }

    /**
     * @return the toSquare
     */
    public int getToSquare() {
        return toSquare;
    }
}
