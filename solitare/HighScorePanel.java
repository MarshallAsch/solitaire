/* class file that contains the the panel of high scores and the buttons and main layout
 * 
 */

import javax.swing.JPanel;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

public class HighScorePanel extends JPanel implements ActionListener
{
     private ArrayList<ArrayList> info;
     private int index, num;
     private JFrame frame;
     private HighScorePanel2 showScores;
     private JButton b1, b2, b3, b4, b5;
     private JPanel p1;
     
     public HighScorePanel(ArrayList<ArrayList> toPrint, JFrame f1)
     {
          frame = f1;
          info = toPrint;
          index = num;
          
          //make buttons
          b1 = new JButton("OK");
          b2 = new JButton("Clear");
          b3 = new JButton("by Score");
          b4 = new JButton("by time");
          b5 = new JButton("by Num-moves");
          
          //add listeners
          b1.setActionCommand("ok");
          b1.addActionListener(this);
          
          b2.setActionCommand("clear");
          b2.addActionListener(this);
          
          b3.setActionCommand("sort1");
          b3.addActionListener(this);
          
          b4.setActionCommand("sort2");
          b4.addActionListener(this);
          
          b5.setActionCommand("sort3");
          b5.addActionListener(this);
          
          p1 = new JPanel(new GridLayout(2,2));
          
          //add to panel
          p1.add(b1);
          p1.add(b2);
          p1.add(b3);
          p1.add(b4);
          p1.add(b5);
          add(p1);
          
          showScores = new HighScorePanel2(info);
          add(showScores);  
          
          setSize(400, 300);
     }
     
     //when one of the buttons is pressed
     public void actionPerformed (ActionEvent e)
     {
          if (e.getActionCommand().equals("ok"))
          {
               frame.dispose();        //close frame when ok is pressed
          }
          else if (e.getActionCommand().equals("clear"))
          {
               info.clear();  //empty the array list
               
               //open file and empty it
               try{
                    PrintWriter output = new PrintWriter(new FileWriter("highScores.txt"));
                    output.print("");
                    output.close();
               }
               catch(Exception exc)
               {}
               
               //close frame
               frame.dispose();
          }
          else if (e.getActionCommand().equals("sort1"))  //sort by score
          {
               info = new Sort(info, 0);
               
               showScores.update(info, 1);
          }
          else if (e.getActionCommand().equals("sort2"))   //sort by time
          {
               info = new Sort(info, 1);
               showScores.update(info, 0);
          }
          else if (e.getActionCommand().equals("sort3"))   //sort by number of moves
          {
               info = new Sort(info, 2);
               showScores.update(info, 0);
          }
     }
}