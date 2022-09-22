import java.util.HashSet;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

   
public class panelFormat extends JPanel implements KeyListener
{
   public static final int XSIZE = 1920;
   public static final int YSIZE = 1200;
   public static final int LOBBY = 0;
   
   public static Character mainPlayer;
   public static final int defaultSpeed = 4;
   public static int location;
   public static final int PLAYER_HEIGHT = 72;
   public static final int PLAYER_WIDTH = 27;
   
   private static Timer t;
   private static HashSet<Integer> pressedKeys;
    
   public panelFormat()
   {
      t = new Timer(1, new Listener());
      t.start();
      pressedKeys = new HashSet<Integer>();
      location = LOBBY;
      mainPlayer = new Character(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, "images/Player.png", "Kaden", 100, 4, 1);
      
   }
   
   public void movePlayer()
   {
      
      if(pressedKeys.contains(KeyEvent.VK_W) && mainPlayer.getY() - mainPlayer.getSpeed() >= 0) 
         mainPlayer.moveY(-mainPlayer.getSpeed());
      else if(pressedKeys.contains(KeyEvent.VK_S) && mainPlayer.getY() + PLAYER_HEIGHT  + mainPlayer.getSpeed() <= YSIZE)
         mainPlayer.moveY(mainPlayer.getSpeed());     
      if(pressedKeys.contains(KeyEvent.VK_A) && mainPlayer.getX() - mainPlayer.getSpeed() >= 0)
         mainPlayer.moveX(-mainPlayer.getSpeed());
      else if(pressedKeys.contains(KeyEvent.VK_D) && mainPlayer.getX() + PLAYER_WIDTH + mainPlayer.getSpeed() <= XSIZE)
         mainPlayer.moveX(mainPlayer.getSpeed());
   }
   
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      showBoard(g);
   }
   
   public void showBoard(Graphics g) //displays stuff
   {
      if(location == LOBBY)
      {
         ImageIcon pic = new ImageIcon("images/lobby.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      g.drawImage(mainPlayer.getFrame().getImage(), mainPlayer.getX(), mainPlayer.getY(), mainPlayer.getHeight(), mainPlayer.getWidth(), null);
   }      
   
   public void setBoundaries(Character c)
   {
      if(location == LOBBY)
      {
         if(c.getX() < 0)
            c.setX(0);
         if(c.getY() < 0)
            c.setY(0);
      }
   }
   
   public void keyTyped(KeyEvent e) //methods called when key is typed
   {
      
   }
      
   public void keyPressed(KeyEvent e) //methods called when key is pressed
   {
      if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
         System.exit(1);
      pressedKeys.add(e.getKeyCode());
   }
   
   public void keyReleased(KeyEvent e)
   {
      pressedKeys.remove((Integer)(e.getKeyCode()));
   }
    
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e) //methods called every frame
      {
         movePlayer();
        // setBoundaries(mainPlayer);
         repaint();  
      }
   }
}