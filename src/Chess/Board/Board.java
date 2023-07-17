/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.Board;

import Chess.Pieces.Bishop;
import Chess.Pieces.Knight;
import Chess.Pieces.Piece;
import Chess.Pieces.PieceColour;
import Chess.Pieces.Queen;
import Chess.Pieces.Rook;
import java.util.HashSet;

/**
 *
 * @author rh200
 */
public final class Board {
    
    private Piece[] board;
    private BoardPieces pieces;
    private HashSet<Integer> checkPath;
    private PieceColour colourTurn;
    private boolean isChecked;
    private int moveNum;
    private int enPassantTarget;
    private int promotionPawn;
    
    public Board()
    {
        board = new Piece[64];
        pieces = new BoardPieces(this);
        checkPath = new HashSet<>();
        colourTurn = PieceColour.WHITE;
        isChecked = false;
        moveNum = 0;
        enPassantTarget = -1;
        promotionPawn = -1;
        pieces.setDefault();
        updateBoard();
    }
    
    public Board(Board boardCopy)
    {
        pieces = new BoardPieces(this);
        checkPath = new HashSet<>();
        colourTurn = boardCopy.getColourTurn();
        isChecked = boardCopy.isChecked();
        moveNum = boardCopy.getMoveNum();
        enPassantTarget = boardCopy.getEnPassantTarget();
        promotionPawn = -1;
        pieces.setCopy(boardCopy.getPieces());
        checkPath.addAll(boardCopy.getCheckPath());
        updateBoard();
        updateStatus();
    }
    
    public int movePiece(int fromSquare, int toSquare)
    {
        Piece selectedPiece = board[fromSquare];
        if (selectedPiece != null && selectedPiece.getColour() == colourTurn)
        {
            //castle move
            if (isCastling(selectedPiece, toSquare)) {
                castle(selectedPiece, toSquare);
                return 1;
            }
            //en passant move
            else if (isEnPassant(selectedPiece, toSquare)) {
                enPassant(selectedPiece, toSquare);
                return 2;
            }
            //other moves
            else if (selectedPiece.getAvailableMoves().contains(toSquare)) {
                move(fromSquare, toSquare);
                //check en passont target pawn
                if (selectedPiece.getSymbol().contains("P") && Math.abs(fromSquare - toSquare) == 16) {
                    setEnPassantTarget(toSquare);
                }
                //check promotion
                else if (isPromoting(board[toSquare])) {
                    promotionPawn = toSquare;
                    colourTurn = colourTurn.getOppColour();
                }
                return 3;
            }
        }
        return 0;
    }
    
    private void move(int fromSquare, int toSquare)
    {
        moveNum++;
        pieces.removePiece(toSquare);
        board[fromSquare].setIsMoved(true);
        board[fromSquare].setPosition(toSquare);
        colourTurn = colourTurn.getOppColour();
        updateBoard();
        updateStatus();
    }
    
    public boolean isPromoting(Piece pawn)
    {
        if (!pawn.getSymbol().contains("P")) {
            return false;
        }
        
        int position = pawn.getPosition();
        return (position >= 0 && position <= 7) || (position >= 56 && position <= 63);
    }
    
    public boolean canPromote()
    {
        return promotionPawn != -1;
    }
    
    public void promote(String promoteType) {
        if (promotionPawn != -1) 
        {
            Piece promotion;
            switch (promoteType)
            {
                case "Q":
                    promotion = new Queen(colourTurn, promotionPawn, true,this);
                    break;
                case "N":
                    promotion = new Knight(colourTurn, promotionPawn, true,this);
                    break;
                case "B":
                    promotion = new Bishop(colourTurn,promotionPawn,true,this);
                    break;
                default:
                    promotion = new Rook(colourTurn, promotionPawn,true, this);
                    break;
            }
            colourTurn = colourTurn.getOppColour();
            pieces.replacePiece(promotionPawn, promotion);
            updateBoard();
            updateStatus();
        }
    }
    
    public boolean isCastling(Piece king, int destination)
    {
        if (!(king.getSymbol().contains("K") && !king.isMoved())) {
            return false;
        }
        
        //long castle
        if ((destination == 2 || destination == 58) && board[destination - 2] != null && !board[destination - 2].isMoved()
                && board[destination - 1] == null && board[destination] == null && board[destination + 1] == null)
        {
            for (int position = king.getPosition(); position >= destination - 2; position--) {
                if (pieces.getAllTargetSquares(king.getColour().getOppColour()).contains(position)) {
                    return false;
                }
            }
            return true;
        }
        //short castle
        else if ((destination == 6 || destination == 62) && board[destination + 1] != null && !board[destination + 1].isMoved()
                && board[destination - 1] == null && board[destination] == null)
        {
            for (int position = king.getPosition(); position <= destination + 1; position++) {
                if (pieces.getAllTargetSquares(king.getColour().getOppColour()).contains(position)) {
                    return false;
                }
            }
            return true;
        }
        
        return false;
    }
    
