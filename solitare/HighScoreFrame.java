/* Frame that displays the high scores from a text file
 * 
 */

import javax.swing.JFrame;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import java.awt.*;

public class HighScoreFrame extends JFrame implements WindowListener
{
     private Board board;
     
     //if from winning at the end of the game
     public HighScoreFrame (ArrayList<ArrayList> info, int index)
     {
          add(new HighScorePanel(info, this));
          setAlwaysOnTop(true);
          init();
     }
     
     //if from mid game
     public HighScoreFrame(Board b)
     {
          board = b;
          add(new HighScorePanel(readHighScores(), this));
          setAlwaysOnTop(true);
          addWindowListener(this);
          init();
     }
     
     //initlilize the frame
     private void init()
     {
          setResizable(false);
          pack();
          setSize(400, 320);
          setTitle("High Score");
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          setLocationRelativeTo(null);
     }
     
     //read stats from the file
     private ArrayList<ArrayList> readHighScores()
     {
          ArrayList<String> lineInNames = new ArrayList<String>();
          ArrayList<Integer>lineInScore = new ArrayList<Integer>(), lineInTime = new ArrayList<Integer>(),
               lineInMoves = new ArrayList<Integer>();
          ArrayList<ArrayList> info = new ArrayList<ArrayList>();
          ArrayList<Character> colorToPrint = new ArrayList<Character>();
          
          Scanner fileIn = null;
          try{
               fileIn = new Scanner(new File("highScores.txt"));  //open file
          }
          catch(Exception e)
          {}
          
          if (fileIn != null)
          {
               //get info from file and put it into the arraylists
               while(fileIn.hasNextLine())
               {
                    lineInScore.add(fileIn.nextInt());
                    lineInTime.add(fileIn.nextInt());
                    colorToPrint.add('B');
                    lineInMoves.add(fileIn.nextInt());
                    lineInNames.add(fileIn.nextLine().trim());
               }
               fileIn.close();  //close file
               
               //add to main arraylisy
               info.add(lineInScore);
               info.add(lineInTime);
               info.add(lineInMoves);
               info.add(lineInNames);
               info.add(colorToPrint);
          }
          return info;
     } 
     
     //required override methods
     public void windowDeactivated(WindowEvent e)
     {}
     
     public void windowActivated(WindowEvent e)
     {}
     
     public void windowDeiconified(WindowEvent e)
     {}
     
     public void windowIconified(WindowEvent e)
     {}
     
     public void windowClosing(WindowEvent e)
     {}
     
     //to reactivate the game when the window is closed
     public void windowClosed(WindowEvent e)
     {
          board.play = true;
          Window[] win = getWindows();
          for (int i = 0; i < win.length; i++)
          {
               if (win[i].getClass().getName().equals("Application"))
                    win[i].setEnabled(true);  //reactivate any Application frame
          }
     }
     
     //deactivate game when the frame is opened
     public void windowOpened(WindowEvent e)
     {
          board.play = false;
     }
}