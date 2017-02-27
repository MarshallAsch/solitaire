/* Panel to display the scores
 * 
 */

import javax.swing.JPanel;
import java.util.*;
import java.awt.*;

public class HighScorePanel2 extends JPanel
{
     private ArrayList<ArrayList> info;
     private int num, x  = 0;
     
     //if mid game
     public HighScorePanel2(ArrayList<ArrayList> toPrint)
     {
          info = toPrint;
          
          setSize(400, 320);
          setPreferredSize(new Dimension(400, 320));
          repaint();
     }
     
     
     //if at the end of the game when the user won
     public void update(ArrayList<ArrayList> in, int x)
     {
          this.x = x;
          info = in;
          repaint();
     }
     
     //override paint
     public void paint(Graphics g)
     {
          int up;
          
          //paint the titles
          g.drawString("Name", 15, 20);
          g.drawString("Score", 115, 20);
          g.drawString("Time", 165, 20);
          g.drawString("Number of Moves", 215, 20);
          
          num = info.get(0).size();
          if (x == 0)
          {
               //output from top down
               for (int i = 0; i < num; i++)
               {
                    //if it is the user that won make colour red
                    if ((Character) info.get(4).get(i) == 'R')
                    {
                         g.setColor(new Color(255, 0, 0));
                    }
                    
                    //output the info
                    g.drawString(Integer.toString(i+1) + ".", 0, 20 + 20*(i+1));
                    g.drawString((String)info.get(3).get(i), 15, 20 + 20*(i+1));
                    g.drawString(Integer.toString((Integer)info.get(0).get(i)), 115, 20 + 20*(i+1));
                    g.drawString(Integer.toString((Integer)info.get(1).get(i)), 165, 20 + 20*(i+1));
                    g.drawString(Integer.toString((Integer)info.get(2).get(i)), 215, 20 + 20*(i+1));
                    
                    //make colour black
                    g.setColor(new Color(0, 0, 0));
               }
          }
          else
          {
               up = 1;
               //output from bottem up
               for (int i = num -1; i >= 0; i--)
               {
                    //if it is the user that won make colour red
                    if ((Character) info.get(4).get(i) == 'R')
                    {
                         g.setColor(new Color(255, 0, 0));
                    }
                    
                    //output info
                    g.drawString(Integer.toString(up) + ".", 0, 20 + 20*(up));
                    g.drawString((String)info.get(3).get(i), 15, 20 + 20*(i+1));
                    g.drawString(Integer.toString((Integer)info.get(0).get(i)), 115, 20 + 20*(up));
                    g.drawString(Integer.toString((Integer)info.get(1).get(i)), 165, 20 + 20*(up));
                    g.drawString(Integer.toString((Integer)info.get(2).get(i)), 215, 20 + 20*(up));
                    up++;
                    
                    //make colour black
                    g.setColor(new Color(0, 0, 0));
               }
          }
     }
}