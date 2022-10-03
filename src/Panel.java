import java.util.HashSet;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Rectangle;
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
   public static final int LOBBY = 1;
   public static final int EHALL = 2;
   public static final int FHALL = 3;
   
   public static Character mainPlayer;
   public static final int defaultSpeed = 4;
   public static final int PLAYER_HEIGHT = YSIZE/13;
   public static final int PLAYER_WIDTH = XSIZE/45; 
   
   public static int location;
   public static int frames;
   
   private static Timer t;
   private static HashSet<Integer> pressedKeys;
   
   private ArrayList<Rectangle> obstacles = new ArrayList<Rectangle>();
    
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
      if(pressedKeys.contains(KeyEvent.VK_W) && !checkObstacleCollisions(mainPlayer.getX(), mainPlayer.getY()-mainPlayer.getSpeed()) && !checkObstacleCollisions(mainPlayer.getX() + PLAYER_WIDTH, mainPlayer.getY()-mainPlayer.getSpeed()))
         mainPlayer.moveY(-mainPlayer.getSpeed());
         
      else if(pressedKeys.contains(KeyEvent.VK_S) && !checkObstacleCollisions(mainPlayer.getX(), mainPlayer.getY()+mainPlayer.getSpeed() + PLAYER_HEIGHT) && !checkObstacleCollisions(mainPlayer.getX() + PLAYER_WIDTH, mainPlayer.getY()+mainPlayer.getSpeed() + PLAYER_HEIGHT))
         mainPlayer.moveY(mainPlayer.getSpeed()); 
             
      if(pressedKeys.contains(KeyEvent.VK_A) && !checkObstacleCollisions(mainPlayer.getX()-mainPlayer.getSpeed(), mainPlayer.getY()) && !checkObstacleCollisions(mainPlayer.getX()-mainPlayer.getSpeed(), mainPlayer.getY() + PLAYER_HEIGHT))
         mainPlayer.moveX(-mainPlayer.getSpeed());
         
      else if(pressedKeys.contains(KeyEvent.VK_D)&& !checkObstacleCollisions(mainPlayer.getX()+mainPlayer.getSpeed() + PLAYER_WIDTH, mainPlayer.getY()) && !checkObstacleCollisions(mainPlayer.getX()+mainPlayer.getSpeed() + PLAYER_WIDTH, mainPlayer.getY() + PLAYER_HEIGHT))
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
         if(obstacles == null){
            Color myColor = new Color(0, 0, 0, 0);
            g.setColor(myColor);
            for(Rectangle r:obstacles)
               g.fillRect((int)(r.getX()), (int)(r.getY()), (int)(r.getWidth()), (int)(r.getHeight()));
         }
      }
      else if(location == EHALL)
      {
         ImageIcon pic = new ImageIcon("images/eHall.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
         if(obstacles == null){
            Color myColor = new Color(0, 0, 0, 0);
            g.setColor(myColor);
            for(Rectangle r:obstacles)
               g.fillRect((int)(r.getX()), (int)(r.getY()), (int)(r.getWidth()), (int)(r.getHeight()));
         }
      }
      else if(location == FHALL)
      {
         ImageIcon pic = new ImageIcon("images/fHall.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
        if(obstacles == null){
            Color myColor = new Color(0, 0, 0, 0);
            g.setColor(myColor);
            for(Rectangle r:obstacles)
               g.fillRect((int)(r.getX()), (int)(r.getY()), (int)(r.getWidth()), (int)(r.getHeight()));
         }
         
      }
      g.drawImage(mainPlayer.getFrame().getImage(), mainPlayer.getX(), mainPlayer.getY(), mainPlayer.getWidth(), mainPlayer.getHeight(), null);
   }      
   
   public void setWalls()
   {
      for(int i=0; i<obstacles.size(); i++)
         obstacles.remove(i);
      if(location == LOBBY)
      {
         obstacles.add(new Rectangle(0, YSIZE/8*2, XSIZE/64*17, YSIZE));
      }
      else if(location == EHALL)
      {
         obstacles.add(new Rectangle(0, 0, XSIZE, YSIZE/5));
         obstacles.add(new Rectangle(0, YSIZE/40*29, XSIZE, YSIZE/3));
      }
      else if(location == FHALL)
      {
         obstacles.add(new Rectangle(0, 0, XSIZE/128*35, YSIZE));
         obstacles.add(new Rectangle(XSIZE/64*53, 0, XSIZE, YSIZE/5));
         obstacles.add(new Rectangle(XSIZE/64*53, YSIZE/36*13, XSIZE, YSIZE/12*5));
      }
   }
   
   public void setBoundaries(Character c)
   {
      setWalls();
      if(location == LOBBY)
      {
         if(c.getX() <= 0){
            location = EHALL;
            c.setX(XSIZE - c.getWidth()*2);
            c.setY(YSIZE/2);
         }
         if(c.getX() >= XSIZE/4 && c.getX() <= XSIZE/4*3 && c.getY() <=0){
            location = FHALL;
            c.setX(XSIZE/2);
            c.setY(YSIZE-PLAYER_HEIGHT);
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
      if(location == FHALL)
      {
         if(c.getY() > YSIZE-PLAYER_HEIGHT){
            location = LOBBY;
            c.setY(0);
         }
         if(c.getY() <= 0)
            c.setY(0);
         
      }
   }
   
   
   public static double distance(int x1, int y1, int x2, int y2){
      return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
   }
   
   public boolean checkObstacleCollisions(int x, int y){
      for(Rectangle r: obstacles){
         if(r.contains(x, y))
            return true;
      }
      return false;
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
