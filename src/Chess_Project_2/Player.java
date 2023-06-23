/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess_Project_2;

/**
 *
 * @author rh200
 */
public class Player {
    
    private PieceColour colourPiece;
    private String playerName;
    
    public Player(PieceColour colourPiece,String playerName)
    {
        this.playerName = playerName;
        this.colourPiece = colourPiece;
    }

    public PieceColour getColourPiece() 
    {
        return colourPiece;
    }

    public void setColourPiece(PieceColour colourPiece) 
    {
        this.colourPiece = colourPiece;
    }

    public String getName() 
    {
        return playerName;
    }
 
    public void setName(String playerName) 
    {
        this.playerName = playerName;
    }
}