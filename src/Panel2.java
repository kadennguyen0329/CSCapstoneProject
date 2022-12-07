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

   
public class Panel2 extends JPanel implements KeyListener
{
   public static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
   public static final int XSIZE = (int) size.getWidth();
   public static final int YSIZE = (int) size.getHeight();
   public static final int LOBBY = 1;
   public static final int EHALL = 2;
   public static final int FHALL = 3;
   public static final int CAFEA = 4;
   public static final int CAFEB = 5;
   public static final int AHALL1 = 6;
   public static final int AHALL2 = 7;
   public static final int BHALL = 8;
   public static final int CHALL1 = 9;
   public static final int CHALL2 = 10;
   public static final int DHALL = 11;
   public static final int FHALL2 = 12;
   public static final int GHALL = 13;
   public static final int KHALL = 14;
   public static final int LHALL = 15;
   public static final int END = 16;
   
   public static Character mainPlayer;
   public static final int defaultSpeed = (int)(XSIZE*(0.5/120));
   public static final int enemySpeed = defaultSpeed/5;
   public static final int PLAYER_HEIGHT = YSIZE/13;
   public static final int PLAYER_WIDTH = XSIZE/45; 
   public static final Color obstacleColor = new Color(10, 10, 10, 200);
   private static int hallMonitorStage;
   private static boolean hasMovedHallMonitor;
   
   public static int location;
   public static int frames;
   
   private static Timer t;
   private static HashSet<Integer> pressedKeys;
   
   private ArrayList<Rectangle> obstacles = new ArrayList<Rectangle>();
   private ArrayList<Character> enemies = new ArrayList<Character>();
    
