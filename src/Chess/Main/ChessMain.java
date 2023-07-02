/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.Main;

import Chess.GUI.ChessFrame;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author rh200
 */
public class ChessMain {
    
    public static void main(String[] args) 
    {
        try {
              for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                  if ("Nimbus".equals(info.getName())) {
                      javax.swing.UIManager.setLookAndFeel(info.getClassName());
                      break;
                  }
              }
          } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
              java.util.logging.Logger.getLogger(ChessFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
          }

          // Create and display the form on the Event Dispatch Thread (EDT)
          javax.swing.SwingUtilities.invokeLater(() -> {
              ChessFrame chessFrame = new ChessFrame();
              ChessController controller = new ChessController();
              chessFrame.setChessController(controller);
              chessFrame.setVisible(true);
              chessFrame.setResizable(false);
        });
    }
    
}
