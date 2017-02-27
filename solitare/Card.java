/* Main object of the game and contains all the info for each card
 * 
 */

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.lang.*;

import javax.swing.SpringLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;

public class Card
{
     private char suit;
     private String fileName;
     private boolean isFace = false, inPile = false, inAce = false;
     private Image cardFace, cardBack = new ImageIcon("Cards/cardBack1.png").getImage();
     private int x, y, cardNumber;
     public String cardBackFile;  
     
     //initilizes the cards\
     public Card(int number, char suitIn)
     {
          cardBackFile = "Cards/cardBack1.png";
          cardNumber = number;
          suit = suitIn;
          
          fileName = "Cards/" + number + suit + ".png";
          cardFace = new ImageIcon(fileName).getImage();
     }
     
     //set background from a string
     public void setCardBack(String s)
     {
          cardBack = new ImageIcon(s).getImage();
          cardBackFile = s;
     }
     
     //gets filename back
     public String getCardBackFile()
     {
          return cardBackFile;
     }
     
     //set card back from a image
     public void setCardBack(Image back)
     {
          cardBack = back;
     }
     
     //get card back image
     public Image getCardBack()
     {
          return cardBack;
     }
     
     //setposition
     public void setPosition (int x1, int y1)
     {
          x = x1;
          y = y1;
     }
     
     //set X location of card
     public void setX (int x1)
     {
          x = x1;
     }
     
     //set Y location of card
     public void setY (int y1)
     {
          y = y1;
     }
     
     //get X location of card
     public int getX()
     {
          return x;
     }
     
     //get Y location of card
     public int getY()
     {
          return y;
     }
     
     /*****************************************************
       * posibly not used
       * 
       *******************************************************/
     //get position
     public Point getPosition()
     {
          return new Point(x, y);
     }
     
     //get suit of card
     public char getSuit()
     {
          return suit;
     }
     
     //get card num
     public int getNum()
     {
          return cardNumber;
     }
     
     //get image to display
     public Image getDisplay()
     {
          if (isFace)
               return cardFace;
          else
               return cardBack;
     }
     
     //if showing front or back
     public boolean isShowFace()
     {
          return isFace;
     }
     
     //if in a pile
     public boolean isInPile()
     {
          return inPile;
     }
     
     //if in ace pile
     public boolean isInAce()
     {
          return inAce;
     }
     
     //set in ace pile
     public void setInAce (boolean ace)
     {
          inAce = ace;
          
     }
     
     //set in pile
     public void setInPile(boolean pile)
     {
          inPile = pile;
     }
     
     //set is showing face
     public void showFace (boolean f)
     {
          isFace = f;
     }
     
     
     //debuging info for cards (not used in final version)
     public String toString()
     {
          String str = "X: " + x + " Y: " + y + " CardNum: " + cardNumber + " suit: " + suit;
          return str;
     }
}