import java.util.HashSet;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

   
public class Panel extends JPanel implements KeyListener
{
   public static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
   public static final int XSIZE = (int) size.getWidth();
   public static final int YSIZE = (int) size.getHeight();
   public static final int LOBBY = 0;
   public static final int EHALL = 1;
   
   public static Character mainPlayer;
   public static final int defaultSpeed = 4;
   public static final int PLAYER_HEIGHT = YSIZE/13;
   public static final int PLAYER_WIDTH = XSIZE/45; 
   
   public static int location;
   public static int frames;
   
   private static Timer t;
   private static HashSet<Integer> pressedKeys;
    
   public Panel()
   {
      t = new Timer(1, new Listener());
      t.start();
      pressedKeys = new HashSet<Integer>();
      location = LOBBY;
      frames = 0;
      mainPlayer = new Character(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, "images/Player.png", "Kaden", 100, defaultSpeed, 1);
   }
   
   public void movePlayer()
   {
      if(pressedKeys.contains(KeyEvent.VK_W)) 
         mainPlayer.moveY(-mainPlayer.getSpeed());
      else if(pressedKeys.contains(KeyEvent.VK_S))
         mainPlayer.moveY(mainPlayer.getSpeed());     
      if(pressedKeys.contains(KeyEvent.VK_A))
         mainPlayer.moveX(-mainPlayer.getSpeed());
      else if(pressedKeys.contains(KeyEvent.VK_D))
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
      else if(location == EHALL)
      {
         ImageIcon pic = new ImageIcon("images/eHall.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      g.drawImage(mainPlayer.getFrame().getImage(), mainPlayer.getX(), mainPlayer.getY(), mainPlayer.getWidth(), mainPlayer.getHeight(), null);
   }      
   
   public void setBoundaries(Character c)
   {
      if(location == LOBBY)
      {
         if(c.getX() <= 0){
            location = EHALL;
            c.setX(XSIZE - c.getWidth()*2);
            c.setY(YSIZE/2);
         }
         if(c.getY() <= 0)
            c.setY(0);
         if(c.getX() >= XSIZE-c.getWidth())
            c.setX(XSIZE-c.getWidth());
         if(c.getY() >= YSIZE-c.getHeight())
            c.setY(YSIZE-c.getHeight());
      }
      if(location == EHALL)
      {
         if(c.getX() <= 0)
            c.setX(0);
         if(c.getY() <= 0)
            c.setY(0);
         if(c.getX() >= XSIZE-c.getWidth()){
            location = LOBBY;
            c.setX(c.getWidth());
            c.setY(YSIZE - ((YSIZE/9)*8));
         }
         if(c.getY() >= YSIZE-c.getHeight())
            c.setY(YSIZE-c.getHeight());
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
         if(frames > 100)
            setBoundaries(mainPlayer);
         repaint();
         frames++;
      }
   }
}