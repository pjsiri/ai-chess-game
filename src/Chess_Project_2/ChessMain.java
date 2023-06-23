/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess_Project_2;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author rh200
 */
public class ChessMain {
    
    public static void main(String[] args) 
    {
        ChessFrame frame = new ChessFrame();
        ChessController chessController = new ChessController();
        frame.setChessController(chessController);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.getChessController().quit();
                System.exit(0); 
            }
        });
    }
    
}
