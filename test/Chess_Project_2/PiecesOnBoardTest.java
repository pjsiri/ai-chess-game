/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Chess_Project_2;

import ChessGame.Piece.Piece;
import ChessGame.Board.PiecesOnBoard;
import ChessGame.Piece.PieceColour;
import ChessGame.Piece.Pawn;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rh200
 */
public class PiecesOnBoardTest {
    
    public PiecesOnBoardTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of movePiece method, of class PiecesOnBoard. For unavailable move
     */
    @Test
    public void testMovePiece() {
        System.out.println("movePiece");
        int fromCol = 9;
        int fromRow = 34;
        int toCol = -2;
        int toRow = 4;
        PiecesOnBoard instance = new PiecesOnBoard();
        boolean expResult = false;
        boolean result = instance.movePiece(fromCol, fromRow, toCol, toRow);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of movePiece method, of class PiecesOnBoard. For available move
     */
    @Test
    public void testMovePiece2() {
        System.out.println("movePiece");
        int fromCol = 1;
        int fromRow = 1;
        int toCol = 1;
        int toRow = 3;
        PiecesOnBoard instance = new PiecesOnBoard();
        boolean expResult = true;
        boolean result = instance.movePiece(fromCol, fromRow, toCol, toRow);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getMoveNum method, of class PiecesOnBoard. No move, move number = 0
     */
    @Test
    public void testGetMoveNum() {
        System.out.println("getMoveNum");
        PiecesOnBoard instance = new PiecesOnBoard();
        instance.resetBoardAndPieces();
        int expResult = 0;
        int result = instance.getMoveNum();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of getMoveNum method, of class PiecesOnBoard. Move once, move number = 1
     */
    @Test
    public void testGetMoveNum2() {
        System.out.println("getMoveNum");
        PiecesOnBoard instance = new PiecesOnBoard();
        instance.resetBoardAndPieces();
        instance.movePiece(1, 1, 1, 3);
        int expResult = 1;
        int result = instance.getMoveNum();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getPiece method, of class PiecesOnBoard. Non-existing piece
     */
    @Test
    public void testGetPiece() {
        System.out.println("getPiece");
        int col = 38;
        int row = -4;
        PiecesOnBoard instance = new PiecesOnBoard();
        Piece expResult = null;
        Piece result = instance.getPiece(col, row);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of getPiece method, of class PiecesOnBoard. Existing piece
     */
    @Test
    public void testGetPiece2() {
        System.out.println("getPiece");
        int col = 0;
        int row = 0;
        PiecesOnBoard instance = new PiecesOnBoard();
        Piece expResult = instance.getBoard()[0][0];
        Piece result = instance.getPiece(col, row);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of canPromote method, of class PiecesOnBoard. No pawn reach the end
     */
    @Test
    public void testCanPromote() {
        System.out.println("canPromote");
        PiecesOnBoard instance = new PiecesOnBoard();
        boolean expResult = false;
        boolean result = instance.canPromote();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of addPiece method, of class PiecesOnBoard.
     */
    @Test
    public void testAddPiece() {
        System.out.println("addPiece");
        Piece piece = new Pawn(PieceColour.BLACK, 0, 0);
        PiecesOnBoard instance = new PiecesOnBoard();
        instance.addPiece(piece);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of clearAllPieces method, of class PiecesOnBoard.
     */
    @Test
    public void testClearAllPieces() {
        System.out.println("clearAllPieces");
        PiecesOnBoard instance = new PiecesOnBoard();
        instance.clearAllPieces();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of resetBoardAndPieces method, of class PiecesOnBoard.
     */
    @Test
    public void testResetBoardAndPieces() {
        System.out.println("resetBoardAndPieces");
        PiecesOnBoard instance = new PiecesOnBoard();
        instance.resetBoardAndPieces();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of refreshBoard method, of class PiecesOnBoard.
     */
    @Test
    public void testRefreshBoard() {
        System.out.println("refreshBoard");
        PiecesOnBoard instance = new PiecesOnBoard();
        instance.refreshBoard();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getCheckPath method, of class PiecesOnBoard.
     */
    @Test
    public void testGetCheckPath() {
        System.out.println("getCheckPath");
        PiecesOnBoard instance = new PiecesOnBoard();
        boolean[][] path = new boolean[8][8];
        for(int col = 0; col < 8; col++)
        {
            for(int row = 0; row < 8; row++)
            {
                path[col][row] = true;
            }
        }
        boolean[][] expResult = path;
        boolean[][] result = instance.getCheckPath();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setInCheck method, of class PiecesOnBoard.
     */
    @Test
    public void testSetInCheck() {
        System.out.println("setInCheck");
        PieceColour colour = PieceColour.WHITE;
        boolean[][] path = new boolean[8][8];
        for(int col = 0; col < 8; col++)
        {
            for(int row = 0; row < 8; row++)
            {
                path[col][row] = true;
            }
        }
        boolean[][] checkPath = path;
        PiecesOnBoard instance = new PiecesOnBoard();
        instance.setInCheck(colour, checkPath);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isInCheck method, of class PiecesOnBoard.
     */
    @Test
    public void testIsInCheck() {
        System.out.println("isInCheck");
        PieceColour colour = PieceColour.BLACK;
        PiecesOnBoard instance = new PiecesOnBoard();
        boolean expResult = false;
        boolean result = instance.isInCheck(colour);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of refreshPiecesStatus method, of class PiecesOnBoard.
     */
    @Test
    public void testRefreshPiecesStatus() {
        System.out.println("refreshPiecesStatus");
        PiecesOnBoard instance = new PiecesOnBoard();
        instance.refreshPiecesStatus();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isCheckmate method, of class PiecesOnBoard.
     */
    @Test
    public void testIsCheckmate() {
        System.out.println("isCheckmate");
        PieceColour colour = PieceColour.WHITE;
        PiecesOnBoard instance = new PiecesOnBoard();
        boolean expResult = false;
        boolean result = instance.isCheckmate(colour);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isStalemate method, of class PiecesOnBoard.
     */
    @Test
    public void testIsStalemate() {
        System.out.println("isStalemate");
        PieceColour colour = PieceColour.BLACK;
        PiecesOnBoard instance = new PiecesOnBoard();
        boolean expResult = false;
        boolean result = instance.isStalemate(colour);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isCastling method, of class PiecesOnBoard.
     */
    @Test
    public void testIsCastling() {
        System.out.println("isCastling");
        PiecesOnBoard instance = new PiecesOnBoard();
        Piece king = instance.getPiece(4, 0);
        int toCol = 6;
        int toRow = 0;
        boolean expResult = false;
        boolean result = instance.isCastling(king, toCol, toRow);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isEnPassant method, of class PiecesOnBoard.
     */
    @Test
    public void testIsEnPassant() {
        System.out.println("isEnPassant");
        PiecesOnBoard instance = new PiecesOnBoard();
        Piece pawn = instance.getPiece(0, 1);
        int toCol = 2;
        int toRow = 1;
        boolean expResult = false;
        boolean result = instance.isEnPassant(pawn, toCol, toRow);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of promote method, of class PiecesOnBoard.
     */
    @Test
    public void testPromote() {
        System.out.println("promote");
        String pieceType = "Q";
        PiecesOnBoard instance = new PiecesOnBoard();
        instance.getPieces().replacePiece(new Pawn(PieceColour.WHITE, 0, 6), 0, 6);
        instance.movePiece(0, 6, 1, 7);
        instance.promote(pieceType);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
