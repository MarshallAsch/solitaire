/* Panel to select all of the setings for the new user when it is created
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

public class NewUserSettingsPanel extends JPanel implements ActionListener, ItemListener, PropertyChangeListener,
     ChangeListener
{
     private JCheckBox option1, option2, option3;
     private JButton revert, apply, defaultSettings, ok, save, acceptUserPass;
     private Image revertBack, backChange;
     private int revertScore, r = 0, g = 140, b = 0, toFlipChange, len;
     private JPanel labels, boxes, sliders, panel1, cardPanel, scorePanel, optionPanel, toFlipPanel, userPanel;
     private JRadioButton card1, card2, card3, card4, card5, card6, system3, system4;
     private ButtonGroup buttonGroup1, buttonGroup2, buttonGroup3;
     private JLabel title1, title2, title3, title4, title5, title6, r1, g1, b1;
     private JFormattedTextField  r2, g2, b2;
     private Color color;
     private JSlider r3, g3, b3;
     private String[] userName, password, colorR, colorG, colorB, toFlip, showTime, showNumMoves;
     
     private JTextField userIn, passIn;
     private String user, pass, cardBackFile = "Cards/cardBack1.png";
     
     private boolean showTimeChange, showScoreChange, showMovesChange;
     private boolean[] revertOptions = new boolean[2];
     private final Image CARD_BACK = new ImageIcon("Cards/cardBack1.png").getImage();
     private final int SCORE_USED = 0;
     private final boolean OPTIONS = true;
     private JFrame frame;
     private NumberFormat numFormat;
     private Login log;
     
     //initilize
     public NewUserSettingsPanel(JFrame f1, Login log)
     {
          this.log = log;
          frame = f1;
          
          initCardBack();
          initOptions();
          initColorSelection();
          initToFlip();
          initUserAndPass();
          
          defaultSettings = new JButton("Default");
          save = new JButton("Save");
          
          panel1  = new JPanel(new GridLayout(1, 0));
          
          //add buttons to panel
          panel1.add(defaultSettings);
          panel1.add(save);
          add(panel1);
          
          //addlisteners to buttons
          defaultSettings.addActionListener(this);
          defaultSettings.setActionCommand("default");
          save.addActionListener(this);
          save.setActionCommand("save");  
     }
     
     //get username and password
     private void initUserAndPass()
     {
          userPanel = new JPanel(new GridLayout(2, 0));
          title5 = new JLabel("User name: ");
          title6 = new JLabel("Password: ");
          userIn = new JTextField(15);
          passIn = new JTextField(15);
          
          JPanel userp1 = new JPanel();
          JPanel userp2 = new JPanel();
          
          //add to panels
          userp1.add(title5);
          userp1.add(userIn);
          userp2.add(title6);
          userp2.add(passIn);
          userPanel.add(userp1);
          userPanel.add(userp2);
          add(userPanel); 
     }
     
     //choose background color
     private void initColorSelection()
     {
          labels = new JPanel(new GridLayout(0, 1));
          boxes = new JPanel(new GridLayout(0, 1));
          sliders = new JPanel(new GridLayout(0, 1));
          
          //titles
          r1 = new JLabel("red");
          g1 = new JLabel("green");
          b1 = new JLabel("blue");
          
          labels.add(r1);
          labels.add(g1);
          labels.add(b1);
          
          
          //number boxes
          r2 = new JFormattedTextField(numFormat);
          g2 = new JFormattedTextField(numFormat);
          b2 = new JFormattedTextField(numFormat);
          
          r2.setValue(r);
          g2.setValue(g);
          b2.setValue(b);
          
          r2.addPropertyChangeListener("value", this);
          g2.addPropertyChangeListener("value", this);    
          b2.addPropertyChangeListener("value", this);    
          
          r2.setColumns(3);
          g2.setColumns(3);
          b2.setColumns(3);
          
          boxes.add(r2);
          boxes.add(g2);
          boxes.add(b2);
          
          //sliders
          r3 = new JSlider(0, 255, r);
          g3 = new JSlider(0, 255, g);
          b3 = new JSlider(0, 255, b);
          
          r3.addChangeListener(this);
          g3.addChangeListener(this);
          b3.addChangeListener(this);
          
          sliders.add(r3);
          sliders.add(g3);
          sliders.add(b3);
          
          //add top panel
          add(labels);
          add(boxes);
          add(sliders);
     }
     
     //change number box values when the slider is moved
     public void stateChanged(ChangeEvent e)
     {
          //get values
          r = r3.getValue();
          g = g3.getValue();
          b = b3.getValue();
          
          //change values
          r2.setValue(r);
          g2.setValue(g);
          b2.setValue(b);
     }
     
     //change slider values when numbner box values change
     public void propertyChange(PropertyChangeEvent e) 
     {
          //get values
          r = ((Number)r2.getValue()).intValue();
          g = ((Number)g2.getValue()).intValue();
          b = ((Number)b2.getValue()).intValue();
          
          //make sure numbers are valid
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
          
          //set valid numbners
          r2.setValue(r);
          g2.setValue(g);
          b2.setValue(b);
          
          //set slider values
          r3.setValue(r);
          g3.setValue(g);
          b3.setValue(b);
          
          //make a colour with those numbers
          color = new Color(r, g, b);
     }
     
     //save setings to file
     private void saveSettings()
     {
          /*
           userName = new String[len];
           password = new String[len];
           colorR = new String[len];
           colorG = new String[len];
           colorB = new String[len];
           toFlip = new String[len];
           showTime = new String[len];
           showNumMoves = new String[len];
           */
          
          //add data to array
          String[] send = {user, pass, Integer.toString(r), Integer.toString(g), Integer.toString(b), Integer.toString(toFlipChange), Boolean.toString(showTimeChange), Boolean.toString(showMovesChange), cardBackFile};
          log.set2(send);        //send to puty into file
     }
     
     //chouse the card back
     private void initCardBack()
     {
          buttonGroup1 = new ButtonGroup();
          
          title1 = new JLabel("Choose a card back");
          add(title1);
          
          //make buttons with all the images of the card backs
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
          
          //add butons to grous so only one can be picked at a time
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
          add(cardPanel, BorderLayout.LINE_START);  //add panel
     }
     
     private void initToFlip()
     {
          buttonGroup3 = new ButtonGroup();
          toFlipPanel = new JPanel(new GridLayout(0, 1));
          title4 = new JLabel("Number to flip");
          add(title4);
          
          //make button choises one card or 3 cards
          system3 = new JRadioButton("Flip 3");
          system3.setActionCommand("flip3");
          system3.addActionListener(this);
          
          system4 = new JRadioButton("Flip 1");
          system4.setActionCommand("flip1");
          system4.addActionListener(this);
          system3.setSelected(true);
          
          //add buttons
          buttonGroup3.add(system3);
          buttonGroup3.add(system4);
          
          toFlipPanel.add(system3);
          toFlipPanel.add(system4);
          add(toFlipPanel);
     }
     
     //set other options that the user has
     private void initOptions()
     {
          title3 = new JLabel("Options");
          optionPanel = new JPanel(new GridLayout(0, 1));
          
          //showing the time
          add(title3);
          option1 = new JCheckBox("Show Time", revertOptions[0]);
          option1.addItemListener(this);
          
          //showing the number of moves made
          option3 = new JCheckBox("Show Number of Moves", revertOptions[1]);
          option3.addItemListener(this);
          
          //add butons to panel
          optionPanel.add(option1);
          optionPanel.add(option3);
          add(optionPanel);
     } 
     
     //when one of the options are changed
     public void itemStateChanged (ItemEvent e)
     {
          Object source = e.getItemSelectable();
          boolean change = true;
          
          //if it is deselected
          if (e.getStateChange() == ItemEvent.DESELECTED)
               change = false;
          
          //get whether it came from button1 or button2
          if (source == option1)
               showTimeChange = change;
          else if (source == option3)
               showMovesChange = change;
     }
     
     //when a button is pressed
     public void actionPerformed(ActionEvent e)
     {
          //if a card back is changes
          if (e.getActionCommand().length() == 9)
          {
               //set file name to the name of the fard selected
               backChange = new ImageIcon("Cards/" + e.getActionCommand() + ".png").getImage();
               cardBackFile = "Cards/" + e.getActionCommand() + ".png";
          }
          else if (e.getActionCommand().equals("ok"))     //if ok was pressed
               frame.dispose();
          else if (e.getActionCommand().equals("flip3"))   //flip options
               toFlipChange = 3;
          else if (e.getActionCommand().equals("flip1"))
               toFlipChange = 1;
          else if (e.getActionCommand().equals("save"))      //save settings
          {
               user = userIn.getText();       //get username and password
               pass = passIn.getText();
               saveSettings();            //save to file
          }
     }
}