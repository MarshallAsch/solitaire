/* panel calss to change the settings in the game
 * 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import javax.swing.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import java.io.*;


public class SettingsPanel extends JPanel implements ActionListener, ItemListener, PropertyChangeListener,
     ChangeListener
{
     private Board board;
     private JCheckBox option1, option2, option3;
     private JButton revert, apply, defaultSettings, ok, save;
     private Image revertBack, backChange;
     private int revertScore, r, g, b, toFlipChange, scoreChange, len;
     private JPanel labels, boxes, sliders, panel1, cardPanel, scorePanel, optionPanel, toFlipPanel;
     private JRadioButton card1, card2, card3, card4, card5, card6, system1, system2, system3, system4;
     private ButtonGroup buttonGroup1, buttonGroup2, buttonGroup3;
     private JLabel title1, title2, title3, title4, r1, g1, b1;
     private JFormattedTextField  r2, g2, b2;
     private Color color;
     private JSlider r3, g3, b3;
     private String[] userName, password, colorR, colorG, colorB, toFlip, showTime, showNumMoves, cardBackFile;
     private String cardBackFileChange;
     private boolean showTimeChange, showScoreChange, showMovesChange;
     private boolean[] revertOptions = new boolean[3];
     private final Image CARD_BACK = new ImageIcon("Cards/cardBack1.png").getImage();
     private final int SCORE_USED = 0;
     private final boolean OPTIONS = true;
     private JFrame frame;
     private NumberFormat numFormat;
     
     public SettingsPanel(Board boardIn, JFrame f1)
     {
          frame = f1;
          board = boardIn;
          r = board.r;
          g = board.g;
          b = board.b;
          
          revertBack = board.deck.get(7).getCardBack();
          
          revertOptions[0] = board.showTime;
          revertOptions[1] = board.showNumMoves;
          
          showTimeChange = board.showTime;
          showMovesChange = board.showNumMoves;
          
          initCardBack();
          initOptions();
          initColorSelection();
          initToFlip();
          
          ok = new JButton("OK");
          revert = new JButton("Revert");
          apply = new JButton("Apply");
          defaultSettings = new JButton("Default");
          save = new JButton("Save");
          
          panel1  = new JPanel(new GridLayout(1, 0));
          
          //add buttons to panel
          panel1.add(ok);
          panel1.add(apply);
          panel1.add(revert);
          panel1.add(defaultSettings);
          panel1.add(save);
          add(panel1);
          
          //add liseners to buttons
          ok.addActionListener(this);
          ok.setActionCommand("ok");
          revert.addActionListener(this);
          revert.setActionCommand("undo");
          apply.addActionListener(this);
          apply.setActionCommand("apply");
          defaultSettings.addActionListener(this);
          defaultSettings.setActionCommand("default");
          save.addActionListener(this);
          save.setActionCommand("save");  
     }
     
     //initilize the colour choice for the background
     private void initColorSelection()
     {
          labels = new JPanel(new GridLayout(0, 1));
          boxes = new JPanel(new GridLayout(0, 1));
          sliders = new JPanel(new GridLayout(0, 1));
          
          //labels for each colour
          r1 = new JLabel("red");
          g1 = new JLabel("green");
          b1 = new JLabel("blue");
          
          labels.add(r1);
          labels.add(g1);
          labels.add(b1);
          
          //make box to enter number
          r2 = new JFormattedTextField(numFormat);
          g2 = new JFormattedTextField(numFormat);
          b2 = new JFormattedTextField(numFormat);
          
          //set value of box to the curent background colour
          r2.setValue(r);
          g2.setValue(g);
          b2.setValue(b);
          
          //add listeners
          r2.addPropertyChangeListener("value", this);
          g2.addPropertyChangeListener("value", this);    
          b2.addPropertyChangeListener("value", this);
          
          r2.setColumns(3);
          g2.setColumns(3);
          b2.setColumns(3);
          
          //add to panel
          boxes.add(r2);
          boxes.add(g2);
          boxes.add(b2);
          
          //make sliders
          r3 = new JSlider(0, 255, r);
          g3 = new JSlider(0, 255, g);
          b3 = new JSlider(0, 255, b);
          
          //add listeners
          r3.addChangeListener(this);
          g3.addChangeListener(this);
          b3.addChangeListener(this);
          
          //add to panel
          sliders.add(r3);
          sliders.add(g3);
          sliders.add(b3);
          
          //add panels to main panel
          add(labels);
          add(boxes);
          add(sliders);
     }
     
     //change value when the slider is moved
     public void stateChanged(ChangeEvent e)
     {
          //get new values
          r = r3.getValue();
          g = g3.getValue();
          b = b3.getValue();
          
          //change value in the box
          r2.setValue(r);
          g2.setValue(g);
          b2.setValue(b);
     }
     
     //if the number in the box is changed
     public void propertyChange(PropertyChangeEvent e) 
     {
          //get values
          r = ((Number)r2.getValue()).intValue();
          g = ((Number)g2.getValue()).intValue();
          b = ((Number)b2.getValue()).intValue();
          
          //validate input to make sure it is in the range
          if (r > 255)
               r = 255;
          if (r < 0)
               r = 0;
          
          if (g > 255)
               g = 255;
          if (g < 0)
               g = 0;
          
          if (b > 255)
               b = 255;
          if (b < 0)
               b = 0;
          
          //set value in box
          r2.setValue(r);
          g2.setValue(g);
          b2.setValue(b);
          
          //set value in slider
          r3.setValue(r);
          g3.setValue(g);
          b3.setValue(b);
          
          //create new colour
          color = new Color(r, g, b);
     }
     
     //undo the change in settings
     private void undo()
     {
          //reset other options
          board.showTime = revertOptions[0];
          board.showNumMoves = revertOptions[1];
          
          //reset the card back
          for (int i = 0; i < 52; i++)
          {
               board.deck.get(i).setCardBack(revertBack);
          }
     }
     
     //return values ti default
     private void setDefault()
     {
          //reset other options and color
          board.showTime = OPTIONS;
          board.showNumMoves = OPTIONS;
          board.toFlip = 3;
          board.r = 0;
          board.g = 140;
          board.b = 0;
          
          //reset card backs
          for (int i = 0; i < 52; i++)
          {
               board.deck.get(i).setCardBack(CARD_BACK);
          }
          frame.dispose();  //close frame
     }
     
     //save settings to file
     private void saveSettings()
     {
          try
          {
               File inputFile = new File("UserSetings.dtd");  //open file
               DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
               DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
               Document doc = dBuilder.parse(inputFile);
               doc.getDocumentElement().normalize();
               
               NodeList nList = doc.getElementsByTagName("user");
               len = nList.getLength();
               userName = new String[len];
               password = new String[len];
               colorR = new String[len];
               colorG = new String[len];
               colorB = new String[len];
               toFlip = new String[len];
               showTime = new String[len];
               showNumMoves = new String[len];
               cardBackFile = new String[len];
               
               //get all setings that are currently in the file into arrays
               for (int i = 0; i < nList.getLength(); i++)
               {
                    Node nNode = nList.item(i);
                    
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                         Element eElement = (Element) nNode;
                         
                         //if not loged in user get settings from file
                         if (Run.x != i)
                         {
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
                         else     //else get from changes
                         {
                              userName[i] = eElement.getElementsByTagName("userName").item(0).getTextContent();
                              password[i] = eElement.getElementsByTagName("password").item(0).getTextContent();
                              colorR[i] = Integer.toString(r);
                              colorG[i] = Integer.toString(g);
                              colorB[i] = Integer.toString(b);
                              showTime[i] = Boolean.toString(showTimeChange);
                              showNumMoves[i] = Boolean.toString(showMovesChange);
                              toFlip[i] = Integer.toString(toFlipChange);
                              cardBackFile[i] = cardBackFileChange;
                         }
                    }
                    
               }
               
          }
          catch(Exception exe){}
          saveSettings2();         //put back into file
     }
     
     //put settings back into file
     private void saveSettings2()
     {
          Element sub, sub1, sub2, sub3, sub4, sub5, sub6, sub7, sub8, sub9;
          
          DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
          try {
               
               DocumentBuilder db = dbf.newDocumentBuilder();
               
               Document dom = db.newDocument();
               
               Element rootEle = dom.createElement("UserSetings");
               
               //put everything back into file
               for (int i = 0; i < len; i++)
               {
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
                    
                    sub6 = dom.createElement("cardBack");
                    sub6.appendChild(dom.createTextNode(cardBackFile[i]));
                    sub.appendChild(sub6);
                    
                    sub7 = dom.createElement("toFlip");
                    sub7.appendChild(dom.createTextNode(toFlip[i]));
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
               }
          }
          catch (Exception e)
          {
               System.out.println("fail");
          }
     }
     
     //apply settings to current game
     private void apply()
     {
          //change colour
          board.r = r;
          board.g = g;
          board.b = b;
          
          //change other stats
          board.toFlip = toFlipChange;
          board.showTime = showTimeChange;
          board.showNumMoves = showMovesChange;
          
          //set card back if it was changed
          if (backChange != null)
          {
               for (int i = 0; i < 52; i++)
               {
                    board.deck.get(i).setCardBack(backChange);
               }
          }
     }
     
     //make buttons to set the card back
     private void initCardBack()
     {
          buttonGroup1 = new ButtonGroup();
          
          title1 = new JLabel("Choose a card back");
          add(title1);
          
          //make buttons, add listeners
          card1 = new JRadioButton(new ImageIcon("Cards/cardBack1.png"));
          card1.setActionCommand("cardBack1");
          card1.addActionListener(this);
          
          card2 = new JRadioButton(new ImageIcon("Cards/cardBack2.png"));
          card2.setActionCommand("cardBack2");
          card2.addActionListener(this);
          
          card3 = new JRadioButton(new ImageIcon("Cards/cardBack3.png"));
          card3.setActionCommand("cardBack3");
          card3.addActionListener(this);
          
          card4 = new JRadioButton(new ImageIcon("Cards/cardBack4.png"));
          card4.setActionCommand("cardBack4");
          card4.addActionListener(this);
          
          card5 = new JRadioButton(new ImageIcon("Cards/cardBack5.png"));
          card5.setActionCommand("cardBack5");
          card5.addActionListener(this);
          
          card6 = new JRadioButton(new ImageIcon("Cards/cardBack6.png"));
          card6.setActionCommand("cardBack6");
          card6.addActionListener(this);
          
          //add to a group so only one can be selected at a time
          buttonGroup1.add(card1);
          buttonGroup1.add(card2);
          buttonGroup1.add(card3);
          buttonGroup1.add(card4);
          buttonGroup1.add(card5);
          buttonGroup1.add(card6);
          
          //add to panel
          cardPanel = new JPanel(new GridLayout(1, 0));
          cardPanel.add(card1);
          cardPanel.add(card2);
          cardPanel.add(card3);
          cardPanel.add(card4);
          cardPanel.add(card5);
          cardPanel.add(card6);
          add(cardPanel, BorderLayout.LINE_START);
     }
     
     
     //initlilize to flip options to select the numer of cards to flip from the pile at a time
     private void initToFlip()
     {
          buttonGroup3 = new ButtonGroup();
          toFlipPanel = new JPanel(new GridLayout(0, 1));
          title4 = new JLabel("Number to flip");
          add(title4);
          
          //make buttons and add listeners
          system3 = new JRadioButton("Flip 3");
          system3.setActionCommand("flip3");
          system3.addActionListener(this);
          
          system4 = new JRadioButton("Flip 1");
          system4.setActionCommand("flip1");
          system4.addActionListener(this);
          
          //show the one that is currently selected as being "active"
          if (board.toFlip == 3)
               system3.setSelected(true);
          else
               system4.setSelected(true);
          
          //add to group so only one can be selected at a time
          buttonGroup3.add(system3);
          buttonGroup3.add(system4);
          
          //add to panel
          toFlipPanel.add(system3);
          toFlipPanel.add(system4);
          add(toFlipPanel);
     }
     
     //initlize other options
     private void initOptions()
     {
          title3 = new JLabel("Options");
          optionPanel = new JPanel(new GridLayout(0, 1));
          
          add(title3);
          option1 = new JCheckBox("Show Time", revertOptions[0]);
          option1.addItemListener(this);
          
          option3 = new JCheckBox("Show Number of Moves", revertOptions[1]);
          option3.addItemListener(this);
          
          //add to panel
          optionPanel.add(option1);
          optionPanel.add(option3);
          add(optionPanel);
     } 
     
     //if one of the other options is changed
     public void itemStateChanged (ItemEvent e)
     {
          Object source = e.getItemSelectable();
          boolean change = true;
          
          //get whether it is selected or deselcted
          if (e.getStateChange() == ItemEvent.DESELECTED)
               change = false;
          
          //get the buton that was changed
          if (source == option1)
               showTimeChange = change;
          else if (source == option3)
               showMovesChange = change;
     }
     
     //if a JButon object was pressed
     public void actionPerformed(ActionEvent e)
     {
          //if it was to change the card back
          if (e.getActionCommand().length() == 9)
          {
               backChange = new ImageIcon("Cards/" + e.getActionCommand() + ".png").getImage();
               cardBackFileChange = "Cards/" + e.getActionCommand() + ".png";
               
          }
          else if (e.getActionCommand().equals("ok"))        //if ok button was pressed
          {
               apply();
               frame.dispose();
          }
          else if (e.getActionCommand().equals("undo"))        //if undo button was pressed
               undo();
          else if (e.getActionCommand().equals("apply"))    //apply
               apply();
          else if (e.getActionCommand().equals("default"))  //default settings
               setDefault();
          else if (e.getActionCommand().equals("flip3"))    //flip 3
               toFlipChange = 3;
          else if (e.getActionCommand().equals("flip1"))    //flip 1
               toFlipChange = 1;
          else if (e.getActionCommand().equals("save"))   //save
               saveSettings();
     }
}