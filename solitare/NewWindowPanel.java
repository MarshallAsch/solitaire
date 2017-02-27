/* panel at end of the game that opens if the user wins to show stats
 * 
 */


import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

public class NewWindowPanel extends JPanel
{
     private JButton option1, option2, option3;
     private Board board;
     private int time;
     private boolean done = false;
     private ArrayList<String> lineInNames;
     private ArrayList<Integer> lineInScore, lineInTime,  lineInMoves;
     private ArrayList<Character> colorToPrint;
     private ArrayList<ArrayList> info;
     
     //initilize the  panel
     public NewWindowPanel(Board boardIn, JFrame frame)
     {
          board = boardIn;
          time = board.seconds;
          option1 = new JButton("New Game");
          option2 = new JButton("New Game With Same Deck");
          option3 = new JButton("Exit");
          
          add(new JLabel("Score: " + board.score + " Number of Moves: " + board.numMoves + 
                         " Time Takes: " + time/60 + ":" + time%60));   //show game stats
          JPanel p1 = new JPanel();
          p1.add(option1);  //add buttons
          p1.add(option2);
          p1.add(option3);
          add(p1);
          input();  //gets names of loged in user
          
          //add listeners
          option1.addActionListener(new ActionListener()
                                         {
               public void actionPerformed(ActionEvent e) 
               {
                    Run.newApplication();  //new game
                    frame.dispose();
               }
          });
          
          option2.addActionListener(new ActionListener() 
                                         {
               public void actionPerformed(ActionEvent e) 
               {
                    Run.newApplication(board.deck);  //new game same deck
                    frame.dispose();
               }
          });
          
          option3.addActionListener(new ActionListener() 
                                         {
               public void actionPerformed(ActionEvent e) 
               {
                    System.exit(0);  //close
               }
          });
     }
     
     private void input()
     {
          String str = "user";  //set default name to user
          try
          {
               File inputFile = new File("UserSetings.dtd");  //open info file
               DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
               DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
               Document doc = dBuilder.parse(inputFile);
               doc.getDocumentElement().normalize();
               
               NodeList nList = doc.getElementsByTagName("user");
               
               if (Run.x != -1)  //if loged in get users name
               {
                    Node nNode = nList.item(Run.x);
                    
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                         Element eElement = (Element) nNode;
                         str = eElement.getElementsByTagName("userName").item(0).getTextContent();
                    }
               }
               
          }
          catch(Exception exe){}
          addToHighScore(str); //add then to the high score file
     }
     
     private void addToHighScore(String name)
     {
          lineInNames = new ArrayList<String>();
          lineInScore = new ArrayList<Integer>();
          lineInTime = new ArrayList<Integer>();
          lineInMoves = new ArrayList<Integer>();
          colorToPrint = new ArrayList<Character>();
          
          //get stats
          int numHighScore = 0, score = board.score, numMoves = board.numMoves, index = 0;
          Scanner fileIn = null;
          
          try{
               fileIn = new Scanner(new File("highScores.txt"));   //try opening file
          }
          catch(Exception e)
          {}
          
          if (fileIn != null)
          {
               while(fileIn.hasNextLine())  //get all of the old high scores from file
               {
                    lineInScore.add(fileIn.nextInt());
                    lineInTime.add(fileIn.nextInt());
                    lineInMoves.add(fileIn.nextInt());
                    colorToPrint.add('B');
                    lineInNames.add(fileIn.nextLine().trim());
                    numHighScore ++;
               }
               fileIn.close();  //close file
               
               
               /*****************************************************************************
                 * 
                 * check
                 * 
                 *****************************************************************************/
               for (int i = 0; i < numHighScore; i++)
               {
                    if (score == lineInScore.get(i))
                    {
                         index = i;
                         break;
                    }
               }
               lineInScore.add(index, score);
               lineInTime.add(index, time);
               lineInMoves.add(index, numMoves);
               lineInNames.add(index, name);
               colorToPrint.add(index, 'R');
               
               
               try{
                    PrintWriter output = new PrintWriter(new FileWriter("highScores.txt"));
                    int max = 10;
                    if (lineInScore.size() < 10)
                         max = lineInScore.size();
                    
                    for (int i = 0; i < max; i++)  //put top 10 back into file
                         
                    {
                         output.println(lineInScore.get(i) + " " + lineInTime.get(i) + " " +
                                        lineInMoves.get(i) + " " + lineInNames.get(i));
                    }
                    output.close();  //close file
               }
               catch(Exception e)
               {}
               
               //add stats to arrayList
               info = new ArrayList<ArrayList>();
               info.add(lineInScore);
               info.add(lineInTime);
               info.add(lineInMoves);
               info.add(lineInNames);
               info.add(colorToPrint);
               HighScoreFrame ex = new HighScoreFrame(info, index);  //open high score frame
               ex.setVisible(true);
          }
     }
}