/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.Pieces;

import java.awt.Image;
import java.util.HashSet;
import javax.swing.ImageIcon;

/**
 *
 * @author rh200
 */
public abstract class Piece {
    
    private final PieceColour colour;
    private final int points;
    private int position;
    private boolean isMoved;
    private boolean isPinned;
    private HashSet<Integer> pinPath;
    
    public Piece(PieceColour colour, int points, int position, boolean isMoved)
    {
        this.colour = colour;
        this.points = points;
        this.position = position;
        this.isMoved = isMoved;
        this.isPinned = false;
        this.pinPath = new HashSet<>();
    }
    
    public Image getImage()
    {
        String path = "chessPiece/" + getSymbol() + ".png";
        ImageIcon icon = new ImageIcon(path);
        
        return icon.getImage();
    }

    public PieceColour getColour() {
        return colour;
    }

    public int getPoints() {
        return points;
    }

    public int getPosition() {
        return position;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isMoved() {
        return isMoved;
    }

    public void setIsMoved(boolean hasMoved) {
        this.isMoved = hasMoved;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }

    public HashSet<Integer> getPinPath() {
        return pinPath;
    }

    public void setPinPath(HashSet<Integer> pinPath) {
        this.pinPath = pinPath;
    }
    
    public abstract String getSymbol();
    
    public abstract HashSet<Integer> getAvailableMoves();
    
    public abstract HashSet<Integer> getTargetSquares();
}
