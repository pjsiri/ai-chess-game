/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.AI;

import Chess.Board.Board;
import Chess.Pieces.Piece;
import Chess.Pieces.PieceColour;
import java.util.Random;

/**
 *
 * @author rh200
 */
public class ZobristHashing {
    
    private static final int BOARD_SIZE = 64; // Index: position number
    private static final int PIECE_TYPES = 12; // Index: 0 = empty, 0 ~ 5 = wP,N,R,B,K,Q, 6 ~ 11 = bP,N,R,B,K,Q
    private static final int EN_PASSANT_TARGET = 17; // Index: 0 = no target, target position - 23;
    private static final int CASTLE_RIGHTS = 4; // Index: 0 = no castle, 1 = short, 2 = long, 3 = short & long

    private static long[][][][][] zobristKeys;

    public ZobristHashing()
    {
        initializeKeys();
    }
    
    private static void initializeKeys() {
        Random random = new Random();
        zobristKeys = new long[BOARD_SIZE][PIECE_TYPES][EN_PASSANT_TARGET][CASTLE_RIGHTS][CASTLE_RIGHTS];
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < PIECE_TYPES; j++) {
                for (int k = 0; k < EN_PASSANT_TARGET; k++) {
                    for (int l = 0; l < CASTLE_RIGHTS; l++) {
                        for (int m = 0; m < CASTLE_RIGHTS; m++) {
                            zobristKeys[i][j][k][l][m] = random.nextLong();
                        }
                    }
                }
            }
        }
    }
    
    public long generateKey(Board board) {
        long key = 0;
        int enPassantTarget = 0;
        int whiteCastleRights = getCastleRightsIndex(board, PieceColour.WHITE);
        int blackCastleRights = getCastleRightsIndex(board, PieceColour.BLACK);;
        
        if (board.getEnPassantTarget() != -1) {
            enPassantTarget = board.getEnPassantTarget() - 23;
        }
        
        for (Piece piece : board.getPieces().getAllPieces(PieceColour.WHITE)) {
            key ^= zobristKeys[piece.getPosition()][getPieceTypesIndex(piece)][enPassantTarget][whiteCastleRights][blackCastleRights];
        }

        return key;
    }
    
    private int getPieceTypesIndex(Piece piece)
    {
        int index;
        String type = piece.getSymbol();
        if (type.contains("P")) {
            index = 0;
        }
        else if (type.contains("N")) {
            index = 1;
        }
        else if (type.contains("R")) {
            index = 2;
        }
        else if (type.contains("B")) {
            index = 3;
        }
        else if (type.contains("K")) {
            index = 4;
        }
        else {
            index = 5;
        }
        
        if (piece.getColour() == PieceColour.BLACK) {
            index += 6;
        }
        
        return index;
    }
    
    private int getCastleRightsIndex(Board board, PieceColour colour)
    {
        if (board.getPieces().getKing(colour).isMoved()) {
            return 0;
        }
        
        int index = 0;
        int kingPosition = board.getPieces().getKing(colour).getPosition();
        boolean longCastle;
        boolean shortCastle;
        
        if (board.getBoard()[kingPosition + 3] == null) {
            longCastle = false;
        } else {
            longCastle = !board.getBoard()[kingPosition + 3].isMoved();
        }
        if (board.getBoard()[kingPosition - 4] == null) {
            shortCastle = false;
        } else {
            shortCastle = !board.getBoard()[kingPosition - 4].isMoved();
        }
        
        if (longCastle && shortCastle) {
            index = 3;
        }
        else if (longCastle) {
            index = 2;
        }
        else if (shortCastle) {
            index = 1;
        }
        
        return index;
    }
}
