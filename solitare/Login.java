/* Frame and panels where user can log in at the begining of the game to use the desired
 * look and feel settings
 * 
 */

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import java.io.*;

public class Login extends JFrame implements WindowListener
{
     private Board board;
     private int x = -1, len = 0;
     private String[] userName, password, colorR, colorG, colorB, toFlip, showTime, showNumMoves, cardBackFile;
     
     public Login(Board board)
     {
          this.board = board;
          JPanel panel = new JPanel();
          setAlwaysOnTop(true);
          addWindowListener(this);
          
          JLabel label1 = new JLabel("userName:"), label2 = new JLabel("Password:");
          JTextField userName = new JTextField(15);
          JPasswordField password = new JPasswordField(15);
          JButton ok = new JButton("OK"), cancel = new JButton("cancel"), newUser = new JButton("create Account");
          
          ok.addActionListener(new ActionListener() {
               public void actionPerformed (ActionEvent actionEvent){
                    String  user = userName.getText();
                    String  pass = password.getText();  //try to log in
                    //rebuild(user, pass);
                    login(user, pass);
                    board.settings(x);
                    Run.x = x;
                    dispose();
               }});
          
          cancel.addActionListener(new ActionListener() {
               public void actionPerformed (ActionEvent actionEvent){   //cancel login
                    dispose();  
               }});
          
          newUser.addActionListener(new ActionListener() {
               public void actionPerformed (ActionEvent actionEvent){  //create new user
                    createNewUser();
               }});
          
          //add to panel to be displayed
          panel.add(label1);
          panel.add(userName);
          panel.add(label2);
          panel.add(password);  
          panel.add(ok);
          panel.add(cancel);
          panel.add(newUser);
          add(panel);
          
          setSize(330, 125);
          setPreferredSize(new Dimension(330, 125));
          setResizable(false);
          pack();
          setTitle("Login");
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          setLocationRelativeTo(null); 
     }
     
