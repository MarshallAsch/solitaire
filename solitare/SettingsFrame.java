/* Frame class to hold main settings window
 * 
 */

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class SettingsFrame extends JFrame implements WindowListener
{
     private Board board;
     public SettingsFrame(Board b)
     {
          board = b;
          add(new SettingsPanel(board, this));
          addWindowListener(this);
          setAlwaysOnTop(true);
          pack();
          setResizable(false);
          setSize(500, 500);  //fix size in other frames as well 
          setTitle("Settings");
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          setLocationRelativeTo(null);
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
     
     //reactivate game when frame is closed
     public void windowClosed(WindowEvent e)
     {
          board.play = true;
          Window[] win = getWindows();
          for (int i = 0; i < win.length; i++)
          {
               if (win[i].getClass().getName().equals("Application"))
                    win[i].setEnabled(true);  //reactivate all application windows
          }
     }
     
     //deactivate game when the window is oppened
     public void windowOpened(WindowEvent e)
     {
          board.play = false;
     }
}