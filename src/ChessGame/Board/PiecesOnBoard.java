/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChessGame.Board;

import ChessGame.Piece.Piece;
import ChessGame.Piece.PieceColour;
import ChessGame.Piece.Bishop;
import ChessGame.Piece.Knight;
import ChessGame.Piece.Queen;
import ChessGame.Piece.Rook;

/**
 *
 * @author rh200
 */
public final class PiecesOnBoard {
   
    private final Piece[][] board = new Piece[8][8]; // [column][row]
    private final boolean[][] checkPath = new boolean[8][8]; // [column][row]
    private AllPieces allPieces;
    private boolean whiteIsInCheck = false;
    private boolean blackIsInCheck = false;
    private boolean isPromoting = false;;
    private Piece promotionPawn = null;
    private int moveNum = 0;
    
    //Contructs PiecesOnBoard class
    public PiecesOnBoard()
    {
        allPieces = new AllPieces(this);
        refreshBoard();
    }
    
    // Moves a piece from square to square on the board
    public boolean movePiece(int fromCol, int fromRow, int toCol, int toRow)
    {
        if (!(checkInput(fromCol) && checkInput(fromRow) && checkInput(toCol) && checkInput(toRow))) {
            return false;
        }
        Piece selectedPiece = board[fromCol][fromRow];
        if(selectedPiece != null)
        {
            refreshPiecesStatus();
            
            //castle move
            if(isCastling(selectedPiece, toCol, toRow))
            {
                castle(selectedPiece, toCol);
            }
            //en passant move
            else if(isEnPassant(selectedPiece, toCol, toRow))
            {
                enPassant(selectedPiece, toCol);
            }
            //king move
            else if(selectedPiece.getSymbol().contains("K") && selectedPiece.getAvailableMoves()[toCol][toRow])
            {
                move(fromCol, fromRow, toCol, toRow);
            }
            //other moves
            else if(checkPath[toCol][toRow] && selectedPiece.getAvailableMoves()[toCol][toRow])
            {
                move(fromCol, fromRow, toCol, toRow);
            }
            //unavailable move
            else
            {
                System.out.println("That was a Illegal move. Please enter a new one!");
                return false;
            }
            //check any available promotion
            checkPromotion(board[toCol][toRow]);
        }
        return true;
    }
    
    // Returns true or false if the col_or_row value is within the board boundary
    private boolean checkInput(int col_or_row)
    {
        return (col_or_row <= 7 && col_or_row >= 0);
    }
    
    // Returns current board
    public Piece[][] getBoard()
    {
        refreshBoard();
        return board;
    }
    
    // Returns current move number
    public int getMoveNum()
    {
        return moveNum;
    }
    
    // Sets current move number
    public void setMoveNum(int moveNum)
    {
        this.moveNum = moveNum;
    }
    
    // Returns a piece from a location on the board and returns null if not found
    public Piece getPiece(int col, int row)
    {
        return allPieces.getPiece(col, row);
    }
    
    // Returns all the existed pieces on the board
    public AllPieces getPieces()
    {
        return allPieces;
    }
    
    // Returns true of false if a pawn can be promoted
    public boolean canPromote()
    {
        return isPromoting;
    }
    
    // Adds a new piece into the board
    public void addPiece(Piece piece)
    {
        allPieces.addPiece(piece);
    }
    
    // Removes all the existing pieces
    public void clearAllPieces()
    {
        allPieces.clearPieces();
    }
    
    // Resets board and pieces back to default
    public void resetBoardAndPieces()
    {
        moveNum = 0;
        allPieces = new AllPieces(this);
        refreshBoard();
    }
    
    // Updates the board with correspnding existing pieces
    public void refreshBoard()
    {
        for(int col = 0; col < 8; col++) 
        {
            for(int row = 0; row < 8; row++) 
            {
                board[col][row] = null;
            }
        }
        for(Piece i : allPieces.getAllPieces())
        {
            board[i.getColumn()][i.getRow()] = i;
        }
    }
    
    // Returns a path between a checking piece and a king (check path)
    public boolean[][] getCheckPath()
    {
        refreshPiecesStatus();
        return this.checkPath;
    }
    
    // Sets true or false if the colour piece is in check
    public void setInCheck(PieceColour colour, boolean[][] checkPath)
    {
        for(int col = 0; col < 8; col++)
        {
            for(int row = 0; row < 8; row++)
            {
                this.checkPath[col][row] = this.checkPath[col][row] && checkPath[col][row];
            }
        }
        
        if(colour == PieceColour.WHITE)
        {
            this.whiteIsInCheck = true;
        }
        else if(colour == PieceColour.BLACK)
        {
            this.blackIsInCheck = true;
        }
    }
    
