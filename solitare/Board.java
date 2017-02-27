/*
 * packaging
 * cross platform compatibilty
 */

import javax.swing.JPanel;
import javax.swing.JButton;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

public class Board extends JPanel implements MouseListener, MouseMotionListener, ActionListener
{
     public ArrayList<Card> deck, temp = new ArrayList<Card>(), top = new ArrayList<Card>(), 
          movePile = new ArrayList<Card>();
     private ArrayList<ArrayList<Card>> pile = new ArrayList<ArrayList<Card>>();
     public int index1, index2, tempX1, tempY1, score = 0, moves = 0, numMoves = 0, seconds = 0,
          scoreSystemUsed = 0, r = 0, g = 140, b = 0, toFlip = 3, overTime = 1;
     private Card moved;
     public boolean play = true, showNumMoves = true, showTime = true, showScore  = true;
     int[][] scoringSystem = {{5, 10, 10, 5, -15}};//{deck -> column, column -> pile, deck -> pile, flip, 
     //pile -> column}
     private JButton undoButton, pause;
     private Stack<ArrayList> history = new Stack<ArrayList>();
     private Toolkit tk = Toolkit.getDefaultToolkit();
     private Board thisBoard = this;
     private Timer timer = new Timer();
     
     private String cardBackFile;
     private JPanel pan = new JPanel();
     
     //start new game 
     public Board() 
     {
          for (int i = 0; i < 12; i++)
          {
               pile.add(new ArrayList<Card>());
          }
          deck = initCards(); 
          
          init();
     }
     
     //start with a old deck
     public Board(ArrayList<Card> deckIn)
     {
          for (int i = 0; i < 12; i++)
          {
               pile.add(new ArrayList<Card>());
          }
          deck = initCards(deckIn); 
          
          init();
     }
     
     //initlilze the board
     private void init()
     {
          //initilize the settings
          settings(Run.x);
          //check if a user is logged in
          if (Run.x == -1)
          {
               Login ex = new Login(this);
               ex.setVisible(true);
          }
          
          setLayout(null);
          
          //set background colour
          setBackground(new Color(r, g, b));
          
          //add listeners
          addMouseListener(this);
          addMouseMotionListener(this);
          
          //set the size of the panel
          setSize(681, 700);
          setPreferredSize(new Dimension(681, 700));
          
          //make and add buttons
          undoButton = new JButton("Undo");
          undoButton.addActionListener(this);
          undoButton.setActionCommand("undo");
          undoButton.setVisible(true);
          pan.add(undoButton);
          
          pause = new JButton("pause");
          pause.addActionListener(this);
          pause.setActionCommand("pause");
          pause.setVisible(true);
          pause.setEnabled(true);
          pan.add(pause);
          
          pan.setSize(pan.getPreferredSize());
          pan.setLocation(150, 15);
          add(pan);
          
          //limit how often the user can click a button in a row
          try{
               undoButton.setMultiClickThreshhold(250);
          }
          catch (Exception e)
          {}
          
          try{
               pause.setMultiClickThreshhold(250);
          }
          catch (Exception e)
          {}
          
          //make a timer to count the time of the game rin the task every second
          timer.schedule(new TimerTask()
                              {
               public void run()
               {
                    //only increment seconds if the game is active
                    if (play)
                         seconds++;
                    
                    //open a game over popup so ask is the user wants to keep playing after they time out
                    if (seconds > 960*overTime)  //960
                    {
                         tk.beep();
                         int option = JOptionPane.showOptionDialog(null, "Game has lasted more then "
                                                                        + 16*overTime + 
                                                                   " minutes, would you like to keep playing?"
                                                                        , "Time out", JOptionPane.YES_NO_OPTION
                                                                        , JOptionPane.WARNING_MESSAGE, null,
                                                                   null, 0);
                         //if user presses yes then keep playing
                         if (option == 0)
                              overTime++;
                         else                //else close
                         {
                              play = false;
                              timer.cancel();
                              System.exit(1);
                         }
                    }
                    
                    //if the history list is empty disable the button 
                    if (history.empty())
                         undoButton.setEnabled(false);
                    else
                         undoButton.setEnabled(true);
                    
                    //if the game is won then open game over frame
                    if (win())
                    {
                         //bounce cards
                         NewWindowFrame ex = new NewWindowFrame(thisBoard);
                         ex.setVisible(true);
                         play = false;
                         timer.cancel();
                    }
                    
                    //set backgroung colour
                    setBackground(new Color(r, g, b));
                    pan.setBackground(new Color(r, g, b));
                    repaint();
               }
          }, 0, 1000);
     }
     
