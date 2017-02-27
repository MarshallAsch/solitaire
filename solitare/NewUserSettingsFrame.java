/* the frame class for when a new user is being created to select thier settings prefrences
 * 
 */

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class NewUserSettingsFrame extends JFrame
{
     public  NewUserSettingsFrame(Login log)
     {
          add(new NewUserSettingsPanel(this, log));
          setAlwaysOnTop(true);
          setVisible(true);
          pack();
          setResizable(false);
          setSize(500, 500);
          setTitle("Settings");
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          setLocationRelativeTo(null);       //set in center of screen
     }
}