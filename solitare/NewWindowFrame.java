/* Game over window, class file to display the screen that contains the game statistics
 * 
 */

import javax.swing.JFrame;
import java.util.*;
import java.awt.*;

public class NewWindowFrame extends JFrame
{
     public NewWindowFrame(Board board) 
     {
          init(board); //initlilize the frame
     }
     
     private void init(Board board)
     {    
          add(new NewWindowPanel(board, this));
          setPreferredSize(new Dimension(419, 85));
          setResizable(false);
          pack();
          setTitle("Game Over");
          
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          setLocationRelativeTo(null);
     }
}