    // Returns true or false if the colour is in check, else false
    public boolean isInCheck(PieceColour colour)
    {
        if (colour == PieceColour.WHITE) {
            return whiteIsInCheck;
        } else {
            return blackIsInCheck;
        }
    }
    
    // Updates isInCheck, isUnderPin, and checkPath based on current board situation
    public void refreshPiecesStatus()
    {
        whiteIsInCheck = false;
        blackIsInCheck = false;
        allPieces.resetUnderPin();
        for(int col = 0; col < 8; col++)
        {
            for(int row = 0; row < 8; row++)
            {
                checkPath[col][row] = true;
            }
        }
        allPieces.getTargetAreas(PieceColour.WHITE);
        allPieces.getTargetAreas(PieceColour.BLACK);
    }
    
    // Returns true or false if the colour is checkmated
    public boolean isCheckmate(PieceColour colour)
    {
        refreshPiecesStatus();
        boolean isCheckmate = true;
        //no checkmate if any piece other than king can stop the check
        for(Piece i : allPieces.getAllPieces())
        {
            if (i.getColour() == colour)
            {
                for(int col = 0; col < 8; col++)
                {
                    for(int row = 0; row < 8; row++)
                    {
                        if(i.getAvailableMoves()[col][row] && checkPath[col][row] && !i.getSymbol().contains("K"))
                        {
                            isCheckmate = false;
                        }
                    }
                }
            }
        }
        //no checkmate if king still have available move
        for(int col = 0; col < 8; col++)
        {
            for(int row = 0; row < 8; row++)
            {
                if(!allPieces.getTargetAreas(colour.getOppColour())[col][row] 
                        && allPieces.getKing(colour).getAvailableMoves()[col][row])
                {
                    isCheckmate = false;
                }
            }
        }
        return isCheckmate;
    }
    
    // Returns true or false if the colour is stalemated
    public boolean isStalemate(PieceColour colour)
    {
        refreshPiecesStatus();
        boolean isStalemate = true;
        
        //no stalemate if any piece other than king can still move
        for(Piece i : allPieces.getAllPieces())
        {
            if (i.getColour() == colour)
            {
                for(int col = 0; col < 8; col++)
                {
                    for(int row = 0; row < 8; row++)
                    {
                        if(i.getAvailableMoves()[col][row] && !i.getSymbol().contains("K"))
                        {
                            isStalemate = false;
                        }
                    }
                }
            }
        }
        //no stalemate if king can still move
        for(int col = 0; col < 8; col++)
        {
            for(int row = 0; row < 8; row++)
            {
                if(!allPieces.getTargetAreas(colour.getOppColour())[col][row] 
                        && allPieces.getKing(colour).getAvailableMoves()[col][row])
                {
                    isStalemate = false;
                }
            }
        }
        return isStalemate;
    }
    
    // Returns true or false if castling is avaialable
    public boolean isCastling(Piece king, int toCol, int toRow)
    {
        boolean availability = false;
        //if king has not moved yet
        if(king.getSymbol().contains("K") && king.hasNotMoved() && (toRow == 0 || toRow == 7))
        {
            //long castle: if rook has not moved yet and castle path has no other pieces
            if(toCol == 2 && board[0][king.getRow()] != null && board[0][king.getRow()].hasNotMoved() 
                    && board[1][king.getRow()] == null && board[2][king.getRow()] == null && board[3][king.getRow()] == null)
            {
                availability = true;
                
                //check if king, rook, and their path are not being targeted
                for(int col = 0; col <= 4; col++)
                {
                    if(allPieces.getTargetAreas(king.getColour().getOppColour())[col][toRow])
                    {
                        availability = false;
                    }
                }
            }
            //short castle: if rook has not moved yet and castle path has no other pieces
            else if(toCol == 6 && board[7][king.getRow()] != null && board[7][king.getRow()].hasNotMoved() 
                    && board[5][king.getRow()] == null && board[6][king.getRow()] == null)
            {
                availability = true;
                for(int col = 7; col >= 4; col--)
                {
                    //check if king, rook, and their path are not being targeted
                    if(allPieces.getTargetAreas(king.getColour().getOppColour())[col][toRow])
                    {
                        availability = false;
                    }
                }
            }
        }
        return availability;
    }
    
