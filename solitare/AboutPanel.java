/* Panel class that shows the rules and information about the game
 * 
 */

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;

public class AboutPanel extends JPanel implements ActionListener
{
     private JFrame frame;
     private JTextArea text;
     private JButton ok;
     
     public AboutPanel(JFrame frameIn)
     {
          frame = frameIn;
          text = new JTextArea(20, 50);
          text.setLineWrap(true);
          text.setEditable(false);
          text.setWrapStyleWord(true);
          
          Scanner input;
          try{
               input = new Scanner (new File("about.txt"));  //get text to display from text file
               String str = input.nextLine();
               
               //text formating
               while (input.hasNextLine())
               {
                    str += "\n\n";
                    str += input.nextLine();
               }
               
               //add text to the text area
               text.setText(str);
               add(text);    //add to panel
               input.close();
          }
          catch (Exception e)
          {}
          
          //make button, add listeners and to the panel
          ok = new JButton("OK");
          ok.addActionListener(this);
          ok.setActionCommand("ok");
          add(ok);
     }
     
     //when the button is pressed 
     public void actionPerformed (ActionEvent e)
     {
          if (e.getActionCommand().equals("ok"))
               frame.dispose();  //close the frame
     }
}