    private void castle(Piece king, int destination)
    {
        move(king.getPosition(), destination);
        
        if (destination == 2 || destination == 58) {
            move(destination - 2, destination + 1);
            colourTurn = colourTurn.getOppColour();
            moveNum--;
        }
        else if (destination == 6 || destination == 62) {
            move(destination + 1, destination - 1);
            colourTurn = colourTurn.getOppColour();
            moveNum--;
        }
    }
    
    public boolean isEnPassant(Piece pawn, int destination)
    {
        if (!pawn.getSymbol().contains("P")) {
            return false;
        }

        int position = pawn.getPosition();
        if (pawn.getColour() == PieceColour.WHITE && position >= 32 && position <= 39 
                && destination >= 40 && destination <= 47 && (destination == position + 7 || destination == position + 9) 
                && enPassantTarget == destination - 8)
        {
            return true;
        }
        else if (pawn.getColour() == PieceColour.BLACK && position >= 24 && position <= 31 
                && destination >= 16 && destination <= 23 && (destination == position - 7 || destination == position - 9) 
                && enPassantTarget == destination + 8)
        {
            return true;
        }
        
        return false;
    }
    
    private void enPassant(Piece pawn, int destination)
    {
        if (pawn.getColour() == PieceColour.WHITE) {
            pieces.removePiece(destination - 8);
            move(pawn.getPosition(), destination);
        } else {
            pieces.removePiece(destination + 8);
            move(pawn.getPosition(), destination);
        }
    }
    
    public boolean isCheckmated(PieceColour colour)
    {
        if (!this.isChecked) {
            return false;
        }
        
        for (Piece piece : pieces.getAllPieces(colour)) {
            if (!piece.getAvailableMoves().isEmpty()) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean isStalemated(PieceColour colour)
    {
        if (this.isChecked) {
            return false;
        }
        
        for (Piece piece : pieces.getAllPieces(colour)) {
            if (!piece.getAvailableMoves().isEmpty()) {
                return false;
            }
        }
        
        return true;
    }
    
    public void updateBoard()
    {
        board = new Piece[64];
        for (Piece piece : pieces.getAllPieces(PieceColour.WHITE)) {
            board[piece.getPosition()] = piece;
        }
        for (Piece piece : pieces.getAllPieces(PieceColour.BLACK)) {
            board[piece.getPosition()] = piece;
        }
    }
    
    private void updateStatus()
    {
        isChecked = false;
        setEnPassantTarget(-1);
        promotionPawn = -1;
        checkPath = new HashSet<>();
        for (Piece piece : pieces.getAllPieces(PieceColour.WHITE)) {
            piece.setIsPinned(false);
        }
        for (Piece piece : pieces.getAllPieces(PieceColour.BLACK)) {
            piece.setIsPinned(false);
        }
        pieces.getAllTargetSquares(PieceColour.WHITE);
        pieces.getAllTargetSquares(PieceColour.BLACK);
    }
    
    public void setDefault()
    {
        colourTurn = PieceColour.WHITE;
        isChecked = false;
        moveNum = 0;
        setEnPassantTarget(-1);
        promotionPawn = -1;
        pieces.setDefault();
        updateBoard();
    }
    
    public void clearPieces()
    {
        pieces.clear();
        updateBoard();
    }

    public Piece[] getBoard() {
        return board;
    }

    public BoardPieces getPieces() {
        return pieces;
    }

    public HashSet<Integer> getCheckPath() {
        return checkPath;
    }

    public void setCheckPath(HashSet<Integer> checkPath) {
        this.checkPath = checkPath;
        this.isChecked = true;
    }

    public PieceColour getColourTurn() {
        return colourTurn;
    }
    
    public boolean isChecked() {
        return isChecked;
    }

    public int getMoveNum() {
        return moveNum;
    }

    public void setMoveNum(int moveNum) {
        this.moveNum = moveNum;
    }

    public int getEnPassantTarget() {
        return enPassantTarget;
    }
    
    // Adds a new piece into the board
    public void addPiece(Piece piece)
    {
        pieces.addPiece(piece);
    }

    /**
     * @param enPassantTarget the enPassantTarget to set
     */
    public void setEnPassantTarget(int enPassantTarget) {
        this.enPassantTarget = enPassantTarget;
    }
    
}
