/* main frame for the game contains the panel that does everything
 * 
 */

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class Application extends JFrame
{
     public JMenuItem user;
     
     //make a new game
     public Application() 
     {
          init(new Board());
     }
     
     //make a new game with the same deck
     public Application(ArrayList<Card> deck)
     {
          init(new Board(deck));
     }
     
     //initiate the frame
     private void init(Board board)
     {
          add(board);
          
          //create menu
          JMenuBar  menuBar = new JMenuBar();
          JMenu menu = new JMenu("Menu"), newGame = new JMenu("New Game"), login = new JMenu("Login");
          JMenuItem i1, i2, i3, i4, i5, i6, login1, login2;
          
          //create menu objects
          i1 = new JMenuItem("Random Deck");
          i2 = new JMenuItem("Same Deck");
          i3 = new JMenuItem("Close");
          i4 = new JMenuItem("Options");
          i5 = new JMenuItem("About");
          i6 = new JMenuItem("High Score");
          user = new JMenuItem();
          
          login1 = new JMenuItem("Login");
          login2 = new JMenuItem("Logout");
          login.add(login1);
          login.add(login2);
          
          //add to menu bar
          newGame.add(i1);
          newGame.add(i2);
          menu.add(newGame);
          menu.add(i3);
          menu.add(i4);
          menu.add(i5);
          menu.add(i6);
          
          //add listeners to the menu items
          i1.addActionListener(new ActionListener() {
               public void actionPerformed (ActionEvent actionEvent){  //new game 
                    Run.newApplication();     
                    dispose();
               }});
          i2.addActionListener(new ActionListener() {
               public void actionPerformed (ActionEvent actionEvent){   //new game with same deck
                    Run.newApplication(board.deck);
                    dispose();
               }});
          i3.addActionListener(new ActionListener() {
               public void actionPerformed (ActionEvent actionEvent){      //close
                    System.exit(0);
               }});
          i4.addActionListener(new ActionListener() {
               public void actionPerformed (ActionEvent actionEvent){  //open settings frame
                    if (canOppen())
                    {
                         setEnabled(false);
                         SettingsFrame ex = new SettingsFrame(board);
                         ex.setVisible(true);
                    }
               }});
          i5.addActionListener(new ActionListener() {
               public void actionPerformed (ActionEvent actionEvent){  //open about frame
                    if (canOppen())
                    {
                         setEnabled(false);
                         AboutFrame ex = new AboutFrame(board);
                         ex.setVisible(true);
                    }
               }});
          i6.addActionListener(new ActionListener() {
               public void actionPerformed (ActionEvent actionEvent){   //open high score
                    if (canOppen())
                    {
                         setEnabled(false);
                         HighScoreFrame ex = new HighScoreFrame(board);
                         ex.setVisible(true);
                    }
               }});
          login1.addActionListener(new ActionListener() {
               public void actionPerformed (ActionEvent actionEvent){      //open login
                    if (canOppen())
                    {
                         setEnabled(false);
                         Login ex = new Login(board);
                         ex.setVisible(true);
                    }
               }});
          login2.addActionListener(new ActionListener() {
               public void actionPerformed (ActionEvent actionEvent){       //logout
                    user.setLabel("not logged in");
                    Run.x = -1;
               }});
          menuBar.add(menu);
          menuBar.add(login);
          menuBar.add(user);
          
          //menuBar.insertSeparator(2);
          setJMenuBar(menuBar);
          
          pack();
          setSize(681, 700);
          setPreferredSize(new Dimension(681, 700));
          setResizable(false);
          
          setTitle("Solitare");
          
          //setBackground(new Color(0, 140, 0));
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          setLocationRelativeTo(null);
     }
     
     //checks if there is more then 1 window open or not to determinew if another can be opend
     private boolean canOppen()
     {
          Window[] win = getWindows();
          int show = 0;
          for (int i = 0; i < win.length; i++)
          {
               if (win[i].isShowing())  //count number of displayed windows
                    show++;
          }
          if (show > 1)
               return false;
          else                     //if only one return true
               return true;
     }
}