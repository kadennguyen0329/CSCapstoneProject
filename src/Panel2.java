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
   public static final int AHALL3 = 8;
   public static final int BHALL = 9;
   public static final int CHALL1 = 10;
   public static final int CHALL2 = 11;
   public static final int DHALL = 12;
   public static final int FHALL2 = 13;
   public static final int GHALL = 14;
   public static final int KHALL = 15;
   public static final int LHALL = 16;
   public static final int END = 17;
   
   public static Player mainPlayer;
   public static final int defaultSpeed = (int)(XSIZE*(0.5/120));
   public static final int enemySpeed = defaultSpeed/4;
   public static final int PLAYER_HEIGHT = YSIZE/13;
   public static final int PLAYER_WIDTH = XSIZE/45; 
   public static final Color obstacleColor = new Color(255, 0, 0, 0);
   private static int hallMonitorStage;
   private static boolean hasMovedHallMonitor;
   
   public static int location;
   public static int frames;
   
   private static Timer t;
   private static HashSet<Integer> pressedKeys;
   
   private ArrayList<Rectangle> walls = new ArrayList<Rectangle>();
   private ArrayList<Rectangle> tables = new ArrayList<Rectangle>();
   private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    
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
      enemies.add(new Enemy((int)(XSIZE*(37.0/120)), (int)(YSIZE*(15.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, "images/Enemy.png", 100, enemySpeed, 1, "Hall Monitor"));
   }
   
   public void resetHallMonitor() {hallMonitorStage = 0; hasMovedHallMonitor = false;}
   
   public void hallMonitorMovement()
   {
       
      if(enemies.size() != 0 && distance(enemies.get(0).getX(), enemies.get(0).getY(), mainPlayer.getX(), mainPlayer.getY()) < 500)
      {
         hallMonitorFollow();
         
         if(!enemies.get(0).getIsFollowing())
         {
            enemies.get(0).setIsFollowing(true);
            enemies.get(0).setPreviousX(enemies.get(0).getX());
            enemies.get(0).setPreviousY(enemies.get(0).getY());
         }
      }
      
      else if(enemies.size() != 0 && enemies.get(0).getIsFollowing())
      {
         if(enemies.get(0).getIsFollowing())
         {
            hallMonitorReturn(enemies.get(0).getPreviousX(), enemies.get(0).getPreviousY());
            if(enemies.get(0).getX() == enemies.get(0).getPreviousX() && enemies.get(0).getY() == enemies.get(0).getPreviousY())
            {
               enemies.get(0).setIsFollowing(false);
            }
         }
      }
      
      
      
      if(location == LOBBY && enemies.size() != 0 && !enemies.get(0).getIsFollowing())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(37.0/120)));
            enemies.get(0).setY((int)(YSIZE*(15.0/75)));
            hasMovedHallMonitor = true;
         }
         
         else if(hallMonitorStage == 0 & enemies.get(0).getX() < (int)(XSIZE*(90.0/120)))
         {
            enemies.get(0).moveX(enemies.get(0).getSpeed());
            if((enemies.get(0).getX() >= (int)(XSIZE*(90.0/120))))
               hallMonitorStage = 1;
         }
            
         else if(hallMonitorStage == 1 & enemies.get(0).getY() < (int)(YSIZE*(60.0/75)))
         {
            enemies.get(0).moveY(enemies.get(0).getSpeed());
            if((enemies.get(0).getY() >= (int)(YSIZE*(60.0/75))))
               hallMonitorStage = 2;
         }
         else if(hallMonitorStage == 2 & enemies.get(0).getX() > (int)(XSIZE*(80.0/120)))
         {
            enemies.get(0).moveX(-enemies.get(0).getSpeed());
            if((enemies.get(0).getX() <= (int)(XSIZE*(80.0/120))))
               hallMonitorStage = 3;
         }
         else if(hallMonitorStage == 3 & enemies.get(0).getY() > (int)(YSIZE*(15.0/75)))
         {
            enemies.get(0).moveY(-enemies.get(0).getSpeed());
            if((enemies.get(0).getY() <= (int)(YSIZE*(15.0/75))))
               hallMonitorStage = 4;
         }
         else if(hallMonitorStage == 4 & enemies.get(0).getX() > (int)(XSIZE*(55.0/120)))
         {
            enemies.get(0).moveX(-enemies.get(0).getSpeed());
            if((enemies.get(0).getX() <= (int)(XSIZE*(55.0/120))))
               hallMonitorStage = 5;
         }
         else if(hallMonitorStage == 5 & enemies.get(0).getY() < (int)(YSIZE*(60.0/75)))
         {
            enemies.get(0).moveY(enemies.get(0).getSpeed());
            if((enemies.get(0).getY() >= (int)(YSIZE*(60.0/75))))
               hallMonitorStage = 6;
         }
         else if(hallMonitorStage == 6 & enemies.get(0).getX() > (int)(XSIZE*(37.0/120)))
         {
            enemies.get(0).moveX(-enemies.get(0).getSpeed());
            if((enemies.get(0).getX() <= (int)(XSIZE*(37.0/120))))
               hallMonitorStage = 7;
         }
         else if(hallMonitorStage == 7 & enemies.get(0).getY() > (int)(YSIZE*(15.0/75)))
         {
            enemies.get(0).moveY(-enemies.get(0).getSpeed());
            if((enemies.get(0).getY() <= (int)(YSIZE*(15.0/75))))
               hallMonitorStage = 0;
         }
      }
      else if(location == EHALL && enemies.size() != 0 && !enemies.get(0).getIsFollowing())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(8.0/120)));
            enemies.get(0).setY((int)(YSIZE*(32.0/75)));
            hasMovedHallMonitor = true;
         }
         else if(hallMonitorStage == 0 & enemies.get(0).getX() < (int)(XSIZE*(108.0/120)))
         {
            enemies.get(0).moveX(enemies.get(0).getSpeed());
            if((enemies.get(0).getX() >= (int)(XSIZE*(108.0/120))))
               hallMonitorStage = 1;
         }
         else if(hallMonitorStage == 1 & enemies.get(0).getX() > (int)(XSIZE*(8.0/120)))
         {
            enemies.get(0).moveX(-enemies.get(0).getSpeed());
            if((enemies.get(0).getX() <= (int)(XSIZE*(8.0/120))))
               hallMonitorStage = 0;
         }
      }
      if(location == FHALL && enemies.size() != 0 && !enemies.get(0).getIsFollowing())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(37.0/120)));
            enemies.get(0).setY((int)(YSIZE*(8.0/75)));
            hasMovedHallMonitor = true;
         }
         else if(hallMonitorStage == 0 & enemies.get(0).getX() < (int)(XSIZE*(90.0/120)))
         {
            enemies.get(0).moveX(enemies.get(0).getSpeed());
            if((enemies.get(0).getX() >= (int)(XSIZE*(90.0/120))))
               hallMonitorStage = 1;
         }
            
         else if(hallMonitorStage == 1 & enemies.get(0).getY() < (int)(YSIZE*(60.0/75)))
         {
            enemies.get(0).moveY(enemies.get(0).getSpeed());
            if((enemies.get(0).getY() >= (int)(YSIZE*(60.0/75))))
               hallMonitorStage = 2;
         }
         else if(hallMonitorStage == 2 & enemies.get(0).getX() > (int)(XSIZE*(80.0/120)))
         {
            enemies.get(0).moveX(-enemies.get(0).getSpeed());
            if((enemies.get(0).getX() <= (int)(XSIZE*(80.0/120))))
               hallMonitorStage = 3;
         }
         else if(hallMonitorStage == 3 & enemies.get(0).getY() > (int)(YSIZE*(8.0/75)))
         {
            enemies.get(0).moveY(-enemies.get(0).getSpeed());
            if((enemies.get(0).getY() <= (int)(YSIZE*(8.0/75))))
               hallMonitorStage = 4;
         }
         else if(hallMonitorStage == 4 & enemies.get(0).getX() > (int)(XSIZE*(55.0/120)))
         {
            enemies.get(0).moveX(-enemies.get(0).getSpeed());
            if((enemies.get(0).getX() <= (int)(XSIZE*(55.0/120))))
               hallMonitorStage = 5;
         }
         else if(hallMonitorStage == 5 & enemies.get(0).getY() < (int)(YSIZE*(60.0/75)))
         {
            enemies.get(0).moveY(enemies.get(0).getSpeed());
            if((enemies.get(0).getY() >= (int)(YSIZE*(60.0/75))))
               hallMonitorStage = 6;
         }
         else if(hallMonitorStage == 6 & enemies.get(0).getX() > (int)(XSIZE*(37.0/120)))
         {
            enemies.get(0).moveX(-enemies.get(0).getSpeed());
            if((enemies.get(0).getX() <= (int)(XSIZE*(37.0/120))))
               hallMonitorStage = 7;
         }
         else if(hallMonitorStage == 7 & enemies.get(0).getY() > (int)(YSIZE*(8.0/75)))
         {
            enemies.get(0).moveY(-enemies.get(0).getSpeed());
            if((enemies.get(0).getY() <= (int)(YSIZE*(8.0/75))))
               hallMonitorStage = 0;
         }
      }
      if(location == CAFEB || location == CAFEA)
      {
         enemies.get(0).setX(-100);
         enemies.get(0).setY(-100);
         hasMovedHallMonitor = true;
      }
     
   }

   
   public void hallMonitorFollow()
   {
      if(enemies.get(0).getX() < mainPlayer.getX())
         enemies.get(0).moveX(enemies.get(0).getSpeed());
      if(enemies.get(0).getX() > mainPlayer.getX())
         enemies.get(0).moveX(-enemies.get(0).getSpeed());
      if(enemies.get(0).getY() > mainPlayer.getY())
         enemies.get(0).moveY(-enemies.get(0).getSpeed());
      if(enemies.get(0).getY() < mainPlayer.getY())
         enemies.get(0).moveY(enemies.get(0).getSpeed());
   }
   
   public void hallMonitorReturn(int x, int y)
   {
      if(enemies.get(0).getX() < x)
         enemies.get(0).moveX(enemies.get(0).getSpeed());
      if(enemies.get(0).getX() > x)
         enemies.get(0).moveX(-enemies.get(0).getSpeed());
      if(enemies.get(0).getY() > y)
         enemies.get(0).moveY(-enemies.get(0).getSpeed());
      if(enemies.get(0).getY() < y)
         enemies.get(0).moveY(enemies.get(0).getSpeed());
   }
   
   
   public void movePlayer()
   {
      if(mainPlayer != null)
         if(!mainPlayer.isHiding())
         {
            if(pressedKeys.contains(KeyEvent.VK_W) && !checkObstacleCollisions(mainPlayer.getX(), mainPlayer.getY()-mainPlayer.getSpeed()) && !checkObstacleCollisions(mainPlayer.getX() + PLAYER_WIDTH, mainPlayer.getY()-mainPlayer.getSpeed()))
               mainPlayer.moveY(-mainPlayer.getSpeed());
            
            else if(pressedKeys.contains(KeyEvent.VK_S) && !checkObstacleCollisions(mainPlayer.getX(), mainPlayer.getY()+mainPlayer.getSpeed() + PLAYER_HEIGHT) && !checkObstacleCollisions(mainPlayer.getX() + PLAYER_WIDTH, mainPlayer.getY()+mainPlayer.getSpeed() + PLAYER_HEIGHT))
               mainPlayer.moveY(mainPlayer.getSpeed()); 
             
            if(pressedKeys.contains(KeyEvent.VK_A) && !checkObstacleCollisions(mainPlayer.getX()-mainPlayer.getSpeed(), mainPlayer.getY()) && !checkObstacleCollisions(mainPlayer.getX()-mainPlayer.getSpeed(), mainPlayer.getY() + PLAYER_HEIGHT))
               mainPlayer.moveX(-mainPlayer.getSpeed());
            
            else if(pressedKeys.contains(KeyEvent.VK_D)&& !checkObstacleCollisions(mainPlayer.getX()+mainPlayer.getSpeed() + PLAYER_WIDTH, mainPlayer.getY()) && !checkObstacleCollisions(mainPlayer.getX()+mainPlayer.getSpeed() + PLAYER_WIDTH, mainPlayer.getY() + PLAYER_HEIGHT))
               mainPlayer.moveX(mainPlayer.getSpeed());
         
         
            if(checkEnemyCollisions()){
               for(int i=0; i<enemies.size(); i++){
                  enemies.remove(i);
                  mainPlayer.setX(XSIZE/2);
                  mainPlayer.setY(YSIZE/2);
                  location = END;
                  Sound.randomNote();
               }
            }
         
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
      else if(location == AHALL1)
      {
         ImageIcon pic = new ImageIcon("images/aHall1.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == AHALL2)
      {
         ImageIcon pic = new ImageIcon("images/aHall2.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == AHALL2)
      {
         ImageIcon pic = new ImageIcon("images/aHall2.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == AHALL3)
      {
         ImageIcon pic = new ImageIcon("images/aHall3.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == BHALL)
      {
         ImageIcon pic = new ImageIcon("images/bHall.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == CHALL1)
      {
         ImageIcon pic = new ImageIcon("images/cHall1.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == CHALL2)
      {
         ImageIcon pic = new ImageIcon("images/cHall2.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == DHALL)
      {
         ImageIcon pic = new ImageIcon("images/dHall.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == FHALL2)
      {
         ImageIcon pic = new ImageIcon("images/fHall2.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == GHALL)
      {
         ImageIcon pic = new ImageIcon("images/gHall.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == KHALL)
      {
         ImageIcon pic = new ImageIcon("images/kHall.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == LHALL)
      {
         ImageIcon pic = new ImageIcon("images/lHall.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == END)
      {
         ImageIcon pic = new ImageIcon("images/End.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      seeObstacles(g);
      if(!mainPlayer.isHiding())
      g.drawImage(mainPlayer.getFrame().getImage(), mainPlayer.getX(), mainPlayer.getY(), mainPlayer.getWidth(), mainPlayer.getHeight(), null);
      
      for(Character enemy: enemies){
         g.drawImage(enemy.getFrame().getImage(), enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight(), null);
      }
   }      
   
   private void seeObstacles(Graphics g)
   {
      g.setColor(obstacleColor);
      for(Rectangle r:walls)
         g.fillRect((int)(r.getX()), (int)(r.getY()), (int)(r.getWidth()), (int)(r.getHeight()));
         
      for(Rectangle r:tables)
         g.fillRect((int)(r.getX()), (int)(r.getY()), (int)(r.getWidth()), (int)(r.getHeight()));
   
   }
   
   public void setTables()
   {
      for(int i=0; i<tables.size(); i++)
         tables.remove(i);
         
      if(location == CAFEA)
      {
         tables.add(new Rectangle((int)(XSIZE*(27.0/120)), (int)(YSIZE*(13.0/75)), (int)(XSIZE*(13.0/120)), (int)(YSIZE*(7.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(61.0/120)), (int)(YSIZE*(13.0/75)), (int)(XSIZE*(13.0/120)), (int)(YSIZE*(7.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(92.0/120)), (int)(YSIZE*(13.0/75)), (int)(XSIZE*(14.0/120)), (int)(YSIZE*(7.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(27.0/120)), (int)(YSIZE*(57.0/75)), (int)(XSIZE*(14.0/120)), (int)(YSIZE*(8.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(59.0/120)), (int)(YSIZE*(57.0/75)), (int)(XSIZE*(14.0/120)), (int)(YSIZE*(7.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(93.0/120)), (int)(YSIZE*(57.0/75)), (int)(XSIZE*(13.0/120)), (int)(YSIZE*(7.0/75))));
      }
      else if(location == CAFEB)
      {
         tables.add(new Rectangle((int)(XSIZE*(18.0/120)), (int)(YSIZE*(10.0/75)), (int)(XSIZE*(32.0/120)), (int)(YSIZE*(8.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(75.0/120)), (int)(YSIZE*(10.0/75)), (int)(XSIZE*(35.0/120)), (int)(YSIZE*(8.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(18.0/120)), (int)(YSIZE*(56.0/75)), (int)(XSIZE*(35.0/120)), (int)(YSIZE*(8.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(78.0/120)), (int)(YSIZE*(55.0/75)), (int)(XSIZE*(32.0/120)), (int)(YSIZE*(8.0/75))));
      }
   }

   
   public void setWalls()
   {
      for(int i=0; i<walls.size(); i++)
         walls.remove(i);
      if(location == LOBBY)
      {
         walls.add(new Rectangle(0, (int)(YSIZE*(19.0/75)), (int)(XSIZE*(32.0/120)), YSIZE-((int)(YSIZE*(19.0/75)))));
         walls.add(new Rectangle(0, 0, (int)(XSIZE*(32.0/120)), ((int)(YSIZE*(2.0/75)))));
         walls.add(new Rectangle((int)(XSIZE*(102.0/120)), (int)(YSIZE*(17.0/75)), XSIZE-((int)(XSIZE*(101.0/120))), YSIZE-((int)(YSIZE*(17.0/75)))));
         walls.add(new Rectangle((int)(XSIZE*(102.0/120)), 0, XSIZE-(int)(XSIZE*(102.0/120)), ((int)(YSIZE*(2.0/75)))));
      }
      
      else if(location == EHALL)
      {
         walls.add(new Rectangle(0, 0, XSIZE, (int)(YSIZE*(15.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(54.0/75)), XSIZE, YSIZE-(int)(YSIZE*(54.0/75))));
      }
      else if(location == FHALL)
      {
         walls.add(new Rectangle(0, 0, (int)(XSIZE*(33.0/120)), YSIZE));
         walls.add(new Rectangle((int)(XSIZE*(97.0/120)), 0, XSIZE-(int)(XSIZE*(97.0/120)), (int)(YSIZE*(16.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(97.0/120)), (int)(YSIZE*(27.0/75)), XSIZE-(int)(XSIZE*(97.0/120)), (int)(YSIZE*(32.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(97.0/120)), (int)(YSIZE*(70.0/75)), XSIZE-(int)(XSIZE*(97.0/120)), YSIZE-(int)(YSIZE*(70.0/75))));
      }
      else if(location == CAFEA)
      {
         walls.add(new Rectangle(0, 0, (int)(XSIZE*(5.0/120)), (int)(YSIZE*(28.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(47.0/75)), (int)(XSIZE*(5.0/120)), YSIZE-(int)(YSIZE*(47.0/75))));
      }
      else if(location == CAFEB)
      {
         walls.add(new Rectangle(0, 0, (int)(XSIZE*(5.0/120)), (int)(YSIZE*(28.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(47.0/75)), (int)(XSIZE*(5.0/120)), YSIZE-(int)(YSIZE*(47.0/75))));
      }
      else if(location == AHALL1)
      {
         walls.add(new Rectangle(0, 0, (int)(XSIZE*(23.0/120)), (int)(YSIZE*(26.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(49.0/75)),  (int)(XSIZE*(23.0/120)), (int)(YSIZE*(26.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(97.0/120)), 0,  (int)(XSIZE*(24.0/120)), (int)(YSIZE*(26.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(97.0/120)), (int)(YSIZE*(49.0/75)), (int)(XSIZE*(24.0/120)), (int)(YSIZE*(27.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(73.0/75)), XSIZE, (int)(YSIZE*(10.0/75))));
      }
      else if(location == AHALL2)
      {
         walls.add(new Rectangle(0, 0, (int)(XSIZE*(23.0/120)), (int)(YSIZE*(26.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(49.0/75)),  (int)(XSIZE*(23.0/120)), (int)(YSIZE*(26.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(97.0/120)), 0,  (int)(XSIZE*(24.0/120)), (int)(YSIZE*(26.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(97.0/120)), (int)(YSIZE*(49.0/75)), (int)(XSIZE*(24.0/120)), (int)(YSIZE*(27.0/75))));
      
      }
      else if(location == AHALL3 )
      {
         walls.add(new Rectangle(0, 0, (int)(XSIZE), (int)(YSIZE*(14.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(26.0/75)), (int)(XSIZE*(17.0/120)), (int)(YSIZE*(50.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(103.0/120)), (int)(YSIZE*(26.0/75)), (int)(XSIZE*(16.0/120)), (int)(YSIZE*(49.0/75))));
      }
      else if(location == BHALL)
      {
         walls.add(new Rectangle(0, 0, XSIZE, (int)(YSIZE*(15.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(54.0/75)), XSIZE, YSIZE-(int)(YSIZE*(54.0/75))));
      }
      else if(location == CHALL1)
      {
         walls.add(new Rectangle(0, 0, (int)(XSIZE*(36.0/120)), (int)(YSIZE)));
         walls.add(new Rectangle((int)(XSIZE*(83.0/120)), (int)(YSIZE*(0.0/75)), (int)(XSIZE*(37.0/120)), (int)(YSIZE*(48.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(36.0/120)), (int)(YSIZE*(66.0/75)), (int)(XSIZE*(84.0/120)), (int)(YSIZE*(9.0/75))));
      }
      else if(location == DHALL)
      {
         walls.add(new Rectangle(0, 0, XSIZE, (int)(YSIZE*(15.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(54.0/75)), XSIZE, YSIZE-(int)(YSIZE*(54.0/75))));
      }
      else if(location == FHALL2)
      {
         walls.add(new Rectangle(0, 0, XSIZE, (int)(YSIZE*(4.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(17.0/75)), (int)(XSIZE*(33.0/120)), (int)(YSIZE*(41.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(70.0/75)), (int)(XSIZE*(33.0/120)), (int)(YSIZE*(5.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(83.0/120)), 0, (int)(XSIZE*(837.0/120)), YSIZE));
      }
      else if(location == GHALL)
      {
         walls.add(new Rectangle(0, 0, XSIZE, (int)(YSIZE*(15.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(54.0/75)), XSIZE, YSIZE-(int)(YSIZE*(54.0/75))));
      }
      else if(location == KHALL)
      {
         walls.add(new Rectangle(0, 0, XSIZE, (int)(YSIZE*(15.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(54.0/75)), XSIZE, YSIZE-(int)(YSIZE*(54.0/75))));
      }
      else if(location == LHALL)
      {
         walls.add(new Rectangle(0, 0, XSIZE, (int)(YSIZE*(15.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(54.0/75)), XSIZE, YSIZE-(int)(YSIZE*(54.0/75))));
      }
      else if(location == CHALL2)
      {
         walls.add(new Rectangle(0, 0, (int)(XSIZE*(49.0/120)), YSIZE));
         walls.add(new Rectangle(0, 0,  XSIZE, (int)(YSIZE*(4.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(91.0/120)), (int)(YSIZE*(16.0/75)), (int)(XSIZE*(29.0/120)), (int)(YSIZE*(38.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(92.0/120)), (int)(YSIZE*(71.0/75)), (int)(XSIZE*(29.0/120)), (int)(YSIZE*(7.0/75))));
      }
      //Nathan: aHall1, aHall2, bHall,cHall1, cHall2
      //Kaden: dHall, fHall2, gHall, kHall, lHall
      
      else if(location == END)
      {
         walls.add(new Rectangle((int)(XSIZE*(6.0/120)), (int)(YSIZE*(61.0/75)), (int)(XSIZE*(33.0/120)), (int)(YSIZE*(10.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(78.0/120)), (int)(YSIZE*(59.0/75)), (int)(XSIZE*(27.0/120)), (int)(YSIZE*(12.0/75))));
         
         if(checkObstacleCollisions(mainPlayer.getX(), mainPlayer.getY()-mainPlayer.getSpeed()) && checkObstacleCollisions(mainPlayer.getX() + PLAYER_WIDTH, mainPlayer.getY()-mainPlayer.getSpeed())){
            
            if(mainPlayer.getX()<XSIZE/2){
               location = LOBBY;
               frames = 0;
               mainPlayer = new Player(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, "images/Player.png", 100, defaultSpeed, 1, "Kaden");
               enemies.add(new Enemy((int)(XSIZE*(60.0/120)), (int)(YSIZE*(70.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, "images/Enemy.png", 100, enemySpeed, 1, "Hall Monitor"));
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
               enemies.add(new Enemy((int)(XSIZE*(60.0/120)), (int)(YSIZE*(70.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, "images/Enemy.png", 100, enemySpeed, 1, "Hall Monitor"));
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
               enemies.add(new Enemy((int)(XSIZE*(60.0/120)), (int)(YSIZE*(70.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, "images/Enemy.png", 100, enemySpeed, 1, "Hall Monitor"));
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
               enemies.add(new Enemy((int)(XSIZE*(60.0/120)), (int)(YSIZE*(70.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, "images/Enemy.png", 100, enemySpeed, 1, "Hall Monitor"));
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
         setTables();
         if(location == LOBBY)
         {
            if(c.getX() <= 0){
               location = EHALL;
               resetHallMonitor();
               c.setX(XSIZE - c.getWidth()*2);
               c.setY(YSIZE/2);
            }
            if(c.getY() <= 0)
            {
               location = FHALL;
               resetHallMonitor();
               c.setX(XSIZE/2);
               c.setY(YSIZE - c.getWidth()*2);
            }
            if(c.getX() >= XSIZE-c.getWidth())
               c.setX(XSIZE-c.getWidth());
            if(c.getY() >= YSIZE-c.getHeight())
               c.setY(YSIZE-c.getHeight());
         }
         if(location == EHALL)
         {
            if(c.getX() >= XSIZE-c.getWidth()){
               location = LOBBY;
               resetHallMonitor();
               c.setX(c.getWidth());
               c.setY(YSIZE - ((YSIZE/9)*8));
            }
            if(c.getY() >= YSIZE-c.getHeight())
               c.setY(YSIZE-c.getHeight());
               
            if(c.getX() < 0)
            {
               location = AHALL1;
               resetHallMonitor();
               c.setX(XSIZE);
               c.setY(YSIZE/2);
            }
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
            if(c.getY() < 0){
               location = FHALL2;
               resetHallMonitor();
               c.setY((int)(YSIZE)-PLAYER_HEIGHT);
               c.setX(XSIZE/2);
            }
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
         if(location == FHALL2)
         {
            if(c.getY() > YSIZE-PLAYER_HEIGHT){
               location = FHALL;
               resetHallMonitor();
               c.setY(0);
            }
            if(c.getX() < 0 && c.getY() < YSIZE/2)
            {
               location = LHALL;
               resetHallMonitor();
               c.setX(XSIZE);
               c.setY(YSIZE/2);
            }
            if(c.getX() < 0 && c.getY() > YSIZE/2)
            {
               location = GHALL;
               resetHallMonitor();
               c.setX(XSIZE);
               c.setY(YSIZE/2);
            }
         }
         if(location == LHALL)
         {
            if(c.getX() > XSIZE)
            {
               location = FHALL2;
               resetHallMonitor();
               c.setX(0);
               c.setY(YSIZE*10/75);
            }
            if(c.getX() < 0)
            {
               location = AHALL3;
               resetHallMonitor();
               c.setX(XSIZE);
               c.setY((int)(YSIZE*(17.0/75)));               
            }
         }
         if(location == GHALL)
         {
            if(c.getX() > XSIZE)
            {
               location = FHALL2;
               resetHallMonitor();
               c.setX(0);
               c.setY(YSIZE*62/75);
            }
            if(c.getX() < 0)
            {
               location = AHALL2;
               resetHallMonitor();
               c.setX(XSIZE);
               c.setY((int)(YSIZE*(32.0/75))); 
            }
         }
         if(location == AHALL2)
         {
            if(c.getX() > XSIZE)
            {
               location = GHALL;
               resetHallMonitor();
               c.setX(0);
            }
            if(c.getX() < 0)
            {
               location = BHALL;
               resetHallMonitor();
               c.setX(XSIZE);
            }
            if(c.getY() < 0)
            {
               location = AHALL3;
               resetHallMonitor();
               c.setY(YSIZE);
            }
            if(c.getY() > YSIZE)
            {
               location = AHALL1;
               resetHallMonitor();
               c.setY(0);
            }
         }
         if(location == AHALL1)
         {
            if(c.getX() > XSIZE)
            {
               location = EHALL;
               resetHallMonitor();
               c.setX(0);
            }
            if(c.getX() < 0)
            {
               location = DHALL;
               resetHallMonitor();
               c.setX(XSIZE);
            }
            if(c.getY() < 0)
            {
               location = AHALL2;
               resetHallMonitor();
               c.setY(YSIZE);
            }
            
         }
         if(location == DHALL)
         {
            if(c.getX() > XSIZE)
            {
               location = AHALL1;
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(32.0/75))); 
            }
            if(c.getX() < 0)
            {
               location = CHALL1;
               resetHallMonitor();
               c.setX(XSIZE);
               c.setY((int)(YSIZE*(57.0/75)));
            }
         }
         if(location == CHALL1)
         {
            if(c.getX() > XSIZE)
            {
               location = DHALL;
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(34.0/75)));
            }
            if(c.getY() < 0)
            {
               location = CHALL2;
               resetHallMonitor();
               c.setY(YSIZE);
               c.setX((int)(XSIZE*(68.0/120)));
            
            }
         }
         if(location == CHALL2)
         {
            if(c.getX() > XSIZE && c.getY() < YSIZE/2)
            {
               location = KHALL;
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(50.0/120)));
            }
            if(c.getX() > XSIZE && c.getY() > YSIZE/2)
            {
               location = BHALL;
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(50.0/120)));
            }
            if(c.getY() > YSIZE)
            {
               location = CHALL1;
               resetHallMonitor();
               c.setY(0);
               c.setX((int)(XSIZE*(58.0/120)));
            }
         }
         if(location == BHALL)
         {
            if(c.getX() > XSIZE)
            {
               location = AHALL2;
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(32.0/75))); 
            }
            if(c.getX() < 0)
            {
               location = CHALL2;
               resetHallMonitor();
               c.setX(XSIZE);
               c.setY((int)(YSIZE*(95.0/120)));
            }
         } 
         if(location == KHALL)
         {
            if(c.getX() > XSIZE)
            {
               location = AHALL3;
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(24.0/120)));
            }
            if(c.getX() < 0)
            {
               location = CHALL2;
               resetHallMonitor();
               c.setX(XSIZE);
               c.setY((int)(YSIZE*(13.0/120)));
            }
         }
         if(location == AHALL3)
         {
            if(c.getX() > XSIZE)
            {
               location = LHALL;
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(32.0/75))); 
            }
            if(c.getX() < 0)
            {
               location = KHALL;
               resetHallMonitor();
               c.setX(XSIZE);
               c.setY((int)(YSIZE*(32.0/75))); 
            }
            if(c.getY() > YSIZE)
            {
               location = AHALL2;
               resetHallMonitor();
               c.setY(0);
            }  
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
      for(Rectangle r: walls){
         if(r.contains(x, y))
            return true;
      }
      for(Rectangle r: tables){
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
      if(e.getKeyCode() == KeyEvent.VK_SHIFT)
      {
         for(Rectangle r: tables)
         {
            if(distance(mainPlayer.getX(), mainPlayer.getY(), (int)(r.getX() + (r.getWidth()/2)), (int)(r.getY() + (r.getHeight()/2))) < 200)
            {
               mainPlayer.setIsHiding(true);
            }
         }
      }
   
      pressedKeys.add(e.getKeyCode());
   }
   
   public void keyReleased(KeyEvent e)
   {
      pressedKeys.remove((Integer)(e.getKeyCode()));
      if(e.getKeyCode() == KeyEvent.VK_SHIFT)
      {
         mainPlayer.setIsHiding(false);
      }
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