     //check if the game is imposible
     private boolean possibleMove()
     {
          //if card cards that it can be placed on are in the same pile then false (same suit one below, 
          //one above opposite suit)
          
          //cycle throught the piles that can have all the cards that it can go on underneth it
          for (int i = 4; i < 7; i++)
          {
               int num1, num2, counter = 0;
               char suit1, suit2;
               num1 = pile.get(i).get(pile.get(i).size()-1).getNum();
               suit1 = pile.get(i).get(pile.get(i).size()-1).getSuit();
               
               for (int j = 0; j < pile.get(i).size()-1; j++)
               {
                    num2 = pile.get(i).get(j).getNum();
                    suit2 = pile.get(i).get(j).getSuit();
                    
                    if (num1 == num2+1 && suit1 == suit2)
                         counter++;
                    if(num1+1 == num2 && ((suit1 == 'H' || suit1 == 'D') && (suit2 == 'C' || suit2 == 'S')) 
                            || ((suit2 == 'H' || suit2 == 'D') && (suit1 == 'C' || suit1 == 'S')))
                         counter++; 
               }
               
               //if all the cards it can go on are under it return false
               if (counter == 3)
                    return false;
          }
          return true;
     }
     
     //initilize the settings of the game
     public void settings(int loginStatus)
     {
          //if no user is logged in then set to 0 (default user)
          if (loginStatus == -1)
               loginStatus = 0;
          try
          {
               File inputFile = new File("UserSetings.dtd");   //open file
               DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
               DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
               Document doc = dBuilder.parse(inputFile);
               doc.getDocumentElement().normalize();
               
               NodeList nList = doc.getElementsByTagName("user");
               String s1, s2;
               
               Node nNode = nList.item(loginStatus);
               
               
               //get settings from index 0 of the file
               if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String str = eElement.getElementsByTagName("userName").item(0).getTextContent();
                    if (str.equals(""))
                         str = "not logged in";
                    Window[] window = Window.getWindows();
                    for (int i = 0; i < window.length; i++)
                    {
                         if (window[i].getClass().getName().equals("Application"))
                              ((Application)window[i]).user.setLabel(str);
                    }
                    
                    //get colour values
                    r = Integer.parseInt(eElement.getElementsByTagName("colorR").item(0).getTextContent());
                    g = Integer.parseInt(eElement.getElementsByTagName("colorG").item(0).getTextContent());
                    b = Integer.parseInt(eElement.getElementsByTagName("colorB").item(0).getTextContent());
                    
                    //show values
                    showTime = Boolean.parseBoolean(eElement.getElementsByTagName("showTime").item(0).getTextContent());
                    showNumMoves = Boolean.parseBoolean(eElement.getElementsByTagName("showNumMoves").item(0).getTextContent());
                    
                    //number of cards to flip from the pile
                    toFlip = Integer.parseInt(eElement.getElementsByTagName("toFlip").item(0).getTextContent());
                    
                    //card back image file
                    cardBackFile = eElement.getElementsByTagName("cardBack").item(0).getTextContent();
                    
                    //set all the card backs
                    for (int i = 0; i < 52; i++)
                         deck.get(i).setCardBack(cardBackFile);
               }  
          }
          catch(Exception exe){}
     }
     
     //override the paint compnent method to draw the main parts of the game
     public void paintComponent(Graphics g)
     {
          //seperate by 20
          int timeY = 0, scoreY = 0, numMovesY = 0;
          
          //show the number of moves
          if (showNumMoves)
          {
               numMovesY = 20;
               g.drawString("Number of moves: " + numMoves, 10, 20);
          }
          
          //show the score
          if (showScore)
          {
               if (showNumMoves)
                    scoreY = 40;
               else
                    scoreY = 20;
               g.drawString("Score: " + score, 10, scoreY);
          }
          
          //show the time
          if (showTime)
          {
               timeY = 20;
               if (showNumMoves)
                    timeY += 20;
               if (showScore)
                    timeY += 20;
               String str1 = "Time: " + seconds/60 + ":";
               
               if (seconds%60 < 10)
                    str1= str1 + "0" + seconds%60;
               else
                    str1 = str1 + seconds%60;
               g.drawString(str1, 10, timeY);
          }
          
          //draw ace placeholders
          g.drawRect(404, 5, 63, 91);
          g.drawRect(472, 5, 63, 91);
          g.drawRect(540, 5, 63, 91);
          g.drawRect(608, 5, 63, 91);
          
          //draw card and pile place holders
          g.drawRect(5, 105, 63, 91);
          g.drawRect(200, 105, 63, 91);
          g.drawRect(268, 105, 63, 91);
          g.drawRect(336, 105, 63, 91);
          g.drawRect(404, 105, 63, 91);
          g.drawRect(472, 105, 63, 91);
          g.drawRect(540, 105, 63, 91);
          g.drawRect(608, 105, 63, 91);
          
          //draw the 7 main piles from bottem up
          for (int j = 0; j < 7; j++)
          {
               for (int i = 0; i < pile.get(j).size(); i++)
               {
                    g.drawImage(pile.get(j).get(i).getDisplay(), pile.get(j).get(i).getX(),
                                pile.get(j).get(i).getY(), null);
               }
          }
          
          //draw the deck pile from top down
          for (int i = pile.get(7).size()-1; i >=0 ; i--)
          {
               g.drawImage(pile.get(7).get(i).getDisplay(), pile.get(7).get(i).getX(),
                           pile.get(7).get(i).getY(), null);
          }
          
          //draw the ace piles from bottem up
          for (int j = 8; j < 12; j++)
          {
               for (int i = 0; i < pile.get(j).size(); i++)
               {
                    g.drawImage(pile.get(j).get(i).getDisplay(), pile.get(j).get(i).getX(),
                                pile.get(j).get(i).getY(), null);
               }
          }
          
          //if a card is being movied then draw those cards on top of the rest
          if (moved != null)
               g.drawImage(moved.getDisplay(), moved.getX(), moved.getY(), null);
          
          //if there is a pile being moved draw those on top 
          if (!movePile.isEmpty())
          {
               for (int i = 0; i < movePile.size(); i++)
               {
                    g.drawImage(movePile.get(i).getDisplay(), movePile.get(i).getX(),
                                movePile.get(i).getY(), null);
               }
          }
     }
     
     //add the current board to the history list
     private void setUndo()
     {
          ArrayList<Integer> xValue, yValue, pileNum, pileIndex, topIndex, tempIndex, scoreTemp;
          ArrayList<Boolean> showFace, inPile, inAce;
          ArrayList<ArrayList> l1;
          
          xValue = new ArrayList<Integer>();
          yValue = new ArrayList<Integer>();
          pileNum = new ArrayList<Integer>();
          pileIndex = new ArrayList<Integer>();
          topIndex = new ArrayList<Integer>(52);
          tempIndex = new ArrayList<Integer>(52);
          scoreTemp = new ArrayList<Integer>(1);
          showFace = new ArrayList<Boolean>();
          inPile = new ArrayList<Boolean>();
          inAce = new ArrayList<Boolean>();
          l1 = new ArrayList<ArrayList>();
          
          //cycle through all the piles
          for (int i = 0; i < 12; i++)
          {
               //fill arrays with null place holders
               for (int j = 0; j < 24; j++)
               {
                    topIndex.add(null);
                    tempIndex.add(null);
               }
          }
          
          //cycle through all cards
          for (int i = 0; i < 52; i++)
          {
               //cycle through each pile
               for (int j = 0; j < pile.size(); j++)
               {
                    //cycle through each card in the pile
                    for (int p = 0; p < pile.get(j).size(); p++)
                    {
                         //add stats to history sublists
                         if (deck.get(i).equals(pile.get(j).get(p)))
                         {
                              pileNum.add(j);
                              pileIndex.add(p);
                         }
                    }
                    
                    //if the pile is the deck
                    if (j == 7)
                    {
                         //cycle through cards in that pile
                         for (int p = 0; p < top.size(); p++)
                         {
                              //if its in the top then indicate position
                              if (deck.get(i).equals(top.get(p)))
                              {
                                   topIndex.set(i, p);
                              }
                         }
                         
                         //cycle through the temp list adding the index
                         for (int p = 0; p < temp.size(); p++)
                         {
                              if (deck.get(i).equals(temp.get(p)))
                              {
                                   tempIndex.set(i, p);
                              }
                         }
                    }
               }
               
               //cycle throught the rest of the cards and if they are not in the deck pile then indicate with -1 value
               for (int p = 0; p < 52; p++)
               {
                    if (tempIndex.get(p) == null)
                         tempIndex.set(p, -1);
                    if (topIndex.get(p) == null)
                         topIndex.set(p, -1);
               }
               
               //add value to sublists
               xValue.add(deck.get(i).getX());
               yValue.add(deck.get(i).getY());
               showFace.add(deck.get(i).isShowFace());
               inPile.add(deck.get(i).isInPile());
               inAce.add(deck.get(i).isInAce());
          }
          
          //add sublists to history
          scoreTemp.add(score);
          l1.add(xValue);
          l1.add(yValue);
          l1.add(showFace);
          l1.add(inPile);
          l1.add(inAce);
          l1.add(pileNum);
          l1.add(pileIndex);
          l1.add(topIndex);
          l1.add(tempIndex);
          l1.add(scoreTemp);
          history.addElement(l1);
     }
     
     //method to undo the most recent move 
     public void undo()
     {
          //if the history is empty then do nothing
          if (history.size() == 0)
               return;
          
          //cycle through all the piles and clear them
          for (int i = 0; i < 12; i++)
          {
               pile.get(i).clear();
               top.clear();
               temp.clear();
               for (int j = 0; j < 24; j++)
               {
                    pile.get(i).add(null);
                    top.add(null);
                    temp.add(null);
               }
          }
          
          //put everything back using the info in the history stack
          try
          {
               ArrayList<ArrayList> hist = history.pop();
               score = (int)hist.get(9).get(0);
               
               //cycle through all the cards to put back
               for (int i = 0; i < 52; i++)
               {
                    deck.get(i).setPosition((int)hist.get(0).get(i), (int)hist.get(1).get(i));  //set position
                    deck.get(i).showFace((boolean)hist.get(2).get(i));           //set face up
                    deck.get(i).setInPile((boolean)hist.get(3).get(i));   //set in pile
                    deck.get(i).setInAce((boolean)hist.get(4).get(i));   //set in ace
                    try
                    {
                         pile.get((int)hist.get(5).get(i)).set((int)hist.get(6).get(i), deck.get(i));
                    }
                    catch (Exception e)
                    {
                         pile.get((int)hist.get(5).get(i)).add((int)hist.get(6).get(i), deck.get(i));
                    }
                    
                    //put back in to deck piles 
                    if ((int)hist.get(7).get(i) != -1)
                         top.set((int)hist.get(7).get(i), deck.get(i));
                    if ((int)hist.get(8).get(i) != -1)
                         temp.set((int)hist.get(8).get(i), deck.get(i));
               }
          }
          catch (Exception e)
          {}
          
          //remove all exess null place holders from the lists
          for (int i = 0; i < 12; i++)
          {
               for (int j = 0; j < 24; j++)
               {
                    try
                    {
                         pile.get(i).remove(null);
                    }
                    catch (Exception e)
                    {
                         break;
                    }
               }
          }
          for (int j = 0; j < 24; j++)
          {
               try
               {
                    top.remove(null);
               }
               catch (Exception e)
               {}
               try
               {
                    temp.remove(null);
               }
               catch (Exception e)
               {}
          }
          repaint();   //repaint the board
     }
     
     //initlize the board if the deck to play with is given
     private ArrayList<Card> initCards(ArrayList<Card> shuffled)
     {
          int num = 0;
          
          //put the cards into the correct spot
          for (int i = 1; i < 8; i++)
          {
               for (int j = 0; j < i; j++)
               {
                    if (j == i-1)
                         //place card face up
                         shuffled.get(num).showFace(true);
                    else
                         shuffled.get(num).showFace(false);
                    
                    shuffled.get(num).setPosition((68*(i-1) + 200), (105 + 20*j));
                    pile.get(i-1).add(shuffled.get(num));
                    num++;
               }       
          }
          
          //put rest into pile
          for (int i = num; i < 52; i++)
          {
               shuffled.get(i).showFace(false);
               shuffled.get(i).setInPile(true);
               shuffled.get(i).setPosition(5, 105);
               pile.get(7).add(shuffled.get(i));
          }
          repaint();
          return shuffled;
     }
     
     //create and shuffle the deck at the begining of the game
     public ArrayList<Card> initCards()
     {
          Card[] deck = new Card[52];
          int rand, change = 0, num = 1;
          char suit = 'H';
          
          ArrayList<Card> shuffled = new ArrayList<Card>();
          ArrayList<Integer> cardNum = new ArrayList<Integer>();
          
          //create the card objects
          for (int i = 0; i < 52; i++)
          {
               deck[i] = new Card(num, suit);
               num ++;
               
               if (num == 14)
               {
                    num = 1;
                    change ++;
               }
               
               //make cards with other suits
               if (change == 1)
                    suit = 'C';
               else if (change == 2)
                    suit = 'D';
               else if (change == 3)
                    suit = 'S';
          }
          
          //shuffle
          for (int i = 0; i < 52; i++)
          {
               cardNum.add(i);
          }
          
          //take a random number form a list of 0-52 and take that card from the pile to shuffle it
          for (int i = 0; i < 52; i++)
          {
               rand = (int)(Math.random()*cardNum.size());
               shuffled.add(deck[cardNum.get(rand)]);
               cardNum.remove(rand);
          }
          num = 0;
          
          //put the cards int to initial layout on the board into the 8 piles
          for (int i = 1; i < 8; i++)
          {
               //make the piles each increase in size from containing 1 card to holding 7
               for (int j = 0; j < i; j++)
               {
                    if (j == i-1)
                         //place card face up
                         shuffled.get(num).showFace(true);
                    else
                         shuffled.get(num).showFace(false);
                    
                    shuffled.get(num).setInAce(false);
                    shuffled.get(num).setInPile(false);
                    shuffled.get(num).setPosition((68*(i-1) + 200), (105 + 20*j));
                    pile.get(i-1).add(shuffled.get(num));
                    num++;
               }       
          }
          
          //put rest into pile
          for (int i = num; i < 52; i++)
          {
               shuffled.get(i).showFace(false);
               shuffled.get(i).setInPile(true);
               shuffled.get(i).setInAce(false);
               shuffled.get(i).setPosition(5, 105);
               pile.get(7).add(shuffled.get(i));
               temp.add(shuffled.get(i));
          }
          
          //make sure that one of the ways that the game is unsolvable is false or try again
          if (!possibleMove())
          {
               for (int i = 0; i < 12; i++)
               {
                    pile.get(i).clear();
               }
               return initCards();
          }
          repaint();
          return shuffled;
     }
     
     //flip 3 cards when the deck pile is clicked
     private void flip3()
     {
          int cardLeft = 0, amount = temp.size() - 1;
          
          //pre pair to flip the correct number of cards if there are not enough left to flip 3
          if (amount > 2)
          {
               cardLeft = 3;
          }
          else if (amount == 2)
          {
               cardLeft = 2;
          }
          else if (amount == 1)
          {
               cardLeft = 1;
          }
          else
          {
               //reset cards
               for(int i = 0; i < pile.get(7).size(); i++)
               {
                    pile.get(7).get(i).setX(5);
                    pile.get(7).get(i).showFace(false);
                    temp.add(pile.get(7).get(i));
               }
               cardLeft = 0;
               
               top.clear();
          }
          
          //make sure they are in the right spot
          for (int i = 0; i < top.size(); i++)
          {
               top.get(i).setX(73);
          }
          
          //move the cards into the top list and remove from the other one
          for (int i = 0; i < cardLeft; i++)
          {
               amount = temp.size() -1;
               temp.get(amount).setX(73 + 10*i);
               temp.get(amount).showFace(true);
               top.add(temp.get(amount));              
               temp.remove(amount);                    
          }
     }
     
     //method to flip one card in pile
     private void flip1()
     {
          int cardLeft = 0, amount = temp.size() - 1;
          
          //if deck is not empty
          if (amount > 0)
          {
               cardLeft = 1;
          }
          else
          {
               //reset cards
               for(int i = 0; i < pile.get(7).size(); i++)
               {
                    pile.get(7).get(i).setX(5);
                    pile.get(7).get(i).showFace(false);
                    temp.add(pile.get(7).get(i));
               }
               cardLeft = 0;
               
               top.clear();
          }
          
          //make sure the rest of the cards in the list are in the right spot
          for (int i = 0; i < top.size(); i++)
          {
               top.get(i).setX(73);
          }
          
          //flip over the cardand move in to the other list of cards 
          for (int i = 0; i < cardLeft; i++)
          {
               amount = temp.size() -1;
               temp.get(amount).setX(73 + 10*i);
               temp.get(amount).showFace(true);
               top.add(temp.get(amount));
               temp.remove(amount);
          }
     }
     
     //check if the game is won
     private boolean win()
     {
          //cycle through all the cards making sure that they are all in an ace pile
          for (int i = 0; i <52; i++)
          {
               if (!deck.get(i).isInAce())
                    return false;
          }
          return true;
     }
     
     //if the mouse was clicked to flip a card or the pile
     public void mouseClicked (MouseEvent e)
     {
          //if game is stoped do nothing
          if (!play)
               return;
          
          //cycle throught the pile to see where the click was
          for (int i = 0; i < 12; i++)
          {
               //if the click was on the deck pile
               if (i == 7)
               {
                    //if the pile still has cards in it
                    if (e.getX() >= 5 && e.getX() <= 68 && e.getY() >= 105 && e.getY() <= 201 &&
                        pile.get(7).size() > 0)
                    {
                         setUndo();  //add move to history
                         
                         //flip the cards ether one or 3 at a time 
                         if (toFlip == 3)
                              flip3();
                         else
                              flip1();
                         
                         //increment the number of moves
                         numMoves++;
                    }
               }
               else
               {
                    //if the pile is not empty
                    if (pile.get(i).size() > 0)
                    {
                         //if clicked on the top face down card in the pile
                         if (e.getX() >= pile.get(i).get(pile.get(i).size()-1).getX() && e.getX() <=
                             pile.get(i).get(pile.get(i).size()-1).getX() + 63 && e.getY() >=
                             pile.get(i).get(pile.get(i).size()-1).getY() && e.getY() <= pile.get(i).get
                                  (pile.get(i).size()-1).getY() + 91 && !pile.get(i).get(pile.get(i).size()-1)
                                  .isShowFace())
                         {
                              score += scoringSystem[scoreSystemUsed][3];  //add to score
                              setUndo();     //add move to history
                              pile.get(i).get(pile.get(i).size()-1).showFace(true);
                              numMoves++;
                              break;
                         }
                    }
               }
          }
          repaint();   //repaint the board
     }
     
     //required over ride methods
     public void mouseExited (MouseEvent e)
     {}
     
     public void mouseEntered (MouseEvent e)
     {}
     
     public void mouseMoved (MouseEvent e)
     {}
     
     //if the mouse was pressed
     public void mousePressed (MouseEvent e)
     {
          //if the game is stoped do nothing
          if (!play)
               return;
          
          //cycle through the piles to try to pick up cards
          for (int i = 0; i < 12; i++)
          {
               //if it is from the deck and it is the top face up card
               if (i == 7 && top.size() > 0 && e.getX() >= top.get(top.size()-1).getX() && e.getX() <=
                   top.get(top.size()-1).getX() + 63 && e.getY() >= top.get(top.size()-1).getY() && e.getY()
                        <= top.get(top.size()-1).getY() + 91 && top.get(top.size()-1).isShowFace())  
               {
                    setUndo();  //add move to hystory 
                    index1 = i;  //get index
                    moved = top.get(top.size()-1);
                    tempX1 = moved.getX();  //get location
                    tempY1 = moved.getY(); 
                    
                    break;
               }
               //if it is not from the deck and it is the top card
               else if (i != 7 && pile.get(i).size() > 0 && e.getX() >= pile.get(i).get(pile.get(i).size()-1)
                             .getX() && e.getX() <= pile.get(i).get(pile.get(i).size()-1).getX() + 63 &&
                        e.getY() >= pile.get(i).get(pile.get(i).size()-1).getY() && e.getY() <= pile.get(i).
                             get(pile.get(i).size()-1).getY() + 91 && pile.get(i).get(pile.get(i).size()-1)
                             .isShowFace())
               {
                    setUndo();
                    index1 = i;
                    moved = pile.get(i).get(pile.get(i).size()-1);
                    tempX1 = moved.getX();
                    tempY1 = moved.getY();
                    
                    break;
               }
               
               //if the card is from onw if the main piles
               if (i < 7)
               {
                    //cycle through the pile to see if another card was clicked on
                    for (int j = pile.get(i).size()-1; j >= 0; j--)
                    {
                         //if the card is faced up and was clicked on then get the pile else stop looking in that pile
                         if (e.getX() >= pile.get(i).get(j).getX() && e.getX() <= pile.get(i).get(j).getX()
                                  + 63 && e.getY() >= pile.get(i).get(j).getY() && e.getY() <= pile.get(i)
                                  .get(j).getY() + 91 && pile.get(i).get(j).isShowFace())
                         {
                              setUndo();
                              index1 = i;
                              index2 = j;
                              moved = pile.get(i).get(j);
                              tempX1 = moved.getX();
                              tempY1 = moved.getY();
                              
                              //get pile of cards that are on top of the one that was clicked on
                              for (int p = j+1; p < pile.get(i).size(); p++)
                              {
                                   movePile.add(pile.get(i).get(p));
                              }
                              break;
                         }
                    }
               }
          }
     }
     
     //if a button was pressed
     public void actionPerformed (ActionEvent e)
     {
          if (e.getActionCommand().equals("undo"))    //undo button
               undo();
          else if (e.getActionCommand().equals("pause"))  //pause button
          {
               play = !play;  //disable the game
          }
     }
     
     //method for when the mouse is released to place the cards
     public void mouseReleased (MouseEvent e)
     {
          //if the game is paused then do nothing
          if (!play)
               return;
          
          //if there is a card to place then try to place it
          if (moved != null)
          {
               boolean placed = false;
               
               //cycle through all the pile to pry to put it there
               for (int i = 0; i < 12; i++)
               {
                    
                    //into piles not empty
                    if (pile.get(i).size() > 0 && i < 7 && e.getX() >= 200 + 63*i && e.getX() <= 200 +
                        63*(i+1) && e.getY() >= pile.get(i).get(pile.get(i).size()-1).getY() && e.getY() <=
                        pile.get(i).get(pile.get(i).size()-1).getY() + 91 &&
                        pile.get(i).get(pile.get(i).size()-1).isShowFace() && 
                        pile.get(i).get(pile.get(i).size()-1).getNum() == moved.getNum() + 1 &&
                        (((pile.get(i).get(pile.get(i).size()-1).getSuit() == 'H' ||
                           pile.get(i).get(pile.get(i).size()-1).getSuit() == 'D') && 
                          (moved.getSuit() =='S' || moved.getSuit() == 'C')) || 
                         ((pile.get(i).get(pile.get(i).size()-1).getSuit() == 'C' || 
                           pile.get(i).get(pile.get(i).size()-1).getSuit() == 'S') && 
                          (moved.getSuit() == 'H' || moved.getSuit() == 'D'))))
                    {
                         moved.setPosition(pile.get(i).get(pile.get(i).size()-1).getX(), pile.get(i)
                                                .get(pile.get(i).size()-1).getY() + 20);
                         moved.setInAce(false);  //set in ace false
                         moved.setInPile(false);  //set in pile fasle
                         pile.get(i).add(moved);  //add the card to the new pile
                         
                         //try to place the rest of the cards if a pile was moved
                         if (!movePile.isEmpty())
                         {
                              //cycle through movepile to place
                              for (int j = 0; j < movePile.size(); j++)
                              {
                                   movePile.get(j).setPosition(pile.get(i).get(pile.get(i).size()-1).getX(),
                                                               pile.get(i).get(pile.get(i).size()-1).getY()+
                                                               20);
                                   movePile.get(j).setInAce(false);
                                   movePile.get(j).setInPile(false);
                                   pile.get(i).add(movePile.get(j));
                                   pile.get(index1).remove(index2);  //remove from old pile
                              }
                              movePile.clear();  //empty the movepile
                         }
                         if(index1 != 7)   //if the card is not from the pile
                         {
                              pile.get(index1).remove(pile.get(index1).size()-1);   //remove from old pile
                              if (index1 >7)
                                   score += scoringSystem[scoreSystemUsed][4];   //add to score
                         }
                         else
                         {
                              score += scoringSystem[scoreSystemUsed][0];  //change score
                              top.remove(top.size()-1);                   //remove from the deck piles
                              for(int j = 0; j < pile.get(7).size(); j++)
                              {
                                   if (!pile.get(7).get(j).isInPile())
                                        pile.get(7).remove(j);
                              }
                         }
                         
                         placed = true;
                         break;
                    }
                    
                    //placing ace into ace piles
                    else if (pile.get(i).size() == 0 && i > 7 && moved.getNum() == 1 && e.getY() >= 5 &&
                             e.getY() <= 96 && e.getX() >= 404 + 68*(i-8) && e.getX() <= 404 + 68*(i-7))
                    {
                         moved.setPosition(404 + 68*(i-8), 5);
                         moved.setInAce(true);
                         moved.setInPile(false);
                         pile.get(i).add(moved);
                         
                         //if not from the deck pile
                         if (index1 !=7)
                         {
                              pile.get(index1).remove(pile.get(index1).size()-1);
                              if (index1 < 7)
                                   score += scoringSystem[scoreSystemUsed][2];
                         }
                         else
                         {
                              score += scoringSystem[scoreSystemUsed][1];
                              top.remove(top.size()-1);
                              for(int j = 0; j < pile.get(7).size(); j++)
                              {
                                   if (!pile.get(7).get(j).isInPile())
                                        pile.get(7).remove(j);
                              }
                         }
                         
                         placed = true;
                         break;
                    }
                    
                    //placeing cards into ace piles
                    else if (movePile.isEmpty() && pile.get(i).size() > 0 && i > 7 && e.getY() >= 5 &&
                             e.getY() <= 96 && e.getX() >= 404 + 68*(i-8) && e.getX() <= 404 + 68*(i-7) &&
                             moved.getNum() == pile.get(i).get(pile.get(i).size()-1).getNum() + 1 &&
                             pile.get(i).get(pile.get(i).size()-1).getSuit() == moved.getSuit())
                    {
                         moved.setPosition(404 + 68*(i-8), 5);
                         moved.setInAce(true);
                         moved.setInPile(false);
                         pile.get(i).add(moved);
                         if (index1 !=7)
                              pile.get(index1).remove(pile.get(index1).size()-1);
                         
                         else
                         {
                              top.remove(top.size()-1);
                              for(int j = 0; j < pile.get(7).size(); j++)
                              {
                                   if (!pile.get(7).get(j).isInPile())
                                        pile.get(7).remove(j);
                              }
                         }
                         score += scoringSystem[scoreSystemUsed][1];
                         placed = true;
                         break;
                    }
                    
                    //kings into empty piles
                    else if (pile.get(i).size() == 0 && i < 7 && e.getX() >=  68*(i) + 200 && e.getX() <= 
                             68*(i+1) + 200 && e.getY() >= 105 && e.getY() <= 196 && moved.getNum() == 13)
                    {
                         moved.setPosition(68*(i) + 200, 105);
                         moved.setInAce(false);
                         moved.setInPile(false);
                         pile.get(i).add(moved);
                         if (!movePile.isEmpty())
                         {
                              for (int j = 0; j < movePile.size(); j++)
                              {
                                   movePile.get(j).setPosition(pile.get(i).get(pile.get(i).size()-1).getX(),
                                                               pile.get(i).get(pile.get(i).size()-1).getY()+
                                                               20);
                                   movePile.get(j).setInAce(false);
                                   movePile.get(j).setInPile(false);
                                   pile.get(i).add(movePile.get(j));
                                   pile.get(index1).remove(index2);
                              }
                              movePile.clear();
                         }
                         if(index1 != 7)
                         {
                              pile.get(index1).remove(pile.get(index1).size()-1);//remove rest of cards
                              if (index1 > 7)
                                   score += scoringSystem[scoreSystemUsed][4];
                         }
                         else
                         {
                              score += scoringSystem[scoreSystemUsed][1];
                              top.remove(top.size()-1);
                         }
                         for(int j = 0; j < pile.get(7).size(); j++)
                         {
                              if (!pile.get(7).get(j).isInPile())
                                   pile.get(7).remove(j);
                         }
                         placed = true;
                         break;
                    }
               }
               
               //checks to see if a card was placed if not put everythinmg back where it was before
               if (!placed)
               {
                    try
                    {
                         history.pop();  //get rid of adding the last move to the history
                    }
                    catch (Exception exc)
                    {}
                    moved.setPosition(tempX1, tempY1);  //return to correct position
                    for (int i = 0; i < movePile.size(); i++)
                    {
                         movePile.get(i).setPosition(tempX1, tempY1 + 20*(i+1));
                    }
               }
               else
                    numMoves++;   //increment the number of moves by 1
               moved = null;
               
               movePile.clear();  //clear pile
               repaint();    //repaint the board
          }
     }
     
     //method for when the mouse is dragged to move the cards
     public void mouseDragged (MouseEvent e)
     {
          //if the game is dissabled do nothing
          if (!play)
               return;
          
          //if there is a card to move then move it
          if (moved != null)
          {
               moved.setPosition(e.getX(), e.getY());     //set the position of the card to th position of the mouse
               
               //if there is a pile of cards to move then move them also
               if (!movePile.isEmpty())
               {
                    for (int i = 0; i < movePile.size(); i++)
                    {
                         movePile.get(i).setPosition(e.getX(), e.getY() + 20*(i+1));
                    }
               }
               repaint();  //repaint the board
          }
     } 
}