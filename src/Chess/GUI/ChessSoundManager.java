/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chess.GUI;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author User
 */
public class ChessSoundManager {
    
    public static void playMoveSound() 
    {
        playSound("sound/move-self.wav");
    }

    public static void playCaptureSound() 
    {
        playSound("sound/capture.wav");
    }

    public static void playCheckSound() 
    {
        playSound("sound/check.wav");
    }

    public static void playCastleSound() 
    {
        playSound("sound/castle.wav");
    }
    
    public static void playPromotionSound() 
    {
        playSound("sound/promote.wav");
    }
    
    public static void playCheckMateSound() 
    {
        playSound("sound/checkmate.wav");
    }
    
    private static void playSound(String filePath) {
        try 
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } 
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
        }
    }
}
