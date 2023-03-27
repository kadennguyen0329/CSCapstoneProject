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
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Panel extends JPanel implements KeyListener, MouseListener
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
   public static final int DHALLCLASS = 17;
   public static final int END = 18;
   
   public static final String[][] mainPlayerImages = {{"images/Player.png", null},{"images/PlayerWalk1.png","images/PlayerWalk2.png"}};
   public static final String[][] enemyImages = {{"images/Enemy.png", null},{null,null}};
   public static final String[][] itemImages = {{"images/Bat.png", null},{"images/BatUse.png",null}};

   
   public static Player mainPlayer;
   public static final int defaultSpeed = (int)(XSIZE*(0.5/120));
   public static final int enemySpeed = defaultSpeed/4;
   public static final int PLAYER_HEIGHT = YSIZE/13;
   public static final int PLAYER_WIDTH = XSIZE/45; 
   public static final Color obstacleColor = new Color(255, 0, 0, 0);
   private static int hallMonitorStage;
   private static boolean hasMovedHallMonitor;
   
   public static int location;
   public static double timeDistance;
   public static int followTime;
   public static boolean changingLoc;
   public static int exitX;
   public static int exitY;
   public static int playerTwiceChange;
   public static int frames;
   private static int coolDownCountdown;
   
   private static Timer t;
   private static HashSet<Integer> pressedKeys;
   
   private ArrayList<Rectangle> walls = new ArrayList<Rectangle>();
   private ArrayList<Rectangle> tables = new ArrayList<Rectangle>();
   private ArrayList<Enemy> enemies = new ArrayList<Enemy>();   
   private ArrayList<Item> items = new ArrayList<Item>();
   private ArrayList<Item> inventory = new ArrayList<Item>();
    
   public Panel()
   {
      t = new Timer(1, new Listener());
      t.start();
      pressedKeys = new HashSet<Integer>();
      location = LOBBY;
      Sound.initialize();
      frames = 0;
      hallMonitorStage = 0;
      hasMovedHallMonitor = false;
      mainPlayer = new Player(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, mainPlayerImages, 100, defaultSpeed, 1, "Kaden", 1);
      enemies.add(new Enemy((int)(XSIZE*(37.0/120)), (int)(YSIZE*(15.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, enemyImages, 100, enemySpeed, 1, "Hall Monitor", 1, 0));
      items.add(new MeleeWeapon(XSIZE/3, YSIZE/3, (int)(PLAYER_HEIGHT*0.7), (int)(PLAYER_HEIGHT*0.7), itemImages, 10, 100, 50));
   
   }
   
   public void handeItems()
   {
      for(int i = items.size()-1; i >= 0; i--)
      {
         if(distance(mainPlayer.getX(), mainPlayer.getY(), items.get(i).getX(), items.get(i).getY()) < 60 && pressedKeys.contains(KeyEvent.VK_F))
         {
            inventory.add(items.remove(i));
         }
      }
      
      for(Item item: inventory)
      {
         if(!item.onCoolDown())
         {
            item.setX(mainPlayer.getX());
            item.setY(mainPlayer.getY());
         }
         else
         {
            coolDownCountdown--;
            item.setX(mainPlayer.getX()-item.getWidth());
            item.setY(mainPlayer.getY());
            if(coolDownCountdown <= 0)
            {
            item.setOnCoolDown(false);
            item.setFrame(0,0);
            }
         }
      }
      
      if(pressedKeys.contains(KeyEvent.VK_H) && inventory.size() !=0)
      {
         items.add(inventory.remove(0));
      }
   }

   public void resetHallMonitor() {hallMonitorStage = 0; hasMovedHallMonitor = false;}
   
   public void hallMonitorMovement()
   { 
      if(enemies.size() != 0 && enemies.get(0).getPrevHall() != location)
      {
         if(followTime == 0)
         {
            timeDistance = distance(enemies.get(0).getX(), enemies.get(0).getY(), mainPlayer.getPreviousX(), mainPlayer.getPreviousY())/10;
            changingLoc = true;
            playerTwiceChange = location;
         }
         else if(playerTwiceChange != location)
         {
            timeDistance = distance(enemies.get(0).getX(), enemies.get(0).getY(), mainPlayer.getPreviousX(), mainPlayer.getPreviousY())/100;
            followTime = 0;
            playerTwiceChange = location;
         }
         followTime++;
         if(followTime == 2)
         {
            playerTwiceChange = location;
            enemies.get(0).setPreviousX(mainPlayer.getX());
            enemies.get(0).setPreviousY(mainPlayer.getY());
            exitX = mainPlayer.getX();
            exitY = mainPlayer.getY();
            
         }
         
         if(followTime >= timeDistance && changingLoc)
         {
            enemies.get(0).setX(enemies.get(0).getPreviousX());
            enemies.get(0).setY(enemies.get(0).getPreviousY());
            enemies.get(0).setLocation(location);
            enemies.get(0).setPrevHall(location);
            followTime = 0;
            if(!mainPlayer.isHiding())
            {
               enemies.get(0).setIsFollowing(true);
            }
         }
                 
      }
      
      
         
      if((enemies.size() != 0 && distance(enemies.get(0).getX(), enemies.get(0).getY(), mainPlayer.getX(), mainPlayer.getY()) < 800 && !mainPlayer.isHiding()) || changingLoc)
      {
         if(!mainPlayer.isHiding())
         {
            hallMonitorFollow();
         }
         
         
         if(enemies.size() != 0 && !enemies.get(0).getIsFollowing())
         {
            if(!mainPlayer.isHiding())
            {
               enemies.get(0).setIsFollowing(true);
            }
            else
            {
               enemies.get(0).setIsFollowing(false);
            }
            if(!changingLoc)
            {
               enemies.get(0).setPreviousX(enemies.get(0).getX());
               enemies.get(0).setPreviousY(enemies.get(0).getY());
            }
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
      
      if(location == LOBBY && enemies.size() != 0 && !enemies.get(0).getIsFollowing() && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(10.0/120)));
            enemies.get(0).setY((int)(YSIZE*(12.0/75)));
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
      else if(location == EHALL && enemies.size() != 0 && !enemies.get(0).getIsFollowing() && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(60.0/120)));
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
      else if(location == FHALL && enemies.size() != 0 && !enemies.get(0).getIsFollowing() && enemies.get(0).getLocation() == mainPlayer.getLocation())
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
      
      else if(enemies.size() != 0 && location == AHALL1 && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(60.0/120)));
            enemies.get(0).setY((int)(YSIZE*(30.0/75)));
            hasMovedHallMonitor = true;
         }
      }
      else if(enemies.size() != 0 && location == AHALL2 && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(60.0/120)));
            enemies.get(0).setY((int)(YSIZE*(30.0/75)));
            hasMovedHallMonitor = true;
         }
      }
      else if(enemies.size() != 0 && location == AHALL3 && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(60.0/120)));
            enemies.get(0).setY((int)(YSIZE*(30.0/75)));
            hasMovedHallMonitor = true;
         }
      }
      else if(enemies.size() != 0 && location == BHALL && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(60.0/120)));
            enemies.get(0).setY((int)(YSIZE*(32.0/75)));
            hasMovedHallMonitor = true;
         }
      }
      else if(enemies.size() != 0 && location == CHALL1 && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(55.0/120)));
            enemies.get(0).setY((int)(YSIZE*(30.0/75)));
            hasMovedHallMonitor = true;
         }
      }
      else if(enemies.size() != 0 && location == CHALL2 && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(70.0/120)));
            enemies.get(0).setY((int)(YSIZE*(40.0/75)));
            hasMovedHallMonitor = true;
         }
      }
      else if(enemies.size() != 0 && location == DHALL && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(60.0/120)));
            enemies.get(0).setY((int)(YSIZE*(32.0/75)));
            hasMovedHallMonitor = true;
         }
      }
      else if(enemies.size() != 0 && location == FHALL2 && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(55.0/120)));
            enemies.get(0).setY((int)(YSIZE*(30.0/75)));
            hasMovedHallMonitor = true;
         }
      }
      else if(enemies.size() != 0 && location == GHALL && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(60.0/120)));
            enemies.get(0).setY((int)(YSIZE*(32.0/75)));
            hasMovedHallMonitor = true;
         }
      }
      else if(enemies.size() != 0 && location == KHALL && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(60.0/120)));
            enemies.get(0).setY((int)(YSIZE*(32.0/75)));
            hasMovedHallMonitor = true;
         }
      }
      else if(enemies.size() != 0 && location == LHALL && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(60.0/120)));
            enemies.get(0).setY((int)(YSIZE*(32.0/75)));
            hasMovedHallMonitor = true;
         }
      }
      if(location == DHALLCLASS && enemies.size() != 0 && !enemies.get(0).getIsFollowing() && enemies.get(0).getLocation() == mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX((int)(XSIZE*(37.0/120)));
            enemies.get(0).setY((int)(YSIZE*(5.0/75)));
            hasMovedHallMonitor = true;
         }
         
         else if(hallMonitorStage == 0 & enemies.get(0).getX() < (int)(XSIZE*(84.0/120)))
         {
            enemies.get(0).moveX(enemies.get(0).getSpeed());
            if((enemies.get(0).getX() >= (int)(XSIZE*(84.0/120))))
               hallMonitorStage = 1;
         }
            
         else if(hallMonitorStage == 1 & enemies.get(0).getY() < (int)(YSIZE*(60.0/75)))
         {
            enemies.get(0).moveY(enemies.get(0).getSpeed());
            if((enemies.get(0).getY() >= (int)(YSIZE*(60.0/75))))
               hallMonitorStage = 2;
         }
         else if(hallMonitorStage == 2 & enemies.get(0).getX() > (int)(XSIZE*(40.0/120)))
         {
            enemies.get(0).moveX(-enemies.get(0).getSpeed());
            if((enemies.get(0).getX() <= (int)(XSIZE*(40.0/120))))
               hallMonitorStage = 3;
         }
         else if(hallMonitorStage == 3 & enemies.get(0).getY() > (int)(YSIZE*(5.0/75)))
         {
            enemies.get(0).moveY(-enemies.get(0).getSpeed());
            if((enemies.get(0).getY() <= (int)(YSIZE*(5.0/75))))
               hallMonitorStage = 0;
         }
      }
      else if(enemies.size() != 0 && (location == CAFEB || location == CAFEA) )
      {
         enemies.get(0).setX(-100);
         enemies.get(0).setY(-100);
         hasMovedHallMonitor = true;
      }
      else if(enemies.size() != 0 && enemies.get(0).getLocation() != mainPlayer.getLocation())
      {
         if(!hasMovedHallMonitor)
         {
            enemies.get(0).setX(-10000);
            enemies.get(0).setY(-10000);
            hasMovedHallMonitor = true;
            
         }
      }
      if(enemies.size() != 0 && mainPlayer.isHiding())
      {
         enemies.get(0).setIsFollowing(false);
         hallMonitorReturn(exitX, exitY);
         if(enemies.get(0).getX() == exitX && enemies.get(0).getY() == exitY)
         {
            enemies.remove(0);
         }
      }   
   }

   
   public void hallMonitorFollow()
   {
      if(enemies.size() != 0)
      {
         if(enemies.get(0).getX() < mainPlayer.getX() && !checkObstacleCollisions(enemies.get(0).getX()+enemies.get(0).getSpeed() + PLAYER_WIDTH, enemies.get(0).getY()) && !checkObstacleCollisions(enemies.get(0).getX()+enemies.get(0).getSpeed() + PLAYER_WIDTH, enemies.get(0).getY() + PLAYER_HEIGHT))
            enemies.get(0).moveX(enemies.get(0).getSpeed());
         if(enemies.get(0).getX() > mainPlayer.getX() && !checkObstacleCollisions(enemies.get(0).getX()-enemies.get(0).getSpeed(), enemies.get(0).getY()) && !checkObstacleCollisions(enemies.get(0).getX()-enemies.get(0).getSpeed(), enemies.get(0).getY() + PLAYER_HEIGHT))
            enemies.get(0).moveX(-enemies.get(0).getSpeed());
         if(enemies.get(0).getY() > mainPlayer.getY() && !checkObstacleCollisions(enemies.get(0).getX(), enemies.get(0).getY()-enemies.get(0).getSpeed()) && !checkObstacleCollisions(enemies.get(0).getX() + PLAYER_WIDTH, enemies.get(0).getY()-enemies.get(0).getSpeed()))
            enemies.get(0).moveY(-enemies.get(0).getSpeed());
         if(enemies.get(0).getY() < mainPlayer.getY() && !checkObstacleCollisions(enemies.get(0).getX(), enemies.get(0).getY()+enemies.get(0).getSpeed() + PLAYER_HEIGHT) && !checkObstacleCollisions(enemies.get(0).getX() + PLAYER_WIDTH, enemies.get(0).getY()+enemies.get(0).getSpeed() + PLAYER_HEIGHT))
            enemies.get(0).moveY(enemies.get(0).getSpeed());
      }
   }
   
   public void hallMonitorReturn(int x, int y)
   {
      if(enemies.get(0).getX() < x && !checkObstacleCollisions(enemies.get(0).getX()+enemies.get(0).getSpeed() + PLAYER_WIDTH, enemies.get(0).getY()) && !checkObstacleCollisions(enemies.get(0).getX()+enemies.get(0).getSpeed() + PLAYER_WIDTH, enemies.get(0).getY() + PLAYER_HEIGHT))
         enemies.get(0).moveX(enemies.get(0).getSpeed());
      if(enemies.get(0).getX() > x && !checkObstacleCollisions(enemies.get(0).getX()-enemies.get(0).getSpeed(), enemies.get(0).getY()) && !checkObstacleCollisions(enemies.get(0).getX()-enemies.get(0).getSpeed(), enemies.get(0).getY() + PLAYER_HEIGHT))
         enemies.get(0).moveX(-enemies.get(0).getSpeed());
      if(enemies.get(0).getY() > y && !checkObstacleCollisions(enemies.get(0).getX(), enemies.get(0).getY()-enemies.get(0).getSpeed()) && !checkObstacleCollisions(enemies.get(0).getX() + PLAYER_WIDTH, enemies.get(0).getY()-enemies.get(0).getSpeed()))
         enemies.get(0).moveY(-enemies.get(0).getSpeed());
      if(enemies.get(0).getY() < y && !checkObstacleCollisions(enemies.get(0).getX(), enemies.get(0).getY()+enemies.get(0).getSpeed() + PLAYER_HEIGHT) && !checkObstacleCollisions(enemies.get(0).getX() + PLAYER_WIDTH, enemies.get(0).getY()+enemies.get(0).getSpeed() + PLAYER_HEIGHT))
         enemies.get(0).moveY(enemies.get(0).getSpeed());
   }
   
   public void healthCheck()
   {
      for(int i=0; i<enemies.size(); i++)
      {
         if(enemies.get(i).getHealth() <= 0)
            enemies.remove(i);
      }
      if(checkEnemyCollisions()){
         for(int i=0; i<enemies.size(); i++){
            mainPlayer.damage(enemies.get(i).getDPS());
         }
         if(mainPlayer.getHealth() <= 0)
         { 
            for(int i=0; i<enemies.size(); i++)
            {
               enemies.remove(i);
               mainPlayer.setX(XSIZE/2);
               mainPlayer.setY(YSIZE/2);
               location = END;
               Sound.randomNote();
            }
         }
      }
   }      
   public void movePlayer()
   {
      if(mainPlayer != null)
      {
         if(!mainPlayer.isHiding())
         {
            if(pressedKeys.contains(KeyEvent.VK_W) && !checkObstacleCollisions(mainPlayer.getX(), mainPlayer.getY()-mainPlayer.getSpeed()) && !checkObstacleCollisions(mainPlayer.getX() + PLAYER_WIDTH, mainPlayer.getY()-mainPlayer.getSpeed()))
            {
               mainPlayer.moveY(-mainPlayer.getSpeed());
               if(frames % (defaultSpeed*3) == 0)
                  mainPlayer.advanceWalk();
            }
            
            if(pressedKeys.contains(KeyEvent.VK_S) && !checkObstacleCollisions(mainPlayer.getX(), mainPlayer.getY()+mainPlayer.getSpeed() + PLAYER_HEIGHT) && !checkObstacleCollisions(mainPlayer.getX() + PLAYER_WIDTH, mainPlayer.getY()+mainPlayer.getSpeed() + PLAYER_HEIGHT))
            {
               mainPlayer.moveY(mainPlayer.getSpeed()); 
               if(frames % (defaultSpeed*3) == 0)
                  mainPlayer.advanceWalk();
            }
            
             
            if(pressedKeys.contains(KeyEvent.VK_A) && !checkObstacleCollisions(mainPlayer.getX()-mainPlayer.getSpeed(), mainPlayer.getY()) && !checkObstacleCollisions(mainPlayer.getX()-mainPlayer.getSpeed(), mainPlayer.getY() + PLAYER_HEIGHT))
            {
               mainPlayer.moveX(-mainPlayer.getSpeed());
               if(frames % (defaultSpeed*3) == 0)
                  mainPlayer.advanceWalk();
            }
            
            if(pressedKeys.contains(KeyEvent.VK_D) && !checkObstacleCollisions(mainPlayer.getX()+mainPlayer.getSpeed() + PLAYER_WIDTH, mainPlayer.getY()) && !checkObstacleCollisions(mainPlayer.getX()+mainPlayer.getSpeed() + PLAYER_WIDTH, mainPlayer.getY() + PLAYER_HEIGHT))
            {
               mainPlayer.moveX(mainPlayer.getSpeed());
               if(frames % (defaultSpeed*3) == 0)
                  mainPlayer.advanceWalk();
            }
            if(!pressedKeys.contains(KeyEvent.VK_W) && !pressedKeys.contains(KeyEvent.VK_A) && !pressedKeys.contains(KeyEvent.VK_S) && !pressedKeys.contains(KeyEvent.VK_D))
               mainPlayer.setIdle();
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
      else if(location == DHALLCLASS)
      {
         ImageIcon pic = new ImageIcon("images/dHallClassroom.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      else if(location == END)
      {
         ImageIcon pic = new ImageIcon("images/End.png");
         g.drawImage(pic.getImage(), 0, 0, XSIZE, YSIZE, null);
      }
      if(!mainPlayer.isHiding()){
         g.drawImage(mainPlayer.getFrame().getImage(), mainPlayer.getX(), mainPlayer.getY(), mainPlayer.getWidth(), mainPlayer.getHeight(), null);
         for(Item item: inventory)
         {
            g.drawImage(item.getFrame().getImage(), item.getX(), item.getY(), item.getWidth(), item.getHeight(), null);
            System.out.print(item.getFrame());
         }
         g.setColor(Color.RED);
         g.fillRect(mainPlayer.getX(), mainPlayer.getY()-(mainPlayer.getHeight()/5),mainPlayer.getWidth(), (mainPlayer.getHeight()/8));
         g.setColor(Color.GREEN);
         g.fillRect(mainPlayer.getX(), mainPlayer.getY()-(mainPlayer.getHeight()/5) , (int)(mainPlayer.getWidth()*(mainPlayer.getHealth()/100.0)), (mainPlayer.getHeight()/8)); 
      }
      
      seeObstacles(g);
      
      for(Character enemy: enemies)
      {
         g.drawImage(enemy.getFrame().getImage(), enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight(), null);
         g.setColor(Color.RED);
         g.fillRect(enemy.getX(), enemy.getY()-(enemy.getHeight()/5),enemy.getWidth(), (enemy.getHeight()/8));
         g.setColor(Color.GREEN);
         g.fillRect(enemy.getX(), enemy.getY()-(enemy.getHeight()/5) , (int)(enemy.getWidth()*(enemy.getHealth()/100.0)), (enemy.getHeight()/8));
         g.setColor(Color.BLACK);
         g.setFont(new Font(Font.SERIF, Font.BOLD, 25));
         g.drawString("x:"+enemy.getX() + "   y:"+enemy.getY() + "   enemyPrevX: " + enemies.get(0).getPreviousX() + "    enemyPrevY: " + enemies.get(0).getPreviousY(), 0,YSIZE-100);
      }
      for(Item item: items)
      {
         g.drawImage(item.getFrame().getImage(), item.getX(), item.getY(), item.getWidth(), item.getHeight(), null);
         
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
      else if(location == DHALLCLASS)
      {
         tables.add(new Rectangle((int)(XSIZE*(24.0/120)), (int)(YSIZE*(13.0/75)), (int)(XSIZE*(12.0/120)), (int)(YSIZE*(10.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(46.0/120)), (int)(YSIZE*(13.0/75)), (int)(XSIZE*(12.0/120)), (int)(YSIZE*(10.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(68.0/120)), (int)(YSIZE*(13.0/75)), (int)(XSIZE*(12.0/120)), (int)(YSIZE*(10.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(90.0/120)), (int)(YSIZE*(13.0/75)), (int)(XSIZE*(12.0/120)), (int)(YSIZE*(10.0/75))));
         
         tables.add(new Rectangle((int)(XSIZE*(24.0/120)), (int)(YSIZE*(30.0/75)), (int)(XSIZE*(12.0/120)), (int)(YSIZE*(10.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(46.0/120)), (int)(YSIZE*(30.0/75)), (int)(XSIZE*(12.0/120)), (int)(YSIZE*(10.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(68.0/120)), (int)(YSIZE*(30.0/75)), (int)(XSIZE*(12.0/120)), (int)(YSIZE*(10.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(90.0/120)), (int)(YSIZE*(30.0/75)), (int)(XSIZE*(12.0/120)), (int)(YSIZE*(10.0/75))));
         
         tables.add(new Rectangle((int)(XSIZE*(24.0/120)), (int)(YSIZE*(47.0/75)), (int)(XSIZE*(12.0/120)), (int)(YSIZE*(10.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(46.0/120)), (int)(YSIZE*(47.0/75)), (int)(XSIZE*(12.0/120)), (int)(YSIZE*(10.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(68.0/120)), (int)(YSIZE*(47.0/75)), (int)(XSIZE*(12.0/120)), (int)(YSIZE*(10.0/75))));
         tables.add(new Rectangle((int)(XSIZE*(90.0/120)), (int)(YSIZE*(47.0/75)), (int)(XSIZE*(12.0/120)), (int)(YSIZE*(10.0/75))));
         
         
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
         walls.add(new Rectangle((int)(XSIZE*(97.0/120)), 0,  (int)(XSIZE*(24.0/120)), (int)(YSIZE*(26.0/75))));
         walls.add(new Rectangle(0, (int)(YSIZE*(49.0/75)), XSIZE, (int)(YSIZE*(10.0/75))));
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
         walls.add(new Rectangle(0, 0, (int)(XSIZE*(90.0/120)), (int)(YSIZE*(15.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(104.0/120)), 0, (int)(XSIZE*(80.0/120)), (int)(YSIZE*(15.0/75))));
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
      else if(location == DHALLCLASS)
      {
         walls.add(new Rectangle(0, 0, XSIZE, (int)(YSIZE*(4.0/75))));
         walls.add(new Rectangle(0, 0, (int)(XSIZE*(14.0/120)), YSIZE));
         walls.add(new Rectangle((int)(XSIZE*(111.0/120)), 0, (int)(XSIZE*(37.0/120)), YSIZE));
         walls.add(new Rectangle(0, (int)(YSIZE*(71.0/75)), (int)(XSIZE*(46.0/120)), (int)(YSIZE*(5.0/75))));
         walls.add(new Rectangle((int)(XSIZE*(80.0/120)), (int)(YSIZE*(71.0/75)), (int)(XSIZE*(40.0/120)), YSIZE-(int)(YSIZE*(70.0/75))));
      
      
      
         
      }
      //Nathan: aHall1, aHall2, bHall,cHall1, cHall2
      //Kaden: dHall, fHall2, gHall, kHall, lHall
      
      else if(location == END)
      {
         walls.add(new Rectangle((int)(XSIZE*(32.0/900)), (int)(YSIZE*(460.0/600)), (int)(XSIZE*(300.0/900)), (int)(YSIZE*(110.0/600))));
         walls.add(new Rectangle((int)(XSIZE*(570.0/900)), (int)(YSIZE*(460.0/600)), (int)(XSIZE*(300.0/900)), (int)(YSIZE*(110.0/600))));
         
         if(checkObstacleCollisions(mainPlayer.getX(), mainPlayer.getY()-mainPlayer.getSpeed()) && checkObstacleCollisions(mainPlayer.getX() + PLAYER_WIDTH, mainPlayer.getY()-mainPlayer.getSpeed())){
            
            if(mainPlayer.getX()<XSIZE/2){
               location = LOBBY;
               frames = 0;
               mainPlayer = new Player(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, mainPlayerImages, 100, defaultSpeed, 1, "Kaden", 1);
               enemies.add(new Enemy((int)(XSIZE*(37.0/120)), (int)(YSIZE*(15.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, enemyImages, 100, enemySpeed, 1, "Hall Monitor", 1, 1));
               Sound.silence();
            }
            else
               System.exit(1);
         }
         else if(checkObstacleCollisions(mainPlayer.getX(), mainPlayer.getY()+mainPlayer.getSpeed() + PLAYER_HEIGHT) && checkObstacleCollisions(mainPlayer.getX() + PLAYER_WIDTH, mainPlayer.getY()+mainPlayer.getSpeed() + PLAYER_HEIGHT)){
            if(mainPlayer.getX()<XSIZE/2){
               location = LOBBY;
               frames = 0;
               mainPlayer = new Player(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, mainPlayerImages, 100, defaultSpeed, 1, "Kaden", 1);
               enemies.add(new Enemy((int)(XSIZE*(37.0/120)), (int)(YSIZE*(15.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, enemyImages, 100, enemySpeed, 1, "Hall Monitor", 1, 1));
               Sound.silence();
            }
            else
               System.exit(1);
         }
         if(checkObstacleCollisions(mainPlayer.getX()-mainPlayer.getSpeed(), mainPlayer.getY()) && checkObstacleCollisions(mainPlayer.getX()-mainPlayer.getSpeed(), mainPlayer.getY() + PLAYER_HEIGHT)){
            if(mainPlayer.getX()<XSIZE/2){
               location = LOBBY;
               frames = 0;
               mainPlayer = new Player(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, mainPlayerImages, 100, defaultSpeed, 1, "Kaden", 1);
               enemies.add(new Enemy((int)(XSIZE*(37.0/120)), (int)(YSIZE*(15.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, enemyImages, 100, enemySpeed, 1, "Hall Monitor",1,1));
               Sound.silence();
            }
            else
               System.exit(1);
         }
         else if(checkObstacleCollisions(mainPlayer.getX()+mainPlayer.getSpeed() + PLAYER_WIDTH, mainPlayer.getY()) && checkObstacleCollisions(mainPlayer.getX()+mainPlayer.getSpeed() + PLAYER_WIDTH, mainPlayer.getY() + PLAYER_HEIGHT)){
            if(mainPlayer.getX()<XSIZE/2){
               location = LOBBY;
               frames = 0;
               mainPlayer = new Player(XSIZE/2, YSIZE/2, PLAYER_WIDTH, PLAYER_HEIGHT, mainPlayerImages, 100, defaultSpeed, 1, "Kaden", 1);
               enemies.add(new Enemy((int)(XSIZE*(37.0/120)), (int)(YSIZE*(15.0/75)), PLAYER_WIDTH, PLAYER_HEIGHT, enemyImages, 100, enemySpeed, 1, "Hall Monitor", 1, 1));
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
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(EHALL);
               resetHallMonitor();
               c.setX(XSIZE - c.getWidth()*2);
               c.setY(YSIZE/2);
            }
            if(c.getY() <= 0)
            {
               location = FHALL;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(FHALL);
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
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(LOBBY);
               resetHallMonitor();
               c.setX(c.getWidth());
               c.setY(YSIZE - ((YSIZE/9)*8));
            }
            if(c.getY() >= YSIZE-c.getHeight())
               c.setY(YSIZE-c.getHeight());
               
            if(c.getX() < 0)
            {
               location = AHALL1;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(AHALL1);
               resetHallMonitor();
               c.setX(XSIZE);
               c.setY(YSIZE/2);
            }
         }
         if(location == FHALL)
         {
            if(c.getY() > YSIZE-PLAYER_HEIGHT){
               location = LOBBY;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(LOBBY);
               resetHallMonitor();
               c.setY(0);
            }
            if(c.getX() > XSIZE+PLAYER_WIDTH && c.getY() > YSIZE/2){
               location = CAFEA;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(CAFEA);
               resetHallMonitor();
               c.setY(YSIZE/2);
               c.setX(0);
            }
            if(c.getX() > XSIZE+PLAYER_WIDTH && c.getY() < YSIZE/2){
               location = CAFEB;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(CAFEB);
               resetHallMonitor();
               c.setY(YSIZE/2);
               c.setX(0);
            }
            if(c.getY() < 0){
               location = FHALL2;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(FHALL2);
               resetHallMonitor();
               c.setY((int)(YSIZE)-PLAYER_HEIGHT);
               c.setX(XSIZE/2);
            }
         }
         if(location == CAFEA)
         {
            if(c.getX() < 0){
               location = FHALL;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(FHALL);
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
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(FHALL);
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
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(FHALL);
               resetHallMonitor();
               c.setY(0);
            }
            if(c.getX() < 0 && c.getY() < YSIZE/2)
            {
               location = LHALL;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(LHALL);
               resetHallMonitor();
               c.setX(XSIZE);
               c.setY(YSIZE/2);
            }
            if(c.getX() < 0 && c.getY() > YSIZE/2)
            {
               location = GHALL;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(GHALL);
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
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(FHALL2);
               resetHallMonitor();
               c.setX(0);
               c.setY(YSIZE*10/75);
            }
            if(c.getX() < 0)
            {
               location = AHALL3;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(AHALL3);
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
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(FHALL2);
               resetHallMonitor();
               c.setX(0);
               c.setY(YSIZE*62/75);
            }
            if(c.getX() < 0)
            {
               location = AHALL2;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(AHALL2);
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
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(GHALL);
               resetHallMonitor();
               c.setX(0);
            }
            if(c.getX() < 0)
            {
               location = BHALL;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(BHALL);
               resetHallMonitor();
               c.setX(XSIZE);
            }
            if(c.getY() < 0)
            {
               location = AHALL3;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(AHALL3);
               resetHallMonitor();
               c.setY(YSIZE);
            }
            if(c.getY() > YSIZE)
            {
               location = AHALL1;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(AHALL1);
               resetHallMonitor();
               c.setY(0);
            }
         }
         if(location == AHALL1)
         {
            if(c.getX() > XSIZE)
            {
               location = EHALL;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(EHALL);
               resetHallMonitor();
               c.setX(0);
            }
            if(c.getX() < 0)
            {
               location = DHALL;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(DHALL);
               resetHallMonitor();
               c.setX(XSIZE);
            }
            if(c.getY() < 0)
            {
               location = AHALL2;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(AHALL2);
               resetHallMonitor();
               c.setY(YSIZE);
            }
            
         }
         if(location == DHALL)
         {
            if(c.getX() > XSIZE)
            {
               location = AHALL1;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(AHALL1);
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(32.0/75))); 
            }
            if(c.getY() < 0)
            {
               location = DHALLCLASS;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(DHALLCLASS);
               resetHallMonitor();
               c.setX((int)(XSIZE*(64.0/120)));
               c.setY(YSIZE);
            }
            if(c.getX() < 0)
            {
               location = CHALL1;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(CHALL1);
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
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(DHALL);
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(34.0/75)));
            }
            if(c.getY() < 0)
            {
               location = CHALL2;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(CHALL2);
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
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(KHALL);
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(50.0/120)));
            }
            if(c.getX() > XSIZE && c.getY() > YSIZE/2)
            {
               location = BHALL;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(BHALL);
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(50.0/120)));
            }
            if(c.getY() > YSIZE)
            {
               location = CHALL1;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(CHALL1);
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
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(AHALL2);
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(32.0/75))); 
            }
            if(c.getX() < 0)
            {
               location = CHALL2;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(CHALL2);
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
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(AHALL3);
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(24.0/120)));
            }
            if(c.getX() < 0)
            {
               location = CHALL2;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(CHALL2);
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
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(LHALL);
               resetHallMonitor();
               c.setX(0);
               c.setY((int)(YSIZE*(32.0/75))); 
            }
            if(c.getX() < 0)
            {
               location = KHALL;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(KHALL);
               resetHallMonitor();
               c.setX(XSIZE);
               c.setY((int)(YSIZE*(32.0/75))); 
            }
            if(c.getY() > YSIZE)
            {
               location = AHALL2;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(AHALL2);
               resetHallMonitor();
               c.setY(0);
            }  
         }
         if(location == DHALLCLASS)
         {
            if(c.getY() > YSIZE)
            {
               location = DHALL;
               exitX  = (c.getX());
               exitY = (c.getY());
               c.setLocation(DHALL);
               resetHallMonitor();
               c.setY(0);
               c.setX((int)(XSIZE*(95.0/120)));
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
   public boolean checkObstacleCollisions1(int x, int y){
      for(Rectangle r: tables){
         if(r.contains(x, y))
            return true;
      }
      return false;
   }

   
   public boolean checkTableCollisions(Character player)
   {
      if(checkObstacleCollisions1(player.getX(), player.getY()-player.getSpeed()) || checkObstacleCollisions1(player.getX() + PLAYER_WIDTH, player.getY()-player.getSpeed()))
         return true;
      if(checkObstacleCollisions1(player.getX(), player.getY()+player.getSpeed() + PLAYER_HEIGHT) || checkObstacleCollisions1(player.getX() + PLAYER_WIDTH, player.getY()+player.getSpeed() + PLAYER_HEIGHT))
         return true;
      if(checkObstacleCollisions1(player.getX()-player.getSpeed(), player.getY()) || checkObstacleCollisions1(player.getX()-player.getSpeed(), player.getY() + PLAYER_HEIGHT))
         return true;
      if(checkObstacleCollisions1(player.getX()+player.getSpeed() + PLAYER_WIDTH, player.getY()) || checkObstacleCollisions1(player.getX()+player.getSpeed() + PLAYER_WIDTH, player.getY() + PLAYER_HEIGHT))
         return true;
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
            if(checkTableCollisions(mainPlayer))
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
   
   public void mouseClicked(MouseEvent e)//hit
   {
      if(e.getButton() == MouseEvent.BUTTON1)
      {
         for(Item item: inventory)
         {       
            if(!item.onCoolDown() && enemies.size()!=0)
            {
               item.use(enemies.get(0));
               inventory.get(0).setFrame(1, 0);
               item.setOnCoolDown(true);
               coolDownCountdown = item.getCoolDown();
            }
         }
      
      }
   }
   
   public void mouseEntered(MouseEvent e)
   {
   }
   
   public void mouseExited(MouseEvent e)
   {
   }
   
   public void mousePressed(MouseEvent e)
   {
   }
   
   public void mouseReleased(MouseEvent e)
   {
   }
    
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e) //methods called every frame
      {
         if(frames >= 5)
         {
            movePlayer();
            setBoundaries(mainPlayer);
            hallMonitorMovement(); 
            handeItems();
            healthCheck();
         }
         repaint();
         frames++;
         
      }
   }
}
