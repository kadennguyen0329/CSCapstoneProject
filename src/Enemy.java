public class Enemy extends Character
{
   public boolean isFollowing;
   public int dps;
   public int prevHall, playerTwiceChange, followTime, exitX, exitY;
   public double timeDistance;

   
   public Enemy(int xCoord, int yCoord, int w, int h, String[][] imageFileName, int health1,int strength1 , int speed1, String name1, int location1, int dmg)
   {
      super(xCoord, yCoord, w, h, imageFileName, health1, strength1, speed1, name1, location1);
      previousX = xCoord;
      previousY = yCoord;  
      dps = dmg;      
      prevHall = 1;
      timeDistance = 0;
      followTime = 0;
      exitX = 0; 
      exitY = 0;
    
   }
   
   public int getDPS()
   {
      return dps;
   }
   
   public int getPrevHall()
   {
      return prevHall;
   }
   
   public void setPrevHall(int s)
   {
      prevHall = s;
   }
   
   public boolean getIsFollowing()
   {
      return isFollowing;
   }
   
   public void setIsFollowing(boolean x)
   {
      isFollowing = x;
   }
   
   public void setTimeDistamce(double x)
   {
      timeDistance = x;
   }
   
   public double getTimeDistance()
   {
      return timeDistance;
   }
   
   public int getPlayerTwiceChange()
   {
      return playerTwiceChange;
   }
   
   public int getExitX()
   {
      return exitX;
   }
   
   public int getExitY()
   {
      return exitY;
   }
   
   public boolean followThrough(int location, Player mainPlayer)
   {
      boolean check = false;
      if(prevHall != location)
      {  
         if(followTime == 0)
         {
            timeDistance = distance(getX(), getY(), mainPlayer.getPreviousX(), mainPlayer.getPreviousY())/10;
            playerTwiceChange = location;
         }
         else if(playerTwiceChange != location)
         {
            timeDistance = distance(getX(), getY(), mainPlayer.getPreviousX(), mainPlayer.getPreviousY())/10;
            playerTwiceChange = location;
            followTime = 0;
         }
         followTime++;
         if(followTime == 2)
         {
            setPreviousX(mainPlayer.getX());
            setPreviousY(mainPlayer.getY());
            exitX = mainPlayer.getX();
            exitY = mainPlayer.getY();
         }
         
         if(followTime >= timeDistance)
         {
            setX(exitX);
            setY(exitY);
            setLocation(location);
            prevHall = location;
            followTime = 0;
            if(!mainPlayer.isHiding())
            {
               setIsFollowing(true);
            }
         }
      }
      return check;
   }


   public static double distance(int x1, int y1, int x2, int y2){
      return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
   }


}