    // Performs castling
    private void castle(Piece king, int toCol)
    {
        int col = king.getColumn();
        int row = king.getRow();
        int toRow;
        
        if (king.getColour() == PieceColour.WHITE) {
            toRow = 0;
        } else {
            toRow = 7;
        }
        
        moveNum++;
        allPieces.getPiece(col, row).setLastMoveNum(moveNum);
        allPieces.getPiece(col, row).setMove();
        allPieces.getPiece(col, row).setColAndRow(toCol, toRow);

        if(toCol == 2) //long castle
        {
            allPieces.getPiece(0, toRow).setMove();
            allPieces.getPiece(0, toRow).setColAndRow(3, toRow);
        }
        else if(toCol == 6) //short castle
        {
            allPieces.getPiece(7, toRow).setMove();
            allPieces.getPiece(7, toRow).setColAndRow(5, toRow);
        }
        refreshBoard();
    }
    
    // Returns true or false if en passant is available
    public boolean isEnPassant(Piece pawn, int toCol, int toRow)
    {
        boolean availability = false;
        //if the target square is not empty
        if(board[toCol][pawn.getRow()] != null && (toRow == 5 || toRow == 2))
        {
            /* if selected piece is white pawn at row 5
             * and target piece is black pawn
             * and target piece advanced two squares in previous move
             */
            if(pawn.getSymbol().equals("wP") && pawn.getRow() == 4 
                    && board[toCol][pawn.getRow()].getSymbol().equals("bP") 
                    && board[toCol][pawn.getRow()].getLastMoveNum() == this.moveNum 
                    && board[toCol][pawn.getRow()].hasMovedOnce())
            {
                availability = true;
            }
            /* if selected piece is black pawn at row 4
             * and target piece is white pawn
             * and target piece advanced two squares in previous move
             */
            else if(pawn.getSymbol().equals("bP") && pawn.getRow() == 3 
                    && board[toCol][pawn.getRow()].getSymbol().equals("wP") 
                    && board[toCol][pawn.getRow()].getLastMoveNum() == this.moveNum 
                    && board[toCol][pawn.getRow()].hasMovedOnce())
            {
                availability = true;
            }
        }
        
        return availability;
    }
    
    // Performs en passant
    private void enPassant(Piece pawn, int toCol)
    {
        int col = pawn.getColumn();
        int row = pawn.getRow();
        int toRow;
        
        if (pawn.getColour() == PieceColour.WHITE) {
            toRow = 5;
        } else {
            toRow = 2;
        }
        
        moveNum++;
        allPieces.getPiece(col, row).setLastMoveNum(moveNum);
        allPieces.getPiece(col, row).setMove();
        allPieces.removePiece(toCol, row);
        allPieces.getPiece(col, row).setColAndRow(toCol, toRow);
        refreshBoard();
    }
    
    // Promotes pawn to a chosen piece type such as queen, rook, bishop, and knight
    public void promote(String pieceType)
    {
        if (promotionPawn == null) {
            System.out.println("No available pawn promotion");
            return;
        }
        
        int col = promotionPawn.getColumn();
        int row = promotionPawn.getRow();
        Piece promotion;
        
        if(pieceType.contains("Q"))
        {
            promotion = new Queen(promotionPawn.getColour(), col, row, this);
            allPieces.replacePiece(promotion, col, row);
        }
        else if(pieceType.contains("B"))
        {
            promotion = new Bishop(promotionPawn.getColour(), col, row, this);
            allPieces.replacePiece(promotion, col, row);
        }
        else if(pieceType.contains("N"))
        {
            promotion = new Knight(promotionPawn.getColour(), col, row, this);
            allPieces.replacePiece(promotion, col, row);
        }
        else if(pieceType.contains("R"))
        {
            promotion = new Rook(promotionPawn.getColour(), col, row, this);
            allPieces.replacePiece(promotion, col, row);
        }   
        
        isPromoting = false;
        refreshBoard();
    }
    
    // Saves pawn if promotion is available and sets isPromoting to true
    private void checkPromotion(Piece pawn)
    {
        if(pawn.getSymbol().contains("P") && (pawn.getRow() == 7 || pawn.getRow() == 0))
        {
            promotionPawn = pawn;
            isPromoting = true;
        }
    }
    
    // Moves a piece to a location, and removes any piece that is in the location, then updates board
    private void move(int fromCol, int fromRow, int toCol, int toRow)
    {
        moveNum++;
        allPieces.getPiece(fromCol, fromRow).setLastMoveNum(moveNum);
        allPieces.getPiece(fromCol, fromRow).setMove();
        allPieces.removePiece(toCol, toRow);
        allPieces.getPiece(fromCol, fromRow).setColAndRow(toCol, toRow);
        refreshBoard();
    }
}