   public Panel2()
   {
      t = new Timer(1, new Listener());
      t.start();
      pressedKeys = new HashSet<Integer>();
      location = LOBBY;
      Sound.initialize();
      frames = 0;
      hallMonitorStage = 0;
      hasMovedHallMonitor = false;
      mainPlayer = new Player(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, "images/Player.png", 100, defaultSpeed, 1, "Kaden");
      enemies.add(new Enemy((int)(XSIZE*(37.0/120)), (int)(YSIZE*(8.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, "images/Enemy.png", 100, enemySpeed, 1, "Hall Monitor"));
   }
   
   public void resetHallMonitor() {hallMonitorStage = 0; hasMovedHallMonitor = false;}
   
   public void hallMonitorMovement()
   {
      if(location == LOBBY && enemies.size() != 0)
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(37.0/120)));
            enemies.get(0).setY((int)(YSIZE*(15.0/75)));
            hasMovedHallMonitor = true;
         }
         if(enemies.get(0).getX() < mainPlayer.getX())
            enemies.get(0).moveX(enemies.get(0).getSpeed());
         if(enemies.get(0).getX() > mainPlayer.getX())
            enemies.get(0).moveX(-enemies.get(0).getSpeed());
         if(enemies.get(0).getY() > mainPlayer.getY())
            enemies.get(0).moveY(-enemies.get(0).getSpeed());
         if(enemies.get(0).getY() < mainPlayer.getY())
            enemies.get(0).moveY(enemies.get(0).getSpeed());  
      }
      else if(location == EHALL && enemies.size() != 0)
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(8.0/120)));
            enemies.get(0).setY((int)(YSIZE*(32.0/75)));
            hasMovedHallMonitor = true;
         }
         if(enemies.get(0).getX() < mainPlayer.getX())
            enemies.get(0).moveX(enemies.get(0).getSpeed());
         if(enemies.get(0).getX() > mainPlayer.getX())
            enemies.get(0).moveX(-enemies.get(0).getSpeed());
         if(enemies.get(0).getY() > mainPlayer.getY())
            enemies.get(0).moveY(-enemies.get(0).getSpeed());
         if(enemies.get(0).getY() < mainPlayer.getY())
            enemies.get(0).moveY(enemies.get(0).getSpeed()); 
      }
      if(location == FHALL && enemies.size() != 0)
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(37.0/120)));
            enemies.get(0).setY((int)(YSIZE*(8.0/75)));
            hasMovedHallMonitor = true;
         }
         if(enemies.get(0).getX() < mainPlayer.getX())
            enemies.get(0).moveX(enemies.get(0).getSpeed());
         if(enemies.get(0).getX() > mainPlayer.getX())
            enemies.get(0).moveX(-enemies.get(0).getSpeed());
         if(enemies.get(0).getY() > mainPlayer.getY())
            enemies.get(0).moveY(-enemies.get(0).getSpeed());
         if(enemies.get(0).getY() < mainPlayer.getY())
            enemies.get(0).moveY(enemies.get(0).getSpeed());  
      }
      if(location == CAFEB || location == CAFEA)
      {
         enemies.get(0).setX(-100);
         enemies.get(0).setY(-100);
         hasMovedHallMonitor = true;
      }
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
      
      
      if(checkEnemyCollisions())
         for(int i=0; i<enemies.size(); i++){
            enemies.remove(i);
            mainPlayer.setX(XSIZE/2);
            mainPlayer.setY(YSIZE/2);
            location = END;
            Sound.randomNote();
         }
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
      else if(location == FHALL)
      {
         ImageIcon pic = new ImageIcon("images/fHall.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == CAFEA)
      {
         ImageIcon pic = new ImageIcon("images/cafeA.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == CAFEB)
      {
         ImageIcon pic = new ImageIcon("images/cafeB.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == END)
      {
         ImageIcon pic = new ImageIcon("images/End.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      //seeObstacles(g);
      g.drawImage(mainPlayer.getFrame().getImage(), mainPlayer.getX(), mainPlayer.getY(), mainPlayer.getWidth(), mainPlayer.getHeight(), null);
      for(Character enemy: enemies){
         g.drawImage(enemy.getFrame().getImage(), enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight(), null);
      }
   }      
   
   private void seeObstacles(Graphics g)
   {
      g.setColor(obstacleColor);
      for(Rectangle r:obstacles)
         g.fillRect((int)(r.getX()), (int)(r.getY()), (int)(r.getWidth()), (int)(r.getHeight()));
   }
   
   public void setWalls()
   {
      for(int i=0; i<obstacles.size(); i++)
         obstacles.remove(i);
      if(location == LOBBY)
      {
         obstacles.add(new Rectangle(0, (int)(YSIZE*(19.0/75)), (int)(XSIZE*(33.0/120)), YSIZE-((int)(YSIZE*(19.0/75)))));
         obstacles.add(new Rectangle((int)(XSIZE*(101.0/120)), (int)(YSIZE*(17.0/75)), XSIZE-((int)(XSIZE*(101.0/120))), YSIZE-((int)(YSIZE*(17.0/75)))));
      }
      else if(location == EHALL)
      {
         obstacles.add(new Rectangle(0, 0, XSIZE, (int)(YSIZE*(15.0/75))));
         obstacles.add(new Rectangle(0, (int)(YSIZE*(54.0/75)), XSIZE, YSIZE-(int)(YSIZE*(54.0/75))));
      }
      else if(location == FHALL)
      {
         obstacles.add(new Rectangle(0, 0, (int)(XSIZE*(33.0/120)), YSIZE));
         obstacles.add(new Rectangle((int)(XSIZE*(97.0/120)), 0, XSIZE-(int)(XSIZE*(97.0/120)), (int)(YSIZE*(16.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(99.0/120)), (int)(YSIZE*(27.0/75)), XSIZE-(int)(XSIZE*(99.0/120)), (int)(YSIZE*(32.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(100.0/120)), (int)(YSIZE*(70.0/75)), XSIZE-(int)(XSIZE*(100.0/120)), YSIZE-(int)(YSIZE*(70.0/75))));
      }
      else if(location == CAFEA)
      {
         obstacles.add(new Rectangle(0, 0, (int)(XSIZE*(5.0/120)), (int)(YSIZE*(31.0/75))));
         obstacles.add(new Rectangle(0, (int)(YSIZE*(47.0/75)), (int)(XSIZE*(5.0/120)), YSIZE-(int)(YSIZE*(47.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(27.0/120)), (int)(YSIZE*(13.0/75)), (int)(XSIZE*(13.0/120)), (int)(YSIZE*(7.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(61.0/120)), (int)(YSIZE*(13.0/75)), (int)(XSIZE*(13.0/120)), (int)(YSIZE*(7.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(92.0/120)), (int)(YSIZE*(13.0/75)), (int)(XSIZE*(14.0/120)), (int)(YSIZE*(7.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(27.0/120)), (int)(YSIZE*(57.0/75)), (int)(XSIZE*(14.0/120)), (int)(YSIZE*(8.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(59.0/120)), (int)(YSIZE*(57.0/75)), (int)(XSIZE*(14.0/120)), (int)(YSIZE*(7.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(93.0/120)), (int)(YSIZE*(57.0/75)), (int)(XSIZE*(13.0/120)), (int)(YSIZE*(7.0/75))));
      }
      else if(location == CAFEB)
      {
         obstacles.add(new Rectangle(0, 0, (int)(XSIZE*(4.0/120)), (int)(YSIZE*(30.0/75))));
         obstacles.add(new Rectangle(0, (int)(YSIZE*(47.0/75)), (int)(XSIZE*(5.0/120)), YSIZE-(int)(YSIZE*(47.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(18.0/120)), (int)(YSIZE*(10.0/75)), (int)(XSIZE*(32.0/120)), (int)(YSIZE*(8.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(75.0/120)), (int)(YSIZE*(10.0/75)), (int)(XSIZE*(35.0/120)), (int)(YSIZE*(8.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(18.0/120)), (int)(YSIZE*(56.0/75)), (int)(XSIZE*(35.0/120)), (int)(YSIZE*(8.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(78.0/120)), (int)(YSIZE*(55.0/75)), (int)(XSIZE*(32.0/120)), (int)(YSIZE*(8.0/75))));
      }
      else if(location == AHALL1)
      {
         obstacles.add(new Rectangle(0, 0, (int)(XSIZE*(18.0/120)), (int)(YSIZE*(10.0/75))));
         obstacles.add(new Rectangle(0, (int)(YSIZE*(21.0/75)), (int)(XSIZE*(19.0/120)), (int)(YSIZE*(33.0/75))));
         obstacles.add(new Rectangle(0, (int)(YSIZE*(66.0/75)), (int)(XSIZE*(17.0/120)), (int)(YSIZE*(11.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(103.0/120)), 0, (int)(XSIZE*(17.0/120)), (int)(YSIZE*(5.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(103.0/120)), (int)(YSIZE*(17.0/75)), (int)(XSIZE*(17.0/120)), (int)(YSIZE*(35.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(105.0/120)), (int)(YSIZE*(66.0/75)), (int)(XSIZE*(15.0/120)), (int)(YSIZE*(11.0/75))));
      }
      else if(location == AHALL2)
      {
         obstacles.add(new Rectangle(0, 0, (int)(XSIZE), (int)(YSIZE*(12.0/75))));
         obstacles.add(new Rectangle(0, (int)(YSIZE*(25.0/75)), (int)(XSIZE*(16.0/120)), (int)(YSIZE*(50.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(104.0/120)), (int)(YSIZE*(26.0/75)), (int)(XSIZE*(16.0/120)), (int)(YSIZE*(49.0/75))));
      }
      else if(location == BHALL)
      {
         obstacles.add(new Rectangle(0, 0, XSIZE, (int)(YSIZE*(15.0/75))));
         obstacles.add(new Rectangle(0, (int)(YSIZE*(54.0/75)), XSIZE, YSIZE-(int)(YSIZE*(54.0/75))));
      }
      else if(location == CHALL1)
      {
         obstacles.add(new Rectangle(0, 0, (int)(XSIZE*(55.0/120)), (int)(YSIZE)));
         obstacles.add(new Rectangle((int)(XSIZE*(55.0/120)), (int)(YSIZE*(66.0/75)), (int)(XSIZE*(65.0/120)), (int)(YSIZE*(11.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(101/120)), 0, (int)(XSIZE*(19.0/120)), (int)(YSIZE*(49.0/75))));
      }
      //Nathan: aHall1, aHall2, bHall,cHall1, cHall2
      //Kaden: dHall, fHall2, gHall, kHall, lHall
      
      else if(location == END)
      {
         obstacles.add(new Rectangle((int)(XSIZE*(6.0/120)), (int)(YSIZE*(61.0/75)), (int)(XSIZE*(33.0/120)), (int)(YSIZE*(10.0/75))));
         obstacles.add(new Rectangle((int)(XSIZE*(78.0/120)), (int)(YSIZE*(59.0/75)), (int)(XSIZE*(27.0/120)), (int)(YSIZE*(12.0/75))));
         
         if(checkObstacleCollisions(mainPlayer.getX(), mainPlayer.getY()-mainPlayer.getSpeed()) && checkObstacleCollisions(mainPlayer.getX() + PLAYER_WIDTH, mainPlayer.getY()-mainPlayer.getSpeed())){
            
            if(mainPlayer.getX()<XSIZE/2){
               location = LOBBY;
               frames = 0;
               mainPlayer = new Player(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, "images/Player.png", 100, defaultSpeed, 1, "Kaden");
               enemies.add(new Enemy((int)(XSIZE*(37.0/120)), (int)(XSIZE*(8.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, "images/Enemy.png", 100, enemySpeed, 1, "Hall Monitor"));
               Sound.silence();
            }
            else
               System.exit(1);
         }
         else if(checkObstacleCollisions(mainPlayer.getX(), mainPlayer.getY()+mainPlayer.getSpeed() + PLAYER_HEIGHT) && checkObstacleCollisions(mainPlayer.getX() + PLAYER_WIDTH, mainPlayer.getY()+mainPlayer.getSpeed() + PLAYER_HEIGHT)){
            if(mainPlayer.getX()<XSIZE/2){
               location = LOBBY;
               frames = 0;
               mainPlayer = new Player(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, "images/Player.png", 100, defaultSpeed, 1, "Kaden");
               enemies.add(new Enemy((int)(XSIZE*(37.0/120)), (int)(XSIZE*(8.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, "images/Enemy.png", 100, enemySpeed, 1, "Hall Monitor"));           
               Sound.silence();
            }
            else
               System.exit(1);
         }
         if(checkObstacleCollisions(mainPlayer.getX()-mainPlayer.getSpeed(), mainPlayer.getY()) && checkObstacleCollisions(mainPlayer.getX()-mainPlayer.getSpeed(), mainPlayer.getY() + PLAYER_HEIGHT)){
            if(mainPlayer.getX()<XSIZE/2){
               location = LOBBY;
               frames = 0;
               mainPlayer = new Player(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, "images/Player.png", 100, defaultSpeed, 1, "Kaden");
               enemies.add(new Enemy((int)(XSIZE*(37.0/120)), (int)(XSIZE*(8.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, "images/Enemy.png", 100, enemySpeed, 1, "Hall Monitor")); 
               Sound.silence();
            }
            else
               System.exit(1);
         }
         else if(checkObstacleCollisions(mainPlayer.getX()+mainPlayer.getSpeed() + PLAYER_WIDTH, mainPlayer.getY()) && checkObstacleCollisions(mainPlayer.getX()+mainPlayer.getSpeed() + PLAYER_WIDTH, mainPlayer.getY() + PLAYER_HEIGHT)){
            if(mainPlayer.getX()<XSIZE/2){
               location = LOBBY;
               frames = 0;
               mainPlayer = new Player(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, "images/Player.png", 100, defaultSpeed, 1, "Kaden");
               enemies.add(new Enemy((int)(XSIZE*(37.0/120)), (int)(XSIZE*(8.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, "images/Enemy.png", 100, enemySpeed, 1, "Hall Monitor"));     
               Sound.silence();
            }
            else
               System.exit(1);
           
         }
      }
   }
   
   public void setBoundaries(Character c)
   {
      if(c != null)
      {
         setWalls();
         if(location == LOBBY)
         {
            if(c.getX() <= 0){
               location = EHALL;
               resetHallMonitor();
               c.setX(XSIZE - c.getWidth()*2);
               c.setY(YSIZE/2);
            }
            if(c.getX() >= XSIZE/4 && c.getX() <= XSIZE/4*3 && c.getY() <=0){
               location = FHALL;
               resetHallMonitor();
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
               resetHallMonitor();
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
               resetHallMonitor();
               c.setY(0);
            }
            if(c.getX() > XSIZE+PLAYER_WIDTH && c.getY() > YSIZE/2){
               location = CAFEA;
               resetHallMonitor();
               c.setY(YSIZE/2);
               c.setX(0);
            }
            if(c.getX() > XSIZE+PLAYER_WIDTH && c.getY() < YSIZE/2){
               location = CAFEB;
               resetHallMonitor();
               c.setY(YSIZE/2);
               c.setX(0);
            }
            if(c.getY() <= 0)
               c.setY(0);
         }
         if(location == CAFEA)
         {
            if(c.getX() < 0){
               location = FHALL;
               resetHallMonitor();
               c.setY((int)(YSIZE*(64.0/75)));
               c.setX(XSIZE-PLAYER_WIDTH);
            }
            if(c.getY() <= 0)
               c.setY(0);
            if(c.getX() >= XSIZE-c.getWidth())
               c.setX(XSIZE-c.getWidth());
            if(c.getY() >= YSIZE-c.getHeight())
               c.setY(YSIZE-c.getHeight());
         }
         if(location == CAFEB)
         {
            if(c.getX() < 0){
               location = FHALL;
               resetHallMonitor();
               c.setY((int)(YSIZE*(20.0/75)));
               c.setX(XSIZE-PLAYER_WIDTH);
            }
            if(c.getY() <= 0)
               c.setY(0);
            if(c.getX() >= XSIZE-c.getWidth())
               c.setX(XSIZE-c.getWidth());
            if(c.getY() >= YSIZE-c.getHeight())
               c.setY(YSIZE-c.getHeight());
         }
      }
   }
   
   
   public static double distance(int x1, int y1, int x2, int y2){
      return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
   }
   
   
   //returns true if playerX,playerY has collided with an enemy
   public boolean checkEnemyCollisions()
   {
      for(Character enemy: enemies)
      {
         int ex = enemy.getX();
         int ey = enemy.getY();
         if(distance(ex, ey, mainPlayer.getX(), mainPlayer.getY()) < PLAYER_HEIGHT && Math.abs(ex-mainPlayer.getX()) < PLAYER_WIDTH)
            return true;
      }
      return false;
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
         setBoundaries(mainPlayer);
         hallMonitorMovement(); 
         repaint();
         frames++;
         
      }
   }
}
