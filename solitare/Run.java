/* 
 * Developed by Marshall Asch with the consoltance of Pac Wong, Micheal Le, Caroline White, Darren Huynh
 * and Igal Bord
 * 
 */

//3432 lines
import java.util.*;

public class Run
{
     public static int x = -1;  //user that is logged in
     private static Application window = null;
     
     //main method of the application
     public static void main (String [] arg)
     {
          newApplication();
     }
     
     //create new window gets rid of the old one
     public static void newApplication()
     {
          if (window != null)
          {
               window.dispose();
               window = null;
          }
          window = new Application();
          window.setVisible(true);
     }
     
     //creates new game with same deck
     public static void newApplication(ArrayList<Card> deck)
     {
          if (window != null)
          {
               window.dispose();
               window = null;
          }
          window = new Application(deck);
          window.setVisible(true);
     }
     
     
     /*
      * hardcode changing the size of the image files (only used initaly, not in final version)
      */
     public static void resize()
     {
          String str, value;
          int num = 1, change = 0;
          char suit = 'h';
          
          for (int i = 0; i < 52; i++) //cycle through all card face files
          {
               value = Integer.toString(num);
               str = "sips -z 91 63 Cards/" + value + suit +".png"; //name of the file with comand
               try{
                    Runtime.getRuntime().exec(str); //exicute comind line comand
               }
               catch (Exception e){}
               num ++;
               
               if (num == 14) //make it cycle through the card numbers
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
     }
}