     //check login
     private void login(String user, String pass)
     {
          //if user and pass is admin open control window
          try
          {
               File inputFile = new File("UserSetings.dtd");   //open file
               DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
               DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
               Document doc = dBuilder.parse(inputFile);
               doc.getDocumentElement().normalize();
               
               NodeList nList = doc.getElementsByTagName("user");
               String s1, s2;
               for (int i = 0; i < nList.getLength(); i++)
               {
                    Node nNode = nList.item(i);
                    
                    //check if username and password are in file
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                         Element eElement = (Element) nNode;
                         s1 = eElement.getElementsByTagName("userName").item(0).getTextContent();
                         s2 = eElement.getElementsByTagName("password").item(0).getTextContent();
                         
                         if (s1.equals(user) && s2.equals(pass))
                         {
                              x = i;  //index of logedin user to be used when initilizing the 
                              //settings on the board
                              return;
                         }
                    }
               }
          }
          catch(Exception exe){}
          x = -1;         //otherwise do default -1 if user not found
     }
     
     //rebuld file if messed up (not used in final version)
     private void rebuild(String user, String pass)
     {
          Document dom;
          Element sub, sub1, sub2, sub3, sub4, sub5, sub6, sub7, sub8, sub9;
          int pileNum = 0, pileIndex = 0;
          String str;
          
          DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
          try {
               
               DocumentBuilder db = dbf.newDocumentBuilder();
               
               dom = db.newDocument();
               
               Element rootEle = dom.createElement("UserSetings");
               
               for (int i = 0; i <5; i++)  //remake 5 users (identical)
               {
                    //add info to each user
                    str = "user";
                    sub = dom.createElement(str);
                    sub1 = dom.createElement("userName");
                    sub1.appendChild(dom.createTextNode(user));
                    sub.appendChild(sub1);
                    
                    sub2 = dom.createElement("password");
                    sub2.appendChild(dom.createTextNode(pass));
                    sub.appendChild(sub2);
                    
                    sub3 = dom.createElement("colorR");
                    sub3.appendChild(dom.createTextNode(Integer.toString(board.r)));
                    sub.appendChild(sub3);
                    
                    sub8 = dom.createElement("colorG");
                    sub8.appendChild(dom.createTextNode(Integer.toString(board.g)));
                    sub.appendChild(sub8);
                    
                    sub9 = dom.createElement("colorB");
                    sub9.appendChild(dom.createTextNode(Integer.toString(board.b)));
                    sub.appendChild(sub9);
                    
                    sub4 = dom.createElement("showTime");
                    sub4.appendChild(dom.createTextNode(Boolean.toString(board.showTime)));
                    sub.appendChild(sub4);
                    
                    sub5 = dom.createElement("showNumMoves");
                    sub5.appendChild(dom.createTextNode(Boolean.toString(board.showNumMoves)));
                    sub.appendChild(sub5);
                    
                    sub6 = dom.createElement("cardBack");
                    sub6.appendChild(dom.createTextNode("Cards/cardBack1.png"));
                    sub.appendChild(sub6);
                    
                    sub7 = dom.createElement("toFlip");
                    sub7.appendChild(dom.createTextNode(Integer.toString(board.toFlip)));
                    sub.appendChild(sub7);
                    
                    rootEle.appendChild(sub);     
               }
               
               dom.appendChild(rootEle);
               try {
                    Transformer tr = TransformerFactory.newInstance().newTransformer();
                    tr.setOutputProperty(OutputKeys.INDENT, "yes");
                    tr.setOutputProperty(OutputKeys.METHOD, "xml");
                    tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                    
                    // send DOM to file
                    tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream("UserSetings.dtd")));
               }
               catch (Exception e)
               {
                    System.out.println("fail1");
                    //if error occured in converting file
               }
          }
          catch (Exception e)
          {
               System.out.println("fail");
               //if error occured in making th file ot filling it
          }
     }
     
     //create new user
     private void createNewUser()
     {
          try
          {
               File inputFile = new File("UserSetings.dtd");  //open file to get all old users
               DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
               DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
               Document doc = dBuilder.parse(inputFile);
               doc.getDocumentElement().normalize();
               
               //initilize arrays to hold info
               NodeList nList = doc.getElementsByTagName("user");
               len = nList.getLength() +1;
               userName = new String[len];
               password = new String[len];
               colorR = new String[len];
               colorG = new String[len];
               colorB = new String[len];
               toFlip = new String[len];
               showTime = new String[len];
               showNumMoves = new String[len];
               cardBackFile = new String[len];
               len --;
               
               for (int i = 0; i < len; i++)
               {
                    Node nNode = nList.item(i);
                    
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                         Element eElement = (Element) nNode;
                         
                         //get values
                         userName[i] = eElement.getElementsByTagName("userName").item(0).getTextContent();
                         password[i] = eElement.getElementsByTagName("password").item(0).getTextContent();
                         colorR[i] = eElement.getElementsByTagName("colorR").item(0).getTextContent();
                         colorG[i] = eElement.getElementsByTagName("colorG").item(0).getTextContent();
                         colorB[i] = eElement.getElementsByTagName("colorB").item(0).getTextContent();
                         showTime[i] = eElement.getElementsByTagName("showTime").item(0).getTextContent();
                         showNumMoves[i] = eElement.getElementsByTagName("showNumMoves").item(0).getTextContent();
                         toFlip[i] = eElement.getElementsByTagName("toFlip").item(0).getTextContent();  
                         cardBackFile[i] = eElement.getElementsByTagName("cardBack").item(0).getTextContent();
                    }
               }
               
               //get from popup
               NewUserSettingsFrame ex = new NewUserSettingsFrame(this);  //open popup to make new user
          }catch (Exception e){}
     }
     
     //called from new user to put everything back into the file
     public void set2(String[] recieve)
     {
          try{
               //add new info to the array
               userName[len] = recieve[0];
               password[len] = recieve[1];
               colorR[len] = recieve[2];
               colorG[len] = recieve[3];
               colorB[len] = recieve[4];
               showTime[len] = recieve[6];
               showNumMoves[len] = recieve[7];
               toFlip[len] = recieve[5];
               cardBackFile[len] = recieve[8];
               
               Element sub, sub1, sub2, sub3, sub4, sub5, sub6, sub7, sub8, sub9;
               
               DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
               
               DocumentBuilder db = dbf.newDocumentBuilder();
               
               Document dom = db.newDocument();
               
               Element rootEle = dom.createElement("UserSetings");
               
               for (int i = 0; i < len+1; i++)
               {
                    //put all info back into file
                    sub = dom.createElement("user");
                    sub1 = dom.createElement("userName");
                    sub1.appendChild(dom.createTextNode(userName[i]));
                    sub.appendChild(sub1);
                    
                    sub2 = dom.createElement("password");
                    sub2.appendChild(dom.createTextNode(password[i]));
                    sub.appendChild(sub2);
                    
                    sub3 = dom.createElement("colorR");
                    sub3.appendChild(dom.createTextNode(colorR[i]));
                    sub.appendChild(sub3);
                    
                    sub8 = dom.createElement("colorG");
                    sub8.appendChild(dom.createTextNode(colorG[i]));
                    sub.appendChild(sub8);
                    
                    sub9 = dom.createElement("colorB");
                    sub9.appendChild(dom.createTextNode(colorB[i]));
                    sub.appendChild(sub9);
                    
                    sub4 = dom.createElement("showTime");
                    sub4.appendChild(dom.createTextNode(showTime[i]));
                    sub.appendChild(sub4);
                    
                    sub5 = dom.createElement("showNumMoves");
                    sub5.appendChild(dom.createTextNode(showNumMoves[i]));
                    sub.appendChild(sub5);
                    
                    sub7 = dom.createElement("toFlip");
                    sub7.appendChild(dom.createTextNode(toFlip[i]));
                    sub.appendChild(sub7);
                    sub6 = dom.createElement("cardBack");
                    sub6.appendChild(dom.createTextNode(cardBackFile[i]));
                    sub.appendChild(sub6);
                    
                    rootEle.appendChild(sub);
               }
               dom.appendChild(rootEle);                       
               try {
                    Transformer tr = TransformerFactory.newInstance().newTransformer();
                    tr.setOutputProperty(OutputKeys.INDENT, "yes");
                    tr.setOutputProperty(OutputKeys.METHOD, "xml");
                    tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                    
                    // send DOM to file
                    tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream("UserSetings.dtd")));
               }
               catch (Exception e)
               {
                    System.out.println("fail1");
               }
          }
          catch (Exception e)
          {
               System.out.println("fail");
          }
          Run.x = len;
          x = len;  //log in the new user 
          dispose();
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
     
     //reactivate the game window when closed
     public void windowClosed(WindowEvent e)
     {
          board.play = true;
          Window[] win = getWindows();
          for (int i = 0; i < win.length; i++)
          {
               if (win[i].getClass().getName().equals("Application"))
               {
                    win[i].setEnabled(true); //set any application class to enabled
               }
          }
     }
     
     //deactivate game when opened
     public void windowOpened(WindowEvent e)
     {
          board.play = false;
